package com.example.camelia.trovaripetizioni;

import org.json.JSONArray;
import org.json.JSONObject;

public interface VolleyCallBack{
    void onSuccess(JSONArray result);
    void onSuccess(JSONObject result);
}