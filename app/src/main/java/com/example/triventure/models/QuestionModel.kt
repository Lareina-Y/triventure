//Author(s): Matthew Mailman

//Attribution: This file to create a database model was copied and modified from the UserModel.kt
// file that was part of SQLDemo code that was provided through the course's Brightspace
package com.example.triventure.models

//Creates a database model for a question item that will be used to reference a specific trivia
// question, and its answer options, within a level
class QuestionModel(val questionId: Int, val levelId: Int, val questionName: String, val answer: String, val option1: String, val option2: String, val option3: String)