package com.scti.scti2019checkin.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scti.scti2019checkin.EventActivity;
import com.scti.scti2019checkin.R;
import com.scti.scti2019checkin.holders.EventHolder;
import com.scti.scti2019checkin.interfaces.OnRecyclerViewClickListener;
import com.scti.scti2019checkin.models.Event;
import com.scti.scti2019checkin.utils.DateConverter;

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
        final EventHolder eventHolder = (EventHolder) holder;
        final Event event = this.events.get(position);

        eventHolder.getEventNameView().setText(event.getTitle());
        eventHolder.getEventDateView().setText(DateConverter.getShortFormatedDate(event.getBeginDate()));
        String eventType = event.getType();
        if (eventType.equals(Event.TYPE_SPECIAL)) {
            eventHolder.getEventTypeView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorSpecial));
            eventHolder.getEventIndicatorView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorSpecial));
            eventHolder.getEventTypeView().setText(context.getString(R.string.events_special).toUpperCase());
        } else if (eventType.equals(Event.TYPE_MINICOURSE)) {
            eventHolder.getEventTypeView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorShortCourse));
            eventHolder.getEventIndicatorView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorShortCourse));
            eventHolder.getEventTypeView().setText(context.getString(R.string.events_shortcourse).toUpperCase());
        } else {
            eventHolder.getEventTypeView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorLecture));
            eventHolder.getEventIndicatorView().setBackgroundColor(ContextCompat.getColor(context, R.color.colorLecture));
            eventHolder.getEventTypeView().setText(context.getString(R.string.events_lecture).toUpperCase());
        }

        eventHolder.setOnRecyclerListItemClickListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("idEvento", event.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.events.size();

    }
}
