package com.example.photosearchapp.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.photosearchapp.FlickrModels.FlickerData;
import com.example.photosearchapp.Helper;
import com.example.photosearchapp.Listeners.OnSearchCompleted;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class LoadImagesFromFlickrTask extends AsyncTask<String, Void, List<String>> {
    private ProgressDialog progressDialog;
    private Context context;
    private OnSearchCompleted onSearchCompleted;

    public LoadImagesFromFlickrTask(Context context, OnSearchCompleted onSearchCompleted) {
        this.context = context;
        this.onSearchCompleted = onSearchCompleted;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading images from Flickr. Please wait...");
        progressDialog.show();
    }

    @Override
    protected List<String> doInBackground(String... params) {
        URL url = Helper.createHTTPRequest(params[0]);
        String jsonResponse = null;
        try {
            jsonResponse = Helper.makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FlickerData flickerData = Helper.CreateFlickrDataFromResponse(jsonResponse);

        return Helper.CreatePhotosUrlsFormData(flickerData);
    }

    @Override
    protected void onPostExecute(List<String> urls) {
        progressDialog.dismiss();
        if (onSearchCompleted != null)
            onSearchCompleted.SearchResult(urls);
    }
}

