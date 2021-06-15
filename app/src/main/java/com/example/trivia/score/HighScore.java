package com.example.trivia.score;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;



public class HighScore {
    private static final String HIGHEST_SCORE = "highest_score";
  private SharedPreferences preferences;

 public HighScore(Activity context){
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

}
