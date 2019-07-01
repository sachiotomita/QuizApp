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
    @BindView(R.id.score) TextView score;
    @BindView(R.id.questionCount) TextView questionCount;
    @BindView(R.id.countdown) TextView countdown;
    @BindView(R.id.question) TextView question;
    @BindView(R.id.radioGroup) RadioGroup radioGroup;
    @BindView(R.id.button1) RadioButton button1;
    @BindView(R.id.button2) RadioButton button2;
    @BindView(R.id.button3) RadioButton button3;
    @BindView(R.id.submitButton) Button submitButton;

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
