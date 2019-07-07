package com.example.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import com.example.quizapp.QuizConstants.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuizApp";
    private static final int DATABASE_VERSION = 1;

    //reference to the actual database
    private SQLiteDatabase db;

    public QuizDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER + " INTEGER" +
                ")";

        //for executing SQL commands
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

        //to hold the various questions
        fillQuestionsTable();



    }
    //updates the database when changes are made
    @Override
    public void onUpgrade(SQLiteDatabase dp, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);

    }

    private void fillQuestionsTable(){
        Question q1 = new Question("Which of the following is not a way to store data in android?", "Memory managaer", "Firebase", "Shared Preferences", 1);
        addQuestion(q1);
        Question q2 = new Question("Which of the following is not a HHTP Client method", "Retrofit", "Postman", "OKHTTP", 2);
        addQuestion(q2);
        Question q3 = new Question("Which tool is used to pass data from one activity to another", "Toast", "Button", "Intent", 3);
        addQuestion(q3);
        Question q4 = new Question("Which of the following is not in the Android LifeCycle?", "onRepause()", "onResume()", "onRestart()", 1);
        addQuestion(q4);
        Question q5 = new Question("Which of the following is not an example of a context?", "Application Context", "Class Context", "Activity Context", 3);
        addQuestion(q5);
    }

    //to add a question to the database
    private void addQuestion(Question question){
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER, question.getAnswer());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);



    }

    public ArrayList<Question> getAllQuestions(){
        ArrayList<Question> questionList = new ArrayList<>();

        //to reference the database
        db = getReadableDatabase();

        //to query the database
        Cursor cursor = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        //if there is a query to the database
        if (cursor.moveToFirst()){
            do {

                Question question = new Question();
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswer(cursor.getInt(cursor.getColumnIndex(QuestionsTable.COLUMN_ANSWER)));

                questionList.add(question);


            }while (cursor.moveToNext());
        }

        cursor.close();
        return questionList;
    }
}
