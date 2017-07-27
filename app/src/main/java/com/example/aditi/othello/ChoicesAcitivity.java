package com.example.aditi.othello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ChoicesAcitivity extends AppCompatActivity {

    private static final int NEW_GAME = 1;
    private static final int SCORE = 2;
    private static final int HOW_TO_PLAY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choices);
    }

    public void Play(View view){
        Intent i = new Intent(this, OpeningActivity.class);
        startActivityForResult(i, NEW_GAME);
    }

    public void Scoreboard(View view)
    {
        Intent i = new Intent(this, ScoreBoard.class);
        startActivityForResult(i, SCORE);
    }

    public void howToPlay(View view)
    {
        Intent i = new Intent(this, HowToPlayAcitivty.class);
        startActivityForResult(i, HOW_TO_PLAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_GAME && resultCode == RESULT_CANCELED)
        {

        }
        else if (requestCode == SCORE && resultCode == RESULT_CANCELED)
        {

        }
        else if (requestCode == HOW_TO_PLAY && resultCode == RESULT_CANCELED)
        {

        }
    }
}
