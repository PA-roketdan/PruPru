package cnu.rocket.prupru

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_setting, container, false)

        val user = Firebase.auth.currentUser
        if (user != null) {
            val txt_id=view.findViewById<TextView>(R.id.txt_id)
            txt_id.setText(user.email?.split("@")?.get(0)+"님")
        }

        var logout= view.findViewById<Button>(R.id.logout)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()
            val intent = Intent(getActivity(), StartActivity::class.java)
            startActivity(intent)
//            finish()
        }

        return view

        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

}