package com.neklaway.havermiddle_east.activity.newHMECode;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.entities.HMECode;

public class NewHMECodeAdapter extends ListAdapter<HMECode, NewHMECodeAdapter.ViewHolder> {

    private NewHMECodeAdapter.Listener listener;

    public NewHMECodeAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<HMECode> DIFF_CALLBACK = new DiffUtil.ItemCallback<HMECode>() {
        @Override
        public boolean areItemsTheSame(@NonNull HMECode oldItem, @NonNull HMECode newItem) {
            return oldItem.getId() == newItem.getId();
        }


        @Override
        public boolean areContentsTheSame(@NonNull HMECode oldItem, @NonNull HMECode newItem) {
            return oldItem.getCustomerName().equals(newItem.getCustomerName())
                    && oldItem.getHMECode().equals(newItem.getHMECode())
                    && oldItem.getDescriptionWork().equals(newItem.getDescriptionWork())
                    && oldItem.getFileNumber() == newItem.getFileNumber()
                    && oldItem.getMachineNumber().equals(newItem.getMachineNumber())
                    && oldItem.getMachineType().equals(newItem.getMachineType());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_hme_code_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvHmeCode.setText(getItem(position).getHMECode());
        holder.tvMachineType.setText(getItem(position).getMachineType());
        holder.tvMachineNumber.setText(getItem(position).getMachineNumber());
        holder.tvWorkDescription.setText(getItem(position).getDescriptionWork());

        holder.cardView.setOnClickListener(v -> {
            if(listener!=null){
                listener.onClick(getItem(position));
            }

        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvHmeCode, tvMachineType,tvMachineNumber,tvWorkDescription;
        final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHmeCode = itemView.findViewById(R.id.tv_hme_code);
            tvMachineNumber = itemView.findViewById(R.id.tv_machine_number);
            tvMachineType = itemView.findViewById(R.id.tv_machine_type);
            tvWorkDescription = itemView.findViewById(R.id.tv_work_description);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    public interface Listener {
        void onClick(HMECode hmeCode);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
