package cnu.rocket.prupru

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class CameraFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater!!.inflate(R.layout.fragment_camera, container, false)

        val btn_camera = view.findViewById<Button>(R.id.btn_camera)

        btn_camera.setOnClickListener{
            val intent = Intent(activity, DetectorActivity::class.java)
            startActivity(intent)
        }

        return view

        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

}