package com.example.asteroids.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asteroids.Adapters.ListAdapter;
import com.example.asteroids.Interfaces.CallBack_userProtocol;
import com.example.asteroids.Model.User;
import com.example.asteroids.Other.App;
import com.example.asteroids.R;

import java.util.ArrayList;


public class ListFragment extends Fragment {
    ListView fragmentList_listView_scores;

    private CallBack_userProtocol callback;
    ListAdapter listAdapter;

    public void setCallback(CallBack_userProtocol callback) {
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        findViews(view);


        App.myDB.setUsers(getTop10Users(App.myDB.getUsers()));
        listAdapter = new ListAdapter(getContext(), R.layout.list_item, App.myDB.getUsers());
        fragmentList_listView_scores.setAdapter(listAdapter);

        fragmentList_listView_scores.setOnItemClickListener((parent, view1, position, id) -> {
            User user = (User) parent.getItemAtPosition(position);
            callback.changeLocation(user.getLatitude(), user.getLongitude());
        });


        return view;
    }

    private void findViews(View view) {
        fragmentList_listView_scores = (ListView) view.findViewById(R.id.fragmentList_listView_scores);
    }

    public void addScore(User user) {
        App.myDB.getUsers().add(user);
        // sort users by score
        App.myDB.setUsers(getTop10Users(App.myDB.getUsers()));

        listAdapter.notifyDataSetChanged();
    }

    // method to get list of users sort it and trim it to 10 users
    public ArrayList<User> getTop10Users(ArrayList<User> users) {
        users.sort((o1, o2) -> o2.getScore() - o1.getScore());
        if (App.myDB.getUsers().size() > 10) {
            ArrayList<User> tmp = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                tmp.add(users.get(i));
            }
            return tmp;
        } else {
            return users;
        }
    }
}