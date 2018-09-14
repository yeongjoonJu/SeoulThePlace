package com.ensharp.seoul.seoultheplace;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class CourseVO implements Parcelable {
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

    public CourseVO getCourseVO(CourseVO course) {
        course.code = "j111";
        course.name = "서울시내 대학 경유 273번 버스로 떠나는 청춘과 낭만의 대학 투어";
        course.type = "친구끼리, 연인끼리";
        course.likes = 5;
        course.details = "간선버스 273번은 대학로와 신촌을 가로지르는 노선으로 이 버스를 타면 서울 시내 대학교 투어를 할 수 있다. 고려대부터 성균관대, 이화여대, 연세대, 홍익대 등 9개 이상의 대학교를 거친다. 그만큼 젊은 층의 수요도 높은 버스로 대학마크가 박힌 점퍼를 입은 학생들도 쉽게 마주칠 수 있다. 273버스 노선 중 대표적인 학교 3곳을 소개한다.";
        course.placeCode[0] = "a111";
        course.placeCode[1] = "a222";
        course.placeCode[2] = "a333";
        return course;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
