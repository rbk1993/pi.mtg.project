package project.smarttrip.mytourguide.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import project.smarttrip.mytourguide.R;
import project.smarttrip.mytourguide.fragment.ShowGroupFragment;

public class GroupActivity extends Activity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();

        Bundle bundle = new Bundle();
        bundle.putString(TAG_ADMID,intent.getStringExtra(TAG_ADMID));
        bundle.putString(TAG_DATE,intent.getStringExtra(TAG_DATE));
        bundle.putString(TAG_DUREEH,intent.getStringExtra(TAG_DUREEH));
        bundle.putString(TAG_HORAIRE,intent.getStringExtra(TAG_HORAIRE));
        bundle.putString(TAG_ID,intent.getStringExtra(TAG_ID));
        bundle.putString(TAG_NAME,intent.getStringExtra(TAG_NAME));
        bundle.putString(TAG_NBMBINS,intent.getStringExtra(TAG_NBMBINS));
        bundle.putString(TAG_NBMBMAX,intent.getStringExtra(TAG_NBMBMAX));
        bundle.putString(TAG_URL,intent.getStringExtra(TAG_URL));
        bundle.putString(TAG_USERID,intent.getStringExtra(TAG_USERID));

        Fragment fragment = new ShowGroupFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.groupcontainer, fragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group, menu);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
