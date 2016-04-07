package com.alphalabz.familyapp.objects;

import java.io.Serializable;

/**
 * Created by Aditya Rathi on 10-Feb-16.
 */
public class Event implements Serializable {

    private int event_id;
    private String member_id;
    private String date,city,contact,email;
    private int eventType = -1;
    private String spouse_id = "";

    public Event(int event_id, String member_id, String date, String city, String contact, String email, int eventType) {
        this.event_id = event_id;
        this.member_id = member_id;
        this.date = date;
        this.city = city;
        this.contact = contact;
        this.email = email;
        this.eventType = eventType;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getSpouse_id() {
        return spouse_id;
    }

    public void setSpouse_id(String spouse_id) {
        this.spouse_id = spouse_id;
    }
}
