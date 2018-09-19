package com.ensharp.seoul.seoultheplace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DAO {
    private JSONArray resultData = null;

    // 연결 주소
    final String BASE_URL = "http://ec2-52-79-227-1.ap-northeast-2.compute.amazonaws.com:9000";

    // 중복되지 않으면 true, 중복되었으면 false
    public boolean checkIDduplicaion(String id) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("Id", id);
            jsonObject.accumulate("url", BASE_URL+"/user/register/id_duplicatecheck");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            // 결과 처리
            if(resultData == null)
                return false;

            jsonObject = resultData.getJSONObject(0);
            if(jsonObject.getString("success").equals("true"))
                return true;
        }catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    // SNS Login
    // key : email or name, value : 이메일 혹은 이름
    public MemberVO SNSloginCheck(String key, String value) {
        JSONObject jsonObject = new JSONObject();

        try {
            if(key.equals("email")) {
                jsonObject.accumulate("Id", value);
                jsonObject.accumulate("url", BASE_URL+"/login/bysns");
                // 네트워크 처리 비동기화
                resultData = new NetworkProcessor().execute(jsonObject).get();
            }
            else {
                jsonObject.accumulate("Name", value);
                jsonObject.accumulate("url", BASE_URL+"/login/byname");
                // 네트워크 처리 비동기화
                resultData = new NetworkProcessor().execute(jsonObject).get();
            }

            // 결과 처리
            if(resultData == null)
                return null;

            jsonObject = resultData.getJSONObject(0);
            if(jsonObject.getString("success").equals("true"))
                return new MemberVO(jsonObject);

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MemberVO loginCheck(String id, String password) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("Id", id);
            jsonObject.accumulate("Password", password);
            jsonObject.accumulate("url", BASE_URL+"/login");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            // 결과 처리
            if(resultData == null)
                return null;

            jsonObject = resultData.getJSONObject(0);
            if(jsonObject.getString("success").equals("true"))
                return new MemberVO(jsonObject);

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getPushedCourseData(String code) {
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("Code", code);
            jsonObject.accumulate("url", BASE_URL+"/main/course_pushed");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    public JSONArray getUserCourseData(String type, String id) {
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("Type", type);
            jsonObject.accumulate("Id", id);
            jsonObject.accumulate("url", BASE_URL+"/main/course_info");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            if(resultData == null)
                return null;

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return resultData;
    }

    public CourseVO getCourseData(String key) {
        CourseVO courseData = null;
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("Code", key);
            jsonObject.accumulate("url", BASE_URL+"/course/info");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            if(resultData == null)
                return null;
            courseData = new CourseVO(resultData.getJSONObject(0));

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return courseData;
    }

    public PlaceVO getPlaceData(String key) {
        PlaceVO placeData = null;
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("Code", key);
            jsonObject.accumulate("url", BASE_URL+"/place/info");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            if(resultData == null)
                return null;

            placeData = new PlaceVO(resultData.getJSONObject(0));

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return placeData;
    }

    public ArrayList<PlaceVO> searchPlace(String keyword) {
        ArrayList<PlaceVO> placeData = null;
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("keyword", keyword);
            jsonObject.accumulate("url", BASE_URL+"/search/place");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            if(resultData == null)
                return null;

            placeData = new ArrayList<>();
            for(int i=0; i<resultData.length(); i++) {
                placeData.add(new PlaceVO(resultData.getJSONObject(i)));
            }

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return placeData;
    }

    // 회원 정보 가져오기
    public MemberVO getMemberInformation(String id) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("Id", id);
            jsonObject.accumulate("url", BASE_URL+"/user/info");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            // 결과 처리
            if(resultData == null)
                return null;

            jsonObject = resultData.getJSONObject(0);
            if(jsonObject.getString("success").equals("true"))
                return new MemberVO(jsonObject);

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<CourseVO> searchCourse(String keyword) {
        ArrayList<CourseVO> courseData = null;
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("keyword", keyword);
            jsonObject.accumulate("url", BASE_URL+"/search/course");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            if(resultData == null)
                return null;

            courseData = new ArrayList<>();
            for(int i=0; i < resultData.length(); i++)
                courseData.add(new CourseVO(resultData.getJSONObject(i)));

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return courseData;
    }

    // 회원가입
    public String insertMemberData(String[] information) {
        // 처리 설정
        String[] memberCategory = new String[]{"Id", "Password", "Name"};
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("url", BASE_URL+"/user/register");
            for (int i = 0; i < memberCategory.length; i++)
                jsonObject.accumulate(memberCategory[i], information[i]);

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            // 결과 처리
            if(resultData == null)
                return "incomplete network";
            else {
                jsonObject = resultData.getJSONObject(0);
                if(jsonObject.getString("result").equals("false"))
                    return jsonObject.getString("msg");
            }
        }catch (JSONException e) {
            e.printStackTrace();
            return "json error";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "success";
    }

    // 플레이스 좋아요
    // Key : isPlaceLiked(String), Likes(int)
    public JSONObject likePlace(String code, String id) {
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("Code", code);
            jsonObject.accumulate("Id", id);
            jsonObject.accumulate("url", BASE_URL+"/place/like");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            if(resultData == null)
                return null;
            else
                return resultData.getJSONObject(0);

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 코스 좋아요
    // Key : isCourseLiked(String), Likes(Int)
    public JSONObject likeCourse(String code, String id) {
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("Code", code);
            jsonObject.accumulate("Id", id);
            jsonObject.accumulate("url", BASE_URL+"/course/like");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            if(resultData == null)
                return null;
            else
                return resultData.getJSONObject(0);

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<PlaceVO> searchPlaceByTag(String tag) {
        ArrayList<PlaceVO> placeData = null;
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("Tag", tag);
            jsonObject.accumulate("url", BASE_URL+"/place/tag");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            if(resultData == null)
                return null;

            for(int i=0; i<resultData.length(); i++)
                placeData.add(new PlaceVO(resultData.getJSONObject(i)));

        }catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return placeData;
    }

    // 태그 목록을 불러온다.
    // code "ALL" : 모든 태그 목록을 불러온다.
    // code "플레이스 or 코스 고유 코드"
    public ArrayList<String> getTagList(String code) {
        ArrayList<String> tags = new ArrayList<String>();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("code", code);
            jsonObject.accumulate("url", BASE_URL + "/tag");

            // 네트워크 처리 비동기화
            resultData = new NetworkProcessor().execute(jsonObject).get();

            // 결과 처리
            if(resultData == null)
                return null;
            else {
                for(int idx = 0; idx < resultData.length(); idx++)
                    tags.add(resultData.getJSONObject(idx).getString("tag"));
            }
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return tags;
    }
}