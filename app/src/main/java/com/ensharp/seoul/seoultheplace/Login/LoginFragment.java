package com.ensharp.seoul.seoultheplace.Login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ensharp.seoul.seoultheplace.Login.KakaoLogin.GlobalApplication;
import com.ensharp.seoul.seoultheplace.Login.KakaoLogin.SessionCallback;
import com.ensharp.seoul.seoultheplace.R;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;


public class LoginFragment extends android.support.v4.app.Fragment implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    View view;
    static LoginBackgroundActivity LActivity;

    GoogleApiClient mGoogleApiClient;
    SignInButton googleLoginBtn;
    LoginButton kakaoLoginBtn;

    ImageView kakaoLogin;
    ImageView googleLogin;

    Button emailLogin;
    Button makeID;
    boolean alreadyOpen = false;
    String TAG = "LoginFragment";

    int RC_SIGN_IN = 1000;

    private GlobalApplication globalApplication;
    private SessionCallback sessionCallback;
    public static SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_first2,null); //view를 불러온다.

        LActivity = (LoginBackgroundActivity) getActivity();

        kakaoLoginBtn = (LoginButton)view.findViewById(R.id.kakao_login);
        kakaoLoginBtn.setOnClickListener(this);
        kakaoLogin = (ImageView)view.findViewById(R.id.fake_kakao);

        googleLogin = (ImageView)view.findViewById(R.id.fake_google);
        googleLoginBtn = (SignInButton)view.findViewById(R.id.google_login);
        googleLoginBtn.setSize(SignInButton.SIZE_STANDARD);
        googleLoginBtn.setOnClickListener(this);

        editor = LActivity.getSharedPreferences("data",0).edit();
        editor.clear();
        editor.apply();

        globalApplication = new GlobalApplication();
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);

        makeID = (Button)view.findViewById(R.id.newID);
        makeID.setOnClickListener(this);
        emailLogin = (Button)view.findViewById(R.id.EmailLogin);
        emailLogin.setOnClickListener(this);

        Log.d(TAG,"Ready to Login");
        if(!alreadyOpen) {
            setGoogleLogin();
        }
        return view;//view를 불렀으니 view를 돌려준다.
    }

    //구글로그인 세팅을 합니다.
    private void setGoogleLogin() {
        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleLoginBtn.setScopes(gso.getScopeArray());
        alreadyOpen = true;
        Log.d(TAG,"Ready to Login Google");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.google_login:
                googleSignIn();
                break;
            case R.id.newID:
                LActivity.onFragmentChanged();
                break;
            case R.id.EmailLogin:
                LActivity.EmailLoginChanger();
                break;
            case R.id.fake_google:
                googleLoginBtn.performClick();
                break;
            case R.id.fake_kakao:
                kakaoLoginBtn.performClick();
                break;

        }
    }
    public static void SNSSignIn(){
        Log.d("LoginFragment","KAKAO,NAVER SignIn");
        if(!LActivity.CheckEmail(LActivity.getSharedPreferences("data",0))) {
            Log.d("LoginFragment","KAKAO,NAVER New ID");
            LActivity.SendData(LActivity.getSharedPreferences("data", 0));
        }
        LActivity.NextActivity();
    }

    private void googleSignIn(){
        Log.d(TAG,"Start googleSignin");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG,"End googleSignin");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG,"onActivityResult in google Login");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //구글로그인 결과를 뿌려줍니다.
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            Log.d(TAG,"Success googleSignIn");
            GoogleSignInAccount acct = result.getSignInAccount();
            // Signed in successfully, show authenticated UI.
            editor.clear();
            editor.apply();
            editor.putString("email", acct.getEmail());
            editor.putString("password","google"); //비밀번호로 패스워드 저장
            editor.putString("name", acct.getDisplayName());
            editor.apply();
            if(!LActivity.CheckEmail(LActivity.getSharedPreferences("data",0))) {
                Log.d(TAG,"New ID in googleSignIn");
                LActivity.SendData(LActivity.getSharedPreferences("data", 0));
            }
            LActivity.NextActivity();
        }
    }

    //연결이 끊겼을때 토스트 메세지로 표시해 줍니다.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


}