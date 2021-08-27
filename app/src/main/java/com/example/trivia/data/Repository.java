package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Questions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//This Respository class is for collecting all the data in ArrayList.
public class Repository {

    ArrayList<Questions> questionsArrayList = new ArrayList<>();
    String questionLink ="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Questions> getQuestion(final AnswerListAsyncResponse callBack){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, questionLink, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            Questions question = new Questions(response.getJSONArray(i).getString(0),response.getJSONArray(i).getBoolean(1));

                            //Add Question to ArrayList/List
                            questionsArrayList.add(question);
                            

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(null != callBack) callBack.processFinished(questionsArrayList);

        }, error ->

                Log.d("Questions", "onResponse: failed to get info "));

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionsArrayList;
    }






}
