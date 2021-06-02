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
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResultActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result2)

        var txt_way=this.findViewById<TextView>(R.id.txt_way)
        txt_way.setText("")

        var Img_home= this.findViewById<ImageButton>(R.id.Img_home)
        Img_home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val intent=Intent(this.getIntent())
        var engname:String?=""
        if(intent.hasExtra("name")){
            var name=intent.getStringExtra("name")
            engname=intent.getStringExtra("engname")
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
        if (engname != null) {
            getData(engname)
        }

    }

    private fun getData(engname:String) {
        //Access a Cloud Firestore instance from Activity
        val db = Firebase.firestore
//        var value:Task<DocumentSnapshot>
//        CoroutineScope(Dispatchers.Main).launch {
//            val x= CoroutineScope(Dispatchers.Default).async {
//                db.collection("trashcan").document(engname).get().addOnSuccessListener {result->
//                    result.data
//                }
//            }.await()
//
//            value=x
//        }

        db.collection("trashcan").document(engname).get()
                .addOnSuccessListener { result->
                    var data=result.data

                    var url: Uri? = Uri.parse(data?.get("attachmentUrl").toString())
                    var Img_img=this.findViewById<ImageView>(R.id.Img_img)
                    Glide.with(this).load(url).into(Img_img)

                    var number=data?.get("no").toString()
                    var txt_recycle=this.findViewById<TextView>(R.id.txt_recycle)

                    var Img_mark=this.findViewById<ImageView>(R.id.Img_mark)

                    db.collection("mark").document(engname).get()
                        .addOnSuccessListener { result2->
                            var mark_name=result2.data
                            var url_mark:Uri?=Uri.parse(mark_name?.get("Url").toString())
                            Glide.with(this).load(url_mark).into(Img_mark)

                            var value = Integer.parseInt(number)
                            if (value in 0..6){
                                txt_recycle.append(" 가능")
                            }else if(value in 7..8){
                                txt_recycle.append(" 불가능")
                            }else{
                                txt_recycle.setVisibility(View.INVISIBLE)
                            }
                        }

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