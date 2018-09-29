package com.ensharp.seoul.seoultheplace;

import android.os.AsyncTask;
import android.util.Log;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.CourseFragmentPagerAdapter;
import com.ensharp.seoul.seoultheplace.Course.PlaceView.PlaceFragmentPagerAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkProcessor extends AsyncTask<JSONObject, Void, JSONArray> {
    private HttpURLConnection conn = null;
    CourseFragmentPagerAdapter courseAdapter;
    PlaceFragmentPagerAdapter placeAdapter;

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

            if(reader != null) {
                reader.close(); //버퍼를 닫아줌
            }

            if(!buffer.toString().equals("null"))
                result = new JSONArray(buffer.toString());
            else
                return null;

            return result;
        } catch (IOException | JSONException e) {
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

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        Log.i("yeongjoon", "onPostExecute");
        if(placeAdapter != null)
            placeAdapter.notifyDataSetChanged();
        if(courseAdapter != null)
            courseAdapter.notifyDataSetChanged();
        placeAdapter = null;
        courseAdapter = null;
    }

    @Override
    protected JSONArray doInBackground(JSONObject... jsonObjects) {
        try {
            JSONArray result;

            // 서버 연결
            if (!connectServer(jsonObjects[0].getString("url"))){
                return null;
            }

            // 데이터 송수신
            sendData(jsonObjects[0]);
            result = getData();

            if(conn != null)
                conn.disconnect();

            return result;

        }catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if(conn != null)
                conn.disconnect();
        }
        return null;
    }
}
