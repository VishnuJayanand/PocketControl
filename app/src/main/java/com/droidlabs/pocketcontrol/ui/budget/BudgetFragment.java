package com.droidlabs.pocketcontrol.ui.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.budget.Budget;
import com.droidlabs.pocketcontrol.db.budget.BudgetDao;
import com.droidlabs.pocketcontrol.db.budget.BudgetRepository;

import java.util.List;

public class BudgetFragment extends Fragment {

    private PocketControlDB db = PocketControlDB.getDatabase(getContext());
    private BudgetRepository budgetRepository;
    private List<Budget> allBudgets;
    private ArrayAdapter<String> nameAdapter;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_budget, container, false);

        ListView listView = (ListView) view.findViewById(R.id.budget_list);
        final TextView helpText = (TextView) view.findViewById(R.id.helpText);

        budgetRepository = new BudgetRepository(getActivity().getApplication());
        allBudgets = budgetRepository.getAllBudgets();

        if (allBudgets.isEmpty()) {
            helpText.setVisibility(View.VISIBLE);
            helpText.setText("You don't have any budgets at present, please add one");
        } else {
            BudgetLayoutAdapter adapter = new BudgetLayoutAdapter(getContext(), R.layout.budget_layout, allBudgets);
            listView = (ListView) view.findViewById(R.id.budget_list);
            listView.setAdapter(adapter);
        }

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                BudgetActivity budgetActivity = new BudgetActivity();
                BudgetFragment budgetFragment = new BudgetFragment();

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_budget, budgetFragment).commit();
                getChildFragmentManager().beginTransaction().replace(R.id.fragment_budget, budgetActivity).commit();

            }
        });

        return view;
    }
}
