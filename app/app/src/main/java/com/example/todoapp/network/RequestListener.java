package com.example.todoapp.network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RequestListener {
    public void requestStarted();
    public void requestCompleted();
    public void requestCompleted(JSONObject response);
    public void requestError(int error, String message);
    public void requestError(VolleyError e);
}
