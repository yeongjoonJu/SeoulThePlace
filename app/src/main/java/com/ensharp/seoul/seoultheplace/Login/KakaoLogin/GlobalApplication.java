package com.ensharp.seoul.seoultheplace.Login.KakaoLogin;

import android.app.Application;
import android.util.Log;

import com.kakao.auth.KakaoSDK;


public  class GlobalApplication extends Application {

    private static GlobalApplication instance;
    private static String TAG = "KAKAOTALK GlobalApplication";

    public static GlobalApplication getGlobalApplicationContext() {
        Log.d(TAG,"GlobalApplication on");
        if (instance == null) {
            Log.d(TAG,"Throw new IIIegalStateException");
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }
        Log.d(TAG,"return instance");
        return instance;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // Kakao Sdk 초기화
        Log.d(TAG,"init KAKAOSDK");
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

}
