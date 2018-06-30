package com.example.android.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gbolatto on 6/25/2018.
 */
public class NewsStoryAdapter extends ArrayAdapter<NewsStory> {

    private Context mContext;
    private List<NewsStory> mNewsStories;

    public NewsStoryAdapter(Activity context, ArrayList<NewsStory> newsStories) {
        super(context, 0, newsStories);
        mContext = context;
        mNewsStories = newsStories;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder vHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            vHolder = new ViewHolder();
            vHolder.titleTextView = convertView.findViewById(R.id.title);
            vHolder.sectionTextView = convertView.findViewById(R.id.section);
            vHolder.publicationDateTextView = convertView.findViewById(R.id.publication_date);
            vHolder.authorTextView = convertView.findViewById(R.id.author);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }

        NewsStory currentNewsStory = getItem(position);

        vHolder.titleTextView.setText(currentNewsStory.getTitle());
        vHolder.sectionTextView.setText(currentNewsStory.getSection());
        vHolder.publicationDateTextView.setText(currentNewsStory.getPublicationDate());
        vHolder.authorTextView.setText(currentNewsStory.getAuthor());

        return convertView;
    }

    private static class ViewHolder {
        private TextView titleTextView;
        private TextView sectionTextView;
        private TextView publicationDateTextView;
        private TextView authorTextView;
    }
}