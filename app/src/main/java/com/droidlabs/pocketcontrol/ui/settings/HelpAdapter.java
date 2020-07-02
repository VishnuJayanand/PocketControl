package com.droidlabs.pocketcontrol.ui.settings;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidlabs.pocketcontrol.R;


public class HelpAdapter extends BaseExpandableListAdapter {

    private Context context;


    private String[] faqs = {
            "Q1. How can I update a transaction?",
            "Q2. Which data is exported?",
            "Q3. What is a Public Category?",
            "Q4. What happens if there is no internet connection?",
            "Q5. Can I delete a budget?",
            "Q6. What is a recurring transaction?",
            "Q7. Can I add more icons?",
            "Q8. How can I contact support team?",
            "Q9. Why does the Total Net Balance on the Home Screen differ from the Account Balance?",
            "Q10. Can I filter transactions?",
            "Q11. I see an option 'Add a friend' in Add Transaction. What is it about?"
    };

    private String[][] answer = {
            {"You can edit, duplicate or delete a transaction by swiping left to a transaction and "
                    + "revealing the buttons to edit them"},
            {"The data exported to Google Drive will be a zip file. It will contain 5 files of "
                    + "the format .csv - Accounts, Budgets, Categories, Defaults and Transactions."
                    + "All the data exported will be specific to the user"},
            {"A Public Category is shown across all the accounts of a user. Other new category,"
                    + "when created, will be shown only in the respective account which it is created from."},
            {"Certain functionalities like currency conversion with up-to-date rates and export data will not"
                    + " function as there is a dependency on the internet. A message will be shown"
                    + " to the user and function will be disabled wherever applicable"},
            {"Yes, of course. In case you have crossed the budget limit, you will get a notification"
                    + " saying that you have exceeded the budget."},
            {"Based on our experiences, a user may have an expense occurring at regular intervals."
                    + " You can set a daily, weekly, monthly or even a custom interval transaction."},
            {"We have provided a set of icons based on lifestyle of people. You can"
                    + " contact us via the 'Contact Support' option in the Settings Screen if you"
                    + " want us to add more icons to the app."},
            {"You can contact us via the 'Contact Support' option in the Settings Screen"},
            {"Total Net Balance is the balance from the all the accounts whereas the Account Balance"
                    +
                    " conforms to the selected Account Balance"},
            {"Yes, you can filter transactions by click on the 'Filters' button in the Transactions"
                    +
                    " Screen. You have the option to select a Date range, a Category and a Price range"},
            {"This option is for you to track expenses whenever you have lent to or borrowed from a person."
                    +
                    " It allows you to choose a contact from your phone to keep track of that"
                    +
                    " transaction"}

    };

    private int[][] images = {
            {R.drawable.update},
            {R.drawable.no_internet},
            {R.drawable.filters}
    };

    /**
     * Creating help adapter for Settings.
     * @param helpContext context
     */
    public HelpAdapter(final Context helpContext) {
        this.context = helpContext;
    }

    /**
     * get group count.
     */
    @Override
    public int getGroupCount() {
        return faqs.length;
    }

    /**
     * get children count.
     */
    @Override
    public int getChildrenCount(final int groupPosition) {
        return answer[groupPosition].length;
    }

    /**
     * get group.
     */
    @Override
    public Object getGroup(final int groupPosition) {
        return faqs[groupPosition];
    }

    /**
     * get children .
     */
    @Override
    public Object getChild(final int groupPosition, final int childPosition) {
        return answer[groupPosition][childPosition];
    }

    /**
     * get group id.
     */
    @Override
    public long getGroupId(final int groupPosition) {
        return groupPosition;
    }

    /**
     * get children id.
     */
    @Override
    public long getChildId(final int groupPosition, final int childPosition) {
        return childPosition;
    }

    /**
     * has stable id.
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * get groupview.
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     */
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             final View convertView, final ViewGroup parent) {
        String questionFaq = (String) getGroup(groupPosition);
        View cGroupView = convertView;
        if (cGroupView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            cGroupView = inflater.inflate(R.layout.faq_title, null);
        }
        TextView questionfaq2 = cGroupView.findViewById(R.id.faqTitle);
        questionfaq2.setTypeface(null, Typeface.BOLD);
        questionfaq2.setText(questionFaq);
        return cGroupView;
    }

    /**
     * get child view.
     * @param groupPosition
     * @param childPosition
     * @param convertView
     * @param isLastChild
     * @param parent
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild,
                             final View convertView, final ViewGroup parent) {
        final String answerFaq = (String) getChild(groupPosition, childPosition);
        View cChildView = convertView;
        if (cChildView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cChildView = inflater.inflate(R.layout.faq_answer, null);
        }
        TextView answerFaq2 = cChildView.findViewById(R.id.faqAnswer);
        answerFaq2.setTypeface(null, Typeface.BOLD);
        answerFaq2.setText(answerFaq);

        ImageView img = (ImageView) cChildView.findViewById(R.id.imageView1);
        if (answerFaq == answer[0][0]) {
            img.setImageResource(images[0][0]);
        } else if (answerFaq == answer[3][0]) {
            img.setImageResource(images[1][0]);
        } else if (answerFaq == answer[9][0]) {
            img.setImageResource(images[2][0]);
        } else {
            img.setImageResource(0);
        }
        return cChildView;
    }

    /**
     * get child view.
     * @param groupPosition
     * @param childPosition
     */
    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return false;
    }
}
