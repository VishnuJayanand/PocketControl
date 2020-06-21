package com.droidlabs.pocketcontrol.ui.budget;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.budget.Budget;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
import com.droidlabs.pocketcontrol.ui.settings.DefaultsViewModel;
import com.droidlabs.pocketcontrol.utils.FormatterUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;


public class BudgetActivity extends Fragment {

    private TextInputEditText tiedtBudgetAmount;
    private TextInputLayout tilAmount, tilCategory;
    private TextInputEditText dropdownBudgetCategory;
    private Spinner budgetCategory;
    private CategoryViewModel categoryViewModel;
    private BudgetViewModel budgetViewModel;
    private DefaultsViewModel defaultsViewModel;

    private PocketControlDB db = PocketControlDB.getDatabase(getContext());
    private Budget budget;
    private String name, svalue;
    private float value;
    private EditText nameEdit, categoryEdit, valueEdit;
    private Button button;

    @Override
    public final View onCreateView(
            final LayoutInflater inf, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.budget_activity, container, false);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        defaultsViewModel = new ViewModelProvider(this).get(DefaultsViewModel.class);

        tiedtBudgetAmount = view.findViewById(R.id.budgetAmount);
        tilAmount = view.findViewById(R.id.tileAmount);
        tilCategory = view.findViewById(R.id.tilCategory);
        button = view.findViewById(R.id.save_button);

        setBudgetCategorySpinner(view);

        tiedtBudgetAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (!hasFocus) {
                    Editable editableContent = tiedtBudgetAmount.getText();

                    if (editableContent != null) {
                        try {
                            float amtValue = Float.parseFloat(editableContent.toString());
                            tiedtBudgetAmount.setText(FormatterUtils.roundToTwoDecimals(amtValue));
                        } catch (Exception e) { }
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                submitForm();
            }
        });

        return view;
    }

    /**
     * Validate the input of category name if it's already exist or not.
     * @param category string
     * @return boolean if input is exist or not
     */
    private boolean validateIfCategoryName(final String category) {
        Budget budgetValidate = budgetViewModel.getBudgetForCategory(category);
        if (budgetValidate == null) {
            return true;
        }
        return false;
    }

    /**
     * This method to set the spinner of Transaction Category.
     * @param view the transaction add layout
     */
    private void setBudgetCategorySpinner(final View view) {

        dropdownBudgetCategory = view.findViewById(R.id.spinnerBudgetCategory);
        String[] dropdownItems = categoryViewModel.getCategoriesName();

        //set currency spinner value
        String stringCategory = defaultsViewModel.getDefaultValue("Category");
        dropdownBudgetCategory.setText(stringCategory);

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getContext())
                .setTitle("Select the budget category")
                .setItems(dropdownItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dropdownBudgetCategory.setText(dropdownItems[which]);
                    }
                });

        dropdownBudgetCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (hasFocus) {
                    dialogBuilder.show();
                }
            }
        });

        dropdownBudgetCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogBuilder.show();
            }
        });
        dropdownBudgetCategory.setInputType(0);
    }

    /**
     * Method to check the input of transaction Date.
     * @return Boolean if the input is qualify or not
     */
    private boolean checkTransactionCategory() {
        String[] dropdownItems = categoryViewModel.getCategoriesName();
        if (!Arrays.asList(dropdownItems).contains(dropdownBudgetCategory.getText().toString().trim())) {
            tilCategory.setError("Category is not valid");
            requestFocus(dropdownBudgetCategory);
            return false;
        }
        return true;
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
     * Method to check the input of transaction amount.
     * @return Boolean if the input is qualify or not
     */
    private boolean checkBudgetAmount() {
        if (tiedtBudgetAmount.getText().toString().trim().isEmpty()) {
            tilAmount.setError("Please enter the amount of your transaction");
            requestFocus(tiedtBudgetAmount);
            return false;
        }

        if (Float.parseFloat(tiedtBudgetAmount.getText().toString().trim()) <= 0) {
            tilAmount.setError("Amount should be larger than 0");
            requestFocus(tiedtBudgetAmount);
            return false;
        }
        return true;
    }

    /**
     * Method to check the input of transaction amount is Acceptable or not.
     * @return Boolean if the input is Acceptable or not
     */
    private boolean checkBudgetAmountIsAcceptable() {
        float amount = Float.valueOf(tiedtBudgetAmount.getText().toString().trim());
        boolean isInfinite = Float.isInfinite(amount);
        double amountLimited = 999999999;
        if (isInfinite) {
            tilAmount.setError("The amount of transaction is infinity please enter different amount");
            requestFocus(tiedtBudgetAmount);
            return false;
        }
        if (amount > amountLimited) {
            tilAmount.setError("The amount of transaction need to be between 0 - 999999999");
            requestFocus(tiedtBudgetAmount);
            return false;
        }
        return true;
    }

    /**
     * Submit method to submit the input from user.
     */
    private void submitForm() {
        if (!checkBudgetAmount()) {
            return;
        }
        if (!checkBudgetAmountIsAcceptable()) {
            return;
        }
        if (!checkTransactionCategory()) {
            return;
        }
        Category selectedCategory = categoryViewModel.getSingleCategory(dropdownBudgetCategory.getText().toString());

        if (validateIfCategoryName(selectedCategory.getId().toString())) {

            float bAmount = Float.parseFloat(tiedtBudgetAmount.getText().toString().trim());

            budget = new Budget(bAmount, selectedCategory.getId().toString());

            budgetViewModel.insert(budget);

            Fragment fragment = new BudgetFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_budget, fragment);
            //TODO - fix this bug
            // fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            Toast.makeText(getContext(), "Budget added", Toast.LENGTH_SHORT).show();
        } else {
            String message = "Budget for this category already exist. Please choose other category";
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
