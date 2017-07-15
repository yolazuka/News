package com.example.usuario.news;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<News>>,
        SwipeRefreshLayout.OnRefreshListener {

    public static final int NEWS_LOADER_ID = 1;

    //We specify an ID for our loader
    private static final String LOG_TAG = MainActivity.class.getName();
    /**
     * URL for news data from the GUARDIAN API site
     */
    private static final String THEGUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?api-key=test";

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * Adapter for the list of news
     */
    private NewsAdapter mAdapter;

    //We create a loadercounter //

    private int loaderCounter = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Set the connectivity manager, which checks the state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //To get the details on the current active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            //Get a link to the LoaderManager, in order to interact with loaders
            LoaderManager loaderManager = getLoaderManager();

            //in this moment the loader is initializing. it links to the int ID
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);

            //otherwise display an error. First hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);

        } else {

            //Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        //set an empty view if there is not data from the url on the app

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of news as input
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected news
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mAdapter.getItem
                        (position).getWebsite()));
                startActivity(intent);

            }
        });

        //Get a reference to the LoaderManager, in order to interact with loaders.

        LoaderManager loaderManager = getLoaderManager();

        //Initialize the loader. Pass in the int id constant defined above and pass in null
        //for the bundle. Pass in this activity for the Loadercallbacks parameter
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    }

    @Override

    //EXECUTION OF THREADS THROUGH LOADERS BY PHASES:
    //1- onCreateLoader
    //2- onLoadFinished
    //3- onLoaderReset

    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        // Create a new loader for the URL

        Uri baseUri = Uri.parse(THEGUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        loaderCounter = 1;

        return new NewsLoader(this, uriBuilder.toString());

    }

    // After the background task is done, take it to the main thread

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news_s) {

        /// Set empty state text to display "No news found."
        mEmptyStateTextView.setText(R.string.no_news_found);

        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news_s != null && !news_s.isEmpty()) {
            mAdapter.addAll(news_s);
        }
    }

    @Override

    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();

    }

    @Override
    public void onRefresh() {

        // we reset the current loader with the loadercounter value in order to refresh data
        getLoaderManager().restartLoader(loaderCounter, null, this);
    }
}
