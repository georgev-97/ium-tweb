package com.example.camelia.trovaripetizioni;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Reservation {
    private String course;
    private String professor;
    private String day;
    private String hour;
    private String id;
    private String uid;
    private String cookie;

    public Reservation(String course, String professor,String day, String hour, String id, String uid, String cookie) {
        this.course = course;
        this.professor = professor;
        this.day = day;
        this.hour = hour;
        this.id = id;
        this.uid = uid;
        this.cookie = cookie;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
    public void delete(Context context) {
        //command: 'deleteReservation',
        //                reservationId: rid, reservationUserId: ruid
        String url = "http://192.168.43.236:8084/Ripetizioni/Controller?command=deleteReservation&reservationId="+this.getId()+"&reservationUserId="+this.getUid()+"&sessionid="+this.getCookie();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Res",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
}
