package com.scti.scti2019checkin.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scti.scti2019checkin.R;

public class PresenceHolder extends RecyclerView.ViewHolder {
    private TextView presenceNameView;
    private TextView presenceDateView;
    private TextView presencePositionView;
    private ImageView presenceWarnView;

    public PresenceHolder(View itemView) {
        super(itemView);

        this.presenceNameView = itemView.findViewById(R.id.presence_participant_name);
        this.presenceDateView = itemView.findViewById(R.id.presence_date);
        this.presencePositionView = itemView.findViewById(R.id.presence_position);
        this.presenceWarnView = itemView.findViewById(R.id.presence_forced);
    }

    public TextView getPresenceNameView() {
        return presenceNameView;
    }

    public TextView getPresenceDateView() {
        return presenceDateView;
    }

    public TextView getPresencePositionView() {
        return presencePositionView;
    }

    public ImageView getPresenceWarnView() {
        return presenceWarnView;
    }
}
