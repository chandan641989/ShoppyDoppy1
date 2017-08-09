package com.kartnap.chandan.shoppydoppy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.SignInButtonConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity  {
    GoogleApiClient mGoogleApiClient;
    SignInButton  signInButton;
    private int RC_SIGN_IN=1000;
    CallbackManager callbackManager;
    private URL profile_pic;
    //SharedPreferences mPrefs = getSharedPreferences("User_DB", 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle(" ");


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


// Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //Facebook Login


        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Bundle bFacebookData = null;
                        try {
                            bFacebookData = getFacebookData(object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String Name = bFacebookData.getString("first_name")+bFacebookData.getString("last_name");
                        String email = bFacebookData.getString("email");
                        String image = bFacebookData.getString("profile_pic");
                        //Intent intent = new Intent(LoginActivity.this,Categories.class);
                        Toast.makeText(getApplicationContext(),Name+" "+email+" "+image+"",Toast.LENGTH_LONG).show();

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,Name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //facebook callback
        callbackManager.onActivityResult(requestCode, resultCode, data);
        signInButton.setVisibility(View.GONE);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }
    private void handleSignInResult(GoogleSignInResult result) {
       // Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //updateUI(true);
            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }
    private Bundle getFacebookData(JSONObject jsonObject) throws JSONException {
        Bundle bundle = new Bundle();
        String id = jsonObject.getString("id");
        try {
            profile_pic = new URL("https://graph.faecbook.com/"+ id +"/picture?width=200&height=150");
            bundle.putString("profile_pic", profile_pic.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        bundle.putString("idFacebook",id);
        if (jsonObject.has("first_name")){
            bundle.putString("first_name",jsonObject.getString("first_name"));
        }
        if (jsonObject.has("last_name")){
            bundle.putString("last_name",jsonObject.getString("last_name"));
        }
        if (jsonObject.has("email")){
            bundle.putString("email",jsonObject.getString("email"));
        }
        return bundle;



    }




}
