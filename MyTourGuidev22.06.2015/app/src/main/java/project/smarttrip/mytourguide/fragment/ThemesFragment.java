package project.smarttrip.mytourguide.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.smarttrip.mytourguide.R;
import project.smarttrip.mytourguide.activity.MapActivity;
import project.smarttrip.mytourguide.adapter.GridViewAdapter;
import project.smarttrip.mytourguide.utils.ServerAddress;
import project.smarttrip.mytourguide.utils.ServiceHandler;


public class ThemesFragment extends Fragment {

    public static final int MARKER_SELECT = 1;
    public static final int RESULT_OK = 1;
    private static final String TAG_NOMTHEME = "nom_theme";
    private static final String TAG_ID = "id_theme";

    private GridViewAdapter adapter;

    public static final String TAG_PACKAGE = "project.smarttrip.mytourguide.activity";

    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_LONG = "longitude";
    public static final String EXTRA_LAT = "latitude";
    private ProgressDialog pDialog;

    private static String serverIP= ServerAddress.whatIs();

    private static String url = "http://"+serverIP+":8080/mainpagethemes";

    HashMap<String, String> imgHashMap = new HashMap<String, String>();
    HashMap<String, Bitmap> bitmapHashMap = new HashMap<String, Bitmap>();

    GridView gv;
    Context context;
    ArrayList prgmName;

    public static String[] prgmNameList={"restauration", "patrimoine_culturel", "patrimoine_naturel", "degustation", "commerce_et_service", "hotellerie_plein_air", "restaurant_traditionnel", "bar_et_pub", "patrimoine_mondial_unesco"};
    public static int [] prgmImages={R.drawable.gastronomie,R.drawable.culture,R.drawable.nature,R.drawable.degustation,R.drawable.commerce_et_services,R.drawable.plein_air,R.drawable.resto_trad, R.drawable.bar_pub, R.drawable.unesco};

    ArrayList<String> ReslistId = new ArrayList<String>();
    ArrayList<String> ResListLongitudes = new ArrayList<String>();;
    ArrayList<String> ReslistLatitudes = new ArrayList<String>();;
    ArrayList<String> ReslistDescription = new ArrayList<String>();;

    public static final String EXTRA_EXTRA_UID = "user_id";

    private String UID;

