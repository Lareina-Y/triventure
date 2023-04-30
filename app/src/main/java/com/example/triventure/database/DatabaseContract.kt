//Author(s): Matthew Mailman

//Attribution: This file to create a database model was copied and modified from the DBContract.kt
// file that was part of SQLDemo code that was provided through the course's Brightspace

package com.example.triventure.database

import android.provider.BaseColumns

object DatabaseContract {
    //Defines the contents of the Levels table
    class Level : BaseColumns {
        companion object {
            val TABLE_NAME = "Levels"
            val COLUMN_LEVEL_ID = "LevelId"
            val COLUMN_LEVEL_NAME = "LevelName"
            val COLUMN_IS_COMPLETE = "IsComplete"
            val COLUMN_HIGHEST_SCORE = "HighestScore"
        }
    }
    //Defines the contents of the Questions table
    class Question : BaseColumns {
        companion object {
            val TABLE_NAME = "Questions"
            val COLUMN_QUESTION_ID = "QuestionId"
            val COLUMN_LEVEL_ID = "LevelId"
            val COLUMN_QUESTION_NAME = "QuestionName"
            val COLUMN_ANSWER = "Answer"
            val COLUMN_OPTION1 = "Option1"
            val COLUMN_OPTION2 = "Option2"
            val COLUMN_OPTION3 = "Option3"
        }
    }
}