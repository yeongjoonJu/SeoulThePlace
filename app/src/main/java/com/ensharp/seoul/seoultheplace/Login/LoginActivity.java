package com.ensharp.seoul.seoultheplace.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    SignInButton googleLoginBtn;
    LoginButton kakaoLoginBtn;

    int RC_SIGN_IN = 1000;

    public static TextView nameString;
    public static TextView emailString;
    public static TextView personIdString;
    private GlobalApplication globalApplication;
    private SessionCallback sessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_first);

        kakaoLoginBtn = (LoginButton) findViewById(R.id.kakao_login);
        kakaoLoginBtn.setOnClickListener(this);

        googleLoginBtn = (SignInButton) findViewById(R.id.google_login);
        googleLoginBtn.setSize(SignInButton.SIZE_STANDARD);
        googleLoginBtn.setOnClickListener(this);

        globalApplication = new GlobalApplication();
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);

        setGoogleLogin();
        nameString = (TextView) findViewById(R.id.id);
        emailString = (TextView) findViewById(R.id.email);
        personIdString = (TextView) findViewById(R.id.etc);
    }

    //구글로그인 세팅을 합니다.
    private void setGoogleLogin() {
        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleLoginBtn.setScopes(gso.getScopeArray());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.google_login:
                googleSignIn();
                break;
            case R.id.kakao_login:
                kakaoLoginBtn.performClick();
        }
    }

    private void googleSignIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{

        }
    }

    //구글로그인 결과를 뿌려줍니다.
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            nameString.setText(acct.getDisplayName());
            emailString.setText(acct.getEmail());
            personIdString.setText(acct.getId());
        }
    }


    //연결이 끊겼을때 토스트 메세지로 표시해 줍니다.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), ""+connectionResult, Toast.LENGTH_SHORT).show();
    }
}
