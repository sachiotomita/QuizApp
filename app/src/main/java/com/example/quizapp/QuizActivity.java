package com.example.quizapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {
    @BindView(R.id.score) TextView scorre;
    @BindView(R.id.questionCount) TextView questionCountt;
    @BindView(R.id.countdown) TextView countdownn;
    @BindView(R.id.question) TextView questionn;
    @BindView(R.id.radioGroup) RadioGroup radioGroupp;
    @BindView(R.id.button1) RadioButton Button1;
    @BindView(R.id.button2) RadioButton Button2;
    @BindView(R.id.button3) RadioButton Button3;
    @BindView(R.id.submitButton) Button submitButtonn;

    private List<Question> questionList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        //to create the database
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();
    }
}
