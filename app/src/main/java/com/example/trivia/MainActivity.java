package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Questions;
import com.example.trivia.score.GameStatAndHighScore;
import com.example.trivia.score.Score;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentQuestion = 1;
    List<Questions> questions;
    private int current_score = 0;
    private Score score;
    private int avoidSkipAndDoubleSelect = 0;
    private GameStatAndHighScore gameStatAndHighScore;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        score = new Score();

        //Retrieving game sates
        gameStatAndHighScore = new GameStatAndHighScore(MainActivity.this);
        currentQuestion = gameStatAndHighScore.getCurrentQuestion();
        current_score = gameStatAndHighScore.getCurrentScore();
        avoidSkipAndDoubleSelect = gameStatAndHighScore.getAvoidSkipAndDoubleSelect();





        //Getting our questions and answer from Repository.
    questions = new Repository().getQuestion(questionsArrayList -> {
                //setting current question to question text_view.
                binding.questionTextView.setText(questionsArrayList.get(currentQuestion).getAnswer());
                binding.questionOutOf.setText(String.format("Question : %d/%d", currentQuestion, questions.size()));
                binding.highestScoreTextView.setText(String.format("Highest Score : %d", gameStatAndHighScore.getHighScore()));
                binding.totalScoreTextView.setText(String.format("Current Score\n%d", current_score));
                //Calling new Question
                updateQuestionCounter();

            }
      );

        //setting true button onclick listener.
        binding.trueButton.setOnClickListener(view -> {
            //Putting user's answer.
            checkAnswer(true);

            updateQuestion();

        });
        //setting false button onclick listener.
        binding.falseButton.setOnClickListener(view -> {
            //Putting user's answer.
            checkAnswer(false);

            updateQuestion();

        });

        //setting next button onclick listener.
        binding.nextButton.setOnClickListener(view -> {
            //Incrementing current question value,
            if(avoidSkipAndDoubleSelect == 1){
                avoidSkipAndDoubleSelect--;

                currentQuestion = (currentQuestion +1)  %questions.size() ;
                //Enabling click listener of true and false button;
                binding.trueButton.setEnabled(true);
                binding.falseButton.setEnabled(true);
                //Calling new Question
                updateQuestion();
            }else {
                Snackbar.make(binding.falseButton,"Please Select Your Answer",Snackbar.LENGTH_SHORT).show();
                avoidSkipAndDoubleSelect = 0;

            }

            gameStatAndHighScore.saveAvoidSkipAndDoubleSelect(avoidSkipAndDoubleSelect);
            



        });
    }

    @SuppressLint("DefaultLocale")
    //setting current question of total questions,
    private void updateQuestionCounter() {
        binding.questionOutOf.setText(String.format("Question : %d/%d", currentQuestion, questions.size()));
    }

    //updating new question
    private void updateQuestion() {
        String question = questions.get(currentQuestion).getAnswer();
        binding.questionTextView.setText(question);
        updateQuestionCounter();
    }

    @SuppressLint("DefaultLocale")
    //Checking answer correct or false.
    private void checkAnswer(boolean userChoice) {

        if(avoidSkipAndDoubleSelect == 0){
            avoidSkipAndDoubleSelect++;
        boolean answerIs = questions.get(currentQuestion).isAnswerTrue();
        int snackMessageId ;

        if(userChoice == answerIs){
            snackMessageId = R.string.correct_answer;
            //incrementing score +100 for true answer,
            addPoint();
            //Calling animation for wrong answer.
            correctScoreAnimation();
            alphaAnimation();
        }else{
            snackMessageId = R.string.incorrect_answer;
            //decrementing score -50 for false answer,
            deductPoint();
            //Calling animation for wrong answer.
            wrongScoreAnimation();
            shakeAnimation();

        }


        //saving game states
        gameStatAndHighScore.savedHighScore(score.getScore());
        gameStatAndHighScore.saveCurrentQuestion(currentQuestion);
        gameStatAndHighScore.saveCurrentScore(current_score);
        //setting the new high score once it break prev high score
            binding.highestScoreTextView.setText(String.format("Highest Score : %d", gameStatAndHighScore.getHighScore()));
       

        //Setting total score,
        binding.totalScoreTextView.setText(String.format("Current Score\n%d", current_score));
        //making snack bar based on answer
        Snackbar.make(binding.cardView,snackMessageId,Snackbar.LENGTH_SHORT).show();

        }else {
            //It will now allow users to select answer multiple times.
            //and tells users to go to the next question
            Snackbar.make(binding.falseButton,"Please Select Next Question",Snackbar.LENGTH_SHORT).show();
            avoidSkipAndDoubleSelect = 1;

        }
        gameStatAndHighScore.saveAvoidSkipAndDoubleSelect(avoidSkipAndDoubleSelect);

    }
    //Setting animation for question text_view of Incorrect Answer,
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
    //Setting animation for question text_view of Correct Answer,
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
    //Setting animation for score text_view of Correct Answer,
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
    //Setting animation for score text_view of Wrong Answer,
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

    //Implemented method to increase score each time for correct answer.
    private void addPoint(){
        current_score += 100;
        score.setScore(current_score);
    }
    //Implemented logic method to avoid negative score.
    //and deduct score for wrong answer.
    private void deductPoint(){
        if(current_score >0){
            current_score -=50;
        }else{
            current_score = 0;

        }
        //setting current score
        score.setScore(current_score);
    }

    //Saving SharedPreferences in onPause().
    @Override
    protected void onPause() {
        gameStatAndHighScore.savedHighScore(score.getScore());
        gameStatAndHighScore.saveCurrentQuestion(currentQuestion);
        gameStatAndHighScore.saveCurrentScore(current_score);
        gameStatAndHighScore.saveAvoidSkipAndDoubleSelect(avoidSkipAndDoubleSelect);
        super.onPause();
    }
}