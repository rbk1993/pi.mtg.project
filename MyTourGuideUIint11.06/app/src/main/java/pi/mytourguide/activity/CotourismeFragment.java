package pi.mytourguide.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import pi.mytourguide.R;

public class CotourismeFragment extends Fragment implements View.OnClickListener {
    private Button join;
    private Button create;
    private Button manage;

    ArrayList<HashMap<String, String>> listItem;//array of items
    HashMap<String, String> map;//single item data
    private ProgressDialog progressDialog;
    private TextView textViewBeGrouped;

    public CotourismeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cotourisme, container, false);

        join = (Button) rootView.findViewById(R.id.join);
        create = (Button) rootView.findViewById(R.id.nouveau);
        manage = (Button) rootView.findViewById(R.id.manage);


        join.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Groupes de Cotourisme");

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AfficherGroupFragment fragment = new AfficherGroupFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Create Group");

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CreateGroupFragment fragment = new CreateGroupFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //fragmentTransaction.addToBackStack(null);

            }
        });
        manage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Manage Group");

                showProgress();

            }
        });
        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    public void showProgress(){
        progressDialog = ProgressDialog.show(getActivity(), null,
                "Loading...", true);
    }

    public void hideProgress(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {


    }
}
