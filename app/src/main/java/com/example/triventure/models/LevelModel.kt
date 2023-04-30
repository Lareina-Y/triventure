//Author(s): Matthew Mailman

//Attribution: This file to create a database model was copied and modified from the UserModel.kt
// file that was part of SQLDemo code that was provided through the course's Brightspace
package com.example.triventure.models

//Creates a database model for a level item that will be used to track user progress and organize
// the questions being asked of the user
class LevelModel(val levelId: Int, val levelName: String, val isComplete: Boolean, val highestScore: Int)