package com.example.googleapi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.Objects;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private RelativeLayout relativeLayout;
    private SignInButton SignIn;
    private TextView Name,Email;
    private ImageView ImageView;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = findViewById(R.id.relativeLayout);
        SignIn = findViewById(R.id.SignIn);
        Button signOut = findViewById(R.id.SignOut);
        Name = findViewById(R.id.user_name);
        Email = findViewById(R.id.user_email);
        ImageView = findViewById(R.id.img1);
        SignIn.setOnClickListener(this);
        relativeLayout.setVisibility(View.GONE);
        signOut.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.SignIn:
                signIn();
                break;

            case R.id.SignOut:
                signOut();
                break;
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Toast.makeText(this,"hi",Toast.LENGTH_SHORT).show();
    }
    private void signIn()
    {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i,REQ_CODE);
    }
    private void signOut()
    {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                UpdateUi(false);
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleResult(GoogleSignInResult result)
    {
        if (result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = null;
            if (account != null) {
                name = account.getDisplayName();
            }
            String email = account != null ? account.getEmail() : null;
            String img_url = account != null ? Objects.requireNonNull(account.getPhotoUrl()).toString() : null;
            Name.setText(name);
            Email.setText(email);
            Glide.with(this).load(img_url).into(ImageView);
            UpdateUi(true);
        }
        else UpdateUi(false);
    }
    private void UpdateUi(boolean isLogin)
    {
        if (isLogin)
        {
            relativeLayout.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.GONE);
        }else{
            relativeLayout.setVisibility(View.GONE);
            SignIn.setVisibility(View.VISIBLE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE)
        {
            GoogleSignInResult result =  Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
