package com.ensharp.seoul.seoultheplace;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

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
import java.net.MalformedURLException;
import java.net.URL;

public class DAO extends AsyncTask<Void, Void, Void> {
    // 상태 변수
    private final int PROCESSING = 1;
    private final int WAITING = 2;

    // 처리할 내용
    private final int INSERT = 3;
    private final int MAIN_LIST_UPDATE = 4;
    private final int TAG_LIST_UPDATE = 5;

    private int status = WAITING;
    private int processContent = 0;

    private JSONObject jsonObject;
    private URL url;
    private HttpURLConnection conn = null;

    private ListView mListView;

    public void insertMemberData(String[] information) {
        String[] memberCategory = new String[]{"Id", "Password", "Name", "Age", "Gender", "Type"};
        jsonObject = new JSONObject();
        try {
            for (int i = 0; i < memberCategory.length; i++)
                jsonObject.accumulate(memberCategory[i], information[i]);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        processContent = INSERT;
        status = PROCESSING;
    }

    public void setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // UI 처리 예시 함수, 호출용
    public void mainUpdate(ListView listView) {
        mListView = listView;
        processContent = MAIN_LIST_UPDATE;
        status = PROCESSING;
    }

    // 실제 처리 함수
    protected void mainListViewUpdate(String data) {

    }

    // 서버 연결 시도
    protected boolean connectServer() {
        try {
            conn = (HttpURLConnection) this.url.openConnection();
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

    protected String getData() {
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

            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
    protected Void doInBackground(Void... urls) {
        while(true) {
            // 요구 대기 중
            while(status == WAITING);

            // 서버와 연결 실패 시
            if (!connectServer()) {
                status = WAITING;
                processContent = 0;
                continue;
            }

            switch (processContent) {
                case INSERT:
                    sendData(jsonObject);
                case MAIN_LIST_UPDATE:
                    mainListViewUpdate(getData());
                case TAG_LIST_UPDATE:
                    break;
                default:
                    break;
            }

            if (conn != null)
                conn.disconnect();

            status = WAITING;
            processContent = 0;
        }
    }
}