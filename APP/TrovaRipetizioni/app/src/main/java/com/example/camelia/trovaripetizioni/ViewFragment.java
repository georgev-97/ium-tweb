package com.example.camelia.trovaripetizioni;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewFragment extends Fragment {
    String cookie;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        if (b != null) {
            this.cookie = b.getString("cookie");
        }
        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final View x = view;
        request(new VolleyCallBack() {
            @Override
            public void onSuccess(JSONArray result) {
                ListView ls = x.findViewById(R.id.listView1);
                ls.setAdapter(new ReservationListAdapter(result, getActivity().getApplicationContext(), cookie));
            }

            @Override
            public void onSuccess(JSONObject result) {
                //
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void request(final VolleyCallBack callback){
        String url = "http://192.168.43.236:8084/Ripetizioni/Controller?command=getUserReservation&sessionid="+this.cookie;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject j = new JSONObject(response);
                            callback.onSuccess(j.getJSONArray("reservationList"));
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
}

