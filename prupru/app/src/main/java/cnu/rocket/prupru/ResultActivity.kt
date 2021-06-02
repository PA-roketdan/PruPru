package cnu.rocket.prupru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        var view2= this.findViewById<ConstraintLayout>(R.id.view2)
        var titletext1=this.findViewById<TextView>(R.id.textView8)
        var titletext2=this.findViewById<TextView>(R.id.textView10)
        view2.setVisibility(View.INVISIBLE)
        titletext1.setVisibility(View.INVISIBLE)
        titletext2.setVisibility(View.INVISIBLE)

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

            val EngtoKoMap = mapOf("battery" to "배터리", "glass" to "유리", "metal" to "캔류", "paper" to "종이", "plastic" to "플라스틱")

            var result_1= this.findViewById<Button>(R.id.result_1)
            var result_2= this.findViewById<Button>(R.id.result_2)
            var result_3= this.findViewById<Button>(R.id.result_3)
            if (result_array != null) {
                result_1.setText(EngtoKoMap[result_array.first[0]]+": "+ result_array.second[0]+"%")
                result_2.setText(EngtoKoMap[result_array.first[1]]+": "+ result_array.second[1]+"%")
                result_3.setText(EngtoKoMap[result_array.first[2]]+": "+ result_array.second[2]+"%")
            }

            result_1.setOnClickListener {
                if (result_array != null) {
                    if(result_array.first[0] == "plastic"){
                        pagechange()
                    }else{
                        pageresult(EngtoKoMap[result_array.first[0]],result_array.first[0] )
                    }
                }
            }
            result_2.setOnClickListener {
                if (result_array != null) {
                    if(result_array.first[1]=="plastic"){
                        pagechange()
                    }else{
                        pageresult(EngtoKoMap[result_array.first[1]],result_array.first[1])
                    }
                }
            }
            result_3.setOnClickListener {
                if (result_array != null) {
                    if(result_array.first[2]=="plastic"){
                        pagechange()
                    }else{
                        pageresult(EngtoKoMap[result_array.first[2]],result_array.first[2])
                    }
                }
            }
        }
    }

    private fun str2arr(str: String): Pair<Array<String>, Array<Float?>> {
        var tmp = str.trim().split("\n")
        var labels = arrayOf("", "", "")
        var probs = arrayOfNulls<Float>(3)
        for(i in 0..2){
            var temp = tmp[i].split(":")
            labels[i] = temp[0]
            val toProb: Float = (temp[1].toFloat() * 1000).roundToInt() / 10f
            probs[i] = toProb
        }
        return Pair(labels, probs)
    }

    private fun pagechange(){
        var view1= this.findViewById<ConstraintLayout>(R.id.view1)
        var view2= this.findViewById<ConstraintLayout>(R.id.view2)
        var titletext=this.findViewById<TextView>(R.id.textView5)
        var titletext1=this.findViewById<TextView>(R.id.textView8)
        var titletext2=this.findViewById<TextView>(R.id.textView10)
        view1.setVisibility(View.INVISIBLE)
        view2.setVisibility(View.VISIBLE)
        titletext.setVisibility(View.INVISIBLE)
        titletext1.setVisibility(View.VISIBLE)
        titletext2.setVisibility(View.VISIBLE)
    }

    private fun pageresult(name: String?, engname:String?){
        val intent = Intent(this, ResultActivity2::class.java)
        intent.putExtra("name",name)
        intent.putExtra("engname", engname)
        startActivity(intent)
    }

}