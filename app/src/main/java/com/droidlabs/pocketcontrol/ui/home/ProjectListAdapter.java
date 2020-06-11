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

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.droidlabs.pocketcontrol.R;
import com.droidlabs.pocketcontrol.db.PocketControlDB;
import com.droidlabs.pocketcontrol.db.project.Project;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionListAdapter;
import com.droidlabs.pocketcontrol.ui.transaction.TransactionViewModel;
import com.droidlabs.pocketcontrol.utils.SharedPreferencesUtils;

import java.util.List;

public final class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectViewHolder>{

    private List<Project> projects; // Cached copy of transactions
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final Application application;

    public ProjectListAdapter(final @NonNull Context context, final @NonNull Application application) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.application = application;
    }

    @Override
    public void onBindViewHolder(final @NonNull ProjectViewHolder holder, final int position) {
        if (projects != null) {
            Project project = projects.get(position);

            holder.projectName.setText(project.getName());

            if (project.getColor() != null) {
                holder.projectRowColorColumn.setBackgroundTintList(context.getColorStateList(project.getColor()));
            } else {
                holder.projectRowColorColumn.setBackgroundTintList(context.getColorStateList(R.color.projectColorDefault));
            }

            String currentProjectId = new SharedPreferencesUtils(application).getCurrentProjectIdKey();

            if (String.valueOf(project.getId()).equals(currentProjectId)) {
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
        if (projects != null) {
            return projects.size();
        } else {
            return 0;
        }
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.project_listitem, parent, false);
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
                    Log.v("PROJECTS", "PROJECT " + projects.get(getAdapterPosition()).getId() + " CLICKED");
                    new SharedPreferencesUtils(application).setCurrentProjectId(String.valueOf(projects.get(getAdapterPosition()).getId()));
                    notifyDataSetChanged();
                }
            };

            projectWrapperLinearLayout.setOnClickListener(clickListener);
            projectWrapperInfoLinearLayout.setOnClickListener(clickListener);
        }
    }
}
