package com.ensharp.seoul.seoultheplace;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class  CourseVO {
    private String code;
    private String name;
    private String type;
    private int likes;
    private String details;
    private List<String> placeCode;
    private double x;
    private double y;
    private String image;
    private boolean liked;
    private String location;

    public CourseVO(String code, String name, String type, int likes, String details, String[] placeCode) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.likes = likes;
        this.details = details;
        this.x = x;
        this.y = y;
        this.placeCode = new ArrayList<>(Arrays.asList(placeCode));
    }

    public void setLikedState(boolean liked) {
        this.liked = liked;
    }

    public void changeLiked() {
        liked = !liked;
    }

    public CourseVO(JSONObject jsonObject) {
        try {
            code = jsonObject.getString("Code");
            name = jsonObject.getString("Name");
            if(!jsonObject.isNull("Type"))
                type = jsonObject.getString("Type");
            likes = jsonObject.getInt("Likes");
            placeCode = new ArrayList<String>();
            for(int i = 1; i<=5; i++) {
                if(jsonObject.isNull("PlaceCode"+i))
                    continue;
                if (!jsonObject.getString("PlaceCode" + i).equals("null")) {
                    placeCode.add(jsonObject.getString("PlaceCode" + i));
                }
            }
            if(!jsonObject.isNull("Details")) {
                details = jsonObject.getString("Details");
            }
            if(!jsonObject.isNull("Image")) {
                Log.i("loadImage", jsonObject.getString("image"));
                image = jsonObject.getString("Image");
            }
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

    public CourseVO(EdittedCourseVO edittedCourse, PlaceVO place) {
        name = edittedCourse.getName();
        type = "";
        likes = -1;
        details = edittedCourse.getDescription();
        placeCode = edittedCourse.getPlaceCode();
        x = Double.parseDouble(place.getCoordinate_x());
        y = Double.parseDouble(place.getCoordinate_y());
        image = place.getImageURL()[0];
        liked = false;
        location = place.getLocation();
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

    public List<String> getlatitudes() {
        List<String> latitude = new ArrayList<String>();

        DAO dao = new DAO();
        for (int i = 0; i < placeCode.size(); i++) {
            latitude.add(dao.getPlaceData(placeCode.get(i)).getCoordinate_y());
        }
        return latitude;
    }

    public boolean isLiked() {
        return liked;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }
}
