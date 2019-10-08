package com.example.popularmoviesapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie {

    @PrimaryKey
    private int id;
    private String title;
    private String poster;
    private String description;
    private String backdropImage;
    private String userRating;
    private String releaseDate;

    public FavouriteMovie(int id, String title, String poster, String description, String backdropImage, String userRating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.description = description;
        this.backdropImage = backdropImage;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
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
}
