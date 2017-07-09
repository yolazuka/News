package com.example.usuario.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Usuario on 9/7/17.
 */


public class NewsLoader extends AsyncTaskLoader<List<News>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /**
     * declares the private string for Query URL
     */
    private String mUrl;

    /**
     * Constructs a new Loader
     *
     * @param context of the activity
     * @param url     to load data from
     * Through the following steps:
     * 1- OnStartLoading
     * 2- loadinBackground
     */

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news
        List<News> news_s = QueryUtils.fetchNewsData(mUrl);
        return news_s;
    }
}
