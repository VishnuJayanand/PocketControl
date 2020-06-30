package com.droidlabs.pocketcontrol.ui.home;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.droidlabs.pocketcontrol.ui.signin.UserViewModel;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddAccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private UserViewModel userViewModel;
    private TextInputLayout accountNameInputLayout;
    private TextInputEditText accountNameEditText;
    private CardView selectAccountColor;
    private String[] colorsStr = {
            "Blue",
            "Dark blue",
            "Green",
            "Dark green",
            "Orange",
            "Dark orange",
            "Purple",
            "Dark purple"
    };
    private String selectedAccountColor;
    private MaterialAlertDialogBuilder dialogBuilder;
    private SharedPreferencesUtils sharedPreferencesUtils;

    /**
     * On create view.
     * @param inflater inflated.
     * @param container container.
     * @param savedInstanceState saved state.
     * @return view.
     */
    @Nullable
    @Override
    public View onCreateView(
            final @NonNull LayoutInflater inflater,
            final @Nullable ViewGroup container,
            final @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.account_add, container, false);

        Button addNewAccount = view.findViewById(R.id.addNewAccount);

        sharedPreferencesUtils = new SharedPreferencesUtils(getActivity().getApplication());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountNameInputLayout = view.findViewById(R.id.tilAccountName);
        accountNameEditText = view.findViewById(R.id.tiedtAccountName);
        selectAccountColor = view.findViewById(R.id.selectAccountColor);

        initializeColorDialog();

        selectedAccountColor = getResources().getString(0 + R.color.projectColorBlue);
        selectAccountColor.setCardBackgroundColor(Color.parseColor(selectedAccountColor));

        selectAccountColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });

        addNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                createAccount();
            }
        });

        return view;
    }

    /**
     * Method to focus on the view that have the wrong input.
     * @param view the view
     */
    public void requestFocus(final View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Validate account name.
     * @return whether account name is valid.
     */
    private boolean validateAccountName() {
        String[] accountNames = accountViewModel.getAccountNames();
        boolean isNameTaken = false;

        if (accountNameEditText.getText().toString().isEmpty()) {
            accountNameInputLayout.setError("Account name cannot be empty.");
            return false;
        } else {
            accountNameInputLayout.setError(null);
        }

        if (!accountNameEditText.getText().toString().trim().matches(".*[a-zA-Z]+.*")) {
            accountNameInputLayout.setError("Account name should have at least one character.");
            requestFocus(accountNameEditText);
            return false;
        } else {
            accountNameInputLayout.setError(null);
        }

        for (String s : accountNames) {
            if (s.equalsIgnoreCase(accountNameEditText.getText().toString())) {
                isNameTaken = true;
            }
        }

        if (isNameTaken) {
            accountNameInputLayout.setError("Account name already taken.");
            requestFocus(accountNameEditText);
            return false;
        } else {
            accountNameInputLayout.setError(null);
        }

        return true;
    }

    /**
     * Create account.
     */
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

    /**
     * Populate color dialog.
     */
    private void initializeColorDialog() {
        dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the account color:")
                .setItems(colorsStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                switch (colorsStr[which]) {
                    case "Blue":
                        selectedAccountColor = getResources().getString(0 + R.color.projectColorBlue);
                        selectAccountColor.setCardBackgroundColor(Color.parseColor(selectedAccountColor));
                        break;
                    case "Dark blue":
                        selectedAccountColor = getResources().getString(0 + R.color.projectColorDarkBlue);
                        selectAccountColor.setCardBackgroundColor(Color.parseColor(selectedAccountColor));
                        break;
                    case "Green":
                        selectedAccountColor = getResources().getString(0 + R.color.projectColorGreen);
                        selectAccountColor.setCardBackgroundColor(Color.parseColor(selectedAccountColor));
                        break;
                    case "Dark green":
                        selectedAccountColor = getResources().getString(0 + R.color.projectColorDarkGreen);
                        selectAccountColor.setCardBackgroundColor(Color.parseColor(selectedAccountColor));
                        break;
                    case "Orange":
                        selectedAccountColor = getResources().getString(0 + R.color.projectColorOrange);
                        selectAccountColor.setCardBackgroundColor(Color.parseColor(selectedAccountColor));
                        break;
                    case "Dark orange":
                        selectedAccountColor = getResources().getString(0 + R.color.projectColorDarkOrange);
                        selectAccountColor.setCardBackgroundColor(Color.parseColor(selectedAccountColor));
                        break;
                    case "Purple":
                        selectedAccountColor = getResources().getString(0 + R.color.projectColorPurple);
                        selectAccountColor.setCardBackgroundColor(Color.parseColor(selectedAccountColor));
                        break;
                    case "Dark purple":
                        selectedAccountColor = getResources().getString(0 + R.color.projectColorDarkPurple);
                        selectAccountColor.setCardBackgroundColor(Color.parseColor(selectedAccountColor));
                        break;
                    default:
                        selectedAccountColor = getResources().getString(0 + R.color.projectColorBlue);
                        selectAccountColor.setCardBackgroundColor(Color.parseColor(selectedAccountColor));
                        break;
                }
            }
        });
    }
}
