package com.example.asteroids.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asteroids.Activities.ScoreActivity;
import com.example.asteroids.Adapters.ListAdapter;
import com.example.asteroids.Interfaces.CallBack_userProtocol;
import com.example.asteroids.Model.MyDB;
import com.example.asteroids.Model.User;
import com.example.asteroids.R;

import java.util.ArrayList;


public class ListFragment extends Fragment {


    ListView fragmentList_listView_scores;
    ListAdapter listAdapter;


    private CallBack_userProtocol callback;


    MyDB myDB;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);

        myDB = MyDB.getInstance();
        if (myDB == null) {
            MyDB.initDB();
        }
        ArrayList<User> tmp = myDB.getUsers();

        listAdapter = new ListAdapter(getContext(), R.layout.list_item, tmp);
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
        fragmentList_listView_scores = (ListView) view.findViewById(R.id.fragmentList_listView_scores);
    }

    /**
     * update the list after adding a new user
     */
    public void addScore(User user) {
        myDB.addUser(user);
        listAdapter.notifyDataSetChanged();

    }



}