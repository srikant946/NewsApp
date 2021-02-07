// Only created when Loader was about to be used in the app..

/* Here, we extend AsyncTaskLoader and specify List as the generic parameter, which explains what type of data is expected to be loaded.
In this case, the loader is loading a list of Earthquake objects.
Then we take a String URL in the constructor, and in loadInBackground(), we'll do the exact same operations as in doInBackground back in EarthquakeAsyncTask.
Important: Notice that we also override the onStartLoading() method to call forceLoad() which is a required step to actually trigger the loadInBackground() method to execute.
*/

package com.example.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.newsapp.News;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /** Tag for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
        Log.e(LOG_TAG, "Error with creating UR");
        Log.d(LOG_TAG, "loadUserData() email is " + mUrl);
    }
    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground()
    {
        Log.d(LOG_TAG, "loadUserData) email is " + mUrl);
        if (mUrl == null) {
            return null;
        }


        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<News> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }


}