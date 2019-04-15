package com.example.camelia.trovaripetizioni;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        Button button = rootView.findViewById(R.id.prenota);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), UserHome.class));
            }
        });
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        Spinner spinner1 = view.findViewById(R.id.spinner1);
        List<String> docente = new ArrayList<>();
        docente.add(0, "Scegli il docente");
        docente.add("Prof 1");
        docente.add("Prof 2");
        docente.add("Prof 3");
        docente.add("Prof 4");
        docente.add("Prof 5");


        Spinner spinner2 = view.findViewById(R.id.spinner2);
        List<String> corso = new ArrayList<>();
        corso.add(0, "Scegli il corso");
        corso.add("Programmazione I");
        corso.add("IUM");
        corso.add("Analisi I");
        corso.add("TWEB");
        corso.add("Fisica I");

        Spinner spinner3 = view.findViewById(R.id.spinner3);
        List<String> data = new ArrayList<>();
        data.add(0, "Scegli il giorno");
        data.add("Lunedì");
        data.add("Martedì");
        data.add("Mercoledì");
        data.add("Giovedì");
        data.add("Venerdì");


        Spinner spinner4 = view.findViewById(R.id.spinner4);
        List<String> ora = new ArrayList<>();
        ora.add(0, "Scegli l'ora");
        ora.add("9:00 - 11:00");
        ora.add("11:00 - 13:00");
        ora.add("14:00 - 16:00");
        ora.add("16:00 - 18:00");

        //Style adn populate the spinner

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, docente);

        ArrayAdapter<String> adapter2;
        adapter2 = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, corso);

        ArrayAdapter<String> adapter3;
        adapter3 = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, data);

        ArrayAdapter<String> adapter4;
        adapter4 = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, ora);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Scegli il docente")){
                    //fa niente
                }else{
                    //sceglie

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO auto-generated

            }
        });

        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Scegli il corso")){
                    //fa niente
                }else{
                    //sceglie

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO auto-generated

            }
        });


        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Scegli il giorno")){
                    //fa niente
                }else{
                    //sceglie

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO auto-generated

            }
        });

        spinner4.setAdapter(adapter4);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Scegli l'ora")){
                    //fa niente
                }else{
                    //sceglie

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO auto-generated

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    }
