package com.ensharp.seoul.seoultheplace;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceDistanceVO {
    private String code;
    private String name;
    private String imageURL;
    private String coordinate_x;
    private String coordinate_y;
    private String distance;

    public PlaceDistanceVO(JSONObject jsonObject) {
        try {
            this.code = jsonObject.getString("Code");
            this.name = jsonObject.getString("Name");
            this.imageURL = jsonObject.getString("Image");
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

    public String getCoordinate_x() {
        return coordinate_x;
    }

    public String getCoordinate_y() {
        return coordinate_y;
    }

}
