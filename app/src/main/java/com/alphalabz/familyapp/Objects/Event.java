package com.alphalabz.familyapp.Objects;

import java.io.Serializable;

/**
 * Created by Aditya Rathi on 10-Feb-16.
 */
public class Event implements Serializable {

    private String event_id,date,birthday,anniversary,remarks,years,city,contact,email;

    public Event(String event_id, String date, String birthday, String anniversary, String remarks, String years, String city, String contact, String email) {
        this.event_id = event_id;
        this.date = date;
        this.birthday = birthday;
        this.anniversary = anniversary;
        this.remarks = remarks;
        this.years = years;
        this.city = city;
        this.contact = contact;
        this.email = email;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAnniversary() {
        return anniversary;
    }

    public void setAnniversary(String anniversary) {
        this.anniversary = anniversary;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
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
}
