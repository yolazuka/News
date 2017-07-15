package com.example.usuario.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Usuario on 9/7/17.
 * <p>
 * <p>
 * /**
 * {@link NewsAdapter} is an {@link ArrayAdapter} that can provide the layout for each list item
 * based on a data source, which is a list of {@link News} objects.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Create a new {@link NewsAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param news_s  is the list of earthquakes to be displayed
     */
    public NewsAdapter(Context context, ArrayList<News> news_s) {
        super(context, 0, news_s);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        View newsListView = convertView;
        if (newsListView == null) {
            newsListView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items, parent, false);
        }

        // Get the current News object located at this position in the list
        News currentNews = getItem(position);

        //THIS BLOCK OF CODE IS TO LINK AND SET THE XML VIEWS WITH THE INFLATED newsListView INFO

        //Get the TextView with the News title_view ID and set the title of the current news as text
        TextView titleTextView = (TextView) newsListView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentNews.getNewsTitle());

        //Get the TextView with the section_view ID and set the section of the news as text
        TextView sectionTextView = (TextView) newsListView.findViewById(R.id.section_text_view);
        sectionTextView.setText(currentNews.getSectionName());

        return newsListView;

    }

}
