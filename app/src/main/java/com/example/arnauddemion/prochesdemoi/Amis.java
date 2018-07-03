package com.example.arnauddemion.prochesdemoi;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Amis extends ListActivity {
    private final String TAG = getClass().getSimpleName();
    CurrentUser User = CurrentUser.getInstance();

    private ArrayList<String> persons;

    /**
     * Display of the friends list
     * The list is fetched from the server
     * An unfriend button is displayed next each friend
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amis);

        persons = new ArrayList<String>();

        User.fetchFriends();
        //TODO: move this code in a displayFriends method of CurrentUser
        //TODO: create a button unfriend for each friend
        for (Personne friend : User.getFriends()) {
            persons.add(friend.getFirstname() + " " + friend.getLastname());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                persons);

        setListAdapter(adapter);
    }
}
