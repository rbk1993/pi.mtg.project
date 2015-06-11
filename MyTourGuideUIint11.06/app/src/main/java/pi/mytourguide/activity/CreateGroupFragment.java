package pi.mytourguide.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import pi.mytourguide.R;
import pi.mytourguide.utils.MyApplication;

public class CreateGroupFragment extends Fragment {

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

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        groupName = (EditText) rootView.findViewById(R.id.editTextGroupName);


        PRDV = (EditText) rootView.findViewById(R.id.prdv);
        NbreMaxMembres = (EditText) rootView.findViewById(R.id.NombreMax);


        create = (Button) rootView.findViewById(R.id.addparcours);
        cancel = (Button) rootView.findViewById(R.id.annulergroupe);

        np4 = (NumberPicker) rootView.findViewById(R.id.numberPicker4);
        np5 = (NumberPicker) rootView.findViewById(R.id.numberPicker5);
        np6 = (NumberPicker) rootView.findViewById(R.id.numberPicker6);
        np7 = (NumberPicker) rootView.findViewById(R.id.numberPicker7);
        np8 = (NumberPicker) rootView.findViewById(R.id.numberPicker8);
        np9 = (NumberPicker) rootView.findViewById(R.id.numberPicker9);
        np10 = (NumberPicker) rootView.findViewById(R.id.numberPicker10);

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
                    Toast.makeText(getActivity(), "Empty !", Toast.LENGTH_SHORT).show();
                } else {
                    //sercive de cr�ation de groupe
                    Toast.makeText(getActivity(), "groupe cree !", Toast.LENGTH_SHORT).show();

                    getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        return rootView;
    }
}