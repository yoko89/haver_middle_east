package com.neklaway.havermiddle_east.activity.hmeView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.entities.HMECode;
import com.neklaway.havermiddle_east.R;

import java.util.List;

public class HMEListAdapter extends RecyclerView.Adapter<HMEListAdapter.ViewHolder> {

    private final List<HMECode> hmeCodeData;

    private Listener listener;

    public interface Listener {
        void onClick(String hmeCode);
    }

    public HMEListAdapter(List<HMECode> hmeCodeData) {
        this.hmeCodeData = hmeCodeData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        HMECode hmeCodeData = this.hmeCodeData.get(position);
        TextView item = holder.tvItem;
        item.setText(hmeCodeData.getHMECode());
        item.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(hmeCodeData.getCustomerName());
        });
    }



    @Override
    public int getItemCount() {
        return hmeCodeData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             tvItem = itemView.findViewById(R.id.tvCountry);
            }
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }
}
