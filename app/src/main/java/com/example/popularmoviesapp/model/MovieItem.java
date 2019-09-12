package com.example.popularmoviesapp.model;

public class MovieItem {
    private String title;
    private String poster;
    private String description;
    private String backdropImage;
    private String userRating;

    public MovieItem(String title, String poster, String description, String backdropImage, String userRating) {
        this.title = title;
        this.poster = poster;
        this.description = description;
        this.backdropImage = backdropImage;
        this.userRating = userRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackdropImage() {
        return backdropImage;
    }

    public void setBackdropImage(String backdropImage) {
        this.backdropImage = backdropImage;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }
}
