package com.droidlabs.pocketcontrol.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.droidlabs.pocketcontrol.R;

public class TransactionFragment extends Fragment {
    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        return inf.inflate(R.layout.fragment_transaction, container, false);
    }
}
