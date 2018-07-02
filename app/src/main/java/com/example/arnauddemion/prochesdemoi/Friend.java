package com.example.arnauddemion.prochesdemoi;

public class Friend {
    private Personne friend;
    private boolean is_valid;

    public Personne getFriend() {
        return friend;
    }

    public void setFriend(Personne person) {
        this.friend = person;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }

    public boolean getIs_valid() {
        return is_valid;
    }

}
