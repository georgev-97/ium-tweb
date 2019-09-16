package com.example.camelia.trovaripetizioni;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button btn;
    TextView tw;
    EditText account, password;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = findViewById(R.id.login_btn);
        tw = findViewById(R.id.login_tw);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Registration.class);
                startActivity(i);
            }
        });
    }

    public void login(View view){
        final String acc = account.getText().toString();
        final String pass = password.getText().toString();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.9:8084/Ripetizioni/Controller";
        CustomStringRequest strreq = new CustomStringRequest(Request.Method.POST,
                url,
                new Response.Listener<CustomStringRequest.ResponseM>() {
                    @Override
                    public void onResponse(CustomStringRequest.ResponseM result) {
                        try {
                            String sessionId = result.headers.get("sessionid");
                            String response= result.response;
                            JSONObject res = new JSONObject(response);
                            if(res.getString("error").equals("")){
                                Toast.makeText(getApplicationContext(), "Bentornato/a "+res.getString("account"), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), UserHome.class);
                                i.putExtra("cookie", sessionId);
                                startActivity(i);
                            }else{
                                Toast.makeText(getApplicationContext(), "Email e/o Password errati", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                e.printStackTrace();
            }
        })
        {
            @Override
        public Map<String, String> getParams(){
            Map<String, String> params = new HashMap<>();
            params.put("command", "login");
            params.put("account", acc);
            params.put("password", pass);
            return params;
        }
        };
        queue.add(strreq);
    }
}
