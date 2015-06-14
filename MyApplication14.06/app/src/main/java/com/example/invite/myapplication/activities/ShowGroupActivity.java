package com.example.invite.myapplication.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.invite.myapplication.R;
import com.example.invite.myapplication.frags.ShowGroupFragment;

public class ShowGroupActivity extends Activity {


    private static final String TAG_DUREEH = "duree_parcours_heures";
    private static final String TAG_DATE = "date";
    private static final String TAG_HORAIRE = "horaire";
    private static final String TAG_NBMBMAX = "nombre_membres_max";
    private static final String TAG_NBMBINS = "nombre_membres_inscrits";
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "group_id";
    private static final String TAG_ADMID = "id_administrateur";
    private static final String TAG_USERID = "user_id";

    String getTagUserid;
    String getTagDureeh;
    String getTagHoraire;
    String getTagDate;
    String getTagName;
    String getTagId;
    String getTagAdmid;
    String getTagNbmbmax;
    String getTagNbmbins;

    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_group);

        Intent intent = getIntent();

        getTagAdmid = intent.getStringExtra(TAG_ADMID);
        getTagDureeh = intent.getStringExtra(TAG_DUREEH);
        getTagHoraire = intent.getStringExtra(TAG_HORAIRE);
        getTagDate = intent.getStringExtra(TAG_DATE);
        getTagName = intent.getStringExtra(TAG_NAME);
        getTagId = intent.getStringExtra(TAG_ID);
        getTagUserid = intent.getStringExtra(TAG_USERID);
        getTagNbmbmax = intent.getStringExtra(TAG_NBMBMAX);
        getTagNbmbins = intent.getStringExtra(TAG_NBMBINS);

        Bundle bundle = new Bundle();
        bundle.putString(TAG_ADMID,getTagAdmid);
        bundle.putString(TAG_DUREEH, getTagDureeh);
        bundle.putString(TAG_HORAIRE, getTagHoraire);
        bundle.putString(TAG_DATE, getTagDate);
        bundle.putString(TAG_NAME, getTagName);
        bundle.putString(TAG_ID, getTagId);
        bundle.putString(TAG_USERID, getTagUserid);
        bundle.putString(TAG_NBMBMAX, getTagNbmbmax);
        bundle.putString(TAG_NBMBINS, getTagNbmbins);

        Fragment fragment = new ShowGroupFragment();
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container_2, fragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
