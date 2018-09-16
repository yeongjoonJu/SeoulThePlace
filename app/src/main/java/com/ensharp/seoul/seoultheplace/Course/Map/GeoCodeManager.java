package com.ensharp.seoul.seoultheplace.Course.Map;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class GeoCodeManager extends AsyncTask<String, Void, String>{

    private String clientId = "pGzToR98qOhD0UYuOZZx";
    private String clientSecret = "E3W6V6G3Ve";

    @Override
    protected String doInBackground(String... urls) {
        try {
            String addr = URLEncoder.encode(urls[0], "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr;
            URL url = new URL(apiURL);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader bufferedReader;

            if (responseCode == 200)
                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            else
                bufferedReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null)
                response.append(inputLine);

            bufferedReader.close();

            return response.toString();
        } catch (Exception e) {
            return "";
        }
    }
}