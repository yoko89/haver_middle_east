package com.neklaway.havermiddle_east.activity.newCustomer;

import android.annotation.SuppressLint;
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
import com.neklaway.havermiddle_east.entities.Customer;

public class CustomerListAdapter extends ListAdapter<Customer, CustomerListAdapter.ViewHolder> {

    private CustomerListAdapter.Listener listener;

    public CustomerListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Customer> DIFF_CALLBACK = new DiffUtil.ItemCallback<Customer>() {
        @Override
        public boolean areItemsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
            return oldItem.getId() == newItem.getId();
        }


        @Override
        public boolean areContentsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
            return oldItem.getCustomerCity().equals(newItem.getCustomerCity())
                    && oldItem.getCustomerName().equals(newItem.getCustomerName())
                    && oldItem.getCustomerCountry().equals(newItem.getCustomerCountry());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(getItem(position).getCustomerName() + "\n" + getItem(position).getCustomerCity()+", "+  getItem(position).getCustomerCountry());
        holder.cardView.setOnClickListener(v -> {
            if(listener!=null){
                listener.onClick(getItem(position));
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvCountry);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    public interface Listener {
        void onClick(Customer customer);
    }

    public void setListener(CustomerListAdapter.Listener listener) {
        this.listener = listener;
    }
}



