package com.example.usuario.news;

import android.text.TextUtils;
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
 * Created by Usuario on 9/7/17.
 */

public class QueryUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods
     * <p>
     * /**
     * Query the USGS dataset and return an {@link News} object to represent a single earthquake.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error requesting HTTP", e);
        }

        // Extract relevant fields from the JSON response and create an {@link News} object
        List<News> news_s = extractFeatureFromJson(jsonResponse);

        // Return the LIST OF News
        return news_s;
    }

    /**
     * returns new Url object from the given string URL
     */

    private static URL createUrl(String stringUrl) {

        URL url = null;
        try {
            url = new URL(stringUrl);

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     * Code for the connectivity
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Failed response. Error code:  " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * IN THE FOLLOWING BLOCK OF CODE WE ADD A HELPER METHOD IN ORDER TO READ
     * THE DATA OBTEINED BY THE RESPONSE. THIS DATA ARE BITES (INPUTSTREAM)
     * This inputStream needs to be "translated" into useful info for our app.
     * So we are going to "String" it , through the methods StringBuilder,
     * InputStreamReader and BufferedReader.
     *
     * @param inputStream: Is the InputStream given back by the server
     * @return the response of the request or null
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

    private static List<News> extractFeatureFromJson(String responseJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(responseJSON)) {
            return null;

        }

        //* Create an empty ArrayList that we can start adding News to the list.
        //ALSO PARSE JSON!

        List<News> news_s = new ArrayList<>();

        try {

            //We create the primary JSON Object,
            JSONObject primaryJsonObject = new JSONObject(responseJSON);

            // then we create the JSON Object from the JSON key "response"
            JSONObject newsResult = primaryJsonObject.getJSONObject("response");

            //We create now the array for the keys contained into the results
            JSONArray itemsArray = newsResult.getJSONArray("results");

            //Declare the rest of the strings
            String section = "";

            String website = "";

            String title = "";

            //Loop through all the news, get the title of the news, and news section
            for (int i = 0; i < itemsArray.length(); i++) {

                JSONObject currentItem = itemsArray.getJSONObject(i);

                website = currentItem.getString("webUrl");

                //If there is title info make this:

                if (newsResult.has("webTitle")) {

                    title = currentItem.getString("webTitle");

                    Log.v(LOG_TAG, "The key webTitle got title info");

                    //if there is not, show this message
                } else {
                    title = "Header N/A";

                }

                //if there is News section info, get the string from sectionName JSON

                if (newsResult.has("sectionName")) {
                    section = currentItem.getString("sectionName");
                    Log.v(LOG_TAG, "The key sectionName got title info");

                    //if there is not, show this message

                } else {
                    title = "Unknown title";
                }

                if (newsResult.has("type")) {
                    website = currentItem.getString("type");
                    Log.v(LOG_TAG, "The news got type of info");
                }

                //Create a new Book Object with the data of a given book
                News currentNews = new News(title, section, website);

                //Add the book to the List of Book Objects
                news_s.add(currentNews);
                }

            //if there is url, get the string from sectionName JSON

        } catch (JSONException e) {

            //If it fails, log it
            Log.e(LOG_TAG, "Error extracting the data from the JSON response", e);
        }

        return news_s;
    }

}

