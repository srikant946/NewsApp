/* This File i.e 'EarthquakeActivity.java' Would mediate all the Interaction between the Custom class and the custom array Adapter
How, the ArrayList would be created and how the ArrayAdapter would be set up in conjunction with the ArrayList is setup from here
*/

package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// For Loader related stuff
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.AsyncTaskLoader;
import android.widget.TextView;

// For network Connectivity status
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


// The 'implements....' part was added when we wanted the Loader to Load up the data..before that for AsyncTask it was not Required.
public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>>
{

    public static final String LOG_TAG = NewsActivity.class.getName();

    // New URL that would be set according to User Preferences
    // private static final String USGS_REQUEST_URL = "https://newsapi.org/v2/top-headlines?country=in&apiKey=693a239d37dd4c81a4bf46143f248a86";

    private static final String USGS_REQUEST_URL = "https://hubblesite.org/api/v3/news?page=all";
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    // To access and modify the instance of the EarthquakeAdapter, we need to make it a global variable in the EarthquakeActivity.
    /** Adapter for the list of earthquakes */
    private NewsAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // Just For checking the network connectivity, the lines 64-73 were added and the 'else' block was also added here for displaying message in case of poor internet i.e lines 82-91

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if(networkInfo != null && networkInfo.isConnected())
        {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. The second argument allows us to pass a bundle of additional information.
            // Pass in 'this' activity for the LoaderCallbacks parameter (which is valid because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else
        {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // We have identified our Textview that would display the message of "No Earthquakes Found" by its id.
        // Now, the Text of 'No Earthquakes Found' would be shown only if the setText() message on our Textview in our onLoadFinished() method executes
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        // Since, we want to specify intents for clicks on list item, we would use the 'setOnItemClickListener' method.
        // We need to declare an OnItemClickListener on the ListView. OnItemClickListener is an interface, which contains a single method onItemClick().
        // We declare an anonymous class that implements this interface, and provides customized logic for what should happen in the onItemClick() method.

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
//                Earthquake currentEarthquake = adapter.getItem(position);    // Earlier when it was hardcoded JSON Data, the EarthquakeAdapter was stored in 'adapter'

                // Now this same line of code would be:
                News currentEarthquake = mAdapter.getItem(position);

                // The Intent constructor (that we want to use) requires a Uri object, so we need to convert our URL (in the form of a String) into a URI. We know that our earthquake URL is a more specific form of a URI, so we can use the Uri.parse method
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                // The Intent constructor also accepts a URI for the data resource we want to view, and Android will sort out the best app to handle this sort of content.
                // For instance, if the URI represented a location, Android would open up a mapping app.
                // In this case, the resource is an HTTP URL, so Android will usually open up a browser.
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, USGS_REQUEST_URL);
    }
    @Override

    public void onLoadFinished(Loader<List<News>> loader, List<News> earthquakes)
    {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // The message to be displayed if no Earthquakes are found is declared here and the reason it is declared on the onLoadFinished() method is because we dont want to directly show up "No earthquakes found" when we are just starting the app.
        // We want to show the message once the loader has its work done and then if no data exists, then we would show the message
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty())
        {
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    // Methods that deal with preference selection
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

