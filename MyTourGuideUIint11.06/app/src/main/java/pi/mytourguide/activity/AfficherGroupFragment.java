package pi.mytourguide.activity;


import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pi.mytourguide.R;
import pi.mytourguide.utils.ServerAddress;


public class AfficherGroupFragment extends Fragment {

    ListView groupeView;
    public static ArrayList<HashMap<String, String>> listItem;//array of items

    public static SimpleAdapter adapter1 = null;


    int debutJ, debutM, debutA, finJ, finM, finA, debutHoraireH, debutHoraireM, finHoraireH, finHoraireM, dureeH, dureeM;
    String datedebut, datefin, horairedebut, horairefin, duree;

    ProgressDialog pDialog;

    private static final String TAG_DUREEH = "duree_parcours_heures";
    private static final String TAG_DUREEM = "duree_parcours_minutes";
    private static final String TAG_DATE = "date";
    private static final String TAG_HORAIRE = "horaire";
    private static final String TAG_NBMBMAX = "nombre_membres_max";
    private static final String TAG_NBMBINS = "nombre_membres_inscrits";
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "group_id";
    private static final String TAG_ADMID = "id_administrateur";

    ArrayList<String> listLongitudes = new ArrayList<String>();
    ArrayList<String> listLatitudes = new ArrayList<String>();
    ArrayList<String> listTitle = new ArrayList<String>();
    ArrayList<String> listOfId = new ArrayList<String>();

    private static String theme;
    private static String serverIP = ServerAddress.whatIs();

    private static String url;

    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

    public AfficherGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_afficher_groupe, container, false);


        groupeView = (ListView) rootView.findViewById(R.id.listViewGroupe);
        listItem = new ArrayList<>();

        final Button precedent_group = (Button) rootView.findViewById(R.id.precedent_show_group);

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

        np11.setMinValue(1);
        np11.setMaxValue(31);
        np12.setMinValue(1);
        np12.setMaxValue(12);
        np13.setMinValue(2015);
        np13.setMaxValue(2020);
        np15.setMinValue(1);
        np15.setMaxValue(31);
        np17.setMinValue(1);
        np17.setMaxValue(12);
        np19.setMinValue(2015);
        np19.setMaxValue(2020);
        np21.setMinValue(0);
        np21.setMaxValue(23);
        np33.setMinValue(0);
        np33.setMaxValue(59);
        np29.setMinValue(0);
        np29.setMaxValue(23);
        np23.setMinValue(0);
        np23.setMaxValue(59);
        np14.setMinValue(0);
        np14.setMaxValue(23);
        np16.setMinValue(0);
        np16.setMaxValue(59);

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
        np12.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
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

        final RadioGroup radioGroupdisponibilite = (RadioGroup) rootView.findViewById(R.id.Disponibilite);

        //int selectedId = radioGroupdisponibilite.getCheckedRadioButtonId();
        //if (selectedId == R.id.critere_groupe_complet ) {}
        //if (selectedId == R.id.critere_groupe_disponible)  {}


        final CheckBox checkBoxDate = (CheckBox) rootView.findViewById(R.id.critere_date);
        final CheckBox checkBoxHoraire = (CheckBox) rootView.findViewById(R.id.critere_horaire);
        final CheckBox checkBoxDuree = (CheckBox) rootView.findViewById(R.id.critere_duree_max);

        if (checkBoxDate.isChecked()) {
        }
        if (checkBoxHoraire.isChecked()) {
        }
        if (checkBoxDuree.isChecked()) {
        }

        new GetContacts().execute();

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

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    String jsonPlacesStr = wholeJson.getString("info_allgroups");

                    Log.d("Response (places) : ", "> " + jsonPlacesStr);

                    //JSONArray contacts = new JSONArray(jsonStr);
                    JSONArray contacts = new JSONArray(jsonPlacesStr);

                    int lengthcontacts = contacts.length();

                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject d = contacts.getJSONObject(i);

                        String group_id = d.getString(TAG_ID);
                        String id_admin = d.getString(TAG_ADMID);
                        String duree_parc_heures = d.getString(TAG_DUREEH);
                        String duree_parc_minutes = d.getString(TAG_DUREEM);
                        String date = d.getString(TAG_DATE);
                        String horaire = d.getString(TAG_HORAIRE);
                        String nombre_membres_max = d.getString(TAG_NBMBMAX);
                        String nombre_membres_inscrits = d.getString(TAG_NBMBINS);
                        String name = d.getString("hey hey hey");

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_ID, group_id);
                        contact.put(TAG_ADMID, id_admin);
                        contact.put(TAG_DUREEH, duree_parc_heures);
                        contact.put(TAG_DUREEM, duree_parc_minutes);
                        contact.put(TAG_NAME, name);
                        contact.put(TAG_NBMBMAX, nombre_membres_max);
                        contact.put(TAG_NBMBINS, nombre_membres_inscrits);
                        contact.put(TAG_HORAIRE, horaire);
                        contact.put(TAG_DATE, date);

                        // adding contact to contact list
                        contactList.add(contact);
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

            ListAdapter adapter2 = new SimpleAdapter(
                    getActivity(), contactList,
                    R.layout.list_groups, new String[]{TAG_NAME, TAG_ADMID, TAG_DATE, TAG_DUREEH, TAG_DUREEM,
                    TAG_HORAIRE, TAG_NBMBINS, TAG_NBMBMAX, TAG_ID}, new int[]{
                    R.id.name_value, R.id.latitude_value, R.id.longitude_value,
                    R.id.descr_value, R.id.open_value, R.id.id_value});

            groupeView.setAdapter(adapter2);

        }

    }

    /*
    public void onListItemClick(ListView l, View v, int position, long id) {
        //HashMap<String, String> myHashMap = (HashMap<String, String>) getListAdapter().getItem(position);
        String item = (String) groupeView.getItemAtPosition(position);
        Toast.makeText(getActivity(), item + " selected", Toast.LENGTH_LONG).show();
    }
    */
}

