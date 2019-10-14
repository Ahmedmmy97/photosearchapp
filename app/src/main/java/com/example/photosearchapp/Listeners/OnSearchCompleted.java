package com.example.photosearchapp.Listeners;

import java.util.List;

public interface OnSearchCompleted {
    void SearchResult(List<String> urls);  //Fires when search is completed
}
