package pi.mytourguide.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import pi.mytourguide.R;


public class HomeFragment extends Fragment implements View.OnClickListener {
    private Button cotourisme;
    private Button parcours;


    ArrayList<HashMap<String, String>> listItem;//array of items
    HashMap<String, String> map;//single item data
    private static final String TAG_GROUP_NAME = "Tassarkolat";
    private static final String TAG_REGION = "Lyon";
    private static final String TAG_SUPERVISEUR = "Hassan";
    private ProgressDialog progressDialog;
    private TextView textViewBeGrouped;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        cotourisme = (Button) rootView.findViewById(R.id.cotourisme);
        parcours = (Button) rootView.findViewById(R.id.parcours);



        cotourisme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle("Cotourisme");

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CotourismeFragment fragment = new CotourismeFragment();
                fragmentTransaction.replace(R.id.container_body, fragment,"tag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        parcours.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Parcours Personnalisé");

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MainPageFragment fragment = new MainPageFragment();
                fragmentTransaction.replace(R.id.container_body, fragment, "tag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //fragmentTransaction.addToBackStack(null);

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
