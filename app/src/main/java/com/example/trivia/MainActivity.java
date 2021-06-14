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
    private int correct_score = 0;
    private int wrong_score = 0;

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
            correct_score++;
            correctScoreAnimation();
            setHighestScore();

           alphaAnimation();
        }else{
            snackMessageId = R.string.incorrect_answer;
            wrong_score++;
            wrongScoreAnimation();
           shakeAnimation();

        }

        binding.correctAnswerTextView.setText(String.format("Correct Answer\n%d", correct_score));
        binding.wrongAnswerTextView.setText(String.format("Wrong Answer\n%d", wrong_score));
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
        binding.correctAnswerTextView.setAnimation(scoreAnimation);
    }
    private void wrongScoreAnimation(){
        Animation scoreAnimation = AnimationUtils.loadAnimation(this,R.anim.alpha_score_animation);
        binding.wrongAnswerTextView.setAnimation(scoreAnimation);
    }
    private void setHighestScore(){
        String highestScore = String.valueOf(correct_score);
        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("score",highestScore);
        editor.apply();
    }
    private void getHighestScore(){
        SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
       String score =  sharedPreferences.getString("score","Last Highest Score : 0");
       binding.highestScoreTextView.setText(String.format("Last Highest Score : %s", score));
    }




}