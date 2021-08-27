package com.example.trivia.data;

import com.example.trivia.model.Questions;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {

    void processFinished(ArrayList<Questions> questionsArrayList);

}
