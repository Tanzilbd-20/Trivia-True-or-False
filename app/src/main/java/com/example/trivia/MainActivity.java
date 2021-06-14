package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Questions;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String MESSAGE_ID = "message_prefs";
    private ActivityMainBinding binding;
    private int currentQuestion = 1;
    List<Questions> questions;
    private double total_score = 0.0;
    double high_score;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);





    questions = new Repository().getQuestion(questionsArrayList -> {

                binding.questionTextView.setText(questionsArrayList.get(currentQuestion).getAnswer());
                updateQuestionCounter();
                getHighestScore();
            }
      );

        binding.trueButton.setOnClickListener(view -> {
            checkAnswer(true);
            binding.trueButton.setEnabled(false);
            binding.falseButton.setEnabled(false);
            updateQuestion();


        });
        binding.falseButton.setOnClickListener(view -> {
            checkAnswer(false);
            binding.trueButton.setEnabled(false);
            binding.falseButton.setEnabled(false);
            updateQuestion();



        });

        binding.nextButton.setOnClickListener(view -> {
            currentQuestion = (currentQuestion +1)  %questions.size() ;
            binding.trueButton.setEnabled(true);
            binding.falseButton.setEnabled(true);
            updateQuestion();


        });

    }

    @SuppressLint("DefaultLocale")
    private void updateQuestionCounter() {
        binding.questionOutOf.setText(String.format("Question : %d/%d", currentQuestion, questions.size()));
    }

    private void updateQuestion() {
        String question = questions.get(currentQuestion).getAnswer();
        binding.questionTextView.setText(question);
        updateQuestionCounter();

    }

    @SuppressLint("DefaultLocale")
    private void checkAnswer(boolean userChoice) {

        boolean answerIs = questions.get(currentQuestion).isAnswerTrue();
        int snackMessageId = 0;

        if(userChoice == answerIs){
            snackMessageId = R.string.correct_answer;
            total_score++;
            correctScoreAnimation();
           alphaAnimation();
        }else{
            snackMessageId = R.string.incorrect_answer;
            if(total_score>0) {
                total_score = (total_score - 0.5);
            }else{
                total_score = 0.0;
            }
            wrongScoreAnimation();
           shakeAnimation();


        }

        setHighestScore();
        binding.totalScoreTextView.setText("Total Score\n"+ total_score);
        Snackbar.make(binding.cardView,snackMessageId,Snackbar.LENGTH_SHORT).show();

    }
    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
            binding.cardView.setAnimation(shake);
            shake.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    binding.questionTextView.setTextColor(Color.RED);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    binding.questionTextView.setTextColor(Color.WHITE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }
    private void alphaAnimation(){
        Animation alpha = AnimationUtils.loadAnimation(MainActivity.this, R.anim.alpha_animation);
        binding.cardView.setAnimation(alpha);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void correctScoreAnimation(){
        Animation scoreAnimation = AnimationUtils.loadAnimation(this,R.anim.alpha_score_animation);
        binding.totalScoreTextView.setAnimation(scoreAnimation);
        scoreAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.totalScoreTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.totalScoreTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void wrongScoreAnimation(){
        Animation scoreAnimation = AnimationUtils.loadAnimation(this,R.anim.alpha_score_animation);
        binding.totalScoreTextView.setAnimation(scoreAnimation);
        scoreAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.totalScoreTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.totalScoreTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    private void setHighestScore(){
        if(total_score > high_score){
            binding.highestScoreTextView.setText(String.format("New Highest Score : %s", total_score));
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("getScore", (float) total_score);
        editor.apply();
        }
    }
    private void getHighestScore(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        high_score = sharedPreferences.getFloat("getScore",0.0f);
        if(total_score >high_score) {
            binding.highestScoreTextView.setText(String.format("New Highest Score : %s", high_score));
        }else{
            binding.highestScoreTextView.setText(String.format("Highest Score : %s", high_score));
        }
    }



}