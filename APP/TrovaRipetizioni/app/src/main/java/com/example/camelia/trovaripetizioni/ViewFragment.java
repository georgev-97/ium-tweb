package com.example.camelia.trovaripetizioni;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<String> values = new ArrayList<>();
        ListView ls = view.findViewById(R.id.listView1);
        //ReservationListAdapter reservationListAdapter = new ReservationListAdapter();
        //ls.setAdapter(reservationListAdapter);

        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                view.getContext(), android.R.layout.simple_list_item_1, values );
        ls.setAdapter(arrayAdapter);*/
        super.onViewCreated(view, savedInstanceState);
    }
}

