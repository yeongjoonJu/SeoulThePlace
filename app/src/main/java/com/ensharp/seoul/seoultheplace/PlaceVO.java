package com.ensharp.seoul.seoultheplace;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceVO {
    private String code;
    private String name;
    private String location;
    private String details;
    private String type;
    private int likes;
    private String phone;
    private String parking;
    private String[] imageURL;
    private String businessHours;
    private String parkFee;
    private String tip;
    private String coordinate_x;
    private String coordinate_y;


    public PlaceVO(String code, String name, String location, String[] imageURL, String phone,
                   String tip, String parking, String parkFee, int likes, String details,
                   String type, String businessHours) {

        this.code = code;
        this.name = name;
        this.location = location;
        this.imageURL = imageURL;
        this.phone = phone;
        this.parking = parking;
        this.parkFee = parkFee;
        this.likes = likes;
        this.details = details;
        this.type = type;
        this.businessHours = businessHours;
        this.tip = tip;
    }

    public PlaceVO(JSONObject jsonObject) {
        try {
            this.code = jsonObject.getString("Code");
            this.name = jsonObject.getString("Name");
            this.location = jsonObject.getString("location");
            this.imageURL = new String[3];
            for(int i=0; i<3; i++) {
                this.imageURL[i] = jsonObject.getString("Image" + (i + 1));
            }
            this.phone = jsonObject.getString("Phone");
            this.tip = jsonObject.getString("Tip");
            this.parking = jsonObject.getString("Parking");
            this.parkFee = jsonObject.getString("Fee");
            this.likes = jsonObject.getInt("Likes");
            this.details = jsonObject.getString("Details");
            this.type = jsonObject.getString("Type");
            this.businessHours = jsonObject.getString("BusinessHours");
            this.coordinate_x = jsonObject.getString("Coordinate_X");
            this.coordinate_y = jsonObject.getString("Coordinate_Y");
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCode() { return code; }

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

    public String getDetails() {
        return details;
    }

    public String getType() {
        return type;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public String getCoordinate_x() {
        return coordinate_x;
    }

    public String getCoordinate_y() {
        return coordinate_y;
    }

}
