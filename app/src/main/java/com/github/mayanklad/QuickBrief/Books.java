package com.github.mayanklad.QuickBrief;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Books {
    private String book_id;
    private String title;
    private String isbn;
    private String isbn13;
    private String image_url;
    private String publication_year;
    private String publication_month;
    private String publication_day;
    private String publisher;
    private String description;
    private String text_reviews_count;
    private String rating_dist;
    private Bundle ratings;
    private String average_rating;
    private String num_pages;
    private String format;
    private String comments_url = "";
    private ArrayList<String> authors;
    private String[][] similarBooks;

    private static final String TAG = "Books";

    public Books() {
        this.authors = new ArrayList<String>();
        this.similarBooks = new String[4][2];
    }

    public String[][] getSimilarBooks() {
        return similarBooks;
    }

    public void setSimilarBooks(String[][] similarBooks) {
        this.similarBooks = similarBooks;
    }

    public String getComments_url() {
        return comments_url;
    }

    public void setComments_url(String comments_url) {
        Log.i(TAG, "Comments widget:"+comments_url);

        for (int i=comments_url.indexOf("<iframe id=")+29; true; i++) {
            if (comments_url.charAt(i) == '\"') {
                break;
            } else {
                this.comments_url += comments_url.charAt(i);
            }
        }
        Log.i(TAG, "Comments url:"+this.comments_url);
        //this.comments_url = comments_url;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPublication_year() {
        return publication_year;
    }

    public void setPublication_year(String publication_year) {
        this.publication_year = publication_year;
    }

    public String getPublication_month() {
        return publication_month;
    }

    public void setPublication_month(String publication_month) {
        this.publication_month = publication_month;
    }

    public String getPublication_day() {
        return publication_day;
    }

    public void setPublication_day(String publication_day) {
        this.publication_day = publication_day;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getText_reviews_count() {
        return text_reviews_count;
    }

    public void setText_reviews_count(String text_reviews_count) {
        this.text_reviews_count = text_reviews_count;
    }

    public String getRating_dist() {
        return rating_dist;
    }

    public void setRating_dist(String rating_dist) {

        this.rating_dist = rating_dist;
        this.setRatings();
    }

    public Bundle getRatings() {
        return ratings;
    }

    private void setRatings() {

        this.ratings = new Bundle();
        for (String ratings: this.rating_dist.split(Pattern.quote("|"))) {
            String[] rating = ratings.split(Pattern.quote(":"));
            this.ratings.putString(rating[0], rating[1]);
        }
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getNum_pages() {
        return num_pages;
    }

    public void setNum_pages(String num_pages) {
        this.num_pages = num_pages;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public void addAuthor(String author) { this.authors.add(author); }

}
