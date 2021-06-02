package cnu.rocket.prupru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class ResultActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result2)

        var Img_home= this.findViewById<ImageButton>(R.id.Img_home)
        Img_home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val intent=Intent(this.getIntent())
        var name:String?=""
        if(intent.hasExtra("name")){
            name=intent.getStringExtra("name")
            var engname=intent.getStringExtra("engname")
            var txt_result=this.findViewById<TextView>(R.id.txt_result)
            var txt_value=this.findViewById<TextView>(R.id.txt_value)
            txt_result.setText(name)
            txt_value.setText(engname)
        }

        var btn_camera=this.findViewById<ImageButton>(R.id.btn_camera)
        btn_camera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }
    }
}