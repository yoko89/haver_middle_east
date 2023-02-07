package com.neklaway.havermiddle_east.activity.visaLog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.entities.Visa;
import com.neklaway.havermiddle_east.utils.DateFormatter;

import java.util.Calendar;

public class VisaLogAdapter extends ListAdapter<Visa, VisaLogAdapter.ViewHolder> {

    private VisaLogAdapter.Listener listener;

    private Calendar currentDate, visaDate;

    private SharedPreferences settings;

    public VisaLogAdapter() {
        super(DIFF_CALLBACK);
    }


    private static final DiffUtil.ItemCallback<Visa> DIFF_CALLBACK = new DiffUtil.ItemCallback<Visa>() {
        @Override
        public boolean areItemsTheSame(@NonNull Visa oldItem, @NonNull Visa newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Visa oldItem, @NonNull Visa newItem) {
            return oldItem.getCountry().equals(newItem.getCountry())
                    && oldItem.getDate().equals(newItem.getDate())
                    && (oldItem.isEnabled() == newItem.isEnabled());
        }
    };


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_visa_log, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String Settings = "user_settings";
        final String VisaLogReminder = "visa_log_reminder_key";
        long remainingDays;

        currentDate = Calendar.getInstance();
        visaDate = DateFormatter.formatDateToCalendar(getItem(position).getDate());

        assert visaDate != null;
        remainingDays = (visaDate.getTimeInMillis() - currentDate.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        holder.tvCountry.setText(getItem(position).getCountry());
        holder.tvExpiry.setText(DateFormatter.dateFormatToLocaleString(visaDate));
        holder.tvRemaining.setText(String.valueOf(remainingDays));
        holder.swEnabled.setChecked(getItem(position).isEnabled());


        settings = holder.cardView.getContext().getSharedPreferences(Settings, Context.MODE_PRIVATE);

        if (!getItem(position).isEnabled()){
            holder.cardView.setCardBackgroundColor(Color.LTGRAY);
        }else if (remainingDays <= 0) {
            holder.cardView.setCardBackgroundColor(Color.RED);
            holder.tvCountry.setTextColor(Color.WHITE);
            holder.tvExpiry.setTextColor(Color.WHITE);
            holder.tvRemaining.setTextColor(Color.WHITE);
        } else if (remainingDays <= settings.getInt(VisaLogReminder, 0)) {
            holder.cardView.setCardBackgroundColor(Color.YELLOW);
            holder.tvCountry.setTextColor(Color.BLACK);
            holder.tvExpiry.setTextColor(Color.BLACK);
            holder.tvRemaining.setTextColor(Color.BLACK);
            if ((holder.cardView.getContext().getResources().getConfiguration().uiMode &
                    Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                holder.tvCountry.setTextColor(Color.BLACK);
                holder.tvExpiry.setTextColor(Color.BLACK);
                holder.tvRemaining.setTextColor(Color.BLACK);

            }

        } else {
            holder.cardView.setCardBackgroundColor(Color.rgb(150,255,150));
            holder.tvCountry.setTextColor(Color.BLACK);
            holder.tvExpiry.setTextColor(Color.BLACK);
            holder.tvRemaining.setTextColor(Color.BLACK);
        }

        holder.swEnabled.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEnabledClick(getItem(position));
            }
        });


    }


    public Visa getVisaAt(int position) {
        return getItem(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCountry;
        final TextView tvExpiry;
        final TextView tvRemaining;
        final CardView cardView;
        final SwitchCompat swEnabled;
        final ConstraintLayout cvVisa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvExpiry = itemView.findViewById(R.id.tvExpiryDate);
            tvRemaining = itemView.findViewById(R.id.tvRemainingDays);
            cardView = itemView.findViewById(R.id.card_view);
            swEnabled = itemView.findViewById(R.id.swEnabled);
            cvVisa = itemView.findViewById(R.id.clVisa);
        }
    }

    public interface Listener {
        void onEnabledClick(Visa visa);
    }

    public void setListener(VisaLogAdapter.Listener listener) {
        this.listener = listener;
    }
}




