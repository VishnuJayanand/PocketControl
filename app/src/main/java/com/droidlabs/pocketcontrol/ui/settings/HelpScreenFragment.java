package com.droidlabs.pocketcontrol.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.droidlabs.pocketcontrol.R;

public class HelpScreenFragment extends Fragment {
    private ExpandableListView expandableTextView;

    /**
     * To send the email, input subject and message to gmail.
     * @param inf LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(
            final @NonNull LayoutInflater inf,
            final  @Nullable ViewGroup container,
            final  @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_help, container, false);

        expandableTextView = view.findViewById(R.id.eTV);
        HelpAdapter adapter = new HelpAdapter(getContext());
        expandableTextView.setAdapter(adapter);


        return view;
    }
}
