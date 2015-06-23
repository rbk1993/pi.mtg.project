package project.smarttrip.mytourguide.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.smarttrip.mytourguide.R;
import project.smarttrip.mytourguide.activity.GroupActivity;
import project.smarttrip.mytourguide.adapter.GroupListAdapter;
import project.smarttrip.mytourguide.model.GroupItem;
import project.smarttrip.mytourguide.utils.ServerAddress;
import project.smarttrip.mytourguide.utils.ServiceHandler;

public class MyGroupsFragment extends Fragment {

    ListView myGroupeView;
    ListView groupeView;
    GroupListAdapter groupAdapter;
    GroupListAdapter groupAdapter2;
    ArrayList<GroupItem> arrayGroups = new ArrayList<GroupItem>();
    ArrayList<GroupItem> arrayGroups2 = new ArrayList<GroupItem>();

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

    private static String serverIP = ServerAddress.whatIs();

    public static final String EXTRA_EXTRA_UID = "user_id";

    ProgressDialog pDialog;

    String user_id;

    String url;

    public MyGroupsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_groups, container, false);

        user_id = getArguments().getString(EXTRA_EXTRA_UID);

        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost4);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("mes Groupes");
        tabSpec.setContent(R.id.MesGroupes);
        tabSpec.setIndicator("mes Groupes");
        tabHost.addTab(tabSpec);

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("Gestion");
        tabSpec1.setContent(R.id.Gestion);
        tabSpec1.setIndicator("Gestion");
        tabHost.addTab(tabSpec1);

        myGroupeView = (ListView) rootView.findViewById(R.id.listView4);
        groupeView = (ListView) rootView.findViewById(R.id.listView5);

        url = "http://" + serverIP + ":8080/searchmygroups";

        new GetContacts().execute();

        groupeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GroupItem groupItem = arrayGroups2.get(position);

                Intent grp = new Intent(getActivity().getBaseContext(), GroupActivity.class);

                String thegroupid = groupItem.getId();
                String theadmin = groupItem.getAdmin();
                String thehoraire = groupItem.getHoraire();

                grp.putExtra(TAG_ID, groupItem.getId());
                grp.putExtra(TAG_ADMID, groupItem.getAdmin());
                grp.putExtra(TAG_DUREEH, groupItem.getDuree());
                grp.putExtra(TAG_NAME, groupItem.getName());
                grp.putExtra(TAG_NBMBMAX, groupItem.getMembresmax());
                grp.putExtra(TAG_NBMBINS, groupItem.getMembresinscrits());
                grp.putExtra(TAG_HORAIRE, groupItem.getHoraire());
                grp.putExtra(TAG_DATE, groupItem.getDate());
                grp.putExtra(TAG_URL, groupItem.getUrl());
                grp.putExtra(TAG_USERID, user_id);

                getActivity().startActivity(grp);

            }
        });

        myGroupeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GroupItem groupItem = arrayGroups.get(position);

                Intent grp = new Intent(getActivity().getBaseContext(), GroupActivity.class);

                String thegroupid = groupItem.getId();
                String theadmin = groupItem.getAdmin();
                String thehoraire = groupItem.getHoraire();

                grp.putExtra(TAG_ID, groupItem.getId());
                grp.putExtra(TAG_ADMID, groupItem.getAdmin());
                grp.putExtra(TAG_DUREEH, groupItem.getDuree());
                grp.putExtra(TAG_NAME, groupItem.getName());
                grp.putExtra(TAG_NBMBMAX, groupItem.getMembresmax());
                grp.putExtra(TAG_NBMBINS, groupItem.getMembresinscrits());
                grp.putExtra(TAG_HORAIRE, groupItem.getHoraire());
                grp.putExtra(TAG_DATE, groupItem.getDate());
                grp.putExtra(TAG_URL, groupItem.getUrl());
                grp.putExtra(TAG_USERID, user_id);

                getActivity().startActivity(grp);

            }
        });





        return rootView;
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

            paramsList.add(new BasicNameValuePair(EXTRA_EXTRA_UID,user_id));

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, paramsList);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    String jsonRGroups = wholeJson.getString("info_registeredgroups");
                    String jsonMGroups = wholeJson.getString("info_managedgroups");
                    Log.d("Response (rgroups) : ", "> " + jsonRGroups);
                    Log.d("Response (mgroups) : ", "> " + jsonMGroups);

                    //JSONArray contacts = new JSONArray(jsonStr);
                    JSONArray mesgroupes = new JSONArray(jsonRGroups);
                    JSONArray managedgroupes = new JSONArray(jsonMGroups);

                    for (int i = 0; i < mesgroupes.length(); i++) {

                        JSONObject d = mesgroupes.getJSONObject(i);

                        int group_id = d.getInt(TAG_ID);
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

                        contact.put(TAG_ID, String.valueOf(group_id));
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

                        arrayGroups.add(new GroupItem(name,date,duree_parc_heures,id_admin,String.valueOf(group_id),horaire,nombre_membres_max,nombre_membres_inscrits,url_parcours));
                    }

                    for (int i = 0; i < managedgroupes.length(); i++) {

                        JSONObject s = managedgroupes.getJSONObject(i);

                        int group_id = s.getInt(TAG_ID);
                        String id_admin = s.getString(TAG_ADMID);
                        String duree_parc_heures = s.getString(TAG_DUREEH);
                        String date = s.getString(TAG_DATE);
                        String horaire = s.getString(TAG_HORAIRE);
                        String nombre_membres_max = s.getString(TAG_NBMBMAX);
                        String nombre_membres_inscrits = s.getString(TAG_NBMBINS);
                        String name = s.getString(TAG_NAME);
                        String url_parcours = s.getString(TAG_URL);

                        // tmp hashmap for single contact

                        HashMap<String, String> contact = new HashMap<String, String>();

                        contact.put(TAG_ID, String.valueOf(group_id));
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

                        arrayGroups2.add(new GroupItem(name,date,duree_parc_heures,id_admin,String.valueOf(group_id),horaire,nombre_membres_max,nombre_membres_inscrits,url_parcours));
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

            groupAdapter = new GroupListAdapter(getActivity().getBaseContext(), arrayGroups);
            groupAdapter2 = new GroupListAdapter(getActivity().getBaseContext(), arrayGroups2);
            myGroupeView.setAdapter(groupAdapter);
            groupeView.setAdapter(groupAdapter2);

        }

    }


}
