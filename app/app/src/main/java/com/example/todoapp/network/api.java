package com.example.todoapp.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public  class api {
    private static final String apiUrl = "http://bruno.sch.bme.hu:3001";
    private String accessToken;
    private static RequestQueue queue;
    private static api Instance;
    private Map<String, String> headers;

    private api(Context ctx){
        queue = Volley.newRequestQueue(ctx.getApplicationContext());
        headers = new HashMap<String, String>();
    }

    public static api getInstance(Context context){
        if(Instance == null)
            Instance = new api(context);
        return Instance;
    }

    public static api getInstance(){
        return Instance;
    }

    public void login(String email, String pass, final RequestListener listener){
        listener.requestStarted();
        final Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("pass", pass);
        StringRequest req = new StringRequest(Request.Method.POST, apiUrl + "/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    accessToken = res.getString("token");
                    headers.put("Authorization", "Bearer " +accessToken);
                    listener.requestCompleted();
                }catch (Exception e){
                    Log.e("Api", "responseTryCatch", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Api", "onError - Login", error);
                if (error.networkResponse != null)
                    listener.requestError(error.networkResponse.statusCode, new String(error.networkResponse.data));
                else
                    listener.requestError(error);
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                return params;
            }
        };
        queue.add(req);
    }

    public void getTodoList(final RequestListener listener){

        listener.requestStarted();
        StringRequest req = new StringRequest(Request.Method.GET, apiUrl + "/todos", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    listener.requestCompleted(new JSONObject(response));
                }catch (Exception e){
                    Log.e("Api", "Json parse error - get todo list", e);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Api", "onError - get todo list", error);
                if (error.networkResponse != null)
                    listener.requestError(error.networkResponse.statusCode, new String(error.networkResponse.data));
                else
                    listener.requestError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                return headers;
            }
        };
        queue.add(req);
    }

    public void deleteTodo(String id, final RequestListener listener){
        listener.requestStarted();
        StringRequest req = new StringRequest(Request.Method.DELETE, apiUrl + "/todo/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Api", "onError - get todo list", error);
                if (error.networkResponse != null)
                    listener.requestError(error.networkResponse.statusCode, new String(error.networkResponse.data));
                else
                    listener.requestError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                return headers;
            }
        };
        queue.add(req);
    }

    public void markAsDone(String id, final RequestListener listener){
        final Map<String, String> params = new HashMap<String, String>();
        params.put("status", "1");
        listener.requestStarted();
        StringRequest req = new StringRequest(Request.Method.PATCH, apiUrl + "/setStatus/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                listener.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Api", "onError - get todo list", error);
                if (error.networkResponse != null)
                    listener.requestError(error.networkResponse.statusCode, new String(error.networkResponse.data));
                else
                    listener.requestError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                return headers;
            }
            public Map<String, String> getParams(){return params;}
        };
        queue.add(req);
    }

    public void addTodo(String name, final RequestListener listener){
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        StringRequest req = new StringRequest(Request.Method.PUT, apiUrl + "/todo", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Api", "onError - get todo list", error);
                if (error.networkResponse != null)
                    listener.requestError(error.networkResponse.statusCode, new String(error.networkResponse.data));
                else
                    listener.requestError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                return headers;
            }
            public Map<String, String> getParams(){return params;}
        };
        queue.add(req);
    }

    public void changeTodoName(String name, String id, final RequestListener listener){
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        StringRequest req = new StringRequest(Request.Method.PATCH, apiUrl + "/todoName/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Api", "onError - get todo list", error);
                if (error.networkResponse != null)
                    listener.requestError(error.networkResponse.statusCode, new String(error.networkResponse.data));
                else
                    listener.requestError(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders(){
            return headers;
        }
            public Map<String, String> getParams(){return params;}
        };
        queue.add(req);
    }

}