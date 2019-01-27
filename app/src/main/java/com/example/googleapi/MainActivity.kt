package com.example.googleapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    //Variable's
    private lateinit var googleApiClient:GoogleApiClient
    private val request:Int = 9001
    private lateinit var relativelayout:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        relativelayout = findViewById(R.id.relativeLayout)
        relativelayout.visibility = GONE

        val signInOptions: GoogleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient
            .Builder(this).enableAutoManage(this,this)
            .addApi<GoogleSignInOptions?>(Auth.GOOGLE_SIGN_IN_API,signInOptions)
            .build()

        SignIn.setOnClickListener {
            val i:Intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(i,request) }
        SignOut.setOnClickListener {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback { updateUi(false) }        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request){
            val result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleResult(result)
        }
    }

    private fun handleResult(result:GoogleSignInResult) {

        if (result.isSuccess){
            val account : GoogleSignInAccount? = result.signInAccount
            val name = account?.displayName
            val email = account?.email
            val image = account?.photoUrl
            user_name.text = name
            user_email.text = email
            val requestoption:RequestOptions = RequestOptions().circleCrop()
            Glide.with(this).applyDefaultRequestOptions(requestoption).load(image).into(img1)
            updateUi(true) }

        else updateUi(false)
    }

    private fun updateUi(isLogin: Boolean) {

        if (isLogin) { relativelayout.visibility = VISIBLE
                       SignIn.visibility = GONE }

     else { relativelayout.visibility = GONE
            SignIn.visibility = VISIBLE }
    }
}

