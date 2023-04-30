//Author(s): Cameron Chaffey, Matthew Mailman, Lareina Yang
package com.example.triventure

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.triventure.database.DatabaseHelper

class ResultsActivity : AppCompatActivity() {

    private lateinit var databaseHelper : DatabaseHelper

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        //Retrieves level and score data from the previously-completed LevelActivity
        val levelId = intent.getIntExtra("levelId", 0)
        val numCorrectAnswers = intent.getIntExtra("numCorrectAnswers", 0)
        val numQuestions = intent.getIntExtra("numQuestions", 0)

        //Links to the activity layout's score message
        val messageTextView = findViewById<TextView>(R.id.messageTextView)

        //Links to the activity layout's back button and sets its behaviour to navigating to the
        // LevelActivity
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, LevelActivity::class.java)
            startActivity(intent)
        }

        //Tracks user's score as a percentage
        val percentCorrect = numCorrectAnswers * 100 / numQuestions

        //Displays a specific message depending on the user's score
        when {
            percentCorrect == 100 -> {
                messageTextView.text = "Perfect score! Well done!"
            }
            percentCorrect >= 75 -> {
                messageTextView.text = "Great job! You really know your stuff."
            }
            percentCorrect >= 60 -> {
                messageTextView.text = "Not bad, but you can do better."
            }
            else -> {
                messageTextView.text = "Better luck next time! You need a score of at least 3/5 to proceed to the next level."
            }
        }

        databaseHelper = DatabaseHelper(this)

        //Links to the activity layout's next level button
        val nextLevelButton = findViewById<Button>(R.id.nextLevelButton)

        //Links to the activity layout's warning message (only when there is no "next" level)
        val warningMessage = findViewById<TextView>(R.id.warning)

        //Checks whether the user passed the level
        if(percentCorrect >= 60){
            //User passed; marks level as complete
            databaseHelper.completeLevel(levelId)

            //Checks whether there is a next level
            if (databaseHelper.getLevelsSize() < levelId + 1) {
                //There is no next level; displays warning around the next level button
                warningMessage.visibility = View.VISIBLE;
                nextLevelButton.isEnabled = false
            } else {
                //There is a next level; sets the next level button's behaviour to navigate to it
                nextLevelButton.setOnClickListener {
                    val intent = Intent(this, QuestionActivity::class.java)
                    intent.putExtra("levelId", levelId+1)
                    startActivity(intent)
                }
            }
        } else {
            //User failed; sets the next level button's behaviour to navigate to the same level
            // again for the user to retry it
            nextLevelButton.text = "Retry Level"
            nextLevelButton.setOnClickListener {
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("levelId", levelId)
                startActivity(intent)
            }
        }

        //Links to the activity layout's results TextView
        val resultsTextView = findViewById<TextView>(R.id.resultsTextView)

        //Displays user's score for the level and whether they achieved the highest score
        if(databaseHelper.updateHighScore(levelId, percentCorrect)){
            resultsTextView.text = "High Score!\n$numCorrectAnswers/$numQuestions"
        }
        else {
            resultsTextView.text = "$numCorrectAnswers/$numQuestions"
        }

    }
}
