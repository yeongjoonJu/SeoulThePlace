package com.ensharp.seoul.seoultheplace;

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

public class DAO {
    private JSONObject jsonObject;
    private HttpURLConnection conn = null;

    // 연결 주소
    final String BASE_URL = "http://ec2-52-78-245-211.ap-northeast-2.compute.amazonaws.com";

    public boolean insertMemberData(Bundle information) {
        String[] memberCategory = new String[]{"name", "email", "password", "sex", "age", "type"};
        jsonObject = new JSONObject();
        try {
            // 서버 연결
            if(!connectServer(BASE_URL + "/user/register"))
                return false;

            for (int i = 0; i < memberCategory.length; i++)
                jsonObject.accumulate(memberCategory[i], information.getString(memberCategory[i]));

            sendData(jsonObject);
        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if(conn != null)
                conn.disconnect();
        }
        return true;
    }

    // 태그 목록을 불러온다.
    public String[] getTagList() {
        String[] tags;
        // 서버 연결
        if(!connectServer(BASE_URL + "/tag"))
            return null;
        String result = getData().toString();
        return result.split(",");
    }

    // 플레이스 검색
    public ArrayList<PlaceVO> searchPlace(String keyword) {
        ArrayList<PlaceVO> results = new ArrayList<PlaceVO>();
        // 서버 연결
        if(!connectServer(BASE_URL + "/place"))
            return null;
        JSONArray jsonResult = getData();
        try {
            for (int i = 0; i < jsonResult.length(); i++) {
                PlaceVO place = new PlaceVO(jsonResult.getJSONObject(i));
                if(place.getName().contains(keyword) || place.getLocation().contains(keyword)
                        || place.getDetails().contains(keyword))
                    results.add(place);
            }
        }catch(JSONException e) {
            e.printStackTrace();
        }
        return results;
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
}