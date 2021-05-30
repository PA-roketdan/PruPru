package cnu.rocket.prupru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)


        var Img_home= this.findViewById<ImageButton>(R.id.img_home)
        Img_home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val intent=Intent(this.getIntent())

        if(intent.hasExtra("image_name")){
            var image_name=intent.getStringExtra("image_name")
            var r: CameraRequest = CameraRequest()
            var result_img = r.execute(image_name).get() // image bitmap

            var Img_trash=this.findViewById<ImageView>(R.id.img_trash)
            Img_trash.setImageBitmap(result_img)
        }


        if(intent.hasExtra("result")){
            var result_array= intent.getStringExtra("result")?.let { str2arr(it) }
            println("ya"+result_array)

            var result_1= this.findViewById<Button>(R.id.result_1)
            var result_2= this.findViewById<Button>(R.id.result_2)
            var result_3= this.findViewById<Button>(R.id.result_3)
            if (result_array != null) {
                result_1.setText(result_array.get(0))
                result_2.setText(result_array.get(1))
                result_3.setText(result_array.get(2))
            }

        }
    }


    private fun str2arr(str: String): Array<String> {
        var tmp = str.trim().split("\n")
        var results = arrayOf("", "", "")
        for(i in 0..2){
            val temp = tmp[i].split(":")
            val toProb: Float = (temp[1].toFloat() * 10000).roundToInt() / 100f
            results[i] = temp[0] + ":  " + toProb.toString()+"%"

        }
        return results
    }

}