package com.ensharp.seoul.seoultheplace;

import android.os.AsyncTask;

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

public class DAO extends AsyncTask<String, String, String> {

    private JSONObject jsonObject;
    private URL url;
    private HttpURLConnection conn = null;
    private String currentQuery;

    public final String[] COURSE_TABLE_COLUMN = new String[] {
            "code", "name", "simple_script", "detail_script", "place_order",
            "address", "type", "tag", "area", "term", "good"
    };
    public final String[] PLACE_TABLE_COLUMN = new String[] {
            "code", "name", "address", "simple_script", "picture1",
            "picture2", "picture3", "tel", "detail_script", "tip",
            "recommand", "tag", "good"
    };

    public DAO() {
        this("SeoulThePlace", "Furge");
    }

    public DAO(String userID, String name) {
        try {
            //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
            jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", userID);
            jsonObject.accumulate("name", name);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void insertINTO(String table, String[] columns, String[] data) {
        currentQuery = "INSERT INTO " + table + "(";
        String columnName = "";
        String dataStr = "";
        for(int column = 0; column < columns.length; column++) {
            columnName += columns[column];
            dataStr += "\'" + data[column] + "\'";

            if(column != columns.length - 1) {
                columnName += ",";
                dataStr += ",";
            }
            currentQuery += columnName;
        }
        currentQuery += ( columnName + ") VALUES(" + dataStr + ");" );
    }

    public void selectFrom(String table, String where) {
        currentQuery = "SELECT * From " + table + " " + where + ";";
    }

    public void selectFrom(String table, String[] columns, String where) {
        currentQuery = "SELECT ";
        String columnName = "";
        for(int column = 0; column < columns.length; column++) {
            columnName += columns[column];
            if(column != columns.length - 1) {
                columnName += ",";
            }
        }
        currentQuery += columnName + " From " + table + " " + where + ";";
    }

    // 서버 연결 시도
    protected void connectServer(String url) {
        try {
            this.url = new URL(url);
            conn = (HttpURLConnection) this.url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "text/html");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(String... urls) {

        connectServer(urls[0]);
        BufferedReader reader = null;

        try{
            // 서버로 보내기 위해서 스트림 만듬
            OutputStream outStream = conn.getOutputStream();

            // 버퍼를 생성하고 쿼리를 넣음
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
            writer.write(currentQuery);
            writer.flush();
            writer.close();

            // 질의문이 SELECT문인 경우만 결과를 반환한다
            if(currentQuery.charAt(0) == 'S') {
                // 서버로부터 데이터를 받음
                InputStream stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString(); // 서버로부터 받은 값을 리턴해준다.
            }

        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.disconnect();
            }
            try {
                if(reader != null){
                    reader.close(); //버퍼를 닫아줌
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
