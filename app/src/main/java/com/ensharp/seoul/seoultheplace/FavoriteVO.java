package com.ensharp.seoul.seoultheplace;

public class FavoriteVO {
    private String imageURL;
    private String name;
    private String location;

    public FavoriteVO(String imageURL, String name, String location) {
        this.imageURL = imageURL;
        this.name = name;
        this.location = location;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
