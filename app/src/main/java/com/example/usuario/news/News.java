package com.example.usuario.news;

/**
 * Created by Usuario on 9/7/17.
 */

public class News {

    //THROUGH OUR CUSTOM CLASS, HERE WE INDICATE THE BASE ATTRIBUTES TO WORK WITH

    //Declare the private Strings

    private String mSectionName;

    private String mNewsTitle;

    private String mWebsite;

    private String mTypeOfNews;

    // Assign the strings to the class

    public News(String newsTitle, String sectionName, String website, String typeOfNews) {
        mSectionName = sectionName;
        mNewsTitle = newsTitle;
        mWebsite = website;
        mTypeOfNews = typeOfNews;

    }

    //Through the method getter we will obtein the values of the String

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public String getTypeOfNews() {
        return mTypeOfNews;
    }



}
