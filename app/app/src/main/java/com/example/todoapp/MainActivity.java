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

public class MainActivity extends AppCompatActivity implements RequestListener {
    private api Api;
    private Button login;
    private ProgressDialog pd;
    private Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_main);
        Api = api.getInstance(MainActivity.this);
        login = findViewById(R.id.loginButton);

        pd = new ProgressDialog(this);
        pd.setTitle("Loading");
        pd.setMessage("Logging in");
        pd.setCancelable(false);

        final EditText email = findViewById(R.id.email);
        final EditText pass = findViewById(R.id.pass);
        email.requestFocus();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
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

                Api.login(email.getText().toString(), pass.getText().toString(), MainActivity.this);
            }
        }
        );

        Button register = findViewById(R.id.registerOpen);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(MainActivity.this, registerActivity.class);
                startActivity(regIntent);
            }
        });

        //should be deleted
        //email.setText("u");
        //pass.setText("b");
        //login.callOnClick();
    }

    @Override
    public void requestStarted() {
        pd.show();
    }

    @Override
    public void requestCompleted() {
        pd.dismiss();
        Intent intent = new Intent(MainActivity.this, ToDoList.class);
        startActivity(intent);
    }

    @Override
    public void requestCompleted(JSONObject response) {
        //Implementation not needed
    }

    @Override
    public void requestError(int error, String message) {
        pd.dismiss();
        if(error == 406){
            try {
                JSONObject m = new JSONObject(message);
                t = Toast.makeText(MainActivity.this, m.getString("message"), Toast.LENGTH_LONG);
                t.show();
            }catch (Exception e){
                Log.i("Api", "Json parse error", e);
            }
        }else{
            t = Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_LONG);
            t.show();
        }
    }

    @Override
    public void requestError(VolleyError e) {
        pd.dismiss();
        t = Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_LONG);
        t.show();
    }
}
