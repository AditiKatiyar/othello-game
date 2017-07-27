package com.example.aditi.othello;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScoreBoard extends AppCompatActivity {

    LinearLayout subLayout1, subLayout2, subLayout3;
    TextView[][] t;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        // displaying scoreboard
        subLayout1 = (LinearLayout) findViewById(R.id.subLayout1);
        subLayout2 = (LinearLayout) findViewById(R.id.subLayout2);
        subLayout3 = (LinearLayout) findViewById(R.id.subLayout3);

        ScoreBoardOpenHelper scoreBoardOpenHelper = ScoreBoardOpenHelper.getScoreBoardOpenHelperInstance(this);
        SQLiteDatabase database = scoreBoardOpenHelper.getReadableDatabase();
        Cursor cursor = database.query(ScoreBoardOpenHelper.TABLE_NAME, null, null, null, null, null, null);
        long cnt  = DatabaseUtils.queryNumEntries(database, ScoreBoardOpenHelper.TABLE_NAME);

        if (cursor != null)
        {
            t = new TextView[(int) cnt][3];
            int i = 0;
            int player1NameIndex = cursor.getColumnIndex(ScoreBoardOpenHelper.PLAYER1_NAME);
            int player2NameIndex = cursor.getColumnIndex(ScoreBoardOpenHelper.PLAYER2_NAME);
            int player1ScoreIndex = cursor.getColumnIndex(ScoreBoardOpenHelper.PLAYER1_SCORE);
            int player2ScoreIndex = cursor.getColumnIndex(ScoreBoardOpenHelper.PLAYER2_SCORE);
            int winnerIndex = cursor.getColumnIndex(ScoreBoardOpenHelper.WINNER);

            while (cursor.moveToNext())
            {
                String player1Name = cursor.getString(player1NameIndex);
                String player2Name = cursor.getString(player2NameIndex);
                int player1Score = cursor.getInt(player1ScoreIndex);
                int player2Score = cursor.getInt(player2ScoreIndex);
                String winner = cursor.getString(winnerIndex);
                t[i][0] = new TextView(this, null, 0, R.style.textViewTheme);
                t[i][1] = new TextView(this, null, 0, R.style.textViewTheme);
                t[i][2] = new TextView(this, null, 0, R.style.textViewTheme);
                t[i][0].setText(player1Name+ " (" + player1Score + ")");
                t[i][1].setText(player2Name+ " (" + player2Score + ")");
                t[i][2].setText(winner);
                subLayout1.addView(t[i][0]);
                subLayout2.addView(t[i][1]);
                subLayout3.addView(t[i][2]);
                i++;
            }
        }
    }

    public void Home(View view){
        Intent i = getIntent();
        if (i.getBooleanExtra("flag", false))
        {
            Intent intent = new Intent(ScoreBoard.this, ChoicesAcitivity.class);
            startActivity(intent);
        }
        else
        {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        if (i.getBooleanExtra("flag", false))
        {
            Intent intent = new Intent(ScoreBoard.this, ChoicesAcitivity.class);
            startActivity(intent);
        }
        else
        {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
