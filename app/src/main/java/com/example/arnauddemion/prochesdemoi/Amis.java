package com.example.arnauddemion.prochesdemoi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Amis extends AppCompatActivity {

    /**
     * Display of the friends list
     * The list is fetched from the server
     * An unfriend button is displayed next each friend
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amis);
    }
}
