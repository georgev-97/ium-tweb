package com.example.camelia.trovaripetizioni;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {
    Button btn;
    TextView tw;
    EditText account, password;

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
            Query query = new Query(acc, pass, "login", getApplicationContext());
            query.request(new Callback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        if(!result.getString("error").equals("")){
                            Toast toast = Toast.makeText(getApplicationContext(), result.getString("error"), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(), UserHome.class).putExtra("account", acc));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}
