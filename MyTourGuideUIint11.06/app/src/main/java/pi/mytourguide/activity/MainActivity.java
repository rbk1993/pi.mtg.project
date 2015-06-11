package pi.mytourguide.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import pi.mytourguide.R;


public class MainActivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener {



    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;


    private String provider;
    // la variable i est juste pour le test
    int i=0;


    public MainActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
            drawerFragment.setDrawerListener(this);

            // display the first navigation drawer view on app launch
            displayView(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //* Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent i = new Intent(getApplicationContext(), AuthentificationActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();
            return true;
        }

        //if(id == R.id.action_search){
        //Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
        //return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = new MainPageFragment();
                title = getString(R.string.title_parcours);

                break;
            case 2:
                fragment = new CotourismeFragment();
                title = getString(R.string.title_cotourisme);

                break;
            case 3:
                fragment = new AfficherGroupFragment();
                title = getString(R.string.title_groupe);

                break;
            case 4:
                fragment = new CreateGroupFragment();
                title = getString(R.string.title_create_group);
                break;
            case 5:
                fragment = new ManageGroupFragment();
                title = getString(R.string.title_manage_group);

                break;



            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

}
