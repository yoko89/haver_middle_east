package com.neklaway.havermiddle_east.activity.newTimeSheet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.entities.TimeSheet;
import com.neklaway.havermiddle_east.utils.CalculateHours;
import com.neklaway.havermiddle_east.utils.DateFormatter;

import java.util.Objects;

public class NewTimeSheetAdapter extends ListAdapter<TimeSheet, NewTimeSheetAdapter.ViewHolder> {

    protected NewTimeSheetAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<TimeSheet> DIFF_CALLBACK = new DiffUtil.ItemCallback<TimeSheet>() {
        @Override
        public boolean areItemsTheSame(@NonNull TimeSheet oldItem, @NonNull TimeSheet newItem) {
            return newItem.getId() == oldItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TimeSheet oldItem, @NonNull TimeSheet newItem) {
            return oldItem.isTimeSheetCreated() == newItem.isTimeSheetCreated()
                    && oldItem.isWeekEnd() == newItem.isWeekEnd()
                    && oldItem.getHMECode().equals(newItem.getHMECode())
                    && oldItem.getBreakHour().equals(newItem.getBreakHour())
                    && oldItem.getCustomer().equals(newItem.getCustomer())
                    && oldItem.getDate().equals(newItem.getDate())
                    && oldItem.getIBAUSO().equals(newItem.getIBAUSO())
                    && oldItem.getTravelDistance() == newItem.getTravelDistance()
                    && oldItem.getTravelEndTime().equals(newItem.getTravelEndTime())
                    && oldItem.getTravelStartTime().equals(newItem.getTravelStartTime())
                    && oldItem.getWorkStartTime().equals(newItem.getWorkStartTime())
                    && oldItem.getWorkEndTime().equals(newItem.getWorkEndTime());
        }
    };

    public TimeSheet getTimeSheetAt(int position) {
        return getItem(position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_date_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewTimeSheetAdapter.ViewHolder holder, int position) {

        String[] calculatedHours = new CalculateHours().calculateHours(getItem(position).getTravelStartTime(),getItem(position).getWorkStartTime(),getItem(position).getWorkEndTime(),getItem(position).getTravelEndTime(),getItem(position).getBreakHour(),getItem(position).isOverTime());

        holder.tvDate.setText(DateFormatter.dateFormatToLocaleString(Objects.requireNonNull(DateFormatter.formatDateToCalendar(getItem(position).getDate()))));
        holder.tvTravelStart.setText(getItem(position).getTravelStartTime());
        holder.tvWorkStart.setText(getItem(position).getWorkStartTime());
        holder.tvWorkEnd.setText(getItem(position).getWorkEndTime());
        holder.tvTravelEnd.setText(getItem(position).getTravelEndTime());
        holder.tvBreak.setText(getItem(position).getBreakHour());
        holder.tvTravelDist.setText(String.valueOf(getItem(position).getTravelDistance()));
        holder.tvWorkHours.setText(calculatedHours[0]);
        holder.tvOverTimeHours.setText(calculatedHours[2]);
        holder.tvTravelHours.setText(calculatedHours[1]);

        if (getItem(position).isTimeSheetCreated()) {
            holder.loDate.setBackgroundColor(holder.loDate.getContext().getColor(R.color.light_gray));
        } else {
            holder.loDate.setBackgroundColor(holder.loDate.getContext().getColor(android.R.color.transparent));
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final CardView cdDate;
        final TextView tvDate;
        final TextView tvTravelStart;
        final TextView tvWorkStart;
        final TextView tvWorkEnd;
        final TextView tvTravelEnd;
        final TextView tvBreak;
        final TextView tvTravelDist;
        final TextView tvWorkHours;
        final TextView tvOverTimeHours;
        final TextView tvTravelHours;
        final LinearLayout loDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cdDate = itemView.findViewById(R.id.cvDate);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTravelStart = itemView.findViewById(R.id.tvTravelStart);
            tvWorkStart = itemView.findViewById(R.id.tvWorkStart);
            tvWorkEnd = itemView.findViewById(R.id.tvWorkEnd);
            tvTravelEnd = itemView.findViewById(R.id.tvTravelEnd);
            tvBreak = itemView.findViewById(R.id.tvBreakTime);
            tvTravelDist = itemView.findViewById(R.id.tvTravelDistance);
            tvWorkHours = itemView.findViewById(R.id.tvWorkHours);
            tvOverTimeHours = itemView.findViewById(R.id.tvOverTimeHours);
            tvTravelHours = itemView.findViewById(R.id.tvTravelHours);
            loDate = itemView.findViewById(R.id.loDate);
        }
    }
}

