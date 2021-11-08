package cnu.rocket.prupru

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        if (user != null) {
            // user id
            val txt_uid=view.findViewById<TextView>(R.id.txt_uid)
            val userid=user.email?.split("@")?.get(0).toString()
            txt_uid.setText(userid+"님")

            // user reward
            val txt_reward=view.findViewById<TextView>(R.id.txt_reward)
            db.collection("users").document(userid).get()
                .addOnSuccessListener { result->
                    var reward_value=result.data
                    db.collection("users").document(userid).get()
                        .addOnSuccessListener { result ->
                            var reward_value = result.data?.get("reward").toString()
                            txt_reward.setText(reward_value)
                        }
                }
        }
        return view
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}