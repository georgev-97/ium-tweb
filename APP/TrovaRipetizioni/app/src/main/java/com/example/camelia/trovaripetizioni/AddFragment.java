package com.example.camelia.trovaripetizioni;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class AddFragment extends AppCompatActivity {

    Spinner spinner;
    Button prenota;

    String[] option = {"IUM", "programmazione I", "SAS", "Algoritmi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add);

        spinner = findViewById(R.id.spinner1);
        prenota = findViewById(R.id.prenota);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, option);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(aa);

        prenota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFragment.this, UserHome.class);
                startActivity(intent);
            }
        });
    }
}
