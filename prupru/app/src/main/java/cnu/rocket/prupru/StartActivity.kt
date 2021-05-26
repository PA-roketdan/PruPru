package cnu.rocket.prupru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import java.util.*

import com.facebook.AccessToken
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class StartActivity : AppCompatActivity() {
    var callbackManager: CallbackManager? = null
    private lateinit var auth: FirebaseAuth
    lateinit var authListener : FirebaseAuth.AuthStateListener
    lateinit var googleSigneInClient : GoogleSignInClient //구글 로그인을 관리하는 클래스
    var TAG_f = "Facebook : "
    var TAG_g = "Google : "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

        val intent= Intent(this, MainActivity::class.java)

        var SignButton= this.findViewById<Button>(R.id.SignButton)
        SignButton.setOnClickListener {
            startActivity(intent)
        }

        // 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            }
            else if (token != null) {
                Toast.makeText(applicationContext, "로그인 성공 ${token.accessToken}", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
        }

        var kakao_login_button= this.findViewById<ImageButton>(R.id.kakao_login_button)
        kakao_login_button.setOnClickListener {
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }


        // Configure Google Sign In
        var gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

        googleSigneInClient=GoogleSignIn.getClient(this,gso)
        var google_login_button: SignInButton =this.findViewById(R.id.google_login_button)

        google_login_button.setOnClickListener {
            signIn()
        }




        // Initialize Firebase Auth
        auth = Firebase.auth


        var facebook_login_button:LoginButton= this.findViewById(R.id.facebook_login_button)

        callbackManager = CallbackManager.Factory.create()

        facebook_login_button.setPermissions(Arrays.asList("email", "public_profile"))
        facebook_login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                handleFacebookAccessToken(loginResult.accessToken)

            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "로그인 취소", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException) {
                Toast.makeText(applicationContext, "로그인 실패" + error, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==100){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG_g, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG_g, "Google sign in failed", e)
            }
        }
        else{
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG_g, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG_g, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(TAG_f, "handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG_f, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG_f, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        println("user"+currentUser.toString())
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            println("user2"+user.toString())
        }
    }


    private fun signIn(){
        val signInIntent =googleSigneInClient.signInIntent
        startActivityForResult(signInIntent,100)
    }

}