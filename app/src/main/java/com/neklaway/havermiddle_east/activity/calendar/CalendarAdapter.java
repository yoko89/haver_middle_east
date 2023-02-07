package com.neklaway.havermiddle_east.activity.calendar;

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
import com.neklaway.havermiddle_east.entities.TimeSheet;
import com.neklaway.havermiddle_east.utils.DateFormatter;

import java.util.Calendar;
import java.util.Locale;

public class CalendarAdapter extends ListAdapter<TimeSheet, CalendarAdapter.ViewHolder> {

    public CalendarAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<TimeSheet> DIFF_CALLBACK = new DiffUtil.ItemCallback<TimeSheet>() {
        @Override
        public boolean areItemsTheSame(@NonNull TimeSheet oldItem, @NonNull TimeSheet newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TimeSheet oldItem, @NonNull TimeSheet newItem) {
            return oldItem.getDate().equals(newItem.getDate())
                    && oldItem.getCustomer().equals(newItem.getCustomer())
                    && oldItem.getHMECode().equals(newItem.getHMECode());
        }
    };


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_calendar, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Calendar calendar = DateFormatter.formatDateToCalendar(getItem(position).getDate());

        assert calendar != null;
        String date = calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG_STANDALONE, Locale.ENGLISH) + " " + calendar.get(Calendar.DAY_OF_MONTH);

        holder.tvDate.setText(date);
        holder.tvCustomerName.setText(getItem(position).getCustomer());
        holder.tvHMECode.setText(getItem(position).getHMECode());
        if(getItem(position).getIBAUSO().equals("not applicable")||getItem(position).getIBAUSO().equals("---")){
            holder.tvIBAUCode.setText("");
        }else{
            holder.tvIBAUCode.setText(getItem(position).getIBAUSO());
        }

    }




    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvDate,tvCustomerName,tvHMECode,tvIBAUCode;
        final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvHMECode = itemView.findViewById(R.id.tvHMECode);
            cardView = itemView.findViewById(R.id.card_view);
            tvIBAUCode = itemView.findViewById(R.id.tvIBAUCode);
        }
    }

}

