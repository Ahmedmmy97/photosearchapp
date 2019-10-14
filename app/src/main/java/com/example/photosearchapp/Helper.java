package com.example.photosearchapp;

import com.example.photosearchapp.FlickrModels.FlickerData;
import com.example.photosearchapp.FlickrModels.Photo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Helper {
    private static final String API_KEY = "c9ca501d27bd752dcdbe0bd8313b41d4";
    public static final int TYPE_FlICKR = 0;
    public static final int TYPE_GOOGLE = 1;
    public static URL createHTTPRequest(String query) {
        String baseUrl = "https://www.flickr.com/services/rest/" +
                "?method=flickr.photos.search" +
                "&api_key=%s" +
                "&text=%s" +
                "&format=%s" +
                "&nojsoncallback=%s";
        String url = String.format(baseUrl, API_KEY, query, "json", 1);
        URL photosurl = null;
        try {
            photosurl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return photosurl;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            // urlConnection.setReadTimeout(10000 /* milliseconds */);
            // urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
public static FlickerData CreateFlickrDataFromResponse(String jsonResponse){
        FlickerData data = null;
    try {
        JSONObject root = new JSONObject(jsonResponse);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        data = gson.fromJson(root.toString(), FlickerData.class);

    } catch (JSONException e) {
        e.printStackTrace();
    }
    return data;
}
    public static List<String> CreatePhotosUrlsFormData(FlickerData data){
        List<String> returnedURLS = new ArrayList<String>();

        if(data.getStat().equals("ok")){
            for(Photo photo :data.getPhotos().getPhoto()){
                //http://farm{farmid}.staticflicker.com/{server-id}/{id}_{secret}{size}.jpg
                String photourl = "http://farm%s.staticflickr.com/%s/%s_%s%s.jpg";
                returnedURLS.add(String.format(photourl,photo.getFarm(),photo.getServer(),photo.getId(),photo.getSecret(),"_n"));
            }
        }
        return returnedURLS;
    }
}
