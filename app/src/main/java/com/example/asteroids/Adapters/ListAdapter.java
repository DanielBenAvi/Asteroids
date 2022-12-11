package com.example.asteroids.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.asteroids.Model.User;
import com.example.asteroids.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<User> {

    private final Context context;
    private final int resource;

    public ListAdapter(Context context, int resource, ArrayList<User> users) {
        super(context, resource, users);
        this.context = context;
        this.resource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(resource, null);
        }


        User user = getItem(position);

        if (user != null) {
            MaterialTextView fragmentList_textView_name = (MaterialTextView) v.findViewById(R.id.list_item_TXT_name);
            MaterialTextView fragmentList_textView_score = (MaterialTextView) v.findViewById(R.id.list_item_TXT_score);

            if (fragmentList_textView_name != null) {
                fragmentList_textView_name.setText(user.getName());
            }
            if (fragmentList_textView_score != null) {
                fragmentList_textView_score.setText(String.valueOf(user.getScore()));
            }

        }


        return v;
    }
}
