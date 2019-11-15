package com.scti.scti2019checkin.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scti.scti2019checkin.R;
import com.scti.scti2019checkin.interfaces.OnRecyclerViewClickListener;

public class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView eventNameView;
    private TextView eventDateView;
    private TextView eventTypeView;
    View eventIndicatorView;
    private OnRecyclerViewClickListener listener;

    public EventHolder(View itemView) {
        super(itemView);

        this.eventNameView = itemView.findViewById(R.id.event_item_name);
        this.eventDateView = itemView.findViewById(R.id.event_item_date);
        this.eventTypeView = itemView.findViewById(R.id.event_item_type);
        this.eventIndicatorView = itemView.findViewById(R.id.event_item_indicator);

        itemView.setOnClickListener(this);
    }

    public TextView getEventNameView() {
        return this.eventNameView;
    }

    public TextView getEventDateView() {
        return this.eventDateView;
    }

    public TextView getEventTypeView() {
        return eventTypeView;
    }

    public View getEventIndicatorView() {
        return eventIndicatorView;
    }

    public void setOnRecyclerListItemClickListener(OnRecyclerViewClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        {
            listener.onClick(view, getAdapterPosition());
        }
    }
}
