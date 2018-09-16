package com.ensharp.seoul.seoultheplace;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class CourseVO {
    private String code;
    private String name;
    private String type;
    private int likes;
    private String details;
    private String[] placeCode;

    public CourseVO(JSONObject jsonObject) {
        try {
            code = jsonObject.getString("Code");
            name = jsonObject.getString("Name");
            type = jsonObject.getString("Type");
            likes = jsonObject.getInt("Likes");
            details = jsonObject.getString("Details");
            for(int i = 1; i<=5; i++) {
                placeCode[i-1] = jsonObject.getString("PlaceCode" + i);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public CourseVO() {

    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getLikes() {
        return likes;
    }

    public String getDetails() {
        return details;
    }

    public String[] getPlaceCode() {
        return placeCode;
    }
}
