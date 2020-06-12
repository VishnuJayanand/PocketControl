package com.droidlabs.pocketcontrol.ui.budget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.budget.Budget;
import com.droidlabs.pocketcontrol.db.category.Category;
import com.droidlabs.pocketcontrol.db.category.CategoryDao;
import com.droidlabs.pocketcontrol.utils.CurrencyUtils;

import java.util.List;

public class BudgetLayoutAdapter extends ArrayAdapter<Budget> {

    private CategoryDao categoryDao;
    private LayoutInflater mInflater;
    private List<Budget> arrayList;
    private int mViewResourceId;
    private View convertView;

    /**
     * Creating adapter for Budget.
     * @param context context
     * @param textViewResourceId textViewResourceId
     * @param adapArrayList list of budget
     */
    public BudgetLayoutAdapter(final Context context, final int textViewResourceId, final List<Budget> adapArrayList) {
        super(context, textViewResourceId, adapArrayList);
        this.arrayList = adapArrayList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
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
            categoryDao = PocketControlDB.getDatabase(getContext()).categoryDao();
            Category category = categoryDao.getSingleCategory(Integer.parseInt(budget.getCategory()));

            TextView budgetname = (TextView) convertView.findViewById(R.id.budgetName);
            TextView budgetvalue = (TextView) convertView.findViewById(R.id.budgetValue);
            if (budgetname != null) {
                budgetname.setText(category.getName());
            }
            if (budgetvalue != null) {
                budgetvalue.setText(CurrencyUtils.formatAmount(budget.getMaxAmount()));
            }
        }

        return convertView;
    }
}
