// This is a custom Adapter java file where the Objects of the class 'Earthquake' would be created and the list items would be created over here.
// The final arrayList however that consists of all the list items would be actually created in 'QueryUtils.java' File and that would be passed to 'EarthquakeActivity.java' File where that (i.e the one that was created in QueryUtils.java) arraylist would be stored in a variable and that variable would then be used for linking to custom array adapter.
// The data would come from 'QueryUtils.java' class which would passed to the constructor of 'Earthquake.java' File  and then that data would be accessed by the 'public' methods whose definition is present in the 'Earthquake.java' File.

package com.example.newsapp;

import android.content.Context;

// Now:
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;

import android.graphics.drawable.GradientDrawable;

public class NewsAdapter extends ArrayAdapter<News>    // <Earthquake> is passed beside the ArrayAdapter to indicate that the ArrayAdapter source input is the 'Earthquake' Class
{

    // This separator  would just be passed as input to the 'split()' method and if valid then the appropriate 2 separate strings would be created or else the hardcoded 'near the' would be the first string before the primary location
    private static final String LOCATION_SEPARATOR = " of ";

    public NewsAdapter(Context context, ArrayList<News> pEarthquake) {
        super(context, 0, pEarthquake);  // ArrayAdapter's constructor is being called here.. the Resource Id is kept 0 here so that the ArrayAdapter does not create a list item view for us manually, but we ourselves would create a list item view via our own getView() implementation.
    }

    // View Recycling
    // The below code would decide how the views would be shown up an hence it is written over here
    // This below part is crucial when we are implementing the use of custom adapters in our code.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Creation/Reusing of List item view

        // Storing a view in the 'listItemView' variable
        View listItemView = convertView;

        // 'null' here means that if no views are there to reuse..if no views are there to be reused, we would inflate a new view according to the XML Layout file passed in the inflate() method
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        // The received object at the desired position fetched via the 'getItem()' method would be stored in a variable named 'earthquake' and that would have a return Data type of Custom Class 'Earthquake' because that is the source input of our Superclass.
        // 'Earthquake' is our Custom Class and here we are just creating an Object of that class.
        News earthquake = getItem(position);

        // First, we get the original location String from the Earthquake object and store that in a variable.
        String originalLocation = earthquake.getUrlToImage();

        String locationOffset = "";
        String Locationp = "";

        // Now we would refer to the view that we had created which consisted of 3 textviews.


        // Find the TextView with view ID magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.title);

        /* Earlier when we were simply getting the location in a SINGLE Textview with no Font styling, the below commented out method was valid, but now that we have 2 textviews, the below method is no more valid
        // Getting the Location TextView by its id
        TextView locationTextView = (TextView) listItemView.findViewById(R.id.location);
        locationTextView.setText(earthquake.getEarthquakeLocation());   // The position that was stored in our variable..that gets passed to our getMiwokTranslation() method and the data in the textview gets set in our layout
        */

        // The 2 separate Location related textviews would now be populated accordingly
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.PostTitle);
        primaryLocationView.setText(Locationp);

        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.PostDesc);
        locationOffsetView.setText(locationOffset);

        return listItemView;  // The list is then finally returned once its populated..
    }
}
