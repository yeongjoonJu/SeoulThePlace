package com.ensharp.seoul.seoultheplace.Login;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ensharp.seoul.seoultheplace.Login.KakaoLogin.GlobalApplication;
import com.ensharp.seoul.seoultheplace.Login.KakaoLogin.SessionCallback;
import com.ensharp.seoul.seoultheplace.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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

import org.json.JSONObject;


public class LoginFragment extends android.support.v4.app.Fragment implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {


    static LoginBackgroundActivity LActivity;

    GoogleApiClient mGoogleApiClient;
    SignInButton googleLoginBtn;
    LoginButton kakaoLoginBtn;

    Button makeID;
    boolean alreadyOpen = false;

    int RC_SIGN_IN = 1000;

    private GlobalApplication globalApplication;
    private SessionCallback sessionCallback;

    public static String email = null;
    public static String name = null;
    private CallbackManager callbackManager;
    com.facebook.login.widget.LoginButton facebookLoginBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_first,null); //view를 불러온다.

        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity());

        callbackManager = CallbackManager.Factory.create();
        facebookLoginBtn = (com.facebook.login.widget.LoginButton)view.findViewById(R.id.login_button);
        facebookLoginBtn.setReadPermissions("public_profile","email");
        facebookLoginBtn.setFragment(this);
        // If using in a fragment
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //loginResult.getAccessToken() 정보를 가지고 유저 정보를 가져올수 있습니다.
                        GraphRequest request =GraphRequest.newMeRequest(loginResult.getAccessToken() ,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            Log.e("user profile",object.toString());
                                            String fName = object.getString("name");
                                            Log.e("user profile",fName);
                                            name = fName;
                                            LActivity.onFragmentChanged();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("faceboooook","onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("faceboooook","onError");
                    }
                });

        LActivity = (LoginBackgroundActivity) getActivity();

        kakaoLoginBtn = (LoginButton)view.findViewById(R.id.kakao_login);
        kakaoLoginBtn.setOnClickListener(this);

        googleLoginBtn = (SignInButton)view.findViewById(R.id.google_login);
        googleLoginBtn.setSize(SignInButton.SIZE_STANDARD);
        googleLoginBtn.setOnClickListener(this);

        globalApplication = new GlobalApplication();
        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);

        makeID = (Button)view.findViewById(R.id.newID);
        makeID.setOnClickListener(this);

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
        }
    }
    public static void kakaoSignIn(){
        LActivity.onFragmentChanged();
    }

    private void googleSignIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //구글로그인 결과를 뿌려줍니다.
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            name = acct.getDisplayName();
            email = acct.getEmail();
            LActivity.onFragmentChanged();

        }
    }

    //연결이 끊겼을때 토스트 메세지로 표시해 줍니다.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


}