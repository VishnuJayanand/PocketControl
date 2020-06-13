package com.droidlabs.pocketcontrol.ui.budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.droidlabs.pocketcontrol.R;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.droidlabs.pocketcontrol.db.budget.Budget;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;

import java.util.List;

public class BudgetFragment extends Fragment {

    private BudgetViewModel budgetViewModel;
    private CategoryViewModel categoryViewModel;
    private List<Budget> allBudgets;
    private ArrayAdapter<String> nameAdapter;

    @Nullable
    @Override
    public final View onCreateView(
            final LayoutInflater inf, final @Nullable ViewGroup container, final @Nullable Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_budget, container, false);

        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        ListView listView = (ListView) view.findViewById(R.id.budget_list);
        LinearLayout emptyTransactions = view.findViewById(R.id.emptyPageViewWrapper);

        allBudgets = budgetViewModel.getAllBudgets();

        if (allBudgets.isEmpty()) {
            emptyTransactions.setVisibility(View.VISIBLE);
        } else {
            emptyTransactions.setVisibility(View.GONE);
            BudgetLayoutAdapter adapter = new BudgetLayoutAdapter(
                    getContext(),
                    R.layout.budget_layout,
                    allBudgets,
                    categoryViewModel
            );
            listView = (ListView) view.findViewById(R.id.budget_list);
            listView.setAdapter(adapter);
        }

        AppCompatButton  addBudget = view.findViewById(R.id.button);
        addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Fragment fragment = new BudgetActivity();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentBudget = fragmentManager.beginTransaction();
                fragmentBudget.replace(R.id.fragment_budget, fragment);
                fragmentBudget.addToBackStack(null);
                fragmentBudget.commit();

            }
        });

        return view;
    }
}
