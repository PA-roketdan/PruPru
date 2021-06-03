package cnu.rocket.prupru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.item.view.*

class SearchActivity : AppCompatActivity() {
    var db : FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        db = FirebaseFirestore.getInstance()

        recyclerview.adapter = RecyclerViewAdapter()
        recyclerview.layoutManager = LinearLayoutManager(this)

        var Img_home= this.findViewById<ImageButton>(R.id.Img_home)
        Img_home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        var txt_searchinput:EditText = this.findViewById<EditText>(R.id.txt_searchinput)

        var searchButton= this.findViewById<ImageButton>(R.id.searchButton)

        searchButton.setOnClickListener {
            (recyclerview.adapter as RecyclerViewAdapter).search(txt_searchinput.text.toString())
            txt_searchinput.setText("")
        }

    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var trashBook : ArrayList<Trash> = arrayListOf()

        init {
            db?.collection("database")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // Clear ArrayList
                trashBook.clear()

                for (snapshot in querySnapshot!!.documents) {
                    println("${snapshot["no"]?.javaClass}")
                    var item = snapshot.toObject(Trash::class.java)
                    trashBook.add(item!!)
                }
                notifyDataSetChanged()
            }
        }

        // Create ViewHolder by inflating xml
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        // connect view with real data
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            viewHolder.trashname.text = trashBook[position]?.trashname
            viewHolder.no.text = trashBook[position]?.no
            viewHolder.solution.text = trashBook[position]?.solution
            viewHolder.attachmentUrl.text = trashBook[position]?.attachmentUrl

            var tmp:Trash = trashBook[position]
            viewHolder.rootView.setOnClickListener{
                val intent = Intent(viewHolder.getContext(), ResultActivity2::class.java)
                intent.putExtra("trashname", tmp.trashname)
                intent.putExtra("no", tmp.no)
                intent.putExtra("solution", tmp.solution)
                intent.putExtra("attachmentUrl", tmp.attachmentUrl)
                ContextCompat.startActivity(viewHolder.getContext(), intent, null)
            }

        }

        override fun getItemCount(): Int {
            return trashBook.size
        }

        fun search(searchWord : String) {
            db?.collection("database")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                // Clear ArrayList
                trashBook.clear()

                for (snapshot in querySnapshot!!.documents) {
                    if (snapshot.getString("trashname")!!.contains(searchWord)) {
                        var item = snapshot.toObject(Trash::class.java)
                        trashBook.add(item!!)
                    }
                }
                notifyDataSetChanged()
            }
        }
    }

}