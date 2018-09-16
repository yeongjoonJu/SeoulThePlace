package com.ensharp.seoul.seoultheplace;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DAO extends AsyncTask<Void, Void, Void> {
    private HttpURLConnection conn = null;
    private String url;

    // 상태 상수
    private final int WAIT = 0;
    private final int PROCESSING = 1;
    private final int EXIT = 3;

    // 비동기를 위한 변수
    private int status = WAIT;
    private JSONObject sendingData = null;
    private JSONArray resultData = null;

    // 연결 주소
    final String BASE_URL = "http://ec2-52-78-245-211.ap-northeast-2.compute.amazonaws.com";

    protected void processNetwork(String url, JSONObject sending) {
        this.url = url;
        sendingData = sending;
        status = PROCESSING;

        // 네트워크 처리가 완료될 때까지 기다림
        while(status != WAIT);
    }

    // 중복되지 않으면 true, 중복되었으면 false
    public boolean checkIDduplicaion(String id) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("Id", id);

            // 네트워크 처리 동기화
            processNetwork(BASE_URL+"/user/register/id_duplicatecheck", jsonObject);

            // 결과 처리
            if(resultData == null)
                return false;

            jsonObject = resultData.getJSONObject(0);
            if(jsonObject.getString("success").equals("true"))
                return true;
        }catch (JSONException e) {
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
                // 네트워크 처리 동기화
                processNetwork(BASE_URL+"/login/bysns", jsonObject);
            }
            else {
                jsonObject.accumulate("Name", value);
                // 네트워크 처리 동기화
                processNetwork(BASE_URL+"/login/byname", jsonObject);
            }

            // 결과 처리
            if(resultData == null)
                return null;

            jsonObject = resultData.getJSONObject(0);
            if(jsonObject.getString("success").equals("true"))
                return new MemberVO(jsonObject);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MemberVO loginCheck(String id, String password) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("Id", id);
            jsonObject.accumulate("Password", password);

            // 네트워크 처리 동기화
            processNetwork(BASE_URL+"/login", jsonObject);

            // 결과 처리
            if(resultData == null)
                return null;

            jsonObject = resultData.getJSONObject(0);
            if(jsonObject.getString("success").equals("true"))
                return new MemberVO(jsonObject);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getUserCourseData(String type, String id) {
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("Type", type);
            jsonObject.accumulate("Id", id);

            // 네트워크 처리 동기화
            processNetwork(BASE_URL+"/main/course_info", jsonObject);

            if(resultData.getJSONObject(0).getString("success").equals("false"))
                return null;

        }catch (JSONException e) {
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

            // 네트워크 처리 동기화
            processNetwork(BASE_URL+"/course/info", jsonObject);

            if(resultData == null)
                return null;
            courseData = new CourseVO(resultData.getJSONObject(0));

        }catch (JSONException e) {
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

            // 네트워크 처리 동기화
            processNetwork(BASE_URL+"/place/info", jsonObject);

            if(resultData == null)
                return null;

            placeData = new PlaceVO(resultData.getJSONObject(0));

        }catch (JSONException e) {
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

            // 네트워크 처리 동기화
            processNetwork(BASE_URL+"/search/place", jsonObject);

            if(resultData == null)
                return null;

            placeData = new ArrayList<>();
            for(int i=0; i<resultData.length(); i++) {
                placeData.add(new PlaceVO(resultData.getJSONObject(i)));
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return placeData;
    }

    public ArrayList<CourseVO> searchCourse(String keyword) {
        ArrayList<CourseVO> courseData = null;
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("keyword", keyword);

            // 네트워크 처리 동기화
            processNetwork(BASE_URL+"/search/course", jsonObject);

            if(resultData == null)
                return null;

            courseData = new ArrayList<>();
            for(int i=0; i<resultData.length(); i++)
                courseData.add(new CourseVO(resultData.getJSONObject(i)));

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return courseData;
    }

    // 회원가입
    public String insertMemberData(String[] information) {
        // 처리 설정
        String[] memberCategory = new String[]{"Id", "Password", "Name", "Age", "Gender", "Type"};
        JSONObject jsonObject = new JSONObject();

        try {
            for (int i = 0; i < memberCategory.length; i++)
                jsonObject.accumulate(memberCategory[i], information[i]);

            // 네트워크 처리 동기화
            processNetwork(BASE_URL+"/user/register", jsonObject);

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
        }
        return "success";
    }

    public ArrayList<PlaceVO> searchPlaceByTag(String tag) {
        ArrayList<PlaceVO> placeData = null;
        // 처리 설정
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("Tag", tag);

            // 네트워크 처리 동기화
            processNetwork(BASE_URL+"/place/tag", jsonObject);

            if(resultData == null)
                return null;

            for(int i=0; i<resultData.length(); i++)
                placeData.add(new PlaceVO(resultData.getJSONObject(i)));

        }catch (JSONException e) {
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

            // 네트워크 처리 동기화
            processNetwork(BASE_URL + "/tag", jsonObject);

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
        }
        return tags;
    }

    // 서버 연결 시도
    protected boolean connectServer(String url) {
        try {
            URL urlObject = new URL(url);
            conn = (HttpURLConnection) urlObject.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "text/html");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            return true;
        }catch (Exception e) {
            Log.e("connect", "fail");
            e.printStackTrace();
            return false;
        }
    }

    // 데이터 송신
    protected void sendData(JSONObject jsonObject) {
        try {
            OutputStream outputStream = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
        }catch (IOException e ) {
            e.printStackTrace();
        }
    }

    // 데이터 수신
    protected JSONArray getData() {
        JSONArray result = null;
        BufferedReader reader = null;
        try {
            InputStream stream = conn.getInputStream();

            //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();

            String line = "";

            // 아래 라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
            while ((line = reader.readLine()) != null)
                buffer.append(line);

            result = new JSONArray(buffer.toString());

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader != null) {
                    reader.close(); //버퍼를 닫아줌
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void destory() {
        status = EXIT;
        try {
            if(getStatus() == AsyncTask.Status.RUNNING)
                cancel(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while(true) {
            while(status == WAIT);

            if(status == EXIT || isCancelled())
                return null;

            // 서버 연결
            if(url == null || !connectServer(url)) {
                resultData = null;
                status = WAIT;
                continue;
            }

            // 데이터 송수신
            sendData(sendingData);
            resultData = getData();
            status = WAIT;
        }
    }
}