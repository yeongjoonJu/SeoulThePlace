package com.ensharp.seoul.seoultheplace;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CourseVO {
    private String code;
    private String name;
    private String type;
    private int likes;
    private String details;
    private List<String> placeCode;

    public CourseVO(String code, String name, String type, int likes, String details, List<String> placeCode) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.likes = likes;
        this.details = details;
        this.placeCode = placeCode;
    }

    public CourseVO(JSONObject jsonObject) {
        try {
            code = jsonObject.getString("Code");
            name = jsonObject.getString("Name");
            type = jsonObject.getString("Type");
            likes = jsonObject.getInt("Likes");
            details = jsonObject.getString("Details");

            for(int i = 1; i<=5; i++) {
                placeCode.add(jsonObject.getString("PlaceCode" + i));
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
}
