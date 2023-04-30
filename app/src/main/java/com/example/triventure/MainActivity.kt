//Author(s): Lareina Yang
package com.example.triventure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Links to the activity's Start button and sets its behaviour to navigating to the level
        // activity
        val nextActButton = findViewById<Button>(R.id.button)
        nextActButton.setOnClickListener{
            val intent = Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }

        //Attribution: The following color were generated through Colors (Link: https://coolors.co/ce1483-cc7700-296aa3-644536-417704)
        nextActButton.setBackgroundColor(resources.getColor(R.color.Ochre))
    }
}