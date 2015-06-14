package com.example.invite.myapplication.frags;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;

import com.example.invite.myapplication.R;

public class ShowProfileFragment extends Fragment {

    public ShowProfileFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_showprofile, container, false);

        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost3);

        ImageView profilepic = (ImageView) rootView.findViewById(R.id.imageView6);
        RatingBar ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Infos");
        tabSpec.setContent(R.id.User);
        tabSpec.setIndicator("Infos");
        tabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Mur");
        tabSpec1.setContent(R.id.Avis);
        tabSpec1.setIndicator("Mur");
        tabHost.addTab(tabSpec1);

        return rootView;
    }
}