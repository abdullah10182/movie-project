package com.example.popularmoviesapp.model;

public class Trailer {

    private String movieTitle;
    private String source;
    private String size;
    private String type;

    public Trailer(String movieTitle, String source, String size, String type) {
        this.movieTitle = movieTitle;
        this.source = source;
        this.size = size;
        this.type = type;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getSource() {
        return source;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
