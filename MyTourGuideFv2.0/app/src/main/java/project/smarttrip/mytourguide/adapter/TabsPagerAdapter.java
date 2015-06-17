package project.smarttrip.mytourguide.adapter;

/**
 * Created by invite on 17/06/15.
 */
import project.smarttrip.mytourguide.fragment.GroupInfoFragment;
import project.smarttrip.mytourguide.fragment.GroupMapFragment;
import project.smarttrip.mytourguide.fragment.GroupWallFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new GroupInfoFragment();
            case 1:
                // Games fragment activity
                return new GroupMapFragment();
            case 2:
                // Movies fragment activity
                return new GroupWallFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}