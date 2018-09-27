package com.ensharp.seoul.seoultheplace;

import org.json.JSONException;
import org.json.JSONObject;

public class MemberVO {
    private String id;
    private String name;
    private String favoriteCourse;
    private String favoritePlace;
    private String edittedCourse;
    private String edittedPlace;

    public MemberVO(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("Id");
            name = jsonObject.getString("Name");
            favoriteCourse = jsonObject.getString("FavoriteCourse");
            favoritePlace = jsonObject.getString("FavoritePlace");
            edittedCourse = jsonObject.getString("EdittedCourse");
            edittedPlace = jsonObject.getString("EdittedPlace");
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFavoriteCourse() {
        return favoriteCourse;
    }

    public String getFavoritePlace() {
        return favoritePlace;
    }

    public String getEdittedCourse() {
        return edittedCourse;
    }

    public String getEdittedPlace() {
        return edittedPlace;
    }
}
