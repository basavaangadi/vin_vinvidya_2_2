package com.vinuthana.vinvidya.fragments.noticeBoardFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinuthana.vinvidya.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentsMessageByDateFragment extends Fragment {


    public ParentsMessageByDateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parents_message_by_date, container, false);
    }

}
