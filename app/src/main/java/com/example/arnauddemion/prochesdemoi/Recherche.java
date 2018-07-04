package com.example.arnauddemion.prochesdemoi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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

        ListView myList = findViewById(android.R.id.list);

        persons = new ArrayList<String>();

        List<Personne> searchList = User.searchPersons("isabelle");

        //TODO: create a button add for each friend
        if (searchList != null) {
            for (Personne personne : searchList) {
                persons.add(personne.getFullname());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                persons);

        myList.setAdapter(adapter);
    }
}
