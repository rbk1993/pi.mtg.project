package project.smarttrip.mytourguide.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import project.smarttrip.mytourguide.R;
import project.smarttrip.mytourguide.activity.MainActivity;
import project.smarttrip.mytourguide.adapter.MemberListAdapter;
import project.smarttrip.mytourguide.adapter.MsgListAdapter;
import project.smarttrip.mytourguide.adapter.PlaceListAdapter;
import project.smarttrip.mytourguide.model.PlaceItem;
import project.smarttrip.mytourguide.utils.HttpConnection;
import project.smarttrip.mytourguide.utils.PathJSONParser;
import project.smarttrip.mytourguide.utils.ServerAddress;
import project.smarttrip.mytourguide.utils.ServiceHandler;
import project.smarttrip.mytourguide.model.MemberItem;
import project.smarttrip.mytourguide.model.MsgItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowGroupFragment extends Fragment {

    private static final int PLACE_ITEM_LENGHT = 6;
    ProgressDialog pDialog;

    TreeMap<String, MarkerOptions> markerList = new TreeMap<String,MarkerOptions>();

    ListView memberView;
    ListView msgView;
    ListView placeView;

    EditText ecriremessage;

    MemberListAdapter memberListAdapter;
    MsgListAdapter msgListAdapter;
    PlaceListAdapter placeListAdapter;

    ArrayList<MemberItem> arrayMembers = new ArrayList<MemberItem>();

    ArrayList<PlaceItem> arrayPlaces = new ArrayList<PlaceItem>();

    ArrayList<MsgItem> arrayMsg = new ArrayList<MsgItem>();

    int group_id;
    int user_id;
    int admin_id;
    String getTagUrl;

    private static final String TAG_DUREEH = "duree_parcours_heures";
    private static final String TAG_DATE = "date";
    private static final String TAG_HORAIRE = "horaire";
    private static final String TAG_NBMBMAX = "nombre_membres_max";
    private static final String TAG_NBMBINS = "nombre_membres_inscrits";
    private static final String TAG_GROUPNAME = "name";
    private static final String TAG_GROUPID = "group_id";
    private static final String TAG_ADMID = "id_administrateur";
    private static final String TAG_USERID = "user_id";
    private static final String TAG_URL = "url";

    private static final String TAG_LOGIN="login";
    private static final String TAG_NOM="nom";
    private static final String TAG_PRENOM="prenom";
    private static final String TAG_BIRTH = "date_naissance";

    private static final String TAG_MSG="message";
    private static final String TAG_TIMESTP = "timestamp";

    private static final String TAG_LAT = "latitude";
    private static final String TAG_LONG = "longitude";
    private static final String TAG_DESCR = "typedetail";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_OPEN = "opening";

    String getTagUserid;
    String getTagDureeh;
    String getTagHoraire;
    String getTagDate;
    String getTagName;
    String getTagId;
    String getTagAdmid;
    String getTagNbmbmax;
    String getTagNbmbins;

    String jsonSuccess;
    String jsonProc;

    private static String serverIP = ServerAddress.whatIs();

    private static String url_groupinfos = "http://"+serverIP+":8080/showgroup";

    private static String url_inscription;

    private static String url_annulation;

    private static String url_desinscription;

    private static String url_sending;

    private static String url_message = "http://"+serverIP+":8080/writegroupwall";
    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

    TextView groupName;
    TextView date;
    TextView horaire;
    TextView prdvz;
    TextView duree;
    TextView nbMembresMax;
    TextView nbMembresIns;

    Button rejoindre;
    Button annuler;
    Button desinscrire;
    Button ecriremsg;

    private MapView mMapView;
    private GoogleMap map;
    private Bundle mBundle;
    String message_to_send;

    boolean buttonrejoindre_active=true;
    boolean buttonannuler_active=true;
    boolean buttondesincrire_active=true;

    public ShowGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_show_group, container, false);

        Bundle bundle = getArguments();

        getTagAdmid = bundle.getString(TAG_ADMID);
        getTagDate = bundle.getString(TAG_DATE);
        getTagDureeh = bundle.getString(TAG_DUREEH);
        getTagHoraire = bundle.getString(TAG_HORAIRE);
        getTagId = bundle.getString(TAG_GROUPID);
        getTagName = bundle.getString(TAG_NAME);
        getTagNbmbins = bundle.getString(TAG_NBMBINS);
        getTagNbmbmax = bundle.getString(TAG_NBMBMAX);
        getTagUserid = bundle.getString(TAG_USERID);
        getTagUrl = bundle.getString(TAG_URL);

        ecriremessage = (EditText) rootView.findViewById(R.id.editText_message);

        groupName = (TextView) rootView.findViewById(R.id.titre);
        date = (TextView) rootView.findViewById(R.id.date);
        duree = (TextView) rootView.findViewById(R.id.duree);
        horaire = (TextView) rootView.findViewById(R.id.horaire);
        nbMembresMax = (TextView) rootView.findViewById(R.id.nbpersonnes);

        rejoindre = (Button) rootView.findViewById(R.id.buttonrejoindre);
        desinscrire = (Button) rootView.findViewById(R.id.buttondesinscrire);
        annuler = (Button) rootView.findViewById(R.id.buttonannuler);
        ecriremsg = (Button) rootView.findViewById(R.id.button_envoimsg);

        groupName.setText("Nom du groupe : "+getTagName);
        date.setText("Date : "+getTagDate);
        duree.setText("Duree : "+getTagDureeh.substring(0,5));
        horaire.setText("Horaire : "+getTagHoraire.substring(0,5));
        group_id = Integer.parseInt(getTagId);
        user_id = Integer.parseInt(getTagUserid);
        admin_id = Integer.parseInt(getTagAdmid);
        nbMembresMax.setText("Inscrits : "+getTagNbmbins+ " / " +getTagNbmbmax);

        url_inscription="http://"+serverIP+":8080/subscribegroup";

        url_desinscription="http://"+serverIP+":8080/unsubscribegroup";

        url_annulation="http://"+serverIP+":8080/deletegroup";

        memberView = (ListView) rootView.findViewById(R.id.listViewMembers);

        msgView = (ListView) rootView.findViewById(R.id.msgListView);

        placeView = (ListView) rootView.findViewById(R.id.listViewPlaces);

        new GetContacts().execute();

        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Infos");
        tabSpec.setContent(R.id.Informations);
        tabSpec.setIndicator("Infos");
        tabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Carte");
        tabSpec1.setContent(R.id.Programme);
        tabSpec1.setIndicator("Carte");
        tabHost.addTab(tabSpec1);

        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("Mur");
        tabSpec2.setContent(R.id.Mur);
        tabSpec2.setIndicator("Mur");
        tabHost.addTab(tabSpec2);

        //Check if user is the admin
        if(user_id == admin_id) {

            //Desactivate "unsubscribe" and "join" buttons. Activate "annulation" button.

            buttondesincrire_active = false;
            buttonrejoindre_active = false;
            buttonannuler_active = true;

            desinscrire.setBackgroundColor(Color.parseColor("#deeaf6"));
            rejoindre.setBackgroundColor(Color.parseColor("#deeaf6"));
            annuler.setBackgroundColor(Color.parseColor("#ffb71c1c"));

            desinscrire.setEnabled(false);
            rejoindre.setEnabled(false);
            annuler.setEnabled(true);

        } else { //if user is not the admin. Desactivate "annulation" button.

            annuler.setEnabled(false);
            annuler.setBackgroundColor(Color.parseColor("#deeaf6"));
            buttonannuler_active = false;
        }

        //Check if the groupe is complete or not
        if(Integer.parseInt(getTagNbmbins) >= Integer.parseInt(getTagNbmbmax)) {

            //If complete, desactivate "join" button
            rejoindre.setBackgroundColor(Color.parseColor("#deeaf6"));
            rejoindre.setEnabled(false);
            buttonrejoindre_active = false;
        }


        ecriremsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message_to_send = ecriremessage.getText().toString();

                if(message_to_send.length() > 5) {
                    new SendMessage().execute();
                    ecriremessage.setText("");
                } else {
                    Toast.makeText(getActivity(), "Veuillez entrer plus de 5 caractères", Toast.LENGTH_LONG).show();
                }
            }
        });


        rejoindre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(buttonrejoindre_active) {

                    url_sending = url_inscription;

                    new GetProcedure().execute();
                }

            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(buttonannuler_active) {

                    url_sending = url_annulation;

                    new GetProcedure().execute();
                }

            }
        });

        desinscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(buttondesincrire_active) {

                    url_sending = url_desinscription;

                    new GetProcedure().execute();
                }

            }
        });

        // Inflate the layout for this fragment


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
     * */
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

            paramsList.add(new BasicNameValuePair("group_id",getTagId));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url_groupinfos, ServiceHandler.GET, paramsList);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    String jsonMembers = wholeJson.getString("members");
                    String jsonWall = wholeJson.getString("wall");
                    String jsonPlaces = wholeJson.getString("places");

                    Log.d("Response(jsonMembers):", "> " + jsonMembers);
                    Log.d("Response(jsonWall):", "> " + jsonWall);
                    Log.d("Response(jsonPlaces):", "> " + jsonPlaces);

                    //JSONArray contacts = new JSONArray(jsonStr);
                    JSONArray json_get_members = new JSONArray(jsonMembers);
                    JSONArray json_get_wall = new JSONArray(jsonWall);
                    JSONArray json_get_places = new JSONArray(jsonPlaces);

                    for (int i = 0; i < json_get_members.length(); i++) {

                        JSONObject m = json_get_members.getJSONObject(i);

                        String user_id = m.getString(TAG_USERID);
                        String user_nom = m.getString(TAG_NOM);
                        String user_prenom = m.getString(TAG_PRENOM);
                        String user_birth = m.getString(TAG_BIRTH);

                        arrayMembers.add(new MemberItem(user_prenom,user_nom,user_birth,user_id));
                    }

                    for (int i = 0;i< json_get_places.length();i++) {
                        JSONObject p = json_get_places.getJSONObject(i);

                        String longitude = p.getString(TAG_LONG);
                        String latitude = p.getString(TAG_LAT);
                        String description = p.getString(TAG_DESCR);
                        String open = p.getString(TAG_OPEN);
                        String name = p.getString(TAG_NAME);
                        String id = p.getString(TAG_ID);

                        arrayPlaces.add(new PlaceItem(name,description,open,longitude,latitude,id));

                    }

                    for (int i = 0;i< json_get_wall.length();i++) {
                        JSONObject w = json_get_wall.getJSONObject(i);

                        String login = w.getString(TAG_LOGIN);
                        String msg = w.getString(TAG_MSG);
                        String timestamp = w.getString(TAG_TIMESTP);

                        arrayMsg.add(new MsgItem(login,msg,timestamp));

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


            int hint=0;

            //Check if the user is already a member of the group
            for (int i = 0; i<arrayMembers.size();i++) {
                if (Integer.parseInt(arrayMembers.get(i).getId()) == user_id) {

                    //Desactivate "Join" Button, Activate "unsubscribe button"
                    rejoindre.setBackgroundColor(Color.parseColor("#deeaf6"));
                    //desinscrire.setBackgroundColor(Color.parseColor("#ffb71c1c"));

                    rejoindre.setEnabled(false);
                    //desinscrire.setEnabled(true);

                    buttonrejoindre_active = false;
                    //buttondesincrire_active = true;

                    hint=1;
                }
            }

            //Check if the user is not a member of the group
            if(hint==0) {

                //Desactivate "Delete" and "Unsubscribe" buttons. Activate "join" button.
                buttonannuler_active = false;
                buttondesincrire_active = false;
                //buttonrejoindre_active = true;

                annuler.setBackgroundColor(Color.parseColor("#deeaf6"));
                desinscrire.setBackgroundColor(Color.parseColor("#deeaf6"));
                //rejoindre.setBackgroundColor(Color.parseColor("#6497b1"));

                annuler.setEnabled(false);
                desinscrire.setEnabled(false);
                //rejoindre.setEnabled(true);

            }

            memberListAdapter = new MemberListAdapter(getActivity(), arrayMembers);
            msgListAdapter = new MsgListAdapter(getActivity(), arrayMsg);
            placeListAdapter = new PlaceListAdapter(getActivity(), arrayPlaces);
            memberView.setAdapter(memberListAdapter);
            msgView.setAdapter(msgListAdapter);
            placeView.setAdapter(placeListAdapter);


        }

    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetProcedure extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            //pDialog = new ProgressDialog(ShowGroupActivity.this);
            //pDialog.setMessage("Please wait...");
            //pDialog.setCancelable(false);
            //pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

            paramsList.add(new BasicNameValuePair("user_id",getTagUserid));
            paramsList.add(new BasicNameValuePair("group_id",getTagId));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url_sending, ServiceHandler.GET,paramsList);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    Log.d("Response: ", "> " + wholeJson);

                    jsonProc = wholeJson.getString("procedure");
                    jsonSuccess = wholeJson.getString("success");

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

            if(Integer.parseInt(jsonSuccess) != 0) {

                if(jsonProc.equals("annulation")) {
                    Toast.makeText(getActivity(), "Groupe supprimé avec succès", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else if(jsonProc.equals("desinscription")) {
                    Toast.makeText(getActivity(),"Vous êtes à présent desinscrit du groupe !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else if(jsonProc.equals("inscription")) {
                    Toast.makeText(getActivity(),"Bienvenue dans votre nouveau groupe !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(),"Une opération s'est mal passée...", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(),"Echec procedure ...", Toast.LENGTH_LONG).show();
            }

        }

    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class SendMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            //pDialog = new ProgressDialog(ShowGroupActivity.this);
            //pDialog.setMessage("Please wait...");
            //pDialog.setCancelable(false);
            //pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

            paramsList.add(new BasicNameValuePair("user_id",getTagUserid));
            paramsList.add(new BasicNameValuePair("group_id",getTagId));
            paramsList.add(new BasicNameValuePair("message",message_to_send));


            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url_message, ServiceHandler.GET, paramsList);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    jsonProc = wholeJson.getString("write_success");

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

            if(Integer.parseInt(jsonProc) != 0) {

                if (Integer.parseInt(jsonProc) != 0) {
                    Toast.makeText(getActivity(), "Message envoyé ! ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "msg : Une erreur est survenue", Toast.LENGTH_LONG).show();
                }
            }

        }

    }


}