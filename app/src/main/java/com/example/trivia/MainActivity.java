package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Questions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentQuestion = 0;
    List<Questions> questions;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    questions = new Repository().getQuestion(questionsArrayList -> {

                binding.questionTextView.setText(questionsArrayList.get(currentQuestion).getAnswer());
                updateQuestionCounter();

            }
      );

        binding.trueButton.setOnClickListener(view -> {
            checkAnswer(true);
            updateQuestion();

        });
        binding.falseButton.setOnClickListener(view -> {
            checkAnswer(false);
            updateQuestion();


        });

        binding.nextButton.setOnClickListener(view -> {
            currentQuestion = (currentQuestion +1)  %questions.size() ;

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

    private void checkAnswer(boolean userChoice) {

        boolean answerIs = questions.get(currentQuestion).isAnswerTrue();
        int snackMessageId = 0;

        if(userChoice == answerIs){
            snackMessageId = R.string.correct_answer;

           alphaAnimation();
        }else{
            snackMessageId = R.string.incorrect_answer;
           shakeAnimation();



        }
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
    private void translatedAnimation(){
        Animation translated = AnimationUtils.loadAnimation(this,R.anim.translated_animation);
        binding.cardView.setAnimation(translated);
    }

}