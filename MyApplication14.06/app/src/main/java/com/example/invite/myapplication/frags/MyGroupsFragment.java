package com.example.invite.myapplication.frags;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.invite.myapplication.R;

/**
 * Created by invite on 11/06/15.
 */
public class MyGroupsFragment extends Fragment {

    public MyGroupsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mygroups, container, false);

        return rootView;
    }
}
