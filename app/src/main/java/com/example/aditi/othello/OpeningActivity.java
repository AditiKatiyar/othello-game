package com.example.aditi.othello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OpeningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
    }

    public void goToStartActivity(View view)
    {
        if (view.getId() == R.id.onePlayerGame)
        {
            Intent intent = new Intent(this, StartActivity.class);
            intent.putExtra(IntentConstants.NUMBER_OF_PLAYERS, IntentConstants.ONE_PLAYER);
            startActivity(intent);
        }
        else if (view.getId() == R.id.twoPlayerGame)
        {
            Intent intent = new Intent(this, StartActivity.class);
            intent.putExtra(IntentConstants.NUMBER_OF_PLAYERS, IntentConstants.TWO_PLAYER);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
