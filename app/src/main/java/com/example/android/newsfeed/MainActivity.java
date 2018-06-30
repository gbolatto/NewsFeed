package com.example.android.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsStory>> {

    // TODO: add a key for The Guardian API below since the "TEST" key has constantly exceeded limits
    private String API_KEY = "";
    private String API_URL = "https://content.guardianapis.com/search?show-tags=contributor&q=%22science%22&from-date=2018-06-01&api-key=" + API_KEY;

    private NewsStoryAdapter mAdapter;
    private TextView mEmptyView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            mProgressBar = findViewById(R.id.progress_bar);
            mEmptyView = findViewById(R.id.empty_view);
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setText(getString(R.string.no_internet));
        }

        ListView listView = findViewById(R.id.list_view);

        mAdapter = new NewsStoryAdapter(this, new ArrayList<NewsStory>());
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsStory currentNewsStory = mAdapter.getItem(i);
                Uri newsStoryUri = Uri.parse(currentNewsStory.getWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, newsStoryUri);
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public Loader<List<NewsStory>> onCreateLoader(int id, Bundle args) {
        return new NewsStoryLoader(this, API_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> newsStories) {
        mProgressBar = findViewById(R.id.progress_bar);
        mEmptyView = findViewById(R.id.empty_view);
        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setText(getString(R.string.no_news));
        mAdapter.clear();
        if (newsStories != null && !newsStories.isEmpty()) {
            mAdapter.addAll(newsStories);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsStory>> loader) {
        mAdapter.clear();
    }
}