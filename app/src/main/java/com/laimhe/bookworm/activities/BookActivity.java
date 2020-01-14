package com.laimhe.bookworm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.laimhe.bookworm.R;


/**
 * This is the activity used to show details for a single book
 *
 * TODO: Design this activity
 * TODO: Extract information from the Book model and populate UI with that information
 */
public class BookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
    }
}
