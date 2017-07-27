package com.example.aditi.othello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private static final int PLAY_GAME = 1;
    EditText player1, player2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent resultIntent = getIntent();
        if (resultIntent.getIntExtra(IntentConstants.NUMBER_OF_PLAYERS, 0) == IntentConstants.ONE_PLAYER)
        {
            setContentView(R.layout.activity_start_computer);
            player1 = (EditText) findViewById(R.id.et1);
            Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name1 = player1.getText().toString();

                    if(name1.isEmpty())
                    {
                        Toast.makeText(StartActivity.this, "Enter Player1", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent i = new Intent(StartActivity.this, MainActivity.class);
                    i.putExtra(IntentConstants.PLAYER_1_NAME, name1);
                    i.putExtra(IntentConstants.PLAYER_2_NAME, "Computer");
                    i.putExtra(IntentConstants.WITH_COMPUTER, IntentConstants.YES);
                    startActivityForResult(i, PLAY_GAME);
                }
            });
        }
        else
        {
            setContentView(R.layout.activity_start);
            player1 = (EditText) findViewById(R.id.et1);
            player2 = (EditText) findViewById(R.id.et2);
            Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name1 = player1.getText().toString();
                    String name2 = player2.getText().toString();

                    if(name1.isEmpty())
                    {
                        Toast.makeText(StartActivity.this, "Enter Player1", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if(name2.isEmpty())
                    {
                        Toast.makeText(StartActivity.this, "Enter Player2", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent i = new Intent(StartActivity.this, MainActivity.class);
                    i.putExtra(IntentConstants.PLAYER_1_NAME, name1);
                    i.putExtra(IntentConstants.PLAYER_2_NAME, name2);
                    i.putExtra(IntentConstants.WITH_COMPUTER, IntentConstants.NO);
                    startActivityForResult(i, PLAY_GAME);
                }
            });
        }



    }

    /*public void Play(View v)
    {
        String name1 = player1.getText().toString();
        String name2 = player2.getText().toString();

        if(name1.isEmpty())
        {
            Toast.makeText(this, "Enter Player1", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(name2.isEmpty())
        {
            Toast.makeText(this, "Enter Player2", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("player1Name", name1);
        i.putExtra("player2Name", name2);
        startActivityForResult(i, PLAY_GAME);
    }*/

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLAY_GAME && resultCode == RESULT_CANCELED)
        {
            player1.setText("");
            player2.setText("");
        }
    }
}
