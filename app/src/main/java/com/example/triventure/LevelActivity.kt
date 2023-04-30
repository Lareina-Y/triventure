//Author(s): Lareina Yang
package com.example.triventure

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.triventure.adapters.LevelTableAdapter
import com.example.triventure.database.DatabaseHelper

class LevelActivity : AppCompatActivity() {

    private lateinit var databaseHelper : DatabaseHelper

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        databaseHelper = DatabaseHelper(this)

        //Tracks all the level information stored in the database
        val levels = databaseHelper.getLevels();

        //Creates database if there are no levels; will typically be used only on the first time ran
        if(levels.isEmpty()) {
            databaseHelper.resetDatabase()
        }

        //Tracks the current level that the user is on in terms of progress
        val currentLevel = databaseHelper.getCurrentLevel()

        //Links to the activity layout's current level TextView
        val currentLevelTextView = findViewById<TextView>(R.id.currentLevel)
        currentLevelTextView.text = "Current Level: $currentLevel"

        //Links to the activity layout's level table RecyclerView
        val tableRecyclerViewUI  = findViewById<RecyclerView>(R.id.table_recycler_view)
        tableRecyclerViewUI.layoutManager = LinearLayoutManager(this)

        //Creates an adapter that considers the current level that the user is on
        val adapter = LevelTableAdapter(levels, currentLevel)
        tableRecyclerViewUI.adapter =  adapter

        //Links to the activity layout's continue button and sets its behaviour to navigating to the
        // current level that the user is on
        val nextActButton = findViewById<Button>(R.id.button)
        nextActButton.setOnClickListener{
            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtra("levelId", currentLevel)
            startActivity(intent)
        }
    }
}