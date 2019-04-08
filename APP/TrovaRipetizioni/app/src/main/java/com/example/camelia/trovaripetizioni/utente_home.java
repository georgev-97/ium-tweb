package com.example.camelia.trovaripetizioni;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class utente_home extends AppCompatActivity {

    Toolbar toolbar;
    Menu logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utente_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Nome Utente");
        toolbar.setTitleTextColor(-1);
        toolbar.setSubtitle("Welcome");
        toolbar.setSubtitleTextColor(-1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = " ";
        switch (item.getItemId()) {
            case R.id.logout:
                msg = "Logout";
                Toast.makeText(utente_home.this, msg+ " checked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(utente_home.this, Login.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

