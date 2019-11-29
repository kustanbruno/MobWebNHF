package com.example.todoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.todoapp.adapters.toDoListController;
import com.example.todoapp.adapters.todoListAdapter;
import com.example.todoapp.models.todo;
import com.example.todoapp.network.RequestListener;
import com.example.todoapp.network.api;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ToDoList extends AppCompatActivity implements RequestListener, toDoListController {

    private api Api;
    private ArrayList<todo> todoList;
    private ProgressDialog pd;
    private todoListAdapter adapter;
    private Boolean rvInit = false;
    private Toast t;
    private FloatingActionButton fab;
    private SwipeRefreshLayout srl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        todoList = new ArrayList<todo>();

        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Updating todo list");
        pd.setCancelable(false);

        fab = findViewById(R.id.addTodo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToDoList.this, editToDo.class);
                startActivity(intent);
            }
        });

        srl = findViewById(R.id.swipeRefresh);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Api.getTodoList(ToDoList.this);
            }
        });

        Api = api.getInstance();
        Api.getTodoList(this);
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.todoList);
        adapter = new todoListAdapter(todoList, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void requestStarted() {
        pd.show();
    }

    @Override
    public void requestCompleted() {
        Api.getTodoList(this);
    }

    @Override
    public void requestCompleted(JSONObject response) {
        pd.dismiss();
        srl.setRefreshing(false);
        try {
            JSONArray todos = response.getJSONArray("todos");
            todoList.clear();
            for(int i = 0; i< todos.length(); i++){
                JSONObject t = todos.getJSONObject(i);
                Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:MM:ss.SSS'Z'").parse(t.getString("date"));
                todoList.add(new todo(t.getString("_id"), t.getString("name"), t.getInt("status"), d));
                if(!rvInit){
                    initRecyclerView();
                    rvInit = true;
                }else
                    adapter.notifyDataSetChanged();

            }
        }catch (Exception e){
            Log.e("Api", "Json parse error - ToDoList request completed", e);
        }
    }

    @Override
    public void requestError(int error, String message) {
        pd.dismiss();
        if(error == 406){
            try {
                JSONObject m = new JSONObject(message);
                t = Toast.makeText(ToDoList.this, m.getString("message"), Toast.LENGTH_LONG);
                t.show();
            }catch (Exception e){
                Log.i("Api", "Json parse error", e);
            }
        }else{
            t = Toast.makeText(ToDoList.this, "Server error", Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Override
    public void requestError(VolleyError e) {
        t = Toast.makeText(ToDoList.this, "Server error", Toast.LENGTH_LONG);
        t.show();
    }

    @Override
    public void deleteTodo(final int position) {
        final RequestListener thisClass = this;
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you really want to delete the item?")
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Api.deleteTodo(todoList.get(position).getId(), thisClass);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void markTodoAsDone(int position) {
        Api.markAsDone(todoList.get(position).getId(), this);
    }

    @Override
    public void editTodo(int position) {
        Intent intent = new Intent(ToDoList.this, editToDo.class);
        intent.putExtra("id", todoList.get(position).getId());
        intent.putExtra("name", todoList.get(position).getName());
        startActivity(intent);
    }
}
