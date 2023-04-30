<!--- The following README.md sample file was adapted from https://gist.github.com/PurpleBooth/109311bb0361f32d87a2#file-readme-template-md by Gabriella Mosquera for academic use ---> 
<!--- You may delete any comments in this sample README.md file. If needing to use as a .txt file then simply delete all comments, edit as needed, and save as a README.txt file --->

# CSCI4176-Triventure

* *Date Created*: 26 Feb. 2023
* *Last Modification Date*: 11 Apr. 2023
* *Git URL*: <https://git.cs.dal.ca/mmailman/csci4176-triventure>

## Authors

* [Shiwen(Lareina) Yang](sh836690@dal.ca) - *(B00816905)*
* [Matthew Mailman](mt536151@dal.ca) - *(B00805914)*
* [Cameron Chaffey](cm854657@dal.ca) - *(B00829909)*
* [Lily Andruschak](lily.andruschak@dal.ca) - *(B00798092)*


## Description

### Features

- MainActivity: 
  - The landing page
  - User click the "Start" button to start the app

- LevelActivity:
  - Level select page
  - List all the existing levels and their highest score
  - "Current Level" represents the highest level that the user can access. Only when the user gets an equal or above 60% (3/5) score, the user will be allowed to access the higher level, otherwise, the higher level row will be unclickable.
  - The "Continue" button will help the user to start the current level game quickly.
  - Users allow replaying the low-level game by clicking the represented row.

- QuestionActivity:
  - At each level, there are 5 fixed questions. Each question includes 4 different multiple-choice questions.
  - For each question, player has 30 seconds to answer the question.
  - Clicking the "Pause" button
    - Pause the game anytime and then go back to the landing page or level select page.
    - Restart the current level
  - At the bottom of the page, the player can check the game progress, like 3/5 means the player is answering question 3 out of 5 in total.

- ResultActivity:
  - Count how many questions the player answers correctly
  - "Back to Level" button to go back to the Level Select Page.
  - "Retry" or "Next Level" button
    - When player fails the game (< 60%), display "Retry" button to restart the current level.
    - When player passes the game (>= 60%), display "Next Level" button to access the higher level.
  - Keep store the highest score in each level, and display in the Level Select Page.

## Getting Started

### Prerequisites

Downloading and Installing Android Studio
* [Android Studio](https://developer.android.com/studio) - Integrated Development Environment (IDE) for Android app development

## Built With

* Best Emulator Version: Pixel 6 API 28

## Sources Used

### Image

1. map.png \
    Freepik. (n.d.). Cartoon pirate treasure map game. \
    URL: https://www.freepik.com/premium-vector/cartoon-pirate-treasure-map-game_18372891.htm#query=game%20map&position=11&from_view=search&track=ais \
    Date Accessed: 8 Apr. 2023.

### Material

1. Used Android Studio basic activity templates as the starter for each activity.

2. STechies, SQLite Database Update in Android \
   URL: https://www.stechies.com/sqlite-database-update-android/ \
   Date Accessed: 8 Mar. 2023. \
   The code in DatabaseHelper was implemented and modified following this step-by-step instructions.

3. "SQLExample" (zip file) \
   SQLite Example code posted in CSCI 4176’s Brightspace (contents > Modules > Programming > "SQLExample"), 
   which is used for local persistence. \
   Date Accessed: 13 Mar. 2023.

4. CSCI 4176 Lab4 PDF \
   RecyclerView Example code posted in CSCI 4176 Lab4 - Creating a ListView and a RecyclerView in Android Application
   in Brightspace used for creating the LevelTable in LevelActivity and the LevelTableAdapter. \
   Date Accessed: 38 Mar. 2023.

5. CountDownTimer Tutorial With Example In Android Studio \
   URL: https://abhiandroid.com/ui/countdown-timer \
   Date Accessed: 5 Apr. 2023.

6. CountDownTimer in android - how to restart it \
   URL: https://stackoverflow.com/questions/19997588/countdowntimer-in-android-how-to-restart-it \
   Date Accessed: 9 Apr. 2023.

### Tools

1. Coolors: Color palette generator \
   URL: https://coolors.co/cc7700-296aa3-644536-417704-fafded-f7efde \
   Date Accessed: 1 Apr. 2023.

## Contributions:

**Shiwen(Lareina) Yang**
- Discuss and design the storyboards and wireframes.
- Main Activity Implementation
- Level Activity Implementation
- Helper in Question and Result Activity
- Refactoring

**Matthew Mailman**
- Discuss and design the storyboards and wireframes.
- Question Activity Implementation
- Result Activity Implementation
- Reviewer in Level Activity
- Project Presentation

**Cameron Chaffey**
- Discuss and design the storyboards and wireframes.
- Question Activity Implementation
- Result Activity Implementation
- Reviewer in Level Activity

**Lily Andruschak**
- Timer implementation in Question Activity
- Reviewer in other activities