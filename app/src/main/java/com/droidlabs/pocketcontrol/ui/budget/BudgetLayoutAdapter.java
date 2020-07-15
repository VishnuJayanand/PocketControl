package com.droidlabs.pocketcontrol.ui.budget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.budget.Budget;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.ui.categories.CategoryViewModel;
import com.droidlabs.pocketcontrol.ui.settings.DefaultsViewModel;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionViewModel;
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class BudgetLayoutAdapter extends ArrayAdapter<Budget> {

    private CategoryViewModel categoryViewModel;
    private DefaultsViewModel defaultsViewModel;
    private BudgetViewModel budgetViewModel;
    private TransactionViewModel transactionViewModel;
    private LayoutInflater mInflater;
    private List<Budget> arrayList;
    private int mViewResourceId;
    private View convertView;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    /**
     * Creating adapter for Budget.
     * @param context context
     * @param textViewResourceId textViewResourceId
     * @param adapArrayList list of budget.
     * @param categoryVM category viewmodel.
     * @param defaultVM default viewmodel.
     * @param budgetVM budget view model.
     * @param transactionVM transaction view model.
     */
    public BudgetLayoutAdapter(
            final Context context,
            final int textViewResourceId,
            final List<Budget> adapArrayList,
            final CategoryViewModel categoryVM,
            final DefaultsViewModel defaultVM,
            final BudgetViewModel budgetVM,
            final TransactionViewModel transactionVM

    ) {
        super(context, textViewResourceId, adapArrayList);
        this.arrayList = adapArrayList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
        categoryViewModel = categoryVM;
        defaultsViewModel = defaultVM;
        budgetViewModel = budgetVM;
        transactionViewModel = transactionVM;
    }

    /**
     * Creating adapter for Budget.
     * @param position position
     * @param cView cView
     * @param parent parent
     * @return convertView
     */
    public View getView(final int position, final View cView, final ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        Budget budget = arrayList.get(position);

        if (budget != null) {
            Category category = categoryViewModel.getSingleCategory(Integer.parseInt(budget.getCategory()));

            String sCatId = String.valueOf(category.getId());

            Float budgetAmount = budget.getMaxAmount();
            int ibudgetAmount = budgetAmount.intValue();
            int itotalAmount = 0;

            Float totalAmount = transactionViewModel.getTotalIAmountByCategoryId(sCatId);
            itotalAmount = totalAmount.intValue();

            ProgressBar progressBar = convertView.findViewById(R.id.budgetBar);
            progressBar.setMax(ibudgetAmount);
            progressBar.setProgress(itotalAmount);

            TextView budgetname = (TextView) convertView.findViewById(R.id.budgetName);
            TextView budgetvalue = (TextView) convertView.findViewById(R.id.budgetValue);
            TextView budgetPercent = convertView.findViewById(R.id.budget_percent);

            float percent = (totalAmount / budgetAmount) * 100;

            budgetPercent.setText(String.valueOf(percent) + "%");
            if (percent >= 95) {
                budgetPercent.setTextColor(Color.RED);
            } else {
                budgetPercent.setTextColor(Color.LTGRAY);
            }

            if (budgetname != null) {
                budgetname.setText(category.getName());
            }
            if (budgetvalue != null) {
                String stringCurrencyCode = defaultsViewModel.getDefaultValue("Currency");
                String stringCurrency = defaultsViewModel.getCurrencySymbol(stringCurrencyCode);
                budgetvalue.setText(CurrencyUtils.formatAmount(budget.getMaxAmount(), stringCurrency));
            }

            LinearLayout deleteTransactionButton = convertView.findViewById(R.id.delete_Budget);
            deleteTransactionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                    builder.setTitle("Delete Budget?");
                    builder.setMessage("Are you sure you want to delete the Budget?");
                    builder.setBackground(getContext().getDrawable(
                            (R.drawable.alert_dialogue_box)));
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            budgetViewModel.deleteBudget(budget.getId());

                            Toast.makeText(getContext(), "Budget Deleted", Toast.LENGTH_SHORT).show();

                            Fragment fragment = new BudgetFragment();
                            FragmentManager manager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                            FragmentTransaction fragmentBudget = manager.beginTransaction();
                            fragmentBudget.replace(R.id.fragment_budget, fragment);
                            fragmentBudget.commit();

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });

        }

        return convertView;
    }
}
