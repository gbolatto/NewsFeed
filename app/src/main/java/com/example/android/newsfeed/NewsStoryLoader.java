package com.example.android.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

/**
 * Created by gbolatto on 6/28/2018.
 */
public class NewsStoryLoader extends AsyncTaskLoader<List<NewsStory>> {

    private String mUrl;

    public NewsStoryLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsStory> loadInBackground() {
        return QueryUtils.getNewsStoryData(mUrl);
    }
}