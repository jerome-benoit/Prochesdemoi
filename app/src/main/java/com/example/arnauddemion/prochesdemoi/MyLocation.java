package com.example.arnauddemion.prochesdemoi;

import java.sql.Date;

public class MyLocation {

    private Date timestamp;
    private double latitude;
    private double longitude;

    MyLocation() {

    }

    MyLocation(double latitude, double longitude, Date timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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
}
