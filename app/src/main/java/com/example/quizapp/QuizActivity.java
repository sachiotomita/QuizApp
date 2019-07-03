package com.example.quizapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private static final long COUNTDOWN_TIME = 30000;

    //values to save when screen is rotated
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_TIME_LEFT = "keyTimeLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";


    @BindView(R.id.score) TextView scorre;
    @BindView(R.id.questionCount) TextView questionCountt;
    @BindView(R.id.countdown) TextView countdownn;
    @BindView(R.id.question) TextView questionn;
    @BindView(R.id.radioGroup) RadioGroup radioGroupp;
    @BindView(R.id.button1) RadioButton Button1;
    @BindView(R.id.button2) RadioButton Button2;
    @BindView(R.id.button3) RadioButton Button3;
    @BindView(R.id.submitButton) Button finish;

    private ColorStateList texxtColorDefault;
    private ColorStateList textColorDefaultcd;

    private CountDownTimer countDownTimer;
    private long timeLeft;

    private int questionCounter;
    private int questionTotal;
    private Question currentQuestion;

    private int Score;
    private boolean answered;

    private ArrayList<Question> questionList;

    private long backPressedTime;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);

        texxtColorDefault = Button1.getTextColors();
        textColorDefaultcd = countdownn.getTextColors();

        //if instance is not saved ie. screen is not rotated
        if (savedInstanceState == null) {

            //to create the database
            QuizDbHelper dbHelper = new QuizDbHelper(this);
            questionList = dbHelper.getAllQuestions();

            questionTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();

        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            Score  = savedInstanceState.getInt(KEY_SCORE);
            timeLeft = savedInstanceState.getLong(KEY_TIME_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            //if question is not answered start countdown
            if (!answered) {
                startCountdown();

                //to regain the current color of countdown and radiobuttons
            } else {
                updateCountDownText();
                showSolution();
            }
        }

        finish.setOnClickListener(new View.OnClickListener() {
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
            finish.setText("Confirm");


            //to start the countdown
            timeLeft = COUNTDOWN_TIME;
            startCountdown();


        } else {
            finishQuiz();
        }
    }

    private void startCountdown(){
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateCountDownText();
                checkAnswer();

            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        countdownn.setText(timeFormatted);

        //when countdown gets to below 10
        if (timeLeft < 10000) {
            countdownn.setTextColor(Color.RED);
        } else {
            countdownn.setTextColor(textColorDefaultcd);
        }
    }

    private void checkAnswer(){
        answered = true;

        countDownTimer.cancel();

        //get selected radio button
        RadioButton selected = findViewById(radioGroupp.getCheckedRadioButtonId());
        int answer = radioGroupp.indexOfChild(selected) + 1;

        //correct answer
        if (answer == currentQuestion.getAnswer()){
            Score += 5;
            scorre.setText("Score " + Score);

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

            finish.setText("Next Question");

        } else {
            finish.setText("Finish Quiz");
        }
    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, Score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }


    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            finishQuiz();

        }else {
            Toast.makeText(this, "Press back again to Exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    //stop countdown once cancelled

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    //on screen rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SCORE, Score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_TIME_LEFT, timeLeft);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);

    }
}
