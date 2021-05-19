package org.techtown.prupru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val intent= Intent(this, MainActivity::class.java)

        var SignButton= this.findViewById<Button>(R.id.SignButton)
        SignButton.setOnClickListener {
            startActivity(intent)
        }
    }
}