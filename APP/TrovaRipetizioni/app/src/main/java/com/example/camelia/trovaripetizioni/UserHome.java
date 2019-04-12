package com.example.camelia.trovaripetizioni;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import android.widget.TextView;

public class UserHome extends AppCompatActivity {
    TextView exit;
    int icon [] = {R.drawable.elimina, R.drawable.modifica};
    @SuppressLint("ResourceType")
    Toolbar toolbar;
    Menu logout;
    String account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userhome);
        BottomNavigationView bottonNav = findViewById(R.id.bottomNav);
        bottonNav.setOnNavigationItemSelectedListener(navListener);
        bottonNav.setSelectedItemId(R.id.view);
        exit = findViewById(R.id.exit);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()){
                        case R.id.view:
                            selectedFragment = new ViewFragment();
                            break;
                        case R.id.add:
                            selectedFragment = new AddFragment();
                            break;
                        case R.id.logout:
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            return true;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

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
                Toast.makeText(UserHome.this, msg+ " checked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UserHome.this, Login.class);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}