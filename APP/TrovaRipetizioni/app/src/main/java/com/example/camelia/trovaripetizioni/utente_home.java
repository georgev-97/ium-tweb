package com.example.camelia.trovaripetizioni;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class utente_home extends AppCompatActivity {
    TextView exit;
    int icon [] = {R.drawable.elimina, R.drawable.modifica};


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.utente_home);

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
}

