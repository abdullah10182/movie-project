package com.example.popularmoviesapp.model;

public class Trailer {

    private String name;
    private String source;
    private String size;
    private String type;
    private String thumbnail;
    private String youtubeLink;

    public Trailer(String name, String source, String size, String type) {
        this.name = name;
        this.source = source;
        this.size = size;
        this.type = type;
        this.thumbnail = "https://img.youtube.com/vi/"+source+"/mqdefault.jpg";
        this.youtubeLink = "https://www.youtube.com/watch?v="+source;
    }

    public String getName() {
        return name;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }
}
