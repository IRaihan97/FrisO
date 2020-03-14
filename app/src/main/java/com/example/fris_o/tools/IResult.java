package com.example.fris_o.tools;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IResult {
    public void ObjSuccess(String requestType, JSONObject response);
    public void notifyError(String requestType, VolleyError error);
}
