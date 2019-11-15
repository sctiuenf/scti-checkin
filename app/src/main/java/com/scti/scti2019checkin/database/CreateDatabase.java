package com.scti.scti2019checkin.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CreateDatabase extends SQLiteOpenHelper {
    private static final String database_name = "scti2019checkin.bd";
    private static final int database_version = 1;

    private static final String events_table_name = "eventos";
    private static final String events_id_field = "idEvento";
    private static final String events_title_field = "tituloEvento";
    private static final String events_type_field = "tipoEvento";
    private static final String events_begin_field = "inicioEvento";
    private static final String events_end_field = "fimEvento";

    private static final String participants_table_name = "participantes";
    private static final String participants_id_field = "idParticipante";
    private static final String participants_name_field = "nomeParticipante";
    private static final String participants_lastname_field = "sobrenomeParticipante";

    private static final String subscriptions_table_name = "inscricoes";
    private static final String subscriptions_id_field = "idInscricao";
    private static final String subscriptions_participantId_field = "idParticipante";
    private static final String subscriptions_eventId_field = "idEvento";

    private static final String presences_table_name = "presencas";
    private static final String presences_id_field = "idPresenca";
    private static final String presences_participantId_field = "idParticipante";
    private static final String presences_eventId_field = "idEvento";
    private static final String presences_date_field = "dataPresenca";
    private static final String presences_forced_field = "forced";

    private Context context;


    public CreateDatabase(Context context, String mail) {
        super(context, database_name + mail, null, database_version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlEvents = "CREATE TABLE " + events_table_name + "(" +
                events_id_field + " INTEGER PRIMARY KEY NOT NULL," +
                events_title_field + " TEXT NOT NULL," +
                events_type_field + " TEXT NOT NULL," +
                events_begin_field + " TEXT NOT NULL," +
                events_end_field + " TEXT NOT NULL)";

        String sqlParticipants = "CREATE TABLE " + participants_table_name + "(" +
                participants_id_field + " INTEGER PRIMARY KEY NOT NULL," +
                participants_name_field + " TEXT NOT NULL," +
                participants_lastname_field + " TEXT NOT NULL)";

        String sqlSubscriptions = "CREATE TABLE " + subscriptions_table_name + "(" +
                subscriptions_id_field + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                subscriptions_participantId_field + " INTEGER NOT NULL," +
                subscriptions_eventId_field + " INTEGER NOT NULL," +
                "UNIQUE(" + subscriptions_participantId_field + "," + subscriptions_eventId_field + ")," +
                "FOREIGN KEY(" + subscriptions_participantId_field + ") REFERENCES " + participants_table_name + "(" + participants_id_field + ")," +
                "FOREIGN KEY(" + subscriptions_eventId_field + ") REFERENCES " + events_table_name + "(" + events_id_field + "))";

        String sqlPresences = "CREATE TABLE " + presences_table_name + "(" +
                presences_id_field + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                presences_participantId_field + " INTEGER NOT NULL," +
                presences_eventId_field + " INTEGER NOT NULL," +
                presences_date_field + "  TEXT NOT NULL," +
                presences_forced_field + "  INTEGER NOT NULL," +
                "UNIQUE(" + presences_participantId_field + "," + presences_eventId_field + ")," +
                "FOREIGN KEY(" + presences_participantId_field + ") REFERENCES " + participants_table_name + "(" + participants_id_field + ")," +
                "FOREIGN KEY(" + presences_eventId_field + ") REFERENCES " + events_table_name + "(" + events_id_field + "))";

        db.execSQL(sqlEvents);
        db.execSQL(sqlParticipants);
        db.execSQL(sqlSubscriptions);
        db.execSQL(sqlPresences);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + subscriptions_table_name);
        db.execSQL("DROP TABLE IF EXISTS " + presences_table_name);
        db.execSQL("DROP TABLE IF EXISTS " + events_table_name);
        db.execSQL("DROP TABLE IF EXISTS " + participants_table_name);
        onCreate(db);
    }

    public static String getDatabase_name() {
        return database_name;
    }

    public static int getDatabase_version() {
        return database_version;
    }

    public static String getEvents_table_name() {
        return events_table_name;
    }

    public static String getEvents_id_field() {
        return events_id_field;
    }

    public static String getEvents_title_field() {
        return events_title_field;
    }

    public static String getEvents_type_field() {
        return events_type_field;
    }

    public static String getEvents_begin_field() {
        return events_begin_field;
    }

    public static String getEvents_end_field() {
        return events_end_field;
    }

    public static String getParticipants_table_name() {
        return participants_table_name;
    }

    public static String getParticipants_id_field() {
        return participants_id_field;
    }

    public static String getParticipants_name_field() {
        return participants_name_field;
    }

    public static String getParticipants_lastname_field() {
        return participants_lastname_field;
    }

    public static String getSubscriptions_table_name() {
        return subscriptions_table_name;
    }

    public static String getSubscriptions_id_field() {
        return subscriptions_id_field;
    }

    public static String getSubscriptions_participantId_field() {
        return subscriptions_participantId_field;
    }

    public static String getSubscriptions_eventId_field() {
        return subscriptions_eventId_field;
    }

    public static String getPresences_table_name() {
        return presences_table_name;
    }

    public static String getPresences_id_field() {
        return presences_id_field;
    }

    public static String getPresences_participantId_field() {
        return presences_participantId_field;
    }

    public static String getPresences_eventId_field() {
        return presences_eventId_field;
    }

    public static String getPresences_date_field() {
        return presences_date_field;
    }

    public static String getPresences_forced_field() {
        return presences_forced_field;
    }
}
