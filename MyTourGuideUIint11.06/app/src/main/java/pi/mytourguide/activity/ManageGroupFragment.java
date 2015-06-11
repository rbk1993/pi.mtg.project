package pi.mytourguide.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import pi.mytourguide.R;

public class ManageGroupFragment extends Fragment {

    private Fragment context;
    private ListView maListViewPerso;
    private ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>> listItem;//array of items
    HashMap<String, String> map;//single item data

    String action = "";

    public ManageGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        context = this;
        View rootView = inflater.inflate(R.layout.fragment_manage_group, container, false);

        return rootView;

    }

}