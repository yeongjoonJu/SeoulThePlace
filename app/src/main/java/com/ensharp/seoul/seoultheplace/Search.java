package com.ensharp.seoul.seoultheplace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Search {
    private DAO dao;

    public Search(DAO dao) {
        this.dao = dao;
    }

    public PlaceVO getPlaceData(String code) {
        JSONArray jsonData = dao.getPlaceAllData();
        try {
            for(int i=0; i<jsonData.length(); i++) {
                JSONObject jsonObject = jsonData.getJSONObject(i);
                if(jsonObject.getString("code").equals(code))
                    return new PlaceVO(jsonObject);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<PlaceVO> getPlaceAllData() {
        JSONArray jsonData = dao.getPlaceAllData();
        ArrayList<PlaceVO> placeData = new ArrayList<PlaceVO>();
        try {
            for (int i = 0; i < jsonData.length(); i++)
                placeData.add(new PlaceVO(jsonData.getJSONObject(i)));
        }catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
        return placeData;
    }

    public CourseVO getCourseData(String code) {
        return null;
    }

    public ArrayList<CourseVO> getCourseAllData(String code) {
        return null;
    }

}
