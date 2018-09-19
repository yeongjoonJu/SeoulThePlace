package com.ensharp.seoul.seoultheplace;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CourseVO {
    private String code;
    private String name;
    private String type;
    private int likes;
    private String details;
    private List<String> placeCode;
    private double x;
    private double y;

    public CourseVO(String code, String name, String type, int likes, String details, List<String> placeCode, double x, double y) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.likes = likes;
        this.details = details;
        this.placeCode = placeCode;
        this.x = x;
        this.y = y;
    }

    public CourseVO(JSONObject jsonObject) {
        try {

            code = jsonObject.getString("Code");
            name = jsonObject.getString("Name");
            type = jsonObject.getString("Type");
            likes = jsonObject.getInt("Likes");
            details = jsonObject.getString("Details");
            placeCode = new ArrayList<String>();
            for(int i = 1; i<=5; i++) {
                Log.d("ttttt","PlaceCode"+i+" : " + jsonObject.getString("PlaceCode"+i));
               if(!jsonObject.getString("PlaceCode"+i).equals("null")) {
                   placeCode.add(jsonObject.getString("PlaceCode" + i));
               }
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

    public List<PlaceVO> getPlaceVO(){
        DAO dao = new DAO();
        List<PlaceVO> places = new ArrayList<PlaceVO>();
        for(int i = 0 ; i < placeCode.size();i++) {
            places.add(dao.getPlaceData(placeCode.get(i)));
        }
        return places;
    }

    public List<String> getLongitudes(){
        List<String> longitudes = new ArrayList<String>();
        DAO dao = new DAO();
        for(int i = 0 ; i < placeCode.size();i++) {
            String str = dao.getPlaceData(placeCode.get(i)).getCoordinate_x();
            longitudes.add(str);
        }
        return longitudes;
    }

    public List<String> getlatitudes(){
        List<String> latitude= new ArrayList<String>();

        DAO dao = new DAO();
        for(int i = 0 ; i < placeCode.size();i++) {
            latitude.add(dao.getPlaceData(placeCode.get(i)).getCoordinate_y());
        }
        return latitude;
    }
}
