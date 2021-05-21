package org.techtown.prupru

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

class StartActivity : AppCompatActivity() {
    var callbackManager: CallbackManager? = null

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

        var facebook_login_button= this.findViewById<ImageButton>(R.id.facebook_login_button)

        callbackManager = CallbackManager.Factory.create()
        facebook_login_button.setOnClickListener {
            facebookLogin()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    fun facebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email", "user_friends")
            )

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    //  handleFacebookAccessToken(result?.accessToken)
                    Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()

                }
                override fun onCancel() {
                   // Toast.makeText(applicationContext, "로그인 취소", Toast.LENGTH_SHORT).show()
                }
                override fun onError(error: FacebookException?) {
                    Toast.makeText(applicationContext, "로그인 실패" + error, Toast.LENGTH_SHORT).show()
                }
            })
    }

}