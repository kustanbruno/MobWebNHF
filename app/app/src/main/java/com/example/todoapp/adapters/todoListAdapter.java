package com.example.todoapp.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.models.todo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class todoListAdapter extends RecyclerView.Adapter<todoListAdapter.ViewHolder> {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM");
    private ArrayList<todo> todos = new ArrayList<todo>();
    private Context mContext;
    private toDoListController controller;

    public todoListAdapter(ArrayList<todo> todos, Context mContext, toDoListController controller) {
        this.todos = todos;
        this.mContext = mContext;
        this.controller = controller;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.name.setText(todos.get(position).getName());
        if(todos.get(position).getStatus() == 1) {
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.done.setVisibility(View.GONE);
        }
        holder.date.setText(df.format(todos.get(position).getDate()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.deleteTodo(position);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.editTodo(position);
            }
        });
        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.markTodoAsDone(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return todos.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, date;
        ImageButton delete, done, edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.todoTitle);
            date = itemView.findViewById(R.id.todoDate);
            delete = itemView.findViewById(R.id.delete);
            done = itemView.findViewById(R.id.done);
            edit = itemView.findViewById(R.id.edit);
        }
    }
}
