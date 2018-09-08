package com.ensharp.seoul.seoultheplace;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceVO {
    private String name;
    private String location;
    private String[] imageURL;
    private String phone;
    private String tip;
    private String parking;
    private String parkFee;
    private int likes;
    private String[] tags;
    private String description;
    private String details;
    private String type;
    private String businessHours;

    public PlaceVO(String name, String location, String[] imageURL, String phone,
                   String tip, String parking, String parkFee, int likes, String[] tags,
                   String description, String details, String type, String businessHours) {
        this.name = name;
        this.location = location;
        this.imageURL = imageURL;
        this.phone = phone;
        this.tip = tip;
        this.parking = parking;
        this.parkFee = parkFee;
        this.likes = likes;
        this.tags = tags;
        this.description = description;
        this.details = details;
        this.type = type;
        this.businessHours = businessHours;
    }

    public PlaceVO(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("name");
            this.location = jsonObject.getString("location");
            for(int i=0; i<3; i++)
                this.imageURL[i] = jsonObject.getString("image"+ (i+1));
            this.phone = jsonObject.getString("phone");
            this.tip = jsonObject.getString("tip");
            this.parking = jsonObject.getString("parking");
            this.parkFee = jsonObject.getString("fee");
            this.likes = jsonObject.getInt("likes");
            this.tags = jsonObject.getString("tag").split(",");
            this.description = jsonObject.getString("description");
            this.details = jsonObject.getString("details");
            this.type = jsonObject.getString("type");
            this.businessHours = jsonObject.getString("businesshours");
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String[] getImageURL() {
        return imageURL;
    }

    public String getPhone() {
        return phone;
    }

    public String getTip() {
        return tip;
    }

    public String getParking() {
        return parking;
    }

    public String getParkFee() {
        return parkFee;
    }

    public int getLikes() {
        return likes;
    }

    public String[] getTags() {
        return tags;
    }

    public String getDescription() {
        return description;
    }

    public String getDetails() {
        return details;
    }

    public String getType() {
        return type;
    }

    public String getBusinessHours() {
        return businessHours;
    }
}
