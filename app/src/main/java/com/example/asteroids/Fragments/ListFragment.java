package com.example.asteroids.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asteroids.Adapters.ListAdapter;
import com.example.asteroids.Interfaces.CallBack_userProtocol;
import com.example.asteroids.Model.MyDB;
import com.example.asteroids.Model.MySP;
import com.example.asteroids.Model.User;
import com.example.asteroids.R;


public class ListFragment extends Fragment {


    ListView fragmentList_listView_scores;
    ListAdapter listAdapter;


    private CallBack_userProtocol callback;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);

        MyDB myDB = MyDB.loadFromSP();
        Log.d("DB", myDB.toString());
        listAdapter = new ListAdapter(getContext(), R.layout.list_item, myDB.getUsers());
        fragmentList_listView_scores.setAdapter(listAdapter);


        fragmentList_listView_scores.setOnItemClickListener((parent, view1, position, id) -> {
            User user = (User) parent.getItemAtPosition(position);
            callback.changeLocation(user.getLatitude(), user.getLongitude());
        });


        return view;
    }

    /**
     * set the callback
     *
     * @param callback the callback
     */
    public void setCallback(CallBack_userProtocol callback) {
        this.callback = callback;
    }

    private void findViews(View view) {
        fragmentList_listView_scores = view.findViewById(R.id.fragmentList_listView_scores);
    }
}