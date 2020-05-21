package com.droidlabs.pocketcontrol.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class AddCategoryFragment extends Fragment {
    private TextInputEditText tiedtCategoryName;
    private TextInputLayout tilCategoryName;
    private Spinner dropdown;
    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.category_add, container, false);

        tilCategoryName = view.findViewById(R.id.til_categoryName);
        tiedtCategoryName = view.findViewById(R.id.tiedt_categoryName);
        Button btnAdd = view.findViewById(R.id.addNewCategory);
        //Set spinner
        setCategoryIconSpinner(view);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                submitForm();
            }
        });
        return view;
    }

    /**
     * This method to set the spinner of Transaction Type.
     * @param view the transaction add layout
     */
    private void setCategoryIconSpinner(final View view) {
        //get the spinner from the xml.
        dropdown = view.findViewById(R.id.spinnerCategoryIcon);
        //create a list of items for the spinner.
        String[] dropdownItems = new String[]{"food", "study", "health", "rent", "shopping", "transport"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
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
     * Validate the input of category name.
     * @return boolean if input is qualify or not.
     */
    private boolean validateCategoryName() {
        if (tiedtCategoryName.getText().toString().trim().isEmpty()) {
            tilCategoryName.setError("Please enter category name");
            requestFocus(tiedtCategoryName);
            return false;
        }
        return true;
    }

    /**
     * Submit method to submit the input from user.
     */
    private void submitForm() {
        if (!validateCategoryName()) {
            return;
        }
        String categoryIcon = dropdown.getSelectedItem().toString();
        int resID = this.getResources().getIdentifier(categoryIcon, "drawable", getContext().getPackageName());
        String categoryName = tiedtCategoryName.getText().toString().trim() + "";
        Category newCategory = new Category(categoryName, resID);
        //Get CategoryViewModel
        final CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        //Insert new Category in to the database
        categoryViewModel.insert(newCategory);

        Toast.makeText(getContext(), categoryName, Toast.LENGTH_LONG).show();
    }
}
