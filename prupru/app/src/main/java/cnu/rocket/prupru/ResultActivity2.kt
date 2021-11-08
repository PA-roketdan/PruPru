package cnu.rocket.prupru

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResultActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result2)

        var txt_way=this.findViewById<TextView>(R.id.txt_way)
        txt_way.setText("")

        var Img_home= this.findViewById<ImageButton>(R.id.Img_back)
        Img_home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

//        var btn_camera=this.findViewById<ImageButton>(R.id.btn_camera)
//        btn_camera.setOnClickListener {
//            val intent = Intent(this, CameraActivity::class.java)
//            startActivity(intent)
//        }

        val intent=Intent(this.getIntent())

        var txt_result=this.findViewById<TextView>(R.id.txt_result)
        var txt_value=this.findViewById<TextView>(R.id.txt_value)

        var engname:String?=""
        if(intent.hasExtra("name")){
            var name=intent.getStringExtra("name")
            engname=intent.getStringExtra("engname")
            txt_result.setText(name)
            txt_value.setText(engname)

            if (engname != null) {
                getData(engname)
            }
        } else if(intent.hasExtra("trashname")){
            var trashname = intent.getStringExtra("trashname")
            var no = intent.getStringExtra("no")
            var solution = intent.getStringExtra("solution")
            var attachmentUrl = intent.getStringExtra("attachmentUrl")
            txt_result.setText(trashname)

            setSearchResult(trashname, no, solution, attachmentUrl)
        }
    }

    private fun setSearchResult(
        trashname: String?,
        no: String?,
        solution: String?,
        attachmentUrl: String?
    ) {
        var url: Uri? = Uri.parse(attachmentUrl)
        var Img_img = this.findViewById<ImageView>(R.id.Img_img)
        Glide.with(this).load(url).into(Img_img)

        var number = no
//        var txt_recycle=this.findViewById<TextView>(R.id.txt_recycle)

        var Img_mark=this.findViewById<ImageView>(R.id.Img_mark)

        val db = Firebase.firestore
        val no2Mark = mapOf("0" to "pet", "1" to "plastic", "2" to "bag", "3" to "metal", "4" to "paper", "5" to "pack", "6" to "glass",
        "7" to "trash", "8" to "garbage", "9" to "battery", "10" to "drug")

        no2Mark[no]?.let {
            db.collection("mark").document(it).get()
                .addOnSuccessListener { result2->
                    var mark_name=result2.data
                    var url_mark:Uri?=Uri.parse(mark_name?.get("Url").toString())
                    Glide.with(this).load(url_mark).into(Img_mark)

//                    var value = Integer.parseInt(number)
//                    if (value in 0..6){
//                        txt_recycle.append(" 가능")
//                    }else if(value in 7..8){
//                        txt_recycle.append(" 불가능")
//                    }else{
//                        txt_recycle.setVisibility(View.INVISIBLE)
//                    }
                }
        }

        var txt_way = this.findViewById<TextView>(R.id.txt_way)
        var temp = solution
        var temp_array = temp?.split("\\n")

        var txt:String=""
        if (temp_array != null) {
            temp_array.forEach{
                txt=txt+it+"\n"
            }
        }
        txt_way.setText(txt)
    }


    private fun getData(engname:String) {
        //Access a Cloud Firestore instance from Activity
        val db = Firebase.firestore

        db.collection("trashcan").document(engname).get()
                .addOnSuccessListener { result->
                    var data=result.data

                    var url: Uri? = Uri.parse(data?.get("attachmentUrl").toString())
                    var Img_img=this.findViewById<ImageView>(R.id.Img_img)
                    Glide.with(this).load(url).into(Img_img)

                    var number=data?.get("no").toString()
//                    var txt_recycle=this.findViewById<TextView>(R.id.txt_recycle)

                    var Img_mark=this.findViewById<ImageView>(R.id.Img_mark)

//                    db.collection("mark").document(engname).get()
//                        .addOnSuccessListener { result2->
//                            var mark_name=result2.data
//                            var url_mark:Uri?=Uri.parse(mark_name?.get("Url").toString())
//                            Glide.with(this).load(url_mark).into(Img_mark)
//
//                            var value = Integer.parseInt(number)
//                            if (value in 0..6){
//                                txt_recycle.append(" 가능")
//                            }else if(value in 7..8){
//                                txt_recycle.append(" 불가능")
//                            }else{
//                                txt_recycle.setVisibility(View.INVISIBLE)
//                            }
//                        }

                    var txt_way=this.findViewById<TextView>(R.id.txt_way)
                    var temp=data?.get("solution").toString()
                    var temp_array=temp.split("\\n")
                    println(temp_array)

                    var txt:String=""
                    temp_array.forEach{
                        txt=txt+it+"\n"
                    }
                    txt_way.setText(txt)
                }
    }
}