package com.example.camelia.trovaripetizioni;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class AddFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private String cookie;
    private String course;
    private String professor;
    private String daySelected;
    private String []slotID;
    private String slotId = null;
    private JSONArray reservationMatrix;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        Bundle b = getArguments();
        if (b != null) {
            this.cookie = b.getString("cookie");
        }
        Button pren = rootView.findViewById(R.id.prenota);
        pren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prenota(v);
            }
        });
        return rootView;
    }

    private void request(final VolleyCallBack callback, String params){
        String url = "http://192.168.43.236:8084/Ripetizioni/Controller?"+params+"&sessionid="+this.cookie;
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
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        final Spinner spinner1 = view.findViewById(R.id.spinner1);
        final List<String> corso = new ArrayList<>();
        corso.add(0, "Scegli il corso");
        request(new VolleyCallBack() {

            @Override
            public void onSuccess(JSONArray result) {
                Log.d("ResA", result.toString());
            }

            @Override
            public void onSuccess(JSONObject result) {


                try {
                    JSONArray courseList = result.getJSONArray("courseList");
                    for(int i = 0; i< courseList.length(); i++){
                        corso.add(courseList.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "command=getCourse");


        final Spinner spinner2 = view.findViewById(R.id.spinner2);
        final List<String> docente = new ArrayList<>();
        docente.add(0, "Scegli il docente");

        final Spinner spinner3 = view.findViewById(R.id.spinner3);
        final List<String> data = new ArrayList<>();
        data.add(0, "Scegli il giorno");
        data.add("Lunedì");
        data.add("Martedì");
        data.add("Mercoledì");
        data.add("Giovedì");
        data.add("Venerdì");


        final Spinner spinner4 = view.findViewById(R.id.spinner4);
        final List<String> ora = new ArrayList<>();
        ora.add(0, "Scegli l'ora");

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
                    String p = (String)parent.getItemAtPosition(position);
                    String prof= "";
                    for(int i = 0;i < p.length(); i++){
                        if(p.charAt(i) == '('){
                            for(int j = i+1; j < p.length()-1; j++){
                                prof= prof+p.charAt(j);
                            }
                        }
                    }
                    professor = prof;
                    request(new VolleyCallBack() {

                        @Override
                        public void onSuccess(JSONArray result) {
                            Log.d("ResA", result.toString());
                        }

                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                reservationMatrix = result.getJSONArray("reservationMatrix");
                                Log.d("Res1", reservationMatrix.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },"command=getReservation&course="+course+"&professor="+professor);
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
                    course = (String)parent.getItemAtPosition(position);
                    slotId = null;
                    docente.clear();
                    docente.add("Scegli il docente");
                    spinner3.setSelection(0);
                    ora.clear();
                    ora.add("Scegli l'ora");
                    spinner1.setSelection(0);
                    request(new VolleyCallBack() {
                        @Override
                        public void onSuccess(JSONArray result) {

                        }
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                JSONArray courseList = result.getJSONArray("professorList");
                                for(int i = 0; i< courseList.length(); i++){
                                    docente.add(courseList.get(i).toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, "command=getCourseProfessor&course="+parent.getItemAtPosition(position));
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
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if(parent.getItemAtPosition(position).equals("Scegli il giorno")){
                    //fa niente
                }else{
                    daySelected = (String)parent.getItemAtPosition(position);
                    slotId = null;
                    ora.clear();
                    ora.add("Scegli l'ora");
                    spinner4.setSelection(0);
                    int dayIndex = (position-1)*4;
                    String[] orario = {"9:00 - 11:00", "11:00 - 13:00", "14:00 - 16:00","16:00 - 18:00"};
                    slotID = new String[]{"0", "0", "0", "0"};
                    int k = 0;
                    int s = 0;
                    for(int i = dayIndex; i < dayIndex +4; i++) {
                        JSONArray t = null;
                        try {
                            t = reservationMatrix.getJSONArray(i);
                            if (t.getString(3).equals("free")) {
                                ora.add(orario[k]);
                                slotID[s] = t.getString(4);
                                s++;
                            }
                            k++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
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
                    slotId = slotID[position -1];
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

    public void prenota(View view) {
        final View x = view;
        if(slotId != null){
            Toast.makeText(x.getContext(), "Prenotazione effettuata con successo", Toast.LENGTH_LONG).show();
            x.getContext().startActivity(new Intent(x.getContext(), UserHome.class).putExtra("cookie", cookie));
            request(new VolleyCallBack() {
                @Override
                public void onSuccess(JSONArray result) {
                }
                @Override
                public void onSuccess(JSONObject result) {

                }
            }, "command=reserve&slotId="+slotId);
        }else{
            Toast.makeText(x.getContext(), "Compilare tutti i campi", Toast.LENGTH_LONG).show();
        }
    }
}
