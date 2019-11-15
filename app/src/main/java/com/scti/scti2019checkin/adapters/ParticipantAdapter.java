package com.scti.scti2019checkin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scti.scti2019checkin.R;
import com.scti.scti2019checkin.interfaces.OnRecyclerViewClickListener;
import com.scti.scti2019checkin.models.Participant;

import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter {
    private List<Participant> participants;
    private Context context;
    private OnRecyclerViewClickListener listener;

    public ParticipantAdapter(List<Participant> participants, Context context, OnRecyclerViewClickListener listener) {
        this.participants = participants;
        this.context = context;
        this.listener = listener;
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
        return participants.size();
    }

    public class ParticipantHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView participantNameView;

        ParticipantHolder(View itemView) {
            super(itemView);

            this.participantNameView = itemView.findViewById(R.id.participant_item_name);

            itemView.setOnClickListener(this);
        }

        TextView getParticipantNameView() {
            return participantNameView;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getLayoutPosition());
        }
    }
}
