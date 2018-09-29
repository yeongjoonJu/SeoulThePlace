package com.ensharp.seoul.seoultheplace;

import android.util.Log;
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
    private int distance;

    public PlaceVO(String code, String name, String imageURL,String coordinate_x,String coordinate_y){
        this.code = code;
        this.name = name;
        this.imageURL = new String[3];
        this.imageURL[0] = imageURL;
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
        this.distance = 0;
    }

    public PlaceVO(JSONObject jsonObject) {
        try {
            Log.i("search", jsonObject.toString());

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
            this.distance = 0;
        } catch (JSONException e) {
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

    public void setDistance(int distance){
        this.distance = distance;
    }

    public int getDistance(){ return distance;}

}
