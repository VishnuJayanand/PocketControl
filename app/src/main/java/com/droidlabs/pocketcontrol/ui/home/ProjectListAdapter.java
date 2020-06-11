package com.droidlabs.pocketcontrol.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.project.Project;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionListAdapter;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionViewModel;

import java.util.List;

public final class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder>{

    private List<Project> projects; // Cached copy of transactions
    private final LayoutInflater layoutInflater;
    private final ProjectViewModel projectViewModel;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public ProjectListAdapter(final @NonNull Context context,
                              final ProjectViewModel mProjectViewModel
    ) {
        layoutInflater = LayoutInflater.from(context);
        projectViewModel = mProjectViewModel;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @NonNull
    @Override
    public ProjectListAdapter.ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    final class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProjectViewHolder(final View itemView) {
            super(itemView);

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // onNoteListener.onTransactionClick(transactions.get(getAdapterPosition()), getAdapterPosition());
                }
            };
        }

        /**
         * method to catch onlick event of list adapter.
         * @param v view
         */
        @Override
        public void onClick(final View v) {
            // onNoteListener.onTransactionClick(transactions.get(getAdapterPosition()), getAdapterPosition());
        }
    }
}