    public ThemesFragment() {// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_themes, container, false);

        Bundle bundle = getArguments();

        UID = bundle.getString(EXTRA_EXTRA_UID);

        gv=(GridView) rootView.findViewById(R.id.gridView1);

        adapter = new GridViewAdapter(this.getActivity(),prgmNameList,prgmImages);

        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent thm = new Intent(getActivity().getBaseContext(), MapActivity.class);
                thm.putExtra("theme", (String) adapter.getItem(position));

                if (ReslistId.size() > 0) {
                    thm.putStringArrayListExtra(TAG_PACKAGE + EXTRA_DESCRIPTION, ReslistDescription);
                    thm.putStringArrayListExtra(TAG_PACKAGE + EXTRA_LAT, ReslistLatitudes);
                    thm.putStringArrayListExtra(TAG_PACKAGE + EXTRA_LONG, ResListLongitudes);
                    thm.putStringArrayListExtra(TAG_PACKAGE + EXTRA_ID, ReslistId);
                }
                thm.putExtra(EXTRA_EXTRA_UID, UID);
                startActivityForResult(thm, RESULT_OK);
                ReslistId.clear();
                ReslistDescription.clear();
                ResListLongitudes.clear();
                ReslistLatitudes.clear();
            }
        });

        //int s = ReslistId.size();
        //new GetContacts().execute();

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MARKER_SELECT) {

            if (resultCode == RESULT_OK) {

                if(data.hasExtra(TAG_PACKAGE+EXTRA_DESCRIPTION) && data.hasExtra(TAG_PACKAGE+EXTRA_LONG) &&
                        data.hasExtra(TAG_PACKAGE+EXTRA_LAT) && data.hasExtra(TAG_PACKAGE+EXTRA_ID)) {

                    ResListLongitudes = data.getStringArrayListExtra(TAG_PACKAGE+EXTRA_LONG);
                    ReslistLatitudes = data.getStringArrayListExtra(TAG_PACKAGE+EXTRA_LAT);
                    ReslistDescription = data.getStringArrayListExtra(TAG_PACKAGE+EXTRA_DESCRIPTION);
                    ReslistId = data.getStringArrayListExtra(TAG_PACKAGE+EXTRA_ID);
                }

            }
        }
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            //pDialog = new ProgressDialog(ThemesFragment.this);
            //pDialog.setMessage("Please wait...");
            //pDialog.setCancelable(false);
            //pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) throws NumberFormatException {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            /*TODO: Fct1() : Charger à partir de la mémoire interne un JSON
             * Avec ID Image disponibles et noms de thème associés
             * [{ "id_theme" : "..." , "nom_theme" : "..." },{...},{...}] */
            // [{ "id_theme" : 1 , "nom_theme" : "culture" }{ "id_theme" : 3, "nom_theme" : "nature" }]

            //Fct1() Manuellement ...
            try {

                JSONArray imgthmJA = new JSONArray();
                JSONObject obj1 = new JSONObject();
                JSONObject obj2 = new JSONObject();
                JSONObject obj3 = new JSONObject();
                JSONObject obj4 = new JSONObject();
                obj1.put(TAG_ID,1); obj1.put(TAG_NOMTHEME,"culture");
                obj2.put(TAG_ID,3); obj2.put(TAG_NOMTHEME,"nature");
                obj3.put(TAG_ID,4); obj3.put(TAG_NOMTHEME,"gastronomie");
                obj4.put(TAG_ID,5); obj4.put(TAG_NOMTHEME,"amusement");
                //imgthmJA.put(obj1);
                //imgthmJA.put(obj2);
                //imgthmJA.put(obj3);
                imgthmJA.put(obj4);
                List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
                paramsList.add(new BasicNameValuePair("jsonarray",imgthmJA.toString()));
                Log.d("JsonArray : ", "> " + imgthmJA.toString());
                String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET, paramsList);

                Log.d("Response: ", "> " + jsonStr);


                if (jsonStr != null) {

                    JSONObject wholeJson = new JSONObject(jsonStr);

                    JSONArray imgToDelete = wholeJson.getJSONArray("imgToDelete");
                    JSONArray imgToDownload = wholeJson.getJSONArray("imgToDownload");
                    JSONArray imgOkay = wholeJson.getJSONArray("imgOkay");

                    Log.d("Response (todelete) : ", "> " + imgToDelete);
                    Log.d("Response: (todownload) ", "> " + imgToDownload);
                    Log.d("Response: (okay) ", "> " + imgOkay);


                    for (int i = 0; i < imgToDownload.length(); i++) {

                        JSONObject d = imgToDownload.getJSONObject(i);

                        imgHashMap.put(d.getString("nom_theme"),d.getString("image"));
                    }

                    for(String nomTheme : imgHashMap.keySet()) {
                        String byteString = imgHashMap.get(nomTheme);
                        Log.d("Response (success) : ", "> " + byteString);

                        String[] byteValues = byteString.substring(1, byteString.length() - 1).split(",");

                        byte[] byteArray = new byte[byteValues.length];

                        for (int j = 0; j < byteValues.length; j++) {
                            byteArray[j] = new Byte(byteValues[j]);
                        }

                        bitmapHashMap.put(nomTheme, BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                    }

                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog

            //if (pDialog.isShowing())
            //pDialog.dismiss();

            //gv.setAdapter(new CustomAdapter(ThemesFragment.this, bitmapHashMap.keySet(), bitmapHashMap.values()));
            /*
            for(String nomTheme : bitmapHashMap.keySet()) {

                //ImageView image = new ImageView(ThemesFragment.this);



                image.setImageBitmap(bitmapHashMap.get(nomTheme));
                image.setClickable(true);

                image.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ThemesFragment.this,MapActivity.class);
                        startActivity(intent);
                    }
                });


            }
            */
        }

    }

}