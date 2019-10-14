package com.example.photosearchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.photosearchapp.Listeners.OnSearchCompleted;
import com.example.photosearchapp.Tasks.LoadImagesFromFlickrTask;
import com.example.photosearchapp.Tasks.LoadImagesFromGoogleTask;

import java.util.List;

public class SearchResult extends AppCompatActivity  {
    CustomGridView gridView;
    Adapter adapter;
    String Query;
    TextView title;
    TextView na;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        gridView = findViewById(R.id.recycler);
        gridView.setExpanded(true);
        gridView.setNumColumns(3);

        Query = getIntent().getStringExtra("query");
        type = getIntent().getIntExtra("type",0);
        title = findViewById(R.id.title);
        title.setText(Query);
        na = findViewById(R.id.na);
        switch (type){
            case Helper.TYPE_FlICKR:
                new LoadImagesFromFlickrTask(this,new OnSearchCompleted() {
                    @Override
                    public void SearchResult(final List<String> urls) {
                        if(urls.size()==0)
                            na.setVisibility(View.VISIBLE);
                        adapter = new Adapter(SearchResult.this,urls);
                        gridView.setAdapter(adapter);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(),PhotoPreview.class);
                                intent.putExtra("url",urls.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                }).execute(Query);
                break;
            case Helper.TYPE_GOOGLE:
                new LoadImagesFromGoogleTask(this,new OnSearchCompleted() {
                    @Override
                    public void SearchResult(final List<String> urls) {
                        if(urls.size()==0)
                            na.setVisibility(View.VISIBLE);
                        adapter = new Adapter(SearchResult.this,urls);
                        gridView.setAdapter(adapter);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(),PhotoPreview.class);
                                intent.putExtra("url",urls.get(position));
                                startActivity(intent);
                            }
                        });
                    }
                }).execute(Query);
                break;
        }



    }
}
