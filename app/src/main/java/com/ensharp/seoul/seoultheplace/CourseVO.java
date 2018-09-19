package com.ensharp.seoul.seoultheplace;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseVO {
    private String code;
    private String name;
    private String type;
    private int likes;
    private String details;
    private List<String> placeCode;

    private String image;
    private boolean liked;
    private String location;

    public CourseVO(String code, String name, String type, int likes, String details, String[] placeCode) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.likes = likes;
        this.details = details;
        this.placeCode = new ArrayList<>(Arrays.asList(placeCode));
    }

    public CourseVO(JSONObject jsonObject) {
        try {
            code = jsonObject.getString("Code");
            name = jsonObject.getString("Name");
            if(!jsonObject.isNull("Type"))
                type = jsonObject.getString("Type");
            likes = jsonObject.getInt("Likes");
            if(!jsonObject.isNull("Details"))
                details = jsonObject.getString("Details");
            placeCode = new ArrayList<>();
            for(int i = 1; i<=5; i++) {
                if(!jsonObject.isNull("PlaceCode" + i))
                placeCode.add(jsonObject.getString("PlaceCode" + i));
            }
            if(!jsonObject.isNull("Image"))
                image = jsonObject.getString("Image");
            if(!jsonObject.isNull("location"))
                location = jsonObject.getString("location");
            if(!jsonObject.isNull("User_Likes")) {
                if (jsonObject.getString("User_Likes").equals("true"))
                    liked = true;
                else
                    liked = false;
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

    public List<String> getPlaceCode() {
        return placeCode;
    }

    public String getPlaceCode(int index) {
        return placeCode.get(index);
    }

    public int getPlaceCount() {
        return placeCode.size();
    }

    public boolean isLiked() {
        return liked;
    }

    public String getLocation() {
        return location;
    }
}
