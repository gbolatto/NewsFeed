package com.example.android.newsfeed;

import android.util.Log;

import org.json.JSONArray;
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

/**
 * Created by gbolatto on 6/25/2018.
 */
public class QueryUtils {

    private static final String LOG_TAG = "Network error";

    private QueryUtils() {
    }

    /**
     * access the API and return a list of NewsStory objects
     */
    public static List<NewsStory> getNewsStoryData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return parseNewsStoriesFromJSON(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL. ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results. ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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

    /**
     * returns a list of newsStories
     */
    private static ArrayList<NewsStory> parseNewsStoriesFromJSON(String jsonResponse) {
        ArrayList<NewsStory> newsStories = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(jsonResponse);
            JSONObject response = rootObject.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject tempStory = results.getJSONObject(i);

                String title = tempStory.getString("webTitle");
                String section = tempStory.getString("sectionName");
                String webUrl = tempStory.getString("webUrl");
                String preSplitPublicationDate = tempStory.getString("webPublicationDate");
                // split at the 'T' in the returned api call to get the date only and discard the rest
                String publicationDate = preSplitPublicationDate.split("T")[0];
                JSONArray tempArray = tempStory.getJSONArray("tags");
                String author;
                if (tempArray.length() > 0) {
                    author = tempArray.getJSONObject(0).getString("webTitle");
                } else {
                    author = ""; // if no author, make an empty string for the layout
                }

                newsStories.add(new NewsStory(title, section, webUrl, publicationDate, author));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing JSON: ", e);
        }
        return newsStories;
    }
}