package com.pam.lab2;

public class Event {
    public String title;
    public String date;
    public String startTime;
    public String endTime;
    public String reminder;
    public String notes;
    public String category;

    public Event() {
    }

    public Event(String title, String date, String startTime, String endTime,
                 String reminder, String notes, String category) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reminder = reminder;
        this.notes = notes;
        this.category = category;
    }
}

