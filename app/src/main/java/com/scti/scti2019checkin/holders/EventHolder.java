package com.scti.scti2019checkin.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scti.scti2019checkin.R;

public class EventHolder extends RecyclerView.ViewHolder {
    private TextView eventNameView;
    private TextView eventDateView;

    public EventHolder(View itemView) {
        super(itemView);

        this.eventNameView = itemView.findViewById(R.id.event_item_name);
        this.eventDateView = itemView.findViewById(R.id.event_item_date);
    }

    public TextView getEventNameView() {
        return this.eventNameView;
    }

    public TextView getEventDateView() {
        return this.eventDateView;
    }

}
