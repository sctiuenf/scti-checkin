package com.scti.scti2019checkin.models;

public class Event {
    private int id;
    private int sctiId;
    private String name;
    private String date;

    public Event(int id, int sctiId, String name, String date) {
        this.id = id;
        this.sctiId = sctiId;
        this.name = name;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getSctiId() {
        return sctiId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }


}
