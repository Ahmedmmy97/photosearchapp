package com.example.photosearchapp.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.photosearchapp.Listeners.OnSearchCompleted;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LoadImagesFromGoogleTask extends AsyncTask<String, Void, List<String>> {
    private ProgressDialog progressDialog;
    private Context context;
    private OnSearchCompleted onSearchCompleted;

    public LoadImagesFromGoogleTask(Context context, OnSearchCompleted onSearchCompleted) {
        this.context = context;
        this.onSearchCompleted = onSearchCompleted;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Starting Loading Dialog before retrieving images
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading images from Google. Please wait...");
        progressDialog.show();
    }

    @Override
    protected List<String> doInBackground(String... searchQuery) {
        String encodedSearchUrl = "";
        //assigning userAgent value
        String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
        Document document = null;
        List<String> returnedURLS = new ArrayList<String>();
        try {
            //Creating search string using the entered search value , 'tbs=isz:m' for medium size photos ,'isz:l' for large size
            encodedSearchUrl = "https://www.google.com/search?q=" + URLEncoder.encode(searchQuery[0], "UTF-8") + "&tbm=isch&tbs=isz:m";
            //getting response html document from search results
            document = Jsoup.connect(encodedSearchUrl).userAgent(userAgent).referrer("https://www.google.com/").get();
            //make query to find resulted images' urls
            Elements elements = document.select("div.rg_meta");
            JsonObject jsonObject;
            for (Element element : elements) {
                if (element.childNodeSize() > 0) {
                    jsonObject = new JsonParser().parse(element.childNode(0).toString()).getAsJsonObject();
                    returnedURLS.add(jsonObject.get("ou").getAsString());
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnedURLS;
    }

    protected void onPostExecute(List<String> urls) {
        progressDialog.dismiss();
        //calling search completed listener
        if (onSearchCompleted != null)
            onSearchCompleted.SearchResult(urls);
    }
}

