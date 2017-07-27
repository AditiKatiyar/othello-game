package com.example.aditi.othello;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aditi on 7/3/2017.
 */

public class ScoreBoardOpenHelper extends SQLiteOpenHelper {

    public final static String TABLE_NAME = "ScoreBoard";
    public final static String PLAYER1_NAME = "player1";
    public final static String PLAYER2_NAME = "player2";
    public final static String PLAYER1_SCORE = "player1Score";
    public final static String PLAYER2_SCORE = "player2Score";
    public final static String WINNER = "winner";
    public static ScoreBoardOpenHelper scoreBoardOpenHelper;

    public static ScoreBoardOpenHelper getScoreBoardOpenHelperInstance(Context context)
    {
        if (scoreBoardOpenHelper == null)
            scoreBoardOpenHelper = new ScoreBoardOpenHelper(context);
        return scoreBoardOpenHelper;
    }

    private ScoreBoardOpenHelper(Context context) {
        super(context, "Scoreboard.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "create table " + TABLE_NAME + " ( " + PLAYER1_NAME + " text, " + PLAYER1_SCORE + " integer, " + PLAYER2_NAME +
                " text, " + PLAYER2_SCORE + " integer, " + WINNER + " text );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
