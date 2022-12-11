package com.example.asteroids.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.asteroids.Adapters.ListAdapter;
import com.example.asteroids.Interfaces.CallBack_userProtocol;
import com.example.asteroids.Model.User;
import com.example.asteroids.Other.Constants;
import com.example.asteroids.R;

import es.dmoral.toasty.Toasty;


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


        listAdapter = new ListAdapter(getContext(), R.layout.list_item, Constants.users);
        fragmentList_listView_scores.setAdapter(listAdapter);

        fragmentList_listView_scores.setOnItemClickListener((parent, view1, position, id) -> {
            User user = (User) parent.getItemAtPosition(position);
            Toasty.success(getContext(), "User: " + user.getName(), Toasty.LENGTH_SHORT).show();
        });


        return view;
    }

    private void findViews(View view) {
        fragmentList_listView_scores = (ListView) view.findViewById(R.id.fragmentList_listView_scores);
    }

    public void addScore(User user) {
        Constants.users.add(user);
        listAdapter.notifyDataSetChanged();
    }
}