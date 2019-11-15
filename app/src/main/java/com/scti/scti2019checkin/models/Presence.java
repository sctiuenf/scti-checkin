package com.scti.scti2019checkin.models;

public class Presence {
    private int id;
    private int participantId;
    private int eventId;
    private String date;
    private boolean forced;

    public Presence(int id, int participantId, int eventId, String date, boolean forced) {
        this.id = id;
        this.participantId = participantId;
        this.eventId = eventId;
        this.date = date;
        this.forced = forced;
    }

    public int getId() {
        return id;
    }

    public int getParticipantId() {
        return participantId;
    }

    public int getEventId() {
        return eventId;
    }

    public String getDate() {
        return date;
    }

    public boolean isForced() {
        return forced;
    }
}
