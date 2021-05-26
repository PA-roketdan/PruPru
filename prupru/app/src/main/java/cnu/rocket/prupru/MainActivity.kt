package cnu.rocket.prupru

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnCamera= this.findViewById<ImageButton>(R.id.CameraButton)
        btnCamera.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkPersmission()){ // check permission
                    val intent = Intent(this, CameraActivity::class.java)
                    startActivity(intent)
                }else{
                    requestPermission()
                }
            }
        }

        var SettingButton=this.findViewById<ImageButton>(R.id.SettingButton)
        SettingButton.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private val PERMISSIONS_REQUEST_CODE = 1000; // do not change

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSIONS_REQUEST_CODE)
    }


    private fun checkPersmission(): Boolean {
        var res = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)

        return res
    }

    // permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(applicationContext, "사용자가 권한을 허용했습니다.", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(applicationContext, "사용자가 권한을 거부했습니다.", Toast.LENGTH_LONG).show()
        }
    }
}