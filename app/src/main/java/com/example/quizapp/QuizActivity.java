package com.example.quizapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
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

    private ColorStateList texxtColorDefault;
    private int questionCounter;
    private int questionTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private List<Question> questionList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        texxtColorDefault = Button1.getTextColors();

        //to create the database
        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();

        questionTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();

        submitButtonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //not answered
                if (!answered){

                    if (Button1.isChecked() || Button2.isChecked() || Button3.isChecked() ){

                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please Select an Answer", Toast.LENGTH_SHORT).show();
                    }



                }
                //if question is answered
                else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        Button1.setTextColor(texxtColorDefault);
        Button2.setTextColor(texxtColorDefault);
        Button3.setTextColor(texxtColorDefault);

        radioGroupp.clearCheck();

        //if there are more questions
        if (questionCounter < questionTotal){

            currentQuestion = questionList.get(questionCounter);
            questionn.setText(currentQuestion.getQuestion());
            Button1.setText(currentQuestion.getOption1());
            Button2.setText(currentQuestion.getOption2());
            Button3.setText(currentQuestion.getOption3());

            questionCounter++;
            questionCountt.setText("Question: " + questionCounter + "/" + questionTotal);
            answered = false;
            submitButtonn.setText("Confirm");


        } else {
            finishQuiz();
        }
    }

    private void checkAnswer(){
        answered = true;

        //get selected radio button
        RadioButton selected = findViewById(radioGroupp.getCheckedRadioButtonId());
        int answer = radioGroupp.indexOfChild(selected) + 1;

        //correct answer
        if (answer == currentQuestion.getAnswer()){
            score += 5;
            scorre.setText("Score " + score);

        }

        showSolution();
    }

    private void showSolution(){
        Button1.setTextColor(Color.RED);
        Button2.setTextColor(Color.RED);
        Button3.setTextColor(Color.RED);

        switch (currentQuestion.getAnswer()){
            case 1:
                Button1.setTextColor(Color.GREEN);
                questionn.setText("Option 1 is the correct Answer");
                break;

            case 2:
                Button2.setTextColor(Color.GREEN);
                questionn.setText("Option 2 is the correct Answer");
                break;

            case 3:
                Button3.setTextColor(Color.GREEN);
                questionn.setText("Option 3 is the correct Answer");
                break;

        }
        //if there is another question
        if (questionCounter < questionTotal) {

            submitButtonn.setText("Next Question");

        } else {
            submitButtonn.setText("Finish Quiz");
        }
    }

    private void finishQuiz(){
        finish();
    }
}
