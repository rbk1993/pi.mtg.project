package project.smarttrip.mytourguide.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.smarttrip.mytourguide.R;


public class SearchGroupFragment extends Fragment {
	
	public SearchGroupFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_search_group, container, false);
         
        return rootView;
    }
}
