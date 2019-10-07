package com.example.popularmoviesapp.model;

public class Review {
    private String author;
    private String content;
    private String url;
    private String id;
    private String movieTitle;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public Review(String author, String content, String url, String id) {
        this.author = author;
        this.content = content;
        this.url = url;
        this.id = id;
    }
}
