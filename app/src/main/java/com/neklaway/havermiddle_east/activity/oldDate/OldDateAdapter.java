package com.neklaway.havermiddle_east.activity.oldDate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;

import java.util.List;

public class OldDateAdapter extends RecyclerView.Adapter<OldDateAdapter.ViewHolder> {

    private final List<String> items;
    private OldDateAdapter.Listener listener;

    public OldDateAdapter(List<String> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvItem.setText(items.get(position));

        holder.cardView.setOnClickListener(v -> {
            if(listener!=null){
                listener.onClick(position);
            }
        });

        holder.cardView.setOnLongClickListener(v -> {
            if(listener!=null){
                listener.onLongClick(position);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvItem;
        final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            tvItem = itemView.findViewById(R.id.tvCountry);
        }
    }

    public interface Listener {
        void onClick(int id);
        void onLongClick(int id);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
