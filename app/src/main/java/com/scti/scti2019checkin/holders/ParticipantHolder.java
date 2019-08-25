package com.scti.scti2019checkin.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scti.scti2019checkin.R;

public class ParticipantHolder extends RecyclerView.ViewHolder {
    private TextView participantNameView;

    public ParticipantHolder(View itemView) {
        super(itemView);

        this.participantNameView = itemView.findViewById(R.id.participant_item_nome);
    }

    public TextView getParticipantNameView() {
        return participantNameView;
    }
}
