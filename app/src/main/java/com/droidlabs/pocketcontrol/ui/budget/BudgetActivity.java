package com.droidlabs.pocketcontrol.ui.budget;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.budget.Budget;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;


public class BudgetActivity extends Fragment {

    private Spinner budgetCategory;
    private CategoryViewModel categoryViewModel;
    private BudgetViewModel budgetViewModel;
    private PocketControlDB db = PocketControlDB.getDatabase(getContext());
    private Budget budget;
    private String name, category, svalue;
    private float value;
    private EditText nameEdit, categoryEdit, valueEdit;
    private Button button;

    @Override
    public final View onCreateView(
            final LayoutInflater inf, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.budget_activity, container, false);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        setBudgetCategorySpinner(view);

        nameEdit = (EditText) view.findViewById(R.id.budgetNameEdit);
        valueEdit = (EditText) view.findViewById(R.id.budgetValueEdit);

        button = view.findViewById(R.id.save_button);

        nameEdit.addTextChangedListener(budgetTextWatcher);
        valueEdit.addTextChangedListener(budgetTextWatcher);
        category = budgetCategory.getSelectedItem().toString();

       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                value = Float.parseFloat(svalue);
                budget = new Budget(value, name, category);
                budgetViewModel.insert(budget);

                Fragment fragment = new BudgetFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_budget, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Toast.makeText(getContext(), "Budget added", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }

    private TextWatcher budgetTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

        }

        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
            name = nameEdit.getText().toString().trim();
            svalue = valueEdit.getText().toString().trim();

            if (name.isEmpty() || svalue.isEmpty()) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(final Editable s) {

        }
    };

    /**
     * This method to set the spinner of Transaction Category.
     * @param view the transaction add layout
     */
    private void setBudgetCategorySpinner(final View view) {
        //get the spinner from the xml.
        budgetCategory = view.findViewById(R.id.spinnerBudgetCategory);
        //create a list of items for the spinner.
        String[] dropdownItems = categoryViewModel.getCategoriesName();
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, dropdownItems);
        //set the spinners adapter to the previously created one.
        budgetCategory.setAdapter(adapterCategory);
    }

}
