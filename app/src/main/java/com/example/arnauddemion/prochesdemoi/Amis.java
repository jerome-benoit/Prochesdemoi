package com.example.arnauddemion.prochesdemoi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Amis extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    CurrentUser User = CurrentUser.getInstance();

    private ListView mListView;
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

        User.fetchFriends();
        //TODO: move this code in a displayFriends method of CurrentUser
        for (Personne friend : User.getFriends()) {
            persons.add(friend.getFirstname());
            //friend.getLastname();
        }

        mListView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Amis.this,
                android.R.layout.simple_list_item_1, persons);
        mListView.setAdapter(adapter);
    }
}
