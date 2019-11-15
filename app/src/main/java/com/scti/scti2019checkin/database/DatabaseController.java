package com.scti.scti2019checkin.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.scti.scti2019checkin.R;
import com.scti.scti2019checkin.models.Event;
import com.scti.scti2019checkin.models.Participant;
import com.scti.scti2019checkin.models.Presence;

import java.util.ArrayList;
import java.util.List;

public class DatabaseController {
    private final SharedPreferences sharedPref;
    private CreateDatabase createDatabase;
    private SQLiteDatabase database;
    private Context context;

    public DatabaseController(Context context) {
        sharedPref = context.getSharedPreferences(context.getString(R.string.app_sharedPref_file), Context.MODE_PRIVATE);
        String mail = sharedPref.getString(context.getString(R.string.app_sharedPref_mail), "");
        this.createDatabase = new CreateDatabase(context, mail);
        this.context = context;
    }

    public void insertEvent(int id, String title, String type, String begin, String end) {
        ContentValues values;

        values = new ContentValues();
        values.put(CreateDatabase.getEvents_id_field(), id);
        values.put(CreateDatabase.getEvents_title_field(), title);
        values.put(CreateDatabase.getEvents_type_field(), type);
        values.put(CreateDatabase.getEvents_begin_field(), begin);
        values.put(CreateDatabase.getEvents_end_field(), end);

        database = createDatabase.getWritableDatabase();
        database.insert(CreateDatabase.getEvents_table_name(), null, values);
        database.close();
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        String[] fields = {CreateDatabase.getEvents_id_field(),
                CreateDatabase.getEvents_title_field(),
                CreateDatabase.getEvents_type_field(),
                CreateDatabase.getEvents_begin_field(),
                CreateDatabase.getEvents_end_field()};
        database = createDatabase.getReadableDatabase();
        String orderBy = CreateDatabase.getEvents_begin_field() + " ASC";

        Cursor cursor = database.query(CreateDatabase.getEvents_table_name(), fields, null, null, null, null, orderBy, null);
        if (cursor.moveToFirst()) {
            do {
                //PRA CADA EVENTO RETORNADO, INSTANCIA UM OBJETO EVENTO E ADICIONA NA LISTA CRIADA ANTERIORMENTE
                int id = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getEvents_id_field()));
                String title = cursor.getString(cursor.getColumnIndex(CreateDatabase.getEvents_title_field()));
                String type = cursor.getString(cursor.getColumnIndex(CreateDatabase.getEvents_type_field()));
                String begin = cursor.getString(cursor.getColumnIndex(CreateDatabase.getEvents_begin_field()));
                String end = cursor.getString(cursor.getColumnIndex(CreateDatabase.getEvents_end_field()));
                Event event = new Event(id, title, type, begin, end);
                events.add(event);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return events;
    }

