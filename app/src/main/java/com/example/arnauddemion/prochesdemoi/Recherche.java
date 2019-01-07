package com.example.arnauddemion.prochesdemoi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Recherche extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    CurrentUser User = CurrentUser.getInstance();

    private ArrayList<String> persons;
    private ListView myList;
    private ArrayAdapter<String> adapter;
    private EditText editText;

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

        EditText editText = findViewById(R.id.editText);

        persons = new ArrayList<String>();

        User.fetchPersons();
        //TODO: create a button unfriend for each friend
        for (Personne personne : User.getPersons()) {
            persons.add(personne.getFullname());
        }

        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.friend_name, persons);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                Toast.makeText(getApplicationContext(),"before text change",Toast.LENGTH_LONG).show();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                Toast.makeText(getApplicationContext(),"after text change",Toast.LENGTH_LONG).show();
            }
        });

        myList.setAdapter(adapter);
    }
}
