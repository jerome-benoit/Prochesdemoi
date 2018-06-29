package com.example.arnauddemion.prochesdemoi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Recherche extends AppCompatActivity {

    /**
     * Display of the users list
     * The list is fetched from the server
     * An "Add friend" button is displayed next to each user of the list
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);
    }
}
