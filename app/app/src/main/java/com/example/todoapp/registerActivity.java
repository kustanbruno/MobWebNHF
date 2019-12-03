package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.todoapp.network.RequestListener;
import com.example.todoapp.network.api;

import org.json.JSONObject;

public class registerActivity extends AppCompatActivity implements RequestListener {
    private ProgressDialog pd;
    private Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final api Api = api.getInstance(registerActivity.this);

        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Register");
        pd.setCancelable(false);

        final EditText email = findViewById(R.id.regEmail);
        final EditText pass = findViewById(R.id.regPass);

        Button button = findViewById(R.id.registerButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty()){
                    email.requestFocus();
                    email.setError("Please enter your email address");
                    return;
                }

                if(pass.getText().toString().isEmpty()){
                    pass.requestFocus();
                    pass.setError("Please enter your password");
                    return;
                }

                Api.register(email.getText().toString(), pass.getText().toString(), registerActivity.this);
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
        Intent intent = new Intent(registerActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void requestCompleted(JSONObject response) {
        //wont be used
    }

    @Override
    public void requestError(int error, String message) {

    }

    @Override
    public void requestError(VolleyError e) {
        pd.dismiss();
        t = Toast.makeText(registerActivity.this, "Server error", Toast.LENGTH_LONG);
        t.show();
    }
}
