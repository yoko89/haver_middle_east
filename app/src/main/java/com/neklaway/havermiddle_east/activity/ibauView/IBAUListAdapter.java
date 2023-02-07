package com.neklaway.havermiddle_east.activity.ibauView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.entities.IBAUSO;
import com.neklaway.havermiddle_east.R;

import java.util.List;

public class IBAUListAdapter extends RecyclerView.Adapter<IBAUListAdapter.ViewHolder> {

    private final List<IBAUSO> IBAUCodeData;

    private Listener listener;

    IBAUListAdapter(List<IBAUSO> IBAUCodeData) {
        this.IBAUCodeData = IBAUCodeData;
    }

    public interface Listener {
        void onClick(String hmeCode);
    }

    @NonNull
    @Override
    public IBAUListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IBAUListAdapter.ViewHolder holder, int position) {
        IBAUSO ibauso = this.IBAUCodeData.get(position);
        TextView item = holder.tvItem;
        item.setText(ibauso.getIBAUSO());
        item.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(ibauso.getHMECode());
        });
    }

    @Override
    public int getItemCount() {
        return IBAUCodeData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvCountry);
        }
    }

    public void setListener(IBAUListAdapter.Listener listener){
        this.listener = listener;
    }

}
