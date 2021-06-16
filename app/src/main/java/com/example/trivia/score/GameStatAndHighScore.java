package com.example.trivia.score;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;



public class GameStatAndHighScore {
    private static final String HIGHEST_SCORE = "highest_score";
    public static final String CURRENT_SCORE = "current_score";
    public static final String CURRENT_QUESTION = "current_question";
    public static final String AVOID_SKIP_AND_DOUBLE_SELECT = "avoid_skip_and_double_select";
    private final SharedPreferences preferences;

 public GameStatAndHighScore(Activity context){
     this.preferences = context.getPreferences(Context.MODE_PRIVATE);
 }
 public void savedHighScore(int score){
     int last_score = preferences.getInt(HIGHEST_SCORE,0);
     if(score>last_score){
         preferences.edit().putInt(HIGHEST_SCORE,score).apply();
     }
 }
 public int getHighScore(){
     return preferences.getInt(HIGHEST_SCORE,0);
 }

 public void saveCurrentScore(int current_score){
     preferences.edit().putInt(CURRENT_SCORE,current_score).apply();
 }
 public int getCurrentScore(){
     return preferences.getInt(CURRENT_SCORE,0);
 }
 public void saveCurrentQuestion(int current_question){
     preferences.edit().putInt(CURRENT_QUESTION,current_question).apply();
 }
 public int getCurrentQuestion(){
     return preferences.getInt(CURRENT_QUESTION,1);
 }
 public void saveAvoidSkipAndDoubleSelect(int number){
     preferences.edit().putInt(AVOID_SKIP_AND_DOUBLE_SELECT,number).apply();
 }
 public int getAvoidSkipAndDoubleSelect(){
     return preferences.getInt(AVOID_SKIP_AND_DOUBLE_SELECT,0);
 }
}
