package com.ensharp.seoul.seoultheplace;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EdittedCourseVO {
    private int code;
    private String name;
    private String description;
    private List<String> placeCode;

    public EdittedCourseVO(int code, String name, String description, List<String> placeCode) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.placeCode = placeCode;
    }

    public EdittedCourseVO(JSONObject jsonObject) {

        Log.e("editted_course/EdittedCourseVO", "look:" + jsonObject.toString());

        try {
            code = jsonObject.getInt("edittedCourse_Code");
            name = jsonObject.getString("edittedCourse_Name");
            description = jsonObject.getString("edittedCourse_Description");
            placeCode = new ArrayList<String>();
            for (int i = 1; i <= 5; i++) {
                if (!jsonObject.getString("edittedCourse_PlaceCode" + i).equals("null")) {
                    placeCode.add(jsonObject.getString("edittedCourse_PlaceCode" + i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getPlaceCode() {
        return placeCode;
    }
}
