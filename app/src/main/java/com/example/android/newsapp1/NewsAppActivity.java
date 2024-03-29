package com.example.android.newsapp1;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//NewsCrunchActivity will implement LoaderCallBacks and overrides its methods
public class NewsAppActivity extends AppCompatActivity
        implements LoaderCallbacks<List<NewsItem>> {

    //Store class name for log messages
    private static final String LOG_TAG = NewsAppActivity.class.getName();

    //Id of the loader process we use to fetch the data
    private static final int NEWS_LOADER_ID = 1;

    private ProgressBar ncProgressBar;
    private TextView ncStatusTextView;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ncProgressBar =  findViewById(R.id.loading_spinner);

        ListView newsListView = findViewById(R.id.list);

        newsAdapter = new NewsAdapter(this, new ArrayList<NewsItem>());

        newsListView.setAdapter(newsAdapter);

        //Clicked item listener to open the website of the clicked news item
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri newsUri = Uri.parse(Objects.requireNonNull(newsAdapter.getItem(position)).getUrl());
                Intent newsIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(newsIntent);
            }
        });

        //Get the TextView that should appear if the list is empty
        ncStatusTextView =  findViewById(R.id.status_textview);

        //Set the view that appears when the ListView is empty of news items
        newsListView.setEmptyView(ncStatusTextView);

        /*
        Check if there is an active internet connecting and operate accordingly
        If connection is available then start fetching data from the internet
        otherwise display no internet connection text view
        */
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connMgr).getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager newsLoaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            newsLoaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            ncProgressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            ncStatusTextView.setText(R.string.no_connection);
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "Earthquake Activity onCreateLoader called");
        String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?order-by=newest&show-tags=contributor&page" +
                "=1&page-size=100&q=Android&api-key=8a5daedf-639c-4af2-9086-288bc1c2a188";
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        // Clear the adapter of previous earthquake data and hide progress bar
        newsAdapter.clear();
        ncProgressBar.setVisibility(View.GONE);
        ncStatusTextView.setText(R.string.empty_list);

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            newsAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        // Loader reset, so we can clear out our existing data.
        newsAdapter.clear();
    }
}
