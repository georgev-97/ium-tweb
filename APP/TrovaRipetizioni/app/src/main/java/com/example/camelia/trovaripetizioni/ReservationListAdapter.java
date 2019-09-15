package com.example.camelia.trovaripetizioni;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by george on 3/23/18.
 */
public class ReservationListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Reservation> list;
    private Context context;
    private String cookie;

    ReservationListAdapter(JSONArray res, Context context, String cookie) {
        Log.d("Res", res.toString());
        this.cookie = cookie;
        this.context = context;
        list = new ArrayList<>();
        for(int i = 0; i < res.length(); i++){
            try {
                JSONArray actual = res.getJSONArray(i);
                list.add(new Reservation("Corso: "+actual.getString(0), "Docente: "+actual.getString(1), actual.getString(3)," "+actual.getString(4)+" - "+actual.getString(5), actual.getString(6), actual.getString(7), cookie ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getCount() {
       return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            view = inflater.inflate(R.layout.list_layout, null);
            final TextView course = view.findViewById(R.id.corso);
            final TextView professor = view.findViewById(R.id.professore);
            final TextView day = view.findViewById(R.id.day);
            final TextView hour = view.findViewById(R.id.hour);
            course.setText(list.get(position).getCourse());
            professor.setText(list.get(position).getProfessor());
            day.setText(list.get(position).getDay());
            hour.setText(list.get(position).getHour());
            setButtonListeners(position, view);
        }
        return view;
    }

    /**
     * associates a listener to all buttons in the listview
     *
     * @param position the position of the item in the listview
     * @param view
     */
    private void setButtonListeners(final int position, View view) {
        Button deleteBtn = view.findViewById(R.id.delete_btn);
        final View t = view;
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).delete(t.getContext());
                Toast.makeText(context, list.get(position).getCourse()+" "+list.get(position).getHour() + " deleted", Toast.LENGTH_LONG).show();
                list.remove(position);
                notifyDataSetInvalidated();
            }
        });
    }

}