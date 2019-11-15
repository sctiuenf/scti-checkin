package com.scti.scti2019checkin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scti.scti2019checkin.R;
import com.scti.scti2019checkin.database.DatabaseController;
import com.scti.scti2019checkin.holders.PresenceHolder;
import com.scti.scti2019checkin.models.Participant;
import com.scti.scti2019checkin.models.Presence;
import com.scti.scti2019checkin.utils.DateConverter;

import java.util.List;

public class PresenceAdapter extends RecyclerView.Adapter {
    private List<Presence> presences;
    private Context context;

    public PresenceAdapter(List<Presence> presences, Context context) {
        this.presences = presences;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.presence_item, parent, false);
        return new PresenceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PresenceHolder presenceHolder = (PresenceHolder) holder;
        Presence presence = presences.get(position);

        DatabaseController controller = new DatabaseController(context);
        Participant participant = controller.getParticipantById(presence.getParticipantId());

        presenceHolder.getPresenceNameView().setText(participant.getName());
        presenceHolder.getPresenceDateView().setText(DateConverter.getShortFormatedTime(presence.getDate()));
        presenceHolder.getPresencePositionView().setText("" + (position + 1) + ".");
        ImageView warn = presenceHolder.getPresenceWarnView();
        if(presence.isForced()){
            warn.setVisibility(View.VISIBLE);
        } else {
            warn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return presences.size();
    }
}
