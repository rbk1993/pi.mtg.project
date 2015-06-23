package project.smarttrip.mytourguide.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import project.smarttrip.mytourguide.R;
import project.smarttrip.mytourguide.activity.GroupActivity;
import project.smarttrip.mytourguide.adapter.GroupListAdapter;
import project.smarttrip.mytourguide.model.GroupItem;
import project.smarttrip.mytourguide.utils.ServiceHandler;
import project.smarttrip.mytourguide.utils.ServerAddress;

public class SearchGroupFragment extends Fragment {

    ListView groupeView;
    GroupListAdapter groupAdapter;

    Button button_filter;

    public static ArrayList<HashMap<String, String>> listItem;//array of items

    public static SimpleAdapter adapter1 = null;

    int debutJ, debutM, debutA, finJ, finM, finA, debutHoraireH, debutHoraireM, finHoraireH, finHoraireM, dureeH, dureeM;
    int completeGroups = 0;
    String datedebut, datefin, horairedebut, horairefin, duree;

    ProgressDialog pDialog;

    private static final String TAG_DUREEH = "duree_parcours_heures";
    private static final String TAG_DATE = "date";
    private static final String TAG_HORAIRE = "horaire";
    private static final String TAG_NBMBMAX = "nombre_membres_max";
    private static final String TAG_NBMBINS = "nombre_membres_inscrits";
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "group_id";
    private static final String TAG_ADMID = "id_administrateur";
    private static final String TAG_USERID = "user_id";
    private static final String TAG_URL = "url";

    public static final String EXTRA_EXTRA_UID = "user_id";

    private static String serverIP = ServerAddress.whatIs();

    private static String url;

    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

    String user_id;

    ArrayList<GroupItem> arrayGroups = new ArrayList<GroupItem>();

    Runnable run_refresh;

    public SearchGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_group, container, false);

        user_id = getArguments().getString(EXTRA_EXTRA_UID);

        listItem = new ArrayList<>();

