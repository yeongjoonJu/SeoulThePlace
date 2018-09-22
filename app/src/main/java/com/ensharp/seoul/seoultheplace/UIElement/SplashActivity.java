package com.ensharp.seoul.seoultheplace.UIElement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ensharp.seoul.seoultheplace.Login.LoginBackgroundActivity;

public class SplashActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // MainActivity.class 자리에 다음에 넘어갈 액티비티를 넣어주기
            Intent intent = new Intent(this, LoginBackgroundActivity.class);
            startActivity(intent);
            finish();
    }
}
