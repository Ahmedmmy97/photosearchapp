package com.example.photosearchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {
    EditText searchText;
    RadioButton google;
    RadioButton flickr;
    Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchText = findViewById(R.id.search_text);
        google = findViewById(R.id.google_radio);
        flickr = findViewById(R.id.flickr_radio);
        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchResult.class);
                intent.putExtra("query",searchText.getText().toString());
                intent.putExtra("type",google.isChecked()?Helper.TYPE_GOOGLE:Helper.TYPE_FlICKR);
                startActivity(intent);
            }
        });

    }
}
