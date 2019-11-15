package com.scti.scti2019checkin.models;

public class Event {
    public static final String TYPE_MINICOURSE = "minicurso", TYPE_LECTURE = "palestra", TYPE_SPECIAL = "null";

    private int id;
    private String title;
    private String type;
    private String beginDate;
    private String endDate;

    public Event(int id, String title, String type, String beginDate, String endDate) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
