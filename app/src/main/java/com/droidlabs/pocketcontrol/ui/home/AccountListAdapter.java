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

public final class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.AccountViewHolder> {

    private List<Account> accounts; // Cached copy of transactions
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final Application application;

    /**
     * Constructor.
     * @param ctx context.
     * @param appl application.
     */
    public AccountListAdapter(final @NonNull Context ctx, final @NonNull Application appl) {
        layoutInflater = LayoutInflater.from(ctx);
        this.context = ctx;
        this.application = appl;
    }

    @Override
    public void onBindViewHolder(final @NonNull AccountViewHolder holder, final int position) {
        if (accounts != null) {
            Account account = accounts.get(position);

            holder.accountName.setText(account.getName());

            if (account.getColor() != null) {
                holder.accountRowColorColumn.setBackgroundTintList(
                        context.getColorStateList(account.getColor())
                );
            } else {
                holder.accountRowColorColumn.setBackgroundTintList(
                        context.getColorStateList(R.color.projectColorDefault)
                );
            }

            String currentAccountId = new SharedPreferencesUtils(application).getCurrentAccountIdKey();

            if (String.valueOf(account.getId()).equals(currentAccountId)) {
                holder.accountSelected.setVisibility(View.VISIBLE);
                holder.accountWrapperInfoLinearLayout.setBackground(
                        context.getDrawable(R.drawable.account_row_rounded_right_selected)
                );
            } else {
                holder.accountSelected.setVisibility(View.GONE);
                holder.accountWrapperInfoLinearLayout.setBackground(
                        context.getDrawable(R.drawable.account_row_rounded_right)
                );
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

    /**
     * Set accounts.
     * @param acc accounts.
     */
    public void setAccounts(final List<Account> acc) {
        this.accounts = acc;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {
        View itemView = layoutInflater.inflate(R.layout.account_listitem, parent, false);
        return new AccountViewHolder(itemView);
    }

    final class AccountViewHolder extends RecyclerView.ViewHolder {
        private TextView accountName;
        private TextView accountSelected;
        private LinearLayout accountWrapperLinearLayout;
        private LinearLayout accountWrapperInfoLinearLayout;
        private ImageView accountRowColorColumn;

        /**
         * Constructor.
         * @param itemView item view.
         */
        private AccountViewHolder(final View itemView) {
            super(itemView);

            accountName = itemView.findViewById(R.id.accountName);
            accountSelected = itemView.findViewById(R.id.accountSelectedText);
            accountRowColorColumn = itemView.findViewById(R.id.accountRowColorColumn);
            accountWrapperLinearLayout = itemView.findViewById(R.id.accountWrapperLinearLayout);
            accountWrapperInfoLinearLayout = itemView.findViewById(R.id.accountWrapperInfoLinearLayout);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Log.v("ACCOUNTS", "ACCOUNT " + accounts.get(getAdapterPosition()).getId() + " CLICKED");
                    new SharedPreferencesUtils(application).setCurrentAccountId(
                            String.valueOf(accounts.get(getAdapterPosition()).getId())
                    );
                    notifyDataSetChanged();
                }
            };

            accountWrapperLinearLayout.setOnClickListener(clickListener);
            accountWrapperInfoLinearLayout.setOnClickListener(clickListener);
        }
    }
}
