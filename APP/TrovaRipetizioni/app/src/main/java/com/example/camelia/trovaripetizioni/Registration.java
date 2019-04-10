package com.example.camelia.trovaripetizioni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        password= findViewById(R.id.password);
        passwordConfirmation = findViewById(R.id.passwordConfirm);
    }


    public void register(View view) {
        String acc = account.getText().toString();
        String pass = password.getText().toString();
        String passwordConfirm = passwordConfirmation.getText().toString();
        if(!pass.equals(passwordConfirm)){
            Toast toast = Toast.makeText(getApplicationContext(), "Le password non corrispondono", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }else{
            Query query = new Query(acc, pass, "register", getApplicationContext());
            query.request(new Callback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        if(!result.getString("error").equals("")){
                            Toast toast = Toast.makeText(getApplicationContext(), result.getString("error"), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(),"Registrazione avvenuta con successo", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
