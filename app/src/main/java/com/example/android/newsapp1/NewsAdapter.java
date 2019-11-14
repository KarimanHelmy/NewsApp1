package com.example.android.newsapp1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsItem> {

    //Call the custom ArrayAdapter constructor
    public NewsAdapter(@NonNull Context context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    //Get the recycled view based on position
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        NewsItem currentNewsItem = getItem(position);
        assert currentNewsItem != null;
        String date = currentNewsItem.getDate(); //Store the date value to be checked and adjust layout accordingly
        String author = currentNewsItem.getAuthor();

        LinearLayout listItemLinearLayout = listItemView.findViewById(R.id.root_linearlayout);
        if (position % 2 == 0) {
            listItemLinearLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.evenItemColor));
        } else {
            listItemLinearLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.oddItemColor));
        }

        //Set the title TextView with the headline.
        TextView titleTextView =  listItemView.findViewById(R.id.title_textview);
        titleTextView.setText(currentNewsItem.getTitle());

        //Set the section in the Section TextView
        TextView sectionTextView =  listItemView.findViewById(R.id.section_textview);
        sectionTextView.setText(currentNewsItem.getSection());

        //Set the date TextView by checking its availability and adjust layout accordingly
        TextView dateTextView =  listItemView.findViewById(R.id.date_textview);
        processDateTextView(dateTextView, date);

        //Set the author TextView by checking its availability and adjust layout accordingly
        TextView authorTextView = listItemView.findViewById(R.id.author_textview);
        processAuthorTextView(authorTextView, author);

        //return the populated list_item to ListView
        return listItemView;
    }

    private void processAuthorTextView(TextView authorTextView, String author) {
        if (author == null) {
            authorTextView.setVisibility(View.GONE);
        } else {
            authorTextView.setVisibility(View.VISIBLE);
            authorTextView.setText(author);
        }
    }

    //This will check if the date is available and set it.
    //If it is not, then take out the whole date TextView from the list_item
    private void processDateTextView(TextView dateTextView, String date) {
        if (date == null) {
            dateTextView.setVisibility(View.GONE);
        } else {
            dateTextView.setVisibility(View.VISIBLE);

            //The following to set underline on the date text and set it to the date TextView
            SpannableString finalDate = new SpannableString(date);
            finalDate.setSpan(new UnderlineSpan(), 0, finalDate.length(), 0);
            dateTextView.setText(finalDate);
        }
    }
}
