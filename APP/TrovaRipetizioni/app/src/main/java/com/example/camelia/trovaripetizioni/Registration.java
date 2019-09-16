package com.example.camelia.trovaripetizioni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registration extends AppCompatActivity {
    TextView im;
    EditText account, password, passwordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        im = findViewById(R.id.back);

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        passwordConfirmation = findViewById(R.id.passwordConfirm);
    }


    public void register(View view) {
        String acc = account.getText().toString();
        String pass = password.getText().toString();
        String passwordConfirm = passwordConfirmation.getText().toString();
        if (!pass.equals(passwordConfirm)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Le password non corrispondono", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } else {
            request(new VolleyCallBack() {
                @Override
                public void onSuccess(JSONArray result) {

                }

                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        if(result.getString("error").equals("")){
                            Toast.makeText(getApplicationContext(), "Registrazione avvenuta con successo", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }else{
                            Toast.makeText(getApplicationContext(), result.getString("error"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },"command=register&account="+acc+"&password="+pass);
        }
    }

    private void request(final VolleyCallBack callback, String params) {
        String url = "http://192.168.43.236:8084/Ripetizioni/Controller?" + params;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject j = new JSONObject(response);
                            callback.onSuccess(j);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);
    }
}
