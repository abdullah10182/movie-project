package com.example.popularmoviesapp.model;

import java.io.Serializable;

public class MovieItem implements Serializable {
    private String title;
    private String poster;
    private String description;
    private String backdropImage;
    private String userRating;
    private String releaseDate;
    private int id;


    public MovieItem(String title, String poster, String description, String backdropImage, String userRating, String releaseDate, String id) {
        this.title = title;
        this.poster = poster;
        this.description = description;
        this.backdropImage = backdropImage;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.id = Integer.parseInt(id);
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getDescription() {
        return description;
    }

    public String getBackdropImage() {
        return backdropImage;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() { return id; }

}
