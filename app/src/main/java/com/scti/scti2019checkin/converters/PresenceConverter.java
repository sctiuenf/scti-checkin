package com.scti.scti2019checkin.converters;

import android.content.Context;
import android.widget.Toast;

import com.scti.scti2019checkin.database.DatabaseController;
import com.scti.scti2019checkin.models.Presence;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

public class PresenceConverter {

    private final Context context;

    public PresenceConverter(Context context) {
        this.context = context;
    }

    public String getDataJSON() {
        JSONStringer js = new JSONStringer();
        DatabaseController databaseController = new DatabaseController(context);
        List<Presence> presences = databaseController.getPresences();
        try {
            js.object();
            js.key("attendances").array();
            for (Presence presence : presences) {
                js.object();
                js.key("eventId").value(presence.getEventId());
                js.key("userId").value(presence.getParticipantId());
                js.key("date").value(presence.getDate());
                js.key("force_checkin").value(presence.isForced());
                js.endObject();
            }
            js.endArray();

            js.endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return js.toString();
    }
}
