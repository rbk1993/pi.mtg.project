package com.example.invite.myapplication.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invite.myapplication.R;


public class CreateGroupActivity extends Activity {

    private Button create;
    private Button cancel;
    private EditText groupName;


    private EditText NbreMaxMembres;
    private EditText PRDV;
    private ProgressDialog progressDialog;
    private TextView textViewNewGroup;


    NumberPicker np4;
    NumberPicker np5;
    NumberPicker np6;
    NumberPicker np7;
    NumberPicker np8;
    NumberPicker np9;
    NumberPicker np10;

    int jj, mm, aaaa, dureeH, dureeMin, horaireH, horaireMin;
    String date, duree, horaire;

    private String gn, dr, nb, pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);

        groupName = (EditText) findViewById(R.id.editTextGroupName);


        PRDV = (EditText) findViewById(R.id.prdv);
        NbreMaxMembres = (EditText) findViewById(R.id.NombreMax);


        create = (Button) findViewById(R.id.addparcours);

        np4 = (NumberPicker) findViewById(R.id.numberPicker4);
        np5 = (NumberPicker) findViewById(R.id.numberPicker5);
        np6 = (NumberPicker) findViewById(R.id.numberPicker6);
        np7 = (NumberPicker) findViewById(R.id.numberPicker7);
        np8 = (NumberPicker) findViewById(R.id.numberPicker8);
        np9 = (NumberPicker) findViewById(R.id.numberPicker9);
        np10 = (NumberPicker) findViewById(R.id.numberPicker10);

        np4.setMinValue(0);
        np4.setMaxValue(23);
        np5.setMinValue(0);
        np5.setMaxValue(59);
        np6.setMinValue(1);
        np6.setMaxValue(31);
        np7.setMinValue(1);
        np7.setMaxValue(12);
        np8.setMinValue(2015);
        np8.setMaxValue(2020);
        np9.setMinValue(0);
        np9.setMaxValue(23);
        np10.setMinValue(0);
        np10.setMaxValue(59);

        np4.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              dureeH = newVal;
                                          }
                                      }

        );
        np5.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              dureeMin = newVal;
                                          }
                                      }

        );
        np6.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              jj = newVal;
                                          }
                                      }

        );

        np7.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              mm = newVal;
                                          }
                                      }

        );
        np8.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              aaaa = newVal;
                                          }
                                      }

        );
        np9.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                          @Override
                                          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                              horaireH = newVal;
                                          }
                                      }

        );

        np10.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               horaireMin = newVal;
                                           }
                                       }

        );


        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                gn = groupName.getText().toString();
                pr = PRDV.getText().toString();
                nb = NbreMaxMembres.getText().toString();
                duree = dureeH + ":" + dureeMin;
                date = jj + "-" + mm + "-" + aaaa;
                horaire = horaireH + ":" + horaireMin;

                if (groupName.getText().toString().isEmpty() || PRDV.getText().toString().isEmpty()) {
                    Toast.makeText(CreateGroupActivity.this, "Empty !", Toast.LENGTH_SHORT).show();
                } else {
                    //sercive de cr�ation de groupe
                    Toast.makeText(CreateGroupActivity.this, "groupe cree !", Toast.LENGTH_SHORT).show();

                }
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
