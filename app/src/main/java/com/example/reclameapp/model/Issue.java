package com.example.reclameapp.model;

import java.sql.Time;
import java.sql.Timestamp;

public class Issue {
    private int id;
    private String user;
    private String description;
    private double latitude;
    private double longitude;
    private Timestamp day;
    private String sinc;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Timestamp getDay() {
        return day;
    }

    public void setDay(Timestamp day) {
        this.day = day;
    }

    public String getSinc() {
        return sinc;
    }

    public void setSinc(String sinc) {
        this.sinc = sinc;
    }

}