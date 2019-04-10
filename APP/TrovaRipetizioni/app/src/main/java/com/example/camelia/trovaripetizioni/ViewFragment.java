package com.example.camelia.trovaripetizioni;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewFragment extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view);
        listView = (ListView) findViewById(R.id.listView1);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("IUM");
        arrayList.add("TWEB");
        arrayList.add("Programmazione I");
        arrayList.add("Sistemi operativi");
        arrayList.add("SAS");
        arrayList.add("Analisi I");
        arrayList.add("Fisica");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

    }
}


