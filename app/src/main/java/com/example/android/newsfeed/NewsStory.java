package com.example.android.newsfeed;

/**
 * Created by gbolatto on 6/25/2018.
 */
public class NewsStory {

    private String mTitle;
    private String mSection;
    private String mWebUrl;
    private String mPublicationDate;
    private String mAuthor;

    public NewsStory(String title, String section, String webUrl, String publicationDate, String author) {
        mTitle = title;
        mSection = section;
        mWebUrl = webUrl;
        mPublicationDate = publicationDate;
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getAuthor() {
        return mAuthor;
    }
}