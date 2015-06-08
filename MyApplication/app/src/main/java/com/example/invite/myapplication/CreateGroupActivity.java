package com.example.invite.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CreateGroupActivity extends ActionBarActivity {

    private Button create;
    private Button cancel;
    private EditText groupName;
    private EditText Duree;
    private EditText date;
    private EditText horaire;
    private EditText NbreMaxMembres;
    private EditText PRDV;
    private ProgressDialog pDialog;
    private String creation_success;
    private String registration_success;

    String gn, dr, dt, hr,nb,pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupName = (EditText) findViewById(R.id.editTextGroupName);
        Duree = (EditText) findViewById(R.id.duree);
        horaire = (EditText) findViewById(R.id.horaire);
        PRDV = (EditText) findViewById(R.id.prdv);
        NbreMaxMembres = (EditText) findViewById(R.id.NombreMax);
        date = (EditText) findViewById(R.id.date);

        create = (Button) findViewById(R.id.addparcours);
        cancel = (Button) findViewById(R.id.annulergroupe);

        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                gn = groupName.getText().toString();
                dr = Duree.getText().toString();
                hr = horaire.getText().toString();
                pr = PRDV.getText().toString();
                nb = NbreMaxMembres.getText().toString();
                dt = date.getText().toString();

                if (groupName.getText().toString().isEmpty() || Duree.getText().toString().isEmpty()) {

                } else {
                    //service de création de groupe
                    Toast.makeText(getApplicationContext(), "Tentative de création de groupe", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGroupActivity.this, MainPageActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
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
