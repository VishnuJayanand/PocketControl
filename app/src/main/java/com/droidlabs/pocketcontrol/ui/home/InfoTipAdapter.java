package com.droidlabs.pocketcontrol.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.droidlabs.pocketcontrol.R;

import java.util.ArrayList;

public class InfoTipAdapter extends RecyclerView.Adapter<InfoTipAdapter.InfoTipViewHolder> {

    private ArrayList<InfoTipHelperClass> infoTipLocations;

    /**
     * InfoTip adapter.
     * @param infoTipLocationsFunction array of postions
     */
    public InfoTipAdapter(final ArrayList<InfoTipHelperClass> infoTipLocationsFunction) {
        this.infoTipLocations = infoTipLocationsFunction;
    }

    /**
     * to assign the layout id.
     */
    @NonNull
    @Override
    public InfoTipViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_card, parent, false);
        InfoTipViewHolder infoTipViewHolder = new InfoTipViewHolder(view);
        return infoTipViewHolder;
    }

    /**
     * get holder overrided function.
     */
    @Override
    public void onBindViewHolder(final @NonNull InfoTipViewHolder holder, final int position) {

        InfoTipHelperClass infoTipHelperClass = infoTipLocations.get(position);

        holder.title.setText(infoTipHelperClass.getTitle());
        holder.desc.setText(infoTipHelperClass.getDesc());
    }

    /**
     * Get item count.
     * @return size
     */
    @Override
    public int getItemCount() {
        return infoTipLocations.size();
    }

    public static class InfoTipViewHolder extends RecyclerView.ViewHolder {

        private TextView title, desc;

        /**
         * View holder.
         * @param itemView view
         */
        public InfoTipViewHolder(final @NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.infoTip_title);
            desc = itemView.findViewById(R.id.infoTip_desc);
        }
    }
}
