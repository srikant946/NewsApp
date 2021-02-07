package com.example.newsapp;

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
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = NewsActivity.class.getSimpleName();

    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */

    // Helper methods
    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        Log.d(LOG_TAG, "loadUseDat email is " + url);

        String jsonResponse = "";

        // If the URL is null, then return Back Empty String
        if(url == null)
        {
            return jsonResponse;  // Empty String would be returned
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {

            Log.d(LOG_TAG, "loadrfrrrrrrrr email is " + url);

            // Setting up the HTTP Request

            // The openConnection() method was applied on the URL Object and it was then typecasted to HttpURLConnection
            urlConnection = (HttpURLConnection) url.openConnection();

            Log.d(LOG_TAG, "Ifcvfbnm, me hu" + urlConnection);

            // WE specify the connection type by 'setRequestMethod()'
            urlConnection.setRequestMethod("GET");

            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            Log.d(LOG_TAG, "After Timeout Check" + urlConnection);

            // For sending out the HTTP Request to the Server after the Formation of HTTP Request via the 'setRequestMethod'
            urlConnection.connect();

            // Receiving the HTTP Response

            // Checking if a valid response code has been generated or not..If, yes then we would parse the input stream and return the response
            if(urlConnection.getResponseCode() == 200)
            {
                Log.d(LOG_TAG, "After conbgnm Check" + urlConnection.getResponseCode());

                Log.d(LOG_TAG, "erespf" + urlConnection);

                // getInputStream() would return us the Input stream that contains all the results
                // inputStream is used over here because when we use the HttpURLConnection class (Which we use to return our JSON Data), the server response is in the form of Inputstream.
                inputStream = urlConnection.getInputStream();

                // The results stored in the Input stream i.e raw Binary data is converted via the 'readFromStream' Helper method
                jsonResponse = readFromStream(inputStream);
            }
            else
            {
                Log.e(LOG_TAG, "Error Response Code is: " + urlConnection.getResponseCode());
            }

        }
        catch (IOException e)
        {
            Log.e("Class Name", e.getClass().getSimpleName());
            Log.e(LOG_TAG, "Proble establishing the connection", e);
        }
        finally
        {
            // Cleaning up the Resources used by the app
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // HELPER METHOD TO READ FROM INPUT STREAM i.e TO CONVERT RAW BINARY DATA OF THE INPUT STREAM TO HUMAN READABLE FORM
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException
    {

        Log.d(LOG_TAG, "i/p stream" + inputStream);

        // Creating a StringBuilder Object
        StringBuilder output = new StringBuilder();

        if (inputStream != null)
        {
            // The 'inputStreamReader' reads one character at a time and hence we wrap it to the 'BufferedReader' which in turn reads and saves larger chunk of data
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            // Reading Line by Line
            String line = reader.readLine();
            while (line != null)
            {
                output.append(line);          // Appends the line read to the StringBuilder
                line = reader.readLine();     // Proceed to read the next Line
            }
        }
        return output.toString();    // The toString() method is used to return output in String format from the StringBuilder
    }

    /**
     * parsing the given JSON response.
     */
    private static List<News> extractFeatureFromJson(String earthquakeJSON) {

        Log.d(LOG_TAG, "loadUsert email is " + earthquakeJSON);

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<News> earthquakes = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            JSONObject mainResponse = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray earthquakeArray = mainResponse.getJSONArray("articles");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < earthquakeArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // Extract the value for the key called "mag"
                String title = currentEarthquake.getString("title");

                // Extract the value for the key called "place"
                String author = currentEarthquake.getString("author");

                // Extract the value for the key called "time"
                String date = currentEarthquake.getString("date");

                String urlToImage = currentEarthquake.getString("urlToImage");

                // Extract the value for the key called "url"
                String url = currentEarthquake.getString("url");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                News earthquake = new News(title, author, date, urlToImage, url);

                // Add the new {@link Earthquake} to the list of earthquakes.
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    public static List<News> fetchEarthquakeData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        Log.d(LOG_TAG, "loadUserDat email is " + url);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            Log.d(LOG_TAG, "Passed URL " + url);

            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<News> earthquakes = extractFeatureFromJson(jsonResponse);

        Log.d(LOG_TAG, "loadUserDt email is " + earthquakes);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }
}
