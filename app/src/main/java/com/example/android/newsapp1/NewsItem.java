package com.example.android.newsapp1;

public class NewsItem {
    String title; //webTitle
    String section; //sectionName
    String author; //firstName + lastName
    String date; //webPublicationDate
    String url; //webUrl

    public NewsItem(String title, String section, String author, String date, String url) {
        this.title = title;
        this.section = section;
        this.author = author;
        this.date = date;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

}
