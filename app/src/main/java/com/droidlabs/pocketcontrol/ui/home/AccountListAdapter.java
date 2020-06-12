package com.droidlabs.pocketcontrol.ui.home;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.account.Account;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public final class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ProjectViewHolder>{

    private List<Account> accounts; // Cached copy of transactions
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final Application application;

    public AccountListAdapter(final @NonNull Context context, final @NonNull Application application) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.application = application;
    }

    @Override
    public void onBindViewHolder(final @NonNull ProjectViewHolder holder, final int position) {
        if (accounts != null) {
            Account account = accounts.get(position);

            holder.projectName.setText(account.getName());

            if (account.getColor() != null) {
                holder.projectRowColorColumn.setBackgroundTintList(context.getColorStateList(account.getColor()));
            } else {
                holder.projectRowColorColumn.setBackgroundTintList(context.getColorStateList(R.color.projectColorDefault));
            }

            String currentProjectId = new SharedPreferencesUtils(application).getCurrentProjectIdKey();

            if (String.valueOf(account.getId()).equals(currentProjectId)) {
                holder.projectSelected.setVisibility(View.VISIBLE);
                holder.projectWrapperInfoLinearLayout.setBackground(context.getDrawable(R.drawable.project_row_rounded_right_selected));
            } else {
                holder.projectSelected.setVisibility(View.GONE);
                holder.projectWrapperInfoLinearLayout.setBackground(context.getDrawable(R.drawable.project_row_rounded_right));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (accounts != null) {
            return accounts.size();
        } else {
            return 0;
        }
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.account_listitem, parent, false);
        return new ProjectViewHolder(itemView);
    }

    final class ProjectViewHolder extends RecyclerView.ViewHolder {
        private TextView projectName;
        private TextView projectSelected;
        private LinearLayout projectWrapperLinearLayout;
        private LinearLayout projectWrapperInfoLinearLayout;
        private ImageView projectRowColorColumn;

        private ProjectViewHolder(final View itemView) {
            super(itemView);

            projectName = itemView.findViewById(R.id.projectName);
            projectSelected = itemView.findViewById(R.id.projectSelectedText);
            projectRowColorColumn = itemView.findViewById(R.id.projectRowColorColumn);
            projectWrapperLinearLayout = itemView.findViewById(R.id.projectWrapperLinearLayout);
            projectWrapperInfoLinearLayout = itemView.findViewById(R.id.projectWrapperInfoLinearLayout);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.v("ACCOUNTS", "ACCOUNT " + accounts.get(getAdapterPosition()).getId() + " CLICKED");
                    new SharedPreferencesUtils(application).setCurrentAccountId(String.valueOf(accounts.get(getAdapterPosition()).getId()));
                    notifyDataSetChanged();
                }
            };

            projectWrapperLinearLayout.setOnClickListener(clickListener);
            projectWrapperInfoLinearLayout.setOnClickListener(clickListener);
        }
    }
}
