package com.example.camelia.trovaripetizioni;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import java.util.ArrayList;

/**
 * Created by george on 3/23/18.
 */
public class ReservationListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list;
    private Context context;

    /**
     * Instantiates a new Favourite places adapter.
     *
     * @param list            the list
     * @param favouritePlaces the favourite places
     * @param context         the context
     * @param userID          the user id
     */
    ReservationListAdapter() {

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
            //final TextView listItemText = view.findViewById(R.id.list_item_string);
            //listItemText.setText(list.get(position));
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
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                //deleteData(favouritePlaces.get(position));
                notifyDataSetInvalidated();
                Log.d("POSITION:", String.valueOf(position));
                //Toast.makeText(context, favouritePlaces.get(position).getName() + " deleted", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * removes the favourite place from database
     * after the user touches the delete button
     *
     * @param location the location to be deleted
     */
    //private void deleteData(Location location) {
    //    refUserFavouritePlaces.child(location.getId()).removeValue();
   // }

}