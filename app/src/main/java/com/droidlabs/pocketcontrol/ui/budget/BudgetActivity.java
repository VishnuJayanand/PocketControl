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

public class BudgetActivity extends Fragment {

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

        nameEdit = (EditText) view.findViewById(R.id.budgetNameEdit);
        categoryEdit = (EditText) view.findViewById(R.id.budgetCategoryEdit);
        valueEdit = (EditText) view.findViewById(R.id.budgetValueEdit);

        button = view.findViewById(R.id.save_button);

        nameEdit.addTextChangedListener(budgetTextWatcher);
        categoryEdit.addTextChangedListener(budgetTextWatcher);
        valueEdit.addTextChangedListener(budgetTextWatcher);

       button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                value = Float.parseFloat(svalue);

                budget = new Budget(value, name);
                db.budgetDao().insert(budget);
                Toast.makeText(getContext(), "Entry added", Toast.LENGTH_SHORT).show();

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
            category = categoryEdit.getText().toString().trim();
            svalue = valueEdit.getText().toString().trim();

            if (name.isEmpty() || category.isEmpty() || svalue.isEmpty()) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(final Editable s) {

        }
    };
}
