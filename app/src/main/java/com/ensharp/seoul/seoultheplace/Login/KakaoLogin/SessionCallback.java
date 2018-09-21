package com.ensharp.seoul.seoultheplace.Login.KakaoLogin;
import android.util.Log;


import com.ensharp.seoul.seoultheplace.Login.LoginFragment;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

public class SessionCallback implements ISessionCallback {

    public static boolean alreadyCheck = false;
    public String TAG = "KAKAOTALK SessionCallBack";
    // 로그인에 성공한 상태
    @Override
    public void onSessionOpened() {
        Log.e(TAG, "startRequest" );
        requestMe();
    }

    // 로그인에 실패한 상태
    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        Log.e(TAG, "onSessionOpenFailed : " + exception.getMessage());
    }

    // 사용자 정보 요청
    public void requestMe() {
        // 사용자정보 요청 결과에 대한 Callback
        UserManagement.requestMe(new MeResponseCallback() {
            // 세션 오픈 실패. 세션이 삭제된 경우,
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG, "onSessionClosed : " + errorResult.getErrorMessage());
            }

            // 회원이 아닌 경우,
            @Override
            public void onNotSignedUp() {
                Log.e(TAG, "onNotSignedUp");
            }

            // 사용자정보 요청에 성공한 경우,
            @Override
            public void onSuccess(UserProfile userProfile) {
                if(!alreadyCheck) {
                    Log.e(TAG, "get email,name");
                    LoginFragment.editor.putString("email", String.valueOf(userProfile.getId()));
                    LoginFragment.editor.putString("password","kakaotalk");
                    LoginFragment.editor.putString("name", userProfile.getNickname());
                    LoginFragment.editor.apply();
                    LoginFragment.SNSSignIn();
                    alreadyCheck = true;
                }
            }

            // 사용자 정보 요청 실패
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e(TAG, "onFailure : " + errorResult.getErrorMessage());
            }
        });

    }

}