    public Event getEventById(int id) {
        Event result = null;
        Cursor cursor;
        String[] fields = {CreateDatabase.getEvents_id_field(),
                CreateDatabase.getEvents_title_field(),
                CreateDatabase.getEvents_type_field(),
                CreateDatabase.getEvents_begin_field(),
                CreateDatabase.getEvents_end_field()};
        String where = CreateDatabase.getEvents_id_field() + "=" + id;
        database = createDatabase.getReadableDatabase();
        cursor = database.query(CreateDatabase.getEvents_table_name(), fields, where, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int eventId = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getEvents_id_field()));
            String eventTitle = cursor.getString(cursor.getColumnIndex(CreateDatabase.getEvents_title_field()));
            String eventType = cursor.getString(cursor.getColumnIndex(CreateDatabase.getEvents_type_field()));
            String eventBegin = cursor.getString(cursor.getColumnIndex(CreateDatabase.getEvents_begin_field()));
            String eventEnd = cursor.getString(cursor.getColumnIndex(CreateDatabase.getEvents_end_field()));
            result = new Event(eventId, eventTitle, eventType, eventBegin, eventEnd);
            cursor.close();
        }

        database.close();

        return result;
    }

    public void insertPresence(int participantId, int eventId, String date, boolean forced) {
        ContentValues values;

        values = new ContentValues();
        values.put(CreateDatabase.getPresences_participantId_field(), participantId);
        values.put(CreateDatabase.getPresences_eventId_field(), eventId);
        values.put(CreateDatabase.getPresences_date_field(), date);
        values.put(CreateDatabase.getPresences_forced_field(), (forced) ? 1 : 0);

        database = createDatabase.getWritableDatabase();
        database.insert(CreateDatabase.getPresences_table_name(), null, values);
        database.close();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(context.getString(R.string.app_sharedPref_syncstatus), false);
        editor.apply();
    }

    public List<Presence> getPresencesInEvent(int event) {
        List<Presence> presences = new ArrayList<>();
        String[] fields = {CreateDatabase.getPresences_id_field(),
                CreateDatabase.getPresences_participantId_field(),
                CreateDatabase.getPresences_eventId_field(),
                CreateDatabase.getPresences_date_field(),
                CreateDatabase.getPresences_forced_field()};
        database = createDatabase.getReadableDatabase();
        String orderBy = CreateDatabase.getPresences_date_field() + " DESC";
        String where = CreateDatabase.getPresences_eventId_field() + "=" + event;

        Cursor cursor = database.query(CreateDatabase.getPresences_table_name(), fields, where, null, null, null, orderBy, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getPresences_id_field()));
                int participantId = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getPresences_participantId_field()));
                int eventId = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getPresences_eventId_field()));
                String date = cursor.getString(cursor.getColumnIndex(CreateDatabase.getPresences_date_field()));
                int forced = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getPresences_forced_field()));
                Presence presence = new Presence(id, participantId, eventId, date, forced != 0);
                presences.add(presence);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return presences;
    }

    public List<Presence> getPresences() {
        List<Presence> presences = new ArrayList<>();
        String[] fields = {CreateDatabase.getPresences_id_field(),
                CreateDatabase.getPresences_participantId_field(),
                CreateDatabase.getPresences_eventId_field(),
                CreateDatabase.getPresences_date_field(),
                CreateDatabase.getPresences_forced_field()};
        database = createDatabase.getReadableDatabase();

        Cursor cursor = database.query(CreateDatabase.getPresences_table_name(), fields, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getPresences_id_field()));
                int participantId = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getPresences_participantId_field()));
                int eventId = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getPresences_eventId_field()));
                String date = cursor.getString(cursor.getColumnIndex(CreateDatabase.getPresences_date_field()));
                int forced = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getPresences_forced_field()));
                Presence presence = new Presence(id, participantId, eventId, date, forced != 0);
                presences.add(presence);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return presences;
    }

    public boolean presenceIsDuplicate(int eventId, int participantId) {
        boolean result = false;
        Cursor cursor;
        String[] fields = {CreateDatabase.getPresences_eventId_field(), CreateDatabase.getPresences_participantId_field()};
        String where = CreateDatabase.getPresences_eventId_field() + "=" + eventId + " AND " + CreateDatabase.getPresences_participantId_field() + "=" + participantId;
        database = createDatabase.getReadableDatabase();
        cursor = database.query(CreateDatabase.getPresences_table_name(), fields, where, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            result = true;
        }
        cursor.close();
        return result;
    }

    public void insertParticipant(int id, String name, String lastName) {
        ContentValues values;

        values = new ContentValues();
        values.put(CreateDatabase.getParticipants_id_field(), id);
        values.put(CreateDatabase.getParticipants_name_field(), name);
        values.put(CreateDatabase.getParticipants_lastname_field(), lastName);

        database = createDatabase.getWritableDatabase();
        database.insert(CreateDatabase.getParticipants_table_name(), null, values);
        database.close();
    }

    public List<Participant> getParticipants() {
        List<Participant> participants = new ArrayList<>();
        Cursor cursor;
        String[] fields = {CreateDatabase.getParticipants_id_field(), CreateDatabase.getParticipants_name_field(), CreateDatabase.getParticipants_lastname_field()};
        String orderBy = CreateDatabase.getParticipants_name_field() + " ASC";
        database = createDatabase.getReadableDatabase();
        cursor = database.query(CreateDatabase.getParticipants_table_name(), fields, null, null, null, null, orderBy, null);


        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getParticipants_id_field()));
                String name = cursor.getString(cursor.getColumnIndex(CreateDatabase.getParticipants_name_field()));
                String lastName = cursor.getString(cursor.getColumnIndex(CreateDatabase.getParticipants_lastname_field()));
                Participant participant = new Participant(id, name + " " + lastName);
                participants.add(participant);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return participants;
    }

    public Participant getParticipantById(int id) {
        Participant result = null;
        Cursor cursor;
        String[] fields = {CreateDatabase.getParticipants_id_field(), CreateDatabase.getParticipants_name_field(), CreateDatabase.getParticipants_lastname_field()};
        String where = CreateDatabase.getParticipants_id_field() + "=" + id;
        database = createDatabase.getReadableDatabase();
        cursor = database.query(CreateDatabase.getParticipants_table_name(), fields, where, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int participantId = cursor.getInt(cursor.getColumnIndex(CreateDatabase.getParticipants_id_field()));
            String participantName = cursor.getString(cursor.getColumnIndex(CreateDatabase.getParticipants_name_field()));
            String participantLastName = cursor.getString(cursor.getColumnIndex(CreateDatabase.getParticipants_lastname_field()));
            result = new Participant(participantId, participantName + " " + participantLastName);
            cursor.close();
        }

        database.close();

        return result;
    }

    public void insertSubscription(int eventId, int particicipantId) {
        ContentValues values;

        values = new ContentValues();
        values.put(CreateDatabase.getSubscriptions_eventId_field(), eventId);
        values.put(CreateDatabase.getSubscriptions_participantId_field(), particicipantId);

        database = createDatabase.getWritableDatabase();
        database.insert(CreateDatabase.getSubscriptions_table_name(), null, values);
        database.close();
    }

    public boolean checkSubscription(int eventId, int participantId) {
        boolean result = false;
        Cursor cursor;
        String[] fields = {CreateDatabase.getSubscriptions_eventId_field(), CreateDatabase.getSubscriptions_participantId_field()};
        String where = CreateDatabase.getSubscriptions_eventId_field() + "=" + eventId + " AND " + CreateDatabase.getSubscriptions_participantId_field() + "=" + participantId;
        database = createDatabase.getReadableDatabase();
        cursor = database.query(CreateDatabase.getSubscriptions_table_name(), fields, where, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            result = true;
        }
        cursor.close();
        return result;
    }
}
