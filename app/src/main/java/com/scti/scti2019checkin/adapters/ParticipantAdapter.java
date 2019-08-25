package com.scti.scti2019checkin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scti.scti2019checkin.R;
import com.scti.scti2019checkin.holders.ParticipantHolder;
import com.scti.scti2019checkin.models.Event;
import com.scti.scti2019checkin.models.Participant;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter {
    private List<Participant> participants;
    private Context context;

    public ParticipantAdapter(List<Participant> participants, Context context) {
        this.participants = participants;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.participant_item, parent, false);
        return new ParticipantHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ParticipantHolder participantHolder = (ParticipantHolder) holder;
        Participant participant = participants.get(position);
        participantHolder.getParticipantNameView().setText(participant.getName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
