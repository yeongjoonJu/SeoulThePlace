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
    JSONArray placeInformation;
    private List<String> placeImages = new ArrayList<String>();
    private List<String> placeNames = new ArrayList<String>();
    private List<String> placeLocations = new ArrayList<String>();
    private List<String> placeCoordinateX = new ArrayList<String>();
    private List<String> placeCoordinateY = new ArrayList<String>();

    public EdittedCourseVO(int code, String name, String description, List<String> placeCode, JSONArray placeInformation) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.placeCode = placeCode;
        this.placeInformation = placeInformation;
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
            placeInformation = jsonObject.getJSONArray("placeInfoArray");
            setPlaceDetails();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setPlaceDetails() {
        for (int i = 0; i <= placeInformation.length(); i++) {
            try {
                JSONObject jsonObject = placeInformation.getJSONObject(i);

                if (jsonObject.getString("Image") != null)
                    placeImages.add(jsonObject.getString("Image"));
                else
                    return;
                placeNames.add(jsonObject.getString("Name"));
                placeLocations.add(jsonObject.getString("Location"));
                placeCoordinateX.add(jsonObject.getString("Coordinate_X"));
                placeCoordinateY.add(jsonObject.getString("Coordinate_Y"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    public JSONArray getPlaceInformation() {
        return placeInformation;
    }

    public List<String> getPlaceImages() {
        return placeImages;
    }

    public List<String> getPlaceNames() {
        return placeNames;
    }

    public List<String> getPlaceLocations() {
        return placeLocations;
    }

    public List<String> getPlaceCoordinateX() {
        return placeCoordinateX;
    }

    public List<String> getPlaceCoordinateY() {
        return placeCoordinateY;
    }

    public String getPlaceImage(int index) {
        return placeImages.get(index);
    }

    public String getPlaceLocation(int index) {
        return placeLocations.get(index);
    }

}
