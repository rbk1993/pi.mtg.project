package project.smarttrip.mytourguide.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.smarttrip.mytourguide.R;

public class ShowProfileFragment extends Fragment {
	
	public ShowProfileFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_show_profile, container, false);
         
        return rootView;
    }
}
