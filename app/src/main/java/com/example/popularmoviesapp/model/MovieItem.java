package com.example.popularmoviesapp.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "movie_item_table")
public class MovieItem implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String poster;
    private String description;
    private String backdropImage;
    private String userRating;
    private String releaseDate;


    public MovieItem(String title, String poster, String description, String backdropImage, String userRating, String releaseDate, String id) {
        this.title = title;
        this.poster = poster;
        this.description = description;
        this.backdropImage = backdropImage;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.id = id;
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

    public String getId() { return id; }

}
