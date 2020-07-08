package com.example.olx_nitt.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.olx_nitt.BaseActivity
import com.example.olx_nitt.MainActivity
import com.example.olx_nitt.R
import com.example.olx_nitt.utilities.Constants
import com.example.olx_nitt.utilities.SharedPref
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.login_activity.*
import java.util.*


class LoginActivity : BaseActivity(), View.OnClickListener {
    private var callbackManager: CallbackManager? =null
    private val EMAIL = "email"
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN=100
    private val TAG=LoginActivity::class.java.simpleName
    private var googleSignInOption:GoogleSignInOptions? =null
    private var googleSignInClient:GoogleSignInClient? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        FacebookSdk.sdkInitialize(getApplicationContext());
        auth=FirebaseAuth.getInstance()

        login_button.setReadPermissions(Arrays.asList(EMAIL));
        callbackManager = CallbackManager.Factory.create();

        clickListerners()
        configureGoogleSignin()
        registerFbCallback()
    }

    private fun registerFbCallback() {
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                handleFacebokAccess(loginResult?.accessToken)
                // App code
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })
    }

    private fun handleFacebokAccess(accessToken: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(accessToken?.token!!)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val account = auth.currentUser
                    SharedPref(this).setString(Constants.USER_ID,account?.uid!!)
                    if(account?.email !=null){
                        SharedPref(this).setString(Constants.USER_EMAIL,account.email!!)
                    }
                    if(account.displayName!=null){
                        SharedPref(this).setString(Constants.USER_NAME,account.displayName!!)
                    }
                    if(account.photoUrl!=null){
                        SharedPref(this).setString(Constants.USER_PHOTO,account.photoUrl.toString()!!)
                    }
                    //Toast.makeText(this,user?.displayName+" Logged in",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun clickListerners() {
        btn_facebook.setOnClickListener(this)
        btn_google.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_facebook->{
                login_button.performClick()
            }
            R.id.btn_google->{
                googleSignIn()
            }
        }
    }

    private fun googleSignIn() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun configureGoogleSignin() {
        googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOption!!)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }else{
            callbackManager?.onActivityResult(requestCode, resultCode, data);
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credentials).addOnCompleteListener{
            if(it.isSuccessful){
                if(account.email!=null){
                    SharedPref(this).setString(Constants.USER_EMAIL,account.email!!)
                }
                if(account.id!=null){
                    SharedPref(this).setString(Constants.USER_ID,account.id!!)
                }
                if(account.displayName!=null){
                    SharedPref(this).setString(Constants.USER_NAME,account.displayName!!)
                }
                if(account.photoUrl!=null){
                    SharedPref(this).setString(Constants.USER_PHOTO,account.photoUrl.toString()!!)
                }
               // Toast.makeText(this,"Google Logged In",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this,"Google SignIn Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }

}