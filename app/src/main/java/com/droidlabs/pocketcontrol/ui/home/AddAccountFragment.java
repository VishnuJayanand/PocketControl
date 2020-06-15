package com.droidlabs.pocketcontrol.ui.home;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.account.Account;
import com.droidlabs.pocketcontrol.db.user.User;
import com.droidlabs.pocketcontrol.ui.signin.UserViewModel;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;

public class AddAccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private UserViewModel userViewModel;
    private TextInputLayout accountNameInputLayout;
    private TextInputEditText accountNameEditText;
    private CardView selectAccountColor;
    private String[] colorsStr = {"Blue", "Dark blue", "Green", "Dark green", "Orange", "Dark orange", "Purple", "Dark purple"};
    private int selectedAccountColor;
    private MaterialAlertDialogBuilder dialogBuilder;
    private SharedPreferencesUtils sharedPreferencesUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_add, container, false);

        Button addNewAccount = view.findViewById(R.id.addNewAccount);

        sharedPreferencesUtils = new SharedPreferencesUtils(getActivity().getApplication());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountNameInputLayout = view.findViewById(R.id.tilAccountName);
        accountNameEditText = view.findViewById(R.id.tiedtAccountName);
        selectAccountColor = view.findViewById(R.id.selectAccountColor);

        initializeColorDialog();

        selectedAccountColor = getResources().getColor(R.color.projectColorBlue, null);
        selectAccountColor.setCardBackgroundColor(selectedAccountColor);

        selectAccountColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.show();
            }
        });

        addNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        return view;
    }

    private boolean validateAccountName() {
        if (accountNameEditText.getText().toString().isEmpty()) {
            accountNameInputLayout.setError("Account name cannot be empty");
            return false;
        } else {
            accountNameInputLayout.setError(null);
        }

        if (accountNameEditText.getText().toString().equals("hi")) {
            accountNameInputLayout.setError("Account name already exists");
            return false;
        } else {
            accountNameInputLayout.setError(null);
        }

        return true;
    }

    private void createAccount() {
        if (!validateAccountName()) {
            return;
        }

        Account newAccount = new Account();

        newAccount.setName(accountNameEditText.getText().toString());
        newAccount.setColor(selectedAccountColor);

        long newAccountId = accountViewModel.insert(newAccount);

        userViewModel.updateUserSelectedAccount(String.valueOf(newAccountId));
        sharedPreferencesUtils.setCurrentAccountId(String.valueOf(newAccountId));

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initializeColorDialog() {
        dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the account color:")
                .setItems(colorsStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (colorsStr[which]) {
                    case "Blue":
                        selectedAccountColor = getResources().getColor(R.color.projectColorBlue, null);
                        selectAccountColor.setCardBackgroundColor(selectedAccountColor);
                        break;
                    case "Dark blue":
                        selectedAccountColor = getResources().getColor(R.color.projectColorDarkBlue, null);
                        selectAccountColor.setCardBackgroundColor(selectedAccountColor);
                        break;
                    case "Green":
                        selectedAccountColor = getResources().getColor(R.color.projectColorGreen, null);
                        selectAccountColor.setCardBackgroundColor(selectedAccountColor);
                        break;
                    case "Dark green":
                        selectedAccountColor = getResources().getColor(R.color.projectColorDarkGreen, null);
                        selectAccountColor.setCardBackgroundColor(selectedAccountColor);
                        break;
                    case "Orange":
                        selectedAccountColor = getResources().getColor(R.color.projectColorOrange, null);
                        selectAccountColor.setCardBackgroundColor(selectedAccountColor);
                        break;
                    case "Dark orange":
                        selectedAccountColor = getResources().getColor(R.color.projectColorDarkOrange, null);
                        selectAccountColor.setCardBackgroundColor(selectedAccountColor);
                        break;
                    case "Purple":
                        selectedAccountColor = getResources().getColor(R.color.projectColorPurple, null);
                        selectAccountColor.setCardBackgroundColor(selectedAccountColor);
                        break;
                    case "Dark purple":
                        selectedAccountColor = getResources().getColor(R.color.projectColorDarkPurple, null);
                        selectAccountColor.setCardBackgroundColor(selectedAccountColor);
                        break;
                    default:
                        selectedAccountColor = getResources().getColor(R.color.projectColorBlue, null);
                        selectAccountColor.setCardBackgroundColor(selectedAccountColor);
                        break;
                }
            }
        });
    }
}
