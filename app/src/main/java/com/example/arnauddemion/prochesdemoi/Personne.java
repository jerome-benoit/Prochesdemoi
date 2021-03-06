package com.example.arnauddemion.prochesdemoi;

import com.google.android.gms.maps.model.LatLng;

public class Personne {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private MyLocation location;
    private boolean online;

    Personne() {
        location = new MyLocation();
    }

    Personne(Integer id) {
        this.id = id;
        location = new MyLocation();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFullname() {
        return firstname + " " + lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MyLocation getLocation() {
        return location;
    }

    public void setLocation(MyLocation location) {
        this.location = location;
    }

    public LatLng getLocationLatLng() {
        return new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
    }

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

}
