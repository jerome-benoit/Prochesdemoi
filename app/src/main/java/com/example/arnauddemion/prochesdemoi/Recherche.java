package com.example.arnauddemion.prochesdemoi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Recherche extends Activity {
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
        setContentView(R.layout.activity_recherche);

        ListView myList=(ListView)findViewById(android.R.id.list);

        persons = new ArrayList<String>();

        User.fetchPersons();
        //TODO: move this code in a displayFriends method of CurrentUser
        //TODO: create a button add for each friend
        for (Personne personne : User.getPersons()) {
            persons.add(personne.getFirstname() + " " + personne.getLastname());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                persons);

        myList.setAdapter(adapter);
    }
}
