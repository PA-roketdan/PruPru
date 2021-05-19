package org.techtown.prupru

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.ByteArrayOutputStream

class CameraActivity : AppCompatActivity() {
    private var image: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        pictureIntent()

        btnSend.setOnClickListener { // send 버튼 누르면 서버로 보내기
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                var imgName: String? = uploadImage2Server()
            }
        }
    }

    private var REQUEST_IMAGE_CAPTURE = 1000; // do not change

    private fun pictureIntent() {
        // call camera app
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data?.extras?.get("data") != null) {
                val imageBitmap = data?.extras?.get("data") as Bitmap
                imagePreview.setImageBitmap(imageBitmap)
                this.image = imageBitmap
            } else {
                Toast.makeText(applicationContext, "기본 카메라를 사용해주세요.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadImage2Server(): String?{
        var byteArrayOutputStream = ByteArrayOutputStream()
        var data = this.image as Bitmap
        data.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        var byteArray = byteArrayOutputStream.toByteArray()

        // upload file
        var imgName: String? = upload(byteArray)

        return imgName
    }

    private fun upload(byteArray: ByteArray): String?{
        val timestamp = System.currentTimeMillis()
        var image_name: String = "$timestamp.png"
        var encoded_string: String = Base64.encodeToString(byteArray, 0)

        var requestQueue: RequestQueue = Volley.newRequestQueue(this)

        var url: String = "http://{server_address}/send2Server.php"
        val request: StringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                println(response)
                if (response != null){
                    var r: CameraRequest = CameraRequest()

                    var result = r.execute(image_name).get() // image bitmap
                    imagePreview.setImageBitmap(result)
                    println("Reqeust successfully received!")
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val map: HashMap<String, String> = HashMap()
                map["encoded_string"] = encoded_string
                map["image_name"] = image_name
                return map
            }
        }

        requestQueue.add(request)

        return image_name
    }

}