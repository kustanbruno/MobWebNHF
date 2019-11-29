package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.todoapp.network.RequestListener;
import com.example.todoapp.network.api;

import org.json.JSONObject;

public class editToDo extends AppCompatActivity implements RequestListener {
    private String id;
    private String name;
    private EditText nameField;
    private api Api;
    private ProgressDialog pd;
    private Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do);
        Api = api.getInstance();

        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Saving todo item");
        pd.setCancelable(false);

        nameField = findViewById(R.id.name);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("id");
            name = extras.getString("name");
            nameField.setText(name);
        }

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameField.getText().toString().isEmpty()){
                    nameField.requestFocus();
                    nameField.setError("Please fill the name field!");
                    return;
                }

                if(id != null){
                    Api.changeTodoName(nameField.getText().toString(), id, editToDo.this);
                }else{
                    Api.addTodo(nameField.getText().toString(), editToDo.this);
                }
            }
        });
    }

    @Override
    public void requestStarted() {
        pd.show();
    }

    @Override
    public void requestCompleted() {
        pd.dismiss();
        Intent intent = new Intent(editToDo.this, ToDoList.class);
        startActivity(intent);
    }

    @Override
    public void requestCompleted(JSONObject response) {
        //won't be used
    }

    @Override
    public void requestError(int error, String message) {
        pd.dismiss();
        if(error == 406){
            try {
                JSONObject m = new JSONObject(message);
                t = Toast.makeText(editToDo.this, m.getString("message"), Toast.LENGTH_LONG);
                t.show();
            }catch (Exception e){
                Log.i("Api", "Json parse error", e);
            }
        }else{
            t = Toast.makeText(editToDo.this, "Server error", Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Override
    public void requestError(VolleyError e) {
        pd.dismiss();
        t = Toast.makeText(editToDo.this, "Server error", Toast.LENGTH_LONG);
        t.show();
    }
}