/*
        Runnable run_refresh = new Runnable() {
            public void run() {
                //reload content
                arrayGroups.clear();
                groupAdapter.notifyDataSetChanged();
                groupeView.invalidateViews();
                groupeView.refreshDrawableState();
            }
        };
*/

        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost2);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Groupes");
        tabSpec.setContent(R.id.Groupes);
        tabSpec.setIndicator("Groupes");
        tabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Filtre");
        tabSpec1.setContent(R.id.Filtre);
        tabSpec1.setIndicator("Filtrer");
        tabHost.addTab(tabSpec1);

        groupeView = (ListView) rootView.findViewById(R.id.listViewGroupe);
        //groupeView = getListView();

        url = "http://" + serverIP + ":8080/searchgroup";

        NumberPicker np11 = (NumberPicker) rootView.findViewById(R.id.numberPicker11);
        NumberPicker np12 = (NumberPicker) rootView.findViewById(R.id.numberPicker12);
        NumberPicker np13 = (NumberPicker) rootView.findViewById(R.id.numberPicker13);
        NumberPicker np15 = (NumberPicker) rootView.findViewById(R.id.numberPicker15);
        NumberPicker np17 = (NumberPicker) rootView.findViewById(R.id.numberPicker17);
        NumberPicker np19 = (NumberPicker) rootView.findViewById(R.id.numberPicker19);
        NumberPicker np21 = (NumberPicker) rootView.findViewById(R.id.numberPicker21);
        NumberPicker np33 = (NumberPicker) rootView.findViewById(R.id.numberPicker33);
        NumberPicker np29 = (NumberPicker) rootView.findViewById(R.id.numberPicker29);
        NumberPicker np23 = (NumberPicker) rootView.findViewById(R.id.numberPicker23);
        NumberPicker np14 = (NumberPicker) rootView.findViewById(R.id.numberPicker14);
        NumberPicker np16 = (NumberPicker) rootView.findViewById(R.id.numberPicker16);

        Calendar c = Calendar.getInstance();
        int currentyear= c.get(Calendar.YEAR);
        int currentmonth = c.get(Calendar.MONTH) + 1;
        int currentday = c.get(Calendar.DAY_OF_MONTH);

        button_filter = (Button) rootView.findViewById(R.id.button_filter);

        //Date début
        np11.setMinValue(1);
        np11.setMaxValue(31);
        np11.setValue(currentday);
        debutJ=np11.getMinValue();
        np12.setMinValue(1);
        np12.setMaxValue(12);
        np12.setValue(currentmonth);
        debutM=np12.getMinValue();
        np13.setMinValue(currentyear);
        np13.setMaxValue(currentyear + 3);
        np13.setValue(currentyear);
        debutA=np13.getMinValue();
        //Date fin
        np15.setMinValue(1);
        np15.setMaxValue(31);
        np15.setValue(currentday);
        finJ=np15.getMaxValue();
        np17.setMinValue(1);
        np17.setMaxValue(12);
        np17.setValue((currentmonth + 4) % 13);
        finM=np17.getMaxValue();
        np19.setMinValue(currentyear);
        np19.setMaxValue(currentyear + 3);
        np19.setValue(currentyear);
        finA=np19.getMaxValue();
        //Horaire début
        np21.setMinValue(0);
        np21.setMaxValue(23);
        np21.setValue(9);
        debutHoraireH=np21.getMinValue();
        np23.setMinValue(0);
        np23.setMaxValue(59);
        np23.setValue(0);
        debutHoraireM=np23.getMinValue();

        //Horaire fin
        np29.setMinValue(0);
        np29.setMaxValue(23);
        np29.setValue(20);
        finHoraireH=np29.getMaxValue();
        np33.setMinValue(0);
        np33.setMaxValue(59);
        finHoraireM=np23.getMaxValue();
        //Duree max
        np14.setMinValue(0);
        np14.setMaxValue(23);
        np14.setValue(10);
        dureeH=np14.getValue();
        np16.setMinValue(0);
        np16.setMaxValue(59);
        dureeM=np16.getValue();

        np11.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               debutJ = newVal;
                                           }
                                       }

        );
        np12.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               debutM = newVal;
                                           }
                                       }

        );
        np13.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               debutA = newVal;
                                           }
                                       }

        );

        np15.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               finJ = newVal;
                                           }
                                       }

        );
        np17.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               finM = newVal;
                                           }
                                       }

        );
        np19.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               finA = newVal;
                                           }
                                       }

        );

        np21.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               debutHoraireH = newVal;
                                           }
                                       }

        );
        np23.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               debutHoraireM = newVal;
                                           }
                                       }

        );
        np29.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               finHoraireH = newVal;
                                           }
                                       }

        );
        np33.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               finHoraireM = newVal;
                                           }
                                       }

        );

        np14.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               dureeH = newVal;
                                           }
                                       }

        );
        np16.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                           @Override
                                           public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                               dureeM = newVal;
                                           }
                                       }

        );

        datedebut = debutA + "-" + debutM + "-" + debutJ;
        datefin = finA + "-" + finM + "-" + finJ;
        horairedebut = debutHoraireH + ":" + debutHoraireM;
        horairefin = finHoraireH + ":" + finHoraireM;
        duree = dureeH + ":" + dureeM;

        final CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.checkBox);

        if (checkBox.isChecked()) {
            completeGroups = 1;
        } else {
            completeGroups = 0;
        }

        new GetContacts().execute();

        button_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date_format_min=null;
                Date date_format_max=null;

                datedebut = debutA + "-" + debutM + "-" + debutJ;
                datefin = finA + "-" + finM + "-" + finJ;
                horairedebut = debutHoraireH + ":" + debutHoraireM;
                horairefin = finHoraireH + ":" + finHoraireM;
                duree = dureeH + ":" + dureeM;

                try {
                    date_format_min = sdf.parse(datedebut);
                    date_format_max = sdf.parse(datefin);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(date_format_max.before(date_format_min)) {
                    Toast.makeText(getActivity(), "La date minimale doit être inférieure à la date maximale !", Toast.LENGTH_SHORT).show();
                } else {
                    new GetContacts().execute();
                }

            }
        });

        groupeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GroupItem groupItem = arrayGroups.get(position);

                Intent intent = new Intent(getActivity(), GroupActivity.class);

                intent.putExtra(TAG_ID, groupItem.getId());
                intent.putExtra(TAG_ADMID, groupItem.getAdmin());
                intent.putExtra(TAG_DUREEH, groupItem.getDuree());
                intent.putExtra(TAG_NAME, groupItem.getName());
                intent.putExtra(TAG_NBMBMAX, groupItem.getMembresmax());
                intent.putExtra(TAG_NBMBINS, groupItem.getMembresinscrits());
                intent.putExtra(TAG_HORAIRE, groupItem.getHoraire());
                intent.putExtra(TAG_DATE, groupItem.getDate());
                intent.putExtra(TAG_URL, groupItem.getUrl());
                intent.putExtra(TAG_USERID, user_id);

                startActivity(intent);

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

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

            paramsList.add(new BasicNameValuePair("horaire_min", horairedebut));
            paramsList.add(new BasicNameValuePair("horaire_max", horairefin));
            paramsList.add(new BasicNameValuePair("date_deb",datedebut));
            paramsList.add(new BasicNameValuePair("date_fin",datefin));
            paramsList.add(new BasicNameValuePair("duree_parcours_heures_max",duree));
            paramsList.add(new BasicNameValuePair(EXTRA_EXTRA_UID,user_id));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, paramsList);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    String jsonAllGroups = wholeJson.getString("info_allgroups");

                    Log.d("Response (places) : ", "> " + jsonAllGroups);

                    //JSONArray contacts = new JSONArray(jsonStr);
                    JSONArray contacts = new JSONArray(jsonAllGroups);

                    int lengthcontacts = contacts.length();

                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject d = contacts.getJSONObject(i);

                        String group_id = d.getString(TAG_ID);
                        String id_admin = d.getString(TAG_ADMID);
                        String duree_parc_heures = d.getString(TAG_DUREEH);
                        String date = d.getString(TAG_DATE);
                        String horaire = d.getString(TAG_HORAIRE);
                        String nombre_membres_max = d.getString(TAG_NBMBMAX);
                        String nombre_membres_inscrits = d.getString(TAG_NBMBINS);
                        String name = d.getString(TAG_NAME);
                        String url_parcours = d.getString(TAG_URL);

                        // tmp hashmap for single contact

                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_ID, group_id);
                        contact.put(TAG_ADMID, id_admin);
                        contact.put(TAG_DUREEH, duree_parc_heures);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_NBMBMAX, nombre_membres_max);
                        contact.put(TAG_NBMBINS, nombre_membres_inscrits);
                        contact.put(TAG_HORAIRE, horaire);
                        contact.put(TAG_DATE, date);
                        contact.put(TAG_URL, url_parcours);

                        // adding contact to contact list
                        //contactList.add(contact);

                        //getActivity().runOnUiThread(run_refresh);

                        arrayGroups.add(new GroupItem(name,date,duree_parc_heures,id_admin,group_id,horaire,nombre_membres_max,nombre_membres_inscrits,url_parcours));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

    /*
                 ListAdapter adapter = new SimpleAdapter(
                        MapActivity.this, contactList,
                        R.layout.list_groups, new String[] { TAG_NAME, TAG_LAT,
                        TAG_LONG }, new int[] { R.id.name_value,
                        R.id.latitude_value, R.id.longitude_value });
    */

/*
            groupeView.setAdapter(

                    new SimpleAdapter(
                            getActivity(), contactList,
                            R.layout.list_groups, new String[]{"Nom du groupe : "+TAG_NAME, TAG_ADMID, "Date : "+TAG_DATE, "Durée : "+TAG_DUREEH,
                            "Horaire : " + TAG_HORAIRE, TAG_NBMBINS, TAG_NBMBMAX, TAG_ID, TAG_URL}, new int[]{
                            R.id.name_value, R.id.admin_id_value, R.id.date,
                            R.id.duree_value, R.id.horaire_value, R.id.nombre_membres_inscrits_value,
                            R.id.nombre_membres_max_value, R.id.id_value, R.id.url_value})

            );
*/

            groupAdapter = new GroupListAdapter(getActivity().getBaseContext(), arrayGroups);

            groupeView.setAdapter(groupAdapter);

        }

    }

}

