//Author(s): Cameron Chaffey, Matthew Mailman, Lareina Yang
package com.example.triventure

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.triventure.database.DatabaseHelper
import com.example.triventure.models.QuestionModel

class QuestionActivity : AppCompatActivity() {

    private lateinit var databaseHelper : DatabaseHelper

    //Will link to the activity layout's UI elements and reset for each question
    private lateinit var timerTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button
    private lateinit var option4: Button

    //Will track the user's progress through the level
    private var numCorrectAnswers = 0
    private var currentQuestionIndex = 0
    private var levelId = 0

    //Will be used to track the current question's answer options and which one is correct
    private var timerCounter = 30 //30 second timer per question
    private var isTimerPaused = false //is timer paused
    private lateinit var options: ArrayList<String>
    private lateinit var currentAnswer: String

    //Tracks the level's questions
    private var triviaQuestions = ArrayList<QuestionModel>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        databaseHelper = DatabaseHelper(this)

        //Gets the questions for the level
        levelId = intent.getIntExtra("levelId", 0)
        triviaQuestions = databaseHelper.getLevelQuestions(levelId)

        val newTitle = "Level $levelId"
        title = newTitle

        //Links to the activity layout's UI elements
        timerTextView= findViewById(R.id.timer)
        questionTextView = findViewById(R.id.question_text_view)
        option1 = findViewById(R.id.button1)
        option2 = findViewById(R.id.button2)
        option3 = findViewById(R.id.button3)
        option4 = findViewById(R.id.button4)

        option1.setBackgroundColor(resources.getColor(R.color.Avocado))
        option2.setBackgroundColor(resources.getColor(R.color.Green_Blue))
        option3.setBackgroundColor(resources.getColor(R.color.Avocado))
        option4.setBackgroundColor(resources.getColor(R.color.Green_Blue))
        //Sets behaviour for answer option buttons

        
        //timer resources from:
        //https://abhiandroid.com/ui/countdown-timer 
        //https://stackoverflow.com/questions/19997588/countdowntimer-in-android-how-to-restart-it 

        val myTimer: CountDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if(!isTimerPaused)
                {
                    timerTextView.text = java.lang.String.valueOf(timerCounter)
                    timerCounter--
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                isTimerPaused = true
                timerTextView.text = "Time's Up!"
                //submit answer automatically
                //answerIndex 100 will always be wrong
                submitAnswer(100, this)
            }
        }.start()

        option1.setOnClickListener {
            submitAnswer(0, myTimer)
        }
        option2.setOnClickListener {
            submitAnswer(1, myTimer)
        }
        option3.setOnClickListener {
            submitAnswer(2, myTimer)
        }
        option4.setOnClickListener {
            submitAnswer(3, myTimer)
        }

        //Starts level with the first question
        setQuestion(0)

        val pauseButton = findViewById<Button>(R.id.pause_button)

        pauseButton.setOnClickListener {
            val popupMenu = PopupMenu(this, pauseButton)

            popupMenu.menuInflater.inflate(R.menu.pause_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_item_main_activity -> {
                        myTimer.cancel();
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        true
                    }
                    R.id.menu_item_level_activity -> {
                        myTimer.cancel();
                        val intent = Intent(this, LevelActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                        true
                    }
                    R.id.menu_item_restart_level -> {
                        myTimer.cancel();
                        setQuestion(0)
                        numCorrectAnswers = 0
                        currentQuestionIndex = 0
                        true
                    }
                    R.id.menu_item_resume -> {
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    //Populates the activity's UI elements with the specified question's data
    @SuppressLint("SetTextI18n")
    private fun setQuestion(questionIndex: Int) {
        val question = triviaQuestions[questionIndex]

        //track the question's correct answer and randomize the order of the question's answer options
        currentAnswer = question.answer
        options = arrayListOf(question.answer, question.option1, question.option2, question.option3)
        options.shuffle()

        //Displays the question
        questionTextView.text = question.questionName

        //populate the option button text with the question's randomized answer options
        option1.text = options[0]
        option2.text = options[1]
        option3.text = options[2]
        option4.text = options[3]

        //Displays how many questions into the level the user is
        val indexView = findViewById<TextView>(R.id.question_index_text_view)
        indexView.text = "${questionIndex + 1}/${triviaQuestions.size}"
    }

    //checks if answer is selected correctly and updates UI accordingly
    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun submitAnswer(selectedAnswerIndex: Int, myTimer: CountDownTimer) {
        //pause timer right away
        isTimerPaused = true
        myTimer.cancel() //stop timer
        var isCorrect = false

        if(selectedAnswerIndex!= 100) //100 is error code for time out (no answer given)
        {
            isCorrect = options[selectedAnswerIndex] == currentAnswer
        }

        // Show popup with answer result using alert dialog
        val builder = AlertDialog.Builder(this)
        val message = if (isCorrect) "Correct!" else "Wrong!"

        //Adds to user's score if correct
        if (isCorrect) {
            numCorrectAnswers++
        }

        // Create a SpannableStringBuilder with the message text
        val spannableStringBuilder = SpannableStringBuilder(message)

        // Set the dialog text size
        spannableStringBuilder.setSpan(
            RelativeSizeSpan(1.5f),
            0,
            message.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the dialog text color
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(if (isCorrect) resources.getColor(R.color.Avocado) else Color.RED),
            0,
            message.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        //Attribution: Code to implement a builder in Android studio was copied and modified from
        // [INSERT SOURCE] (Link: [INSERT LINK])
        builder.setMessage(spannableStringBuilder)
            // rest of the builder code
            .setPositiveButton("Next") { _, _ ->
                if (currentQuestionIndex < triviaQuestions.size - 1) {
                    // Still more questions; set up next one
                    currentQuestionIndex++
                    setQuestion(currentQuestionIndex)
                    //reset timer
                    timerCounter = 30
                    isTimerPaused = false
                    myTimer.start()
                } else {
                    // That was the last question; end quiz
                    val intent = Intent(this, ResultsActivity::class.java)
                    intent.putExtra("levelId", levelId)
                    intent.putExtra("numCorrectAnswers", numCorrectAnswers)
                    intent.putExtra("numQuestions", triviaQuestions.size)
                    startActivity(intent)
                }
            }
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }
}
