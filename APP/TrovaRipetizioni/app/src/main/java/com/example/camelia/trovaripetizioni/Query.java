package com.example.camelia.trovaripetizioni;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Query{
    private String account = null;
    private String password = null;
    private String command = null;
    private Context context;
    private final String url = "http://172.16.71.49:8084/Ripetizioni/Controller";

    public Query(String account, String password, String command, Context context) {
        this.account = account;
        this.password = password;
        this.command = command;
        this.context = context;
    }

    public Query(String command, Context context) {
        this.command = command;
        this.context = context;
    }

    protected void request(final Callback callback) {

            RequestQueue queue = Volley.newRequestQueue(this.context);
            String url = "http://172.16.71.49:8084/Ripetizioni/Controller";
            if(this.command != null){
                url += "?command="+this.command;
            }
            if(this.account!= null){
                url+= "&account="+this.account;
            }
            if(this.password != null){
                url += "&password="+password;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                callback.onSuccess(new JSONObject(response));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("errorLog", error.getMessage());
                }
            });
            queue.add(stringRequest);
        }
    }
