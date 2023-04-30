//Author(s): Matthew Mailman, Lareina Yang

//Attribution: This file to create a DatabaseHelper was copied and modified from the UsersDBHelper.kt
// file that was part of SQLDemo code that was provided through the course's Brightspace

package com.example.triventure.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.triventure.models.QuestionModel
import com.example.triventure.models.LevelModel

class DatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    //Creates and populates the database tables
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_LEVEL_TABLE)
        db.execSQL(SQL_CREATE_QUESTION_TABLE)

        db.execSQL(SQL_INSERT_LEVELS)
        db.execSQL(SQL_INSERT_QUESTIONS)
    }

    //When updating the version of the database, drops its tables and recreates them
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DROP_LEVEL_TABLE)
        db.execSQL(SQL_DROP_QUESTION_TABLE)
        onCreate(db)
    }

    //Resets the database; mainly for testing purposes, but could be used to enable a "restart game"
    // feature
    fun resetDatabase(): Boolean {
        val db = writableDatabase

        //drop existing tables
        db.execSQL(SQL_DROP_LEVEL_TABLE)
        db.execSQL(SQL_DROP_QUESTION_TABLE)

        //create tables
        db.execSQL(SQL_CREATE_LEVEL_TABLE)
        db.execSQL(SQL_CREATE_QUESTION_TABLE)

        //insert seed data
        db.execSQL(SQL_INSERT_LEVELS)
        db.execSQL(SQL_INSERT_QUESTIONS)

        return true
    }

    //Retrieves the Id of the earliest level that the user has not completed yet
    @SuppressLint("Recycle")
    fun getCurrentLevel(): Int {
        val db = writableDatabase

        var level = 0
        var cursor: Cursor?

        //Tries running the query to get the current level
        try {
            cursor = db.rawQuery(SQL_GET_CURRENT_LEVEL, null)
        } catch (e: SQLiteException) {
            return 1
        }

        //Returns the current level ID if there is a level that has not been completed yet
        if (cursor!!.moveToFirst()) {
            level = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Level.COLUMN_LEVEL_ID))
        }
        cursor.close()

        //In the case where the user has completed all levels, sets it as if they were on the last
        // level
        if(level == 0){
            try {
                cursor = db.rawQuery(SQL_GET_ALL_LEVELS, null)
            } catch (e: SQLiteException) {
                return 1
            }
            level = cursor.count
        }

        return level
    }

    // Function to get row count of "Level" table
    fun getLevelsSize(): Int {
        val db = writableDatabase
        var cursor: Cursor?
        var count = 0

        //Tries running the query to get the number of levels
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseContract.Level.TABLE_NAME, null)
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
        } catch (e: SQLiteException) {
            return 0
        }

        return count
    }

    //Retrieves all of the levels in the Levels table
    fun getLevels(): ArrayList<LevelModel> {
        val db = writableDatabase
        val levels = ArrayList<LevelModel>()
        var cursor: Cursor?

        try {
            cursor = db.rawQuery(SQL_GET_ALL_LEVELS, null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }

        //Variables to track an individual level's column data
        var levelId: Int
        var levelName: String
        var isComplete: Boolean
        var highestScore: Int

        //Goes through the query results and populates an ArrayList with the data
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                levelId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Level.COLUMN_LEVEL_ID))
                levelName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Level.COLUMN_LEVEL_NAME))
                isComplete = cursor.getColumnIndexOrThrow(DatabaseContract.Level.COLUMN_IS_COMPLETE) == 1
                highestScore = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Level.COLUMN_HIGHEST_SCORE))

                levels.add(LevelModel(levelId, levelName, isComplete, highestScore))
                cursor.moveToNext()
            }
        }

        return levels
    }

    //Retrieves the questions that are part of a specified level
    fun getLevelQuestions(levelId: Int): ArrayList<QuestionModel> {
        val db = writableDatabase
        val questions = ArrayList<QuestionModel>()
        var cursor: Cursor?

        //Tries running the query to get all questions associated with the level
        try {
            cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.Question.TABLE_NAME +
                    " WHERE " + DatabaseContract.Question.COLUMN_LEVEL_ID + " = " + levelId, null)
        } catch (e: SQLiteException) {
            return ArrayList()
        }

        //Variables to track an individual question's column data
        var questionId: Int
        var questionName: String
        var answer: String
        var option1: String
        var option2: String
        var option3: String

        //Goes through the query results and populates an ArrayList with the data
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                questionId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Question.COLUMN_QUESTION_ID))
                questionName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Question.COLUMN_QUESTION_NAME))
                answer = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Question.COLUMN_ANSWER))
                option1 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Question.COLUMN_OPTION1))
                option2 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Question.COLUMN_OPTION2))
                option3 = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Question.COLUMN_OPTION3))

                questions.add(QuestionModel(questionId, levelId, questionName, answer, option1, option2, option3))
                cursor.moveToNext()
            }
        }

        return questions
    }

    //Marks a specified level as having been completed by the user
    fun completeLevel(levelId: Int) {
        val db = writableDatabase

        //Attribution: Code to create a ContentValues object and use it to update an SQLite database
        // entry was copied and modified from STechies (Link: https://www.stechies.com/sqlite-database-update-android/)

        //Sets the condition for an update query which will be used to only update the IsComplete
        // value of the level with the specified ID value
        val values: ContentValues = ContentValues()
        values.put(DatabaseContract.Level.COLUMN_IS_COMPLETE, 1)
        var where = Array<String>(1){levelId.toString()}

        //Updates the record as specified above
        db.update(DatabaseContract.Level.TABLE_NAME, values, DatabaseContract.Level.COLUMN_LEVEL_ID + " =? ", where)
    }

    //Retrieves the highest score a user received on a specified level
    fun getHighScore(levelId: Int): Int {
        val db = writableDatabase
        var cursor: Cursor?

        //Tries running the query to get a specific level's highest score
        try {
            val query = "SELECT * FROM " + DatabaseContract.Level.TABLE_NAME +
                    " WHERE " + DatabaseContract.Level.COLUMN_LEVEL_ID + " = " + levelId
            cursor = db.rawQuery(query, null)
        } catch (e: SQLiteException) {
            return 0
        }

        var result = 0

        //Goes through the query results and gets the highest score value
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                result = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Level.COLUMN_HIGHEST_SCORE))
                cursor.moveToNext()
            }
        }
        return result
    }

    //Updates the highest score value for a specific level
    fun updateHighScore(levelId: Int, score: Int): Boolean {
        val oldHighScore = getHighScore(levelId)

        //
        if(oldHighScore<score) {
            val db = writableDatabase

            //Attribution: Code to create a ContentValues object and use it to update an SQLite database
            // entry was copied and modified from STechies (Link: https://www.stechies.com/sqlite-database-update-android/)

            //Sets the condition for an update query which will be used to only update the
            // HighestScore value of the level with the specified ID value
            val values: ContentValues = ContentValues()
            values.put(DatabaseContract.Level.COLUMN_HIGHEST_SCORE, score)
            var where = Array<String>(1){levelId.toString()}

            //Updates the record as specified above
            db.update(DatabaseContract.Level.TABLE_NAME, values, DatabaseContract.Level.COLUMN_LEVEL_ID + " =? ", where)
        }

        //Returns whether the new score matched or beat the old high score, and therefore whether a
        // new high score was set
        return oldHighScore<=score
    }

    companion object {
        //Database information
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "Triventure.db"

        //Query to create the level table (contains info about the app's levels and player progress)
        private val SQL_CREATE_LEVEL_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DatabaseContract.Level.TABLE_NAME + " (" +
                    DatabaseContract.Level.COLUMN_LEVEL_ID + " INTEGER PRIMARY KEY, " +
                    DatabaseContract.Level.COLUMN_LEVEL_NAME + " STRING, " +
                    DatabaseContract.Level.COLUMN_IS_COMPLETE + " BOOLEAN, " +
                    DatabaseContract.Level.COLUMN_HIGHEST_SCORE + " DECIMAL);"

        //Query to create the question table (contains info about what level the question is part of and what the selectable answer options are)
        private val SQL_CREATE_QUESTION_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DatabaseContract.Question.TABLE_NAME + " (" +
                    DatabaseContract.Question.COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY, " +
                    DatabaseContract.Question.COLUMN_LEVEL_ID + " INTEGER, " +
                    DatabaseContract.Question.COLUMN_QUESTION_NAME + " STRING, " +
                    DatabaseContract.Question.COLUMN_ANSWER + " STRING, " +
                    DatabaseContract.Question.COLUMN_OPTION1 + " STRING, " +
                    DatabaseContract.Question.COLUMN_OPTION2 + " STRING, " +
                    DatabaseContract.Question.COLUMN_OPTION3 + " STRING); "

        //Query to populate the level table with five levels of seed data
        private val SQL_INSERT_LEVELS =
            "INSERT INTO " + DatabaseContract.Level.TABLE_NAME + " (" +
                    DatabaseContract.Level.COLUMN_LEVEL_ID + ", " +
                    DatabaseContract.Level.COLUMN_LEVEL_NAME + ", " +
                    DatabaseContract.Level.COLUMN_IS_COMPLETE + ", " +
                    DatabaseContract.Level.COLUMN_HIGHEST_SCORE + ") " +
            "VALUES (1, 'Level 1', 0, 0), " +
                    "(2, 'Level 2', 0, 0), " +
                    "(3, 'Level 3', 0, 0), " +
                    "(4, 'Level 4', 0, 0), " +
                    "(5, 'Level 5', 0, 0);"

        //query to populate the question table with twenty-five questions seed data
        private val SQL_INSERT_QUESTIONS =
            "INSERT INTO " + DatabaseContract.Question.TABLE_NAME + " (" +
                    DatabaseContract.Question.COLUMN_QUESTION_ID + ", " +
                    DatabaseContract.Question.COLUMN_LEVEL_ID + ", " +
                    DatabaseContract.Question.COLUMN_QUESTION_NAME + ", " +
                    DatabaseContract.Question.COLUMN_ANSWER + ", " +
                    DatabaseContract.Question.COLUMN_OPTION1 + ", " +
                    DatabaseContract.Question.COLUMN_OPTION2 + ", " +
                    DatabaseContract.Question.COLUMN_OPTION3 + ") " +

            //Attribution: The following trivia questions were copied from [INSERT SOURCE] (Link: [INSERT LINK])
            "VALUES (1, 1, 'What color are bananas when they are ripe?', 'Yellow', 'Green', 'Red', 'Purple'), " +
                "(2, 1, 'What is the capital city of Australia?', 'Canberra', 'Sydney', 'Melbourne', 'Perth'), " +
                "(3, 1, 'What is the smallest planet in our solar system?', 'Mercury', 'Venus', 'Earth', 'Mars'), " +
                "(4, 1, 'Which animal is known as the \"king of the jungle\"?', 'Lion', 'Tiger', 'Leopard', 'Panther'), " +
                "(5, 1, 'Which city is famous for its tower that leans?', 'Pisa', 'Rome', 'Florence', 'Venice'), " +
                "(6, 2, 'What is the largest planet in our solar system?', 'Jupiter', 'Uranus', 'Saturn', 'Neptune'), " +
                "(7, 2, 'What is the capital city of Russia?', 'Moscow', 'St. Petersburg', 'Kiev', 'Minsk'), " +
                "(8, 2, 'What is the name of the biggest desert in the world?', 'Sahara', 'Arabian', 'Gobi', 'Kalahari'), " +
                "(9, 2, 'Which bird is known for its long neck?', 'Giraffe', 'Penguin', 'Ostrich', 'Flamingo'), " +
                "(10, 2, 'What is the name of the biggest ocean in the world?', 'Pacific', 'Atlantic', 'Indian', 'Arctic'), " +
                "(11, 3, 'What is the capital city of Canada?', 'Ottawa', 'Toronto', 'Montreal', 'Vancouver'), " +
                "(12, 3, 'What is the smallest country in the world?', 'Vatican City', 'Monaco', 'Nauru', 'Tuvalu'), " +
                "(13, 3, 'Which country is the largest by land area?', 'Russia', 'Canada', 'China', 'United States'), " +
                "(14, 3, 'Which mammal is known for its black and white stripes?', 'Zebra', 'Giraffe', 'Horse', 'Donkey'), " +
                "(15, 3, 'What is the name of the highest mountain in Africa?', 'Mount Kilimanjaro', 'Mount Kenya', 'Mount Meru', 'Mount Elgon'), " +
                "(16, 4, 'What is the capital city of South Africa?', 'Pretoria', 'Cape Town', 'Durban', 'Johannesburg'), " +
                "(17, 4, 'What is the largest country in the world by population?', 'China', 'India', 'United States', 'Indonesia'), " +
                "(18, 4, 'What is the name of the deepest part of the ocean?', 'Mariana Trench', 'Kermadec Trench', 'Java Trench', 'Aleutian Trench'), " +
                "(19, 4, 'What is the name of the largest canyon in the world?', 'Grand Canyon', 'Fish River Canyon', 'Yarlung Tsangpo Grand Canyon', 'Copper Canyon'), " +
                "(20, 4, 'Which famous artist painted the Mona Lisa?', 'Leonardo da Vinci', 'Michelangelo', 'Raphael', 'Donatello'), " +
                "(21, 5, 'What is the smallest bird in the world?', 'Bee Hummingbird', 'Elf Owl', 'Eurasian Wren', 'Goldcrest'), " +
                "(22, 5, 'What is the name of the first human to walk on the moon?', 'Neil Armstrong', 'Buzz Aldrin', 'Yuri Gagarin', 'Alan Shepard'), " +
                "(23, 5, 'Which chemical element has the symbol \"Au\"?', 'Gold', 'Silver', 'Platinum', 'Copper'), " +
                "(24, 5, 'What is the name of the first artificial satellite to orbit the Earth?', 'Sputnik 1', 'Explorer 1', 'Vanguard 1', 'Telstar 1'), " +
                "(25, 5, 'Who wrote the novel \"To Kill a Mockingbird\"?', 'Harper Lee', 'J.D. Salinger', 'Ernest Hemingway', 'F. Scott Fitzgerald'); "

        //Query to find the earliest level that the user has not completed yet
        private val SQL_GET_CURRENT_LEVEL =
            "SELECT " + DatabaseContract.Level.COLUMN_LEVEL_ID + " FROM " + DatabaseContract.Level.TABLE_NAME + " " +
                    "WHERE " + DatabaseContract.Level.COLUMN_IS_COMPLETE + " = 0 " +
                    "LIMIT 1;"

        //Query to get all levels in the database
        private val SQL_GET_ALL_LEVELS =
            "SELECT * FROM " + DatabaseContract.Level.TABLE_NAME + ";"

        //Query to drop the level table
        private val SQL_DROP_LEVEL_TABLE = "DROP TABLE IF EXISTS " + DatabaseContract.Level.TABLE_NAME + ";"

        //Query to drop the question table
        private val SQL_DROP_QUESTION_TABLE = "DROP TABLE IF EXISTS " + DatabaseContract.Question.TABLE_NAME + ";"
    }
}