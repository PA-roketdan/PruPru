package cnu.rocket.prupru

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)
        val user = Firebase.auth.currentUser
        if (user != null) {
            val txt_uid=view.findViewById<TextView>(R.id.txt_uid)
            txt_uid.setText(user.email?.split("@")?.get(0)+"님")
        }

        return view
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}