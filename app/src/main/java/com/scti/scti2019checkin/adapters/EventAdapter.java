package com.scti.scti2019checkin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scti.scti2019checkin.R;
import com.scti.scti2019checkin.holders.EventHolder;
import com.scti.scti2019checkin.models.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter {
    private List<Event> events;
    private Context context;

    public EventAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.event_item, parent, false);
        return new com.scti.scti2019checkin.holders.EventHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EventHolder eventHolder = (EventHolder) holder;
        Event event = this.events.get(position);

        eventHolder.getEventNameView().setText(event.getName());
        eventHolder.getEventDateView().setText(event.getDate());
    }

    @Override
    public int getItemCount() {
        return this.events.size();

    }
}
