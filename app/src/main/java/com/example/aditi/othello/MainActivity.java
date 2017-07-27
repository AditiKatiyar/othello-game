package com.example.aditi.othello;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.aditi.othello.R.color.black;
import static com.example.aditi.othello.R.color.yellow;
import static com.example.aditi.othello.R.drawable.button_image;

public class MainActivity extends AppCompatActivity  {

    public final static int NO_PLAYER = 0;
    public final static int PLAYER_1 = 1;
    public final static int PLAYER_2 = 2;
    public final static int INCOMPLETE = 0;
    public final static int PLAYER_1_WINS = 1;
    public final static int PLAYER_2_WINS = 2;
    public final static int DRAW = 3;
    int player1Discs = 0;
    int player2Discs = 0;
    public final static int[][] direction = {{0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}};
    boolean gameOver = false;
    boolean hintPressed = false;
    int CURRENT_PLAYER = PLAYER_1;
    String player1Name, player2Name;

    LinearLayout mainLayout, layout1, layout2, layout3, row1, row2;
    LinearLayout[] rowLayout;
    MyButton[][] button;
    MyButton button1, button2;
    Button exit;
    TextView text1, text2, player1Text, player2Text;
    int n = 8;

    HashMap<String, Boolean> map;
    ArrayList<Integer> hint;
    int computerHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        setUpBoard();
    }

    private void setUpBoard() {

        mainLayout.removeAllViews();

        //mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.brown));

        layout1 = new LinearLayout(this);   // creating layout1
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 250);
        params.setMargins(10, 10, 10, 10);
        layout1.setLayoutParams(params);
        layout1.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(layout1);

        row1 = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
        params.setMargins(1,1,1,1);
        row1.setLayoutParams(params);
        row1.setOrientation(LinearLayout.HORIZONTAL);
        row1.setPadding(50, 20, 10, 10);
        layout1.addView(row1);

        button1 = new MyButton(this); // adding to layout1
        button1.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_white));
        button1.setMinimumWidth(80);
        button1.setMinimumHeight(80);
        row1.addView(button1);

        text1 = new TextView(this);
        text1.setMinimumHeight(80);
        text1.setMinimumWidth(100);
        text1.setTextSize(30f);
        text1.setTextColor(ContextCompat.getColor(this, yellow));
        text1.setText(""+2);
        text1.setPadding(15, 0, 0, 0);
        row1.addView(text1);

        player1Text = new TextView(this);
        player1Text.setMinimumHeight(80);
        player1Text.setMinimumWidth(300);
        player1Text.setTextSize(30f);
        player1Text.setTextColor(ContextCompat.getColor(this, yellow));
        player1Text.setText("");
        player1Text.setPadding(15, 0, 0, 0);
        row1.addView(player1Text);

        row2 = new LinearLayout(this);
        row2.setLayoutParams(params);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setPadding(50, 20, 10, 10);
        layout1.addView(row2);

        button2 = new MyButton(this); // adding to layout1
        button2.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_black));
        button2.setMinimumHeight(80);
        button2.setMinimumWidth(80);
        row2.addView(button2);

        text2 = new TextView(this);
        text2.setMinimumHeight(80);
        text2.setMinimumWidth(100);
        text2.setTextSize(30f);
        text2.setTextColor(ContextCompat.getColor(this, yellow));
        text2.setText(""+2);
        text2.setPadding(15, 0, 0 ,0);
        row2.addView(text2);

        player2Text = new TextView(this);
        player2Text.setMinimumHeight(80);
        player2Text.setMinimumWidth(300);
        player2Text.setTextSize(30f);
        player2Text.setTextColor(ContextCompat.getColor(this, yellow));
        player2Text.setText("");
        player2Text.setPadding(15, 0, 0, 0);
        row2.addView(player2Text);

        // obtaining intent
        Intent intent = getIntent();
        player1Name = intent.getStringExtra(IntentConstants.PLAYER_1_NAME);
        player1Text.setText(player1Name);
        player2Name = intent.getStringExtra(IntentConstants.PLAYER_2_NAME);
        player2Text.setText(player2Name);
        boolean withComputer = intent.getBooleanExtra(IntentConstants.WITH_COMPUTER, false);

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        layout2 = new LinearLayout(this); // creating layout2
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(width-15, width-15);
        params2.setMargins(10, 10, 10, 10);
        layout2.setLayoutParams(params2);
        layout2.setOrientation(LinearLayout.VERTICAL);
        layout2.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        mainLayout.addView(layout2);

        button = new MyButton[n][n];
        rowLayout = new LinearLayout[n];
        map = new HashMap<>();

        for(int i=0 ; i<n ; i++)
        {
            rowLayout[i] = new LinearLayout(this);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f);
            p.setMargins(1, 1, 1, 0);
            rowLayout[i].setLayoutParams(p);
            rowLayout[i].setOrientation(LinearLayout.HORIZONTAL);
            layout2.addView(rowLayout[i]);
        }

        // setting buttons
        for(int i=0 ; i<n ; i++)
        {
            for(int j=0 ; j<n ; j++)
            {
                button[i][j] = new MyButton(this);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
                p.setMargins(1,1,1,1);
                button[i][j].setLayoutParams(p);
                button[i][j].setBackground(ContextCompat.getDrawable(this, R.drawable.no_player));
                button[i][j].X = i;
                button[i][j].Y = j;
                if (withComputer)
                {
                    button[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            withComputeronClick(v);
                        }
                    });
                }
                else
                {
                    button[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            twoPlayerOnclick(v);
                        }
                    });
                }
                //button[i][j].setOnClickListener(this);
                map.put(""+i+j, true);
                rowLayout[i].addView(button[i][j]);
            }
        }

        layout3 = new LinearLayout(this);
        layout3.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        layout3.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        layout3.setPadding(0, 6, 0, 0);
        mainLayout.addView(layout3);

        int m = n/2;
        button[m][m].setBackground(ContextCompat.getDrawable(this, R.drawable.player1));
        button[m][m].player = PLAYER_1;
        button[m][m].setClickable(false);
        map.remove(""+m+m);
        button[m-1][m].setBackground(ContextCompat.getDrawable(this, R.drawable.player2));
        button[m-1][m].player = PLAYER_2;
        button[m-1][m].setClickable(false);
        map.remove(""+(m-1)+m);
        button[m][m-1].setBackground(ContextCompat.getDrawable(this, R.drawable.player2));
        button[m][m-1].player = PLAYER_2;
        button[m][m-1].setClickable(false);
        map.remove(""+m+(m-1));
        button[m-1][m-1].setBackground(ContextCompat.getDrawable(this, R.drawable.player1));
        button[m-1][m-1].player = PLAYER_1;
        button[m-1][m-1].setClickable(false);
        map.remove(""+(m-1)+(m-1));

        gameOver = false;
        CURRENT_PLAYER = PLAYER_1;
        player1Discs = player2Discs = 2;
        row1.setBackgroundResource(R.drawable.button_image);

        hint = new ArrayList<>();
        hint.add(24);
        hint.add(42);
    }


    public void twoPlayerOnclick(View v) {

        if(gameOver)
        {
            return;
        }

        if (hintPressed)
        {
            hintPressed = false;
            for (Integer k : hint)
            {
                int x = k/10;
                int y = k%10;
                button[x][y].setBackgroundResource(R.drawable.no_player);
            }
        }

        MyButton b = (MyButton) v;

        int x = b.X;
        int y = b.Y;
        boolean flag = false;

        for(int i=0 ; i<8 ; i++)
        {
            int dX = x+direction[i][0];
            int dY = y+direction[i][1];
            if(dX >= 0 && dX < n &&  dY >= 0 && dY < n && button[dX][dY].player != NO_PLAYER) // checking limits and NO_PLAYER
            {
                if(button[dX][dY].player == CURRENT_PLAYER)
                {
                    continue;
                }
                else {                  // when the button belongs to the opponent
                    boolean moveValidity = checkMoveValidity(dX, dY, i, CURRENT_PLAYER);
                    if(moveValidity) {
                        flag = true;    // match is found
                        int discs = invertDiscs(dX, dY, i, CURRENT_PLAYER);
                        if(CURRENT_PLAYER == PLAYER_1)
                        {
                            player1Discs += discs;
                            player2Discs -= discs;
                        }
                        else if(CURRENT_PLAYER == PLAYER_2)
                        {
                            player2Discs += discs;
                            player1Discs -= discs;
                        }
                        b.setClickable(false);
                    }
                }
            }
        }

        if(!flag)       // if no disc in any direction
        {
            Toast.makeText(this, "Invalid Move!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(CURRENT_PLAYER == PLAYER_1)
        {
            b.setBackground(ContextCompat.getDrawable(this, R.drawable.player1));
            b.player = PLAYER_1;
            b.setClickable(false);
            player1Discs++;
            map.remove(""+x+y);
        }
        else if(CURRENT_PLAYER == PLAYER_2)
        {
            b.setBackground(ContextCompat.getDrawable(this, R.drawable.player2));
            b.player = PLAYER_2;
            b.setClickable(false);
            player2Discs++;
            map.remove(""+x+y);
        }

        text1.setText(""+player1Discs);
        text2.setText(""+player2Discs);

        int status = checkGameStatus();
        boolean statusHandled = statusActionHandler(status);
        if (statusHandled)
            return;

        // check whether the this player will be able to do a valid move or not
        if (CURRENT_PLAYER == PLAYER_1)
        {
            Pair<Boolean, ArrayList<Integer>> ans = checkPlayerTurnValidity(PLAYER_2);
            if (ans.first == false)     // PLAYER_2 cannot move
            {
                ans = checkPlayerTurnValidity(PLAYER_1);
                if (ans.first == false) // PLAYER_1 also cannot move
                {
                    gameOver = true;
                    int Status = checkGameStatus();
                    statusActionHandler(Status);
                    Log.i("not 2 not 1", ""+Status);
                    return;
                }
                else                    // PLAYER_1 can move
                {
                    row1.setBackgroundResource(R.drawable.button_image);
                    row2.setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
                    CURRENT_PLAYER = PLAYER_1;
                    Toast.makeText(this, "Player 1 move again", Toast.LENGTH_SHORT);
                    hint = ans.second;
                    Log.i("player 1 move again", "");
                }
            }
            else
            {
                row2.setBackgroundResource(R.drawable.button_image);
                row1.setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
                CURRENT_PLAYER = PLAYER_2;
                hint = ans.second;
                Log.i("turn switched", "");
            }
        }
        else if (CURRENT_PLAYER == PLAYER_2)
        {

            Pair<Boolean, ArrayList<Integer>> ans = checkPlayerTurnValidity(PLAYER_1);
            if (ans.first == false)     // PLAYER_1 cannot move
            {
                ans = checkPlayerTurnValidity(PLAYER_2);
                if (ans.first == false) // PLAYER_2 also cannot move
                {
                    gameOver = true;
                    int Status = checkGameStatus();
                    statusActionHandler(Status);
                    Log.i("not 1 not 2", ""+Status);
                    return;
                }
                else
                {
                    row2.setBackgroundResource(R.drawable.button_image);
                    row1.setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
                    CURRENT_PLAYER = PLAYER_2;
                    Toast.makeText(this, "Player 2move again", Toast.LENGTH_SHORT);
                    hint = ans.second;
                    Log.i("player 2 move again", "");
                }
            }
            else
            {
                row1.setBackgroundResource(R.drawable.button_image);
                row2.setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
                CURRENT_PLAYER = PLAYER_1;
                hint = ans.second;
                Log.i("turn switched", "");
            }
        }
    }

    private boolean statusActionHandler(int status) {
        if(status == DRAW)
        {
            Toast.makeText(this, "Draw", Toast.LENGTH_SHORT).show();
            Exit("DRAW");
            return true;
        }
        else if(status == PLAYER_1_WINS)
        {
            Toast.makeText(this, "Player 1 Wins", Toast.LENGTH_SHORT).show();
            Exit(player1Name);
            return true;
        }
        else if(status == PLAYER_2_WINS)
        {
            Toast.makeText(this, "Player 2 Wins", Toast.LENGTH_SHORT).show();
            Exit(player2Name);
            return true;
        }
        return false;
    }

    private Pair<Boolean, ArrayList<Integer>> checkPlayerTurnValidity(int currentPlayer)
    {
        boolean flag = false;
            int x;
            int y;
            ArrayList<Integer> list = new ArrayList<>();
            for(String key : map.keySet())
            {
                int k = Integer.parseInt(key.toString());
                y = k%10;
                x = k/10;
                for(int i=0 ; i<8 ; i++)
                {
                    int dX = x+direction[i][0];
                    int dY = y+direction[i][1];
                    if(dX >= 0 && dX < n &&  dY >= 0 && dY < n && button[dX][dY].player != NO_PLAYER) // checking limits and NO_PLAYER
                    {
                        if(button[dX][dY].player == currentPlayer)
                        {
                            continue;
                        }
                        else {                  // when the button belongs to the opponent
                            boolean moveValidity = checkMoveValidity(dX, dY, i, currentPlayer);
                            if(moveValidity) {
                                flag = true;    // atleast one valid move is possible
                                list.add(k);
                            }
                        }
                    }
                }
            }

            if (flag)
            {
            return (new Pair<>(true, list));
        }
        else
        {
            return (new Pair<>(false, list));
        }
    }

    private void Exit(final String name) {
        exit = new Button(this);
        exit.setBackgroundResource(R.drawable.button_image);
        exit.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        exit.setTextSize(20f);
        exit.setText("EXIT");
        exit.setTextColor(ContextCompat.getColor(this, R.color.yellow));
        layout3.addView(exit);
        gameOver = true;
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScoreBoard.class);
                ScoreBoardOpenHelper scoreBoardOpenHelper = ScoreBoardOpenHelper.getScoreBoardOpenHelperInstance(MainActivity.this);
                SQLiteDatabase database = scoreBoardOpenHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(ScoreBoardOpenHelper.PLAYER1_NAME, player1Name);
                cv.put(ScoreBoardOpenHelper.PLAYER1_SCORE, player1Discs);
                cv.put(ScoreBoardOpenHelper.PLAYER2_NAME, player2Name);
                cv.put(ScoreBoardOpenHelper.PLAYER2_SCORE, player2Discs);
                cv.put(ScoreBoardOpenHelper.WINNER, name);
                database.insert(ScoreBoardOpenHelper.TABLE_NAME, null, cv);
                intent.putExtra("flag", true);
                startActivity(intent);
            }
        });
    }

    private int checkGameStatus() {

        if(map.size() == 0 || gameOver == true)
        {
            if(player1Discs == player2Discs)
                return DRAW;

            if(player1Discs > player2Discs)
                return PLAYER_1_WINS;

            return PLAYER_2_WINS;
        }

        return INCOMPLETE;
    }

    private int invertDiscs(int dX, int dY, int i, int currentPlayer) {        //
        int OPPONENT = 0;
        int image_opponent = 0;
        if(currentPlayer == PLAYER_1) {
            OPPONENT = PLAYER_2;
            image_opponent = R.drawable.player1;
        }
        else if(currentPlayer == PLAYER_2) {
            OPPONENT = PLAYER_1;
            image_opponent = R.drawable.player2;
        }


        int k, j, discs = 0;
        switch (i)
        {
            case 0:
                for(k=dY ; k>=0 ; k--)
                {
                    if(button[dX][k].player == OPPONENT)
                    {
                        button[dX][k].setBackground(ContextCompat.getDrawable(this, image_opponent));
                        button[dX][k].player = currentPlayer;
                        discs++;
                    }
                    else
                    {
                        break;
                    }
                }
                return discs;

            case 1:
                for(j=dX, k=dY ; k>=0 && j>=0 ; k--, j--)
                {
                    if(button[j][k].player ==OPPONENT)
                    {
                        button[j][k].setBackground(ContextCompat.getDrawable(this, image_opponent));
                        button[j][k].player = currentPlayer;
                        discs++;
                    }
                    else
                    {
                        break;
                    }
                }
                return discs;

            case 2:
                for(j=dX ; j>=0 ; j--)
                {
                    if(button[j][dY].player == OPPONENT)
                    {
                        button[j][dY].setBackground(ContextCompat.getDrawable(this, image_opponent));
                        button[j][dY].player = currentPlayer;
                        discs++;
                    }
                    else
                    {
                        break;
                    }
                }
                return discs;

            case 3:
                for(j=dX, k=dY ; j>=0 && k<n ; j--, k++)
                {
                    if(button[j][k].player == OPPONENT)
                    {
                        button[j][k].setBackground(ContextCompat.getDrawable(this, image_opponent));
                        button[j][k].player = currentPlayer;
                        discs++;
                    }
                    else
                    {
                        break;
                    }
                }
                return discs;

            case 4:
                for(k=dY ; k<n ; k++)
                {
                    if(button[dX][k].player == OPPONENT)
                    {
                        button[dX][k].setBackground(ContextCompat.getDrawable(this, image_opponent));
                        button[dX][k].player = currentPlayer;
                        discs++;
                    }
                    else
                    {
                        break;
                    }
                }
                return discs;

            case 5:
                for(j=dX, k=dY ; k<n && j<n ; k++, j++)
                {
                    if(button[j][k].player ==OPPONENT)
                    {
                        button[j][k].setBackground(ContextCompat.getDrawable(this, image_opponent));
                        button[j][k].player = currentPlayer;
                        discs++;
                    }
                }
                return discs;

            case 6:
                for(j=dX ; j<n ; j++)
                {
                    if(button[j][dY].player == OPPONENT)
                    {
                        button[j][dY].setBackground(ContextCompat.getDrawable(this, image_opponent));
                        button[j][dY].player = currentPlayer;
                        discs++;
                    }
                    else
                    {
                        break;
                    }
                }
                return discs;

            case 7:
                for(j=dX, k=dY ; j<n && k>=0 ; j++, k--)
                {
                    if(button[j][k].player == OPPONENT)
                    {
                        button[j][k].setBackground(ContextCompat.getDrawable(this, image_opponent));
                        button[j][k].player = currentPlayer;
                        discs++;
                    }
                    else
                    {
                        break;
                    }
                }
                return discs;
        }

        return discs;
    }

    private boolean checkMoveValidity(int dX, int dY, int i, int currentPlayer) {  //////////////fix the bug here
        int OPPONENT = 0;
        if(currentPlayer == PLAYER_1)
            OPPONENT = PLAYER_2;
        else if(currentPlayer == PLAYER_2)
            OPPONENT = PLAYER_1;

        int k, j;
        switch (i)
        {
            case 0:
                for(k=dY ; k>=0 ; k--)
                {
                    if(button[dX][k].player != OPPONENT)
                    {
                        break;
                    }
                }

                if(k>=0 && button[dX][k].player == currentPlayer)/////////////////
                    return true;
                else
                    return false;

            case 1:
                for(j=dX, k=dY ; k>=0 && j>=0 ; k--, j--)
                {
                    if(button[j][k].player !=OPPONENT)
                    {
                        break;
                    }
                }
                if(k>=0 && j>=0 && button[j][k].player == currentPlayer)
                    return true;
                else
                    return false;

            case 2:
                for(j=dX ; j>=0 ; j--)
                {
                    if(button[j][dY].player != OPPONENT)
                    {
                        break;
                    }
                }
                if(j>=0 && button[j][dY].player == currentPlayer)
                    return  true;
                else
                    return false;

            case 3:
                for(j=dX, k=dY ; j>=0 && k<n ; j--, k++)
                {
                    if(button[j][k].player != OPPONENT)
                    {
                        break;
                    }
                }
                if(j>=0 && k<n && button[j][k].player == currentPlayer)
                    return true;
                else
                    return false;

            case 4:
                for(k=dY ; k<n ; k++)
                {
                    if(button[dX][k].player != OPPONENT)
                    {
                        break;
                    }
                }

                if(k<n && button[dX][k].player == currentPlayer)
                    return true;
                else
                    return false;

            case 5:
                for(j=dX, k=dY ; k<n && j<n ; k++, j++)
                {
                    if(button[j][k].player !=OPPONENT)
                    {
                        break;
                    }
                }
                if(k<n && j<n && button[j][k].player == currentPlayer)
                    return true;
                else
                    return false;

            case 6:
                for(j=dX ; j<n ; j++)
                {
                    if(button[j][dY].player != OPPONENT)
                    {
                        break;
                    }
                }
                if(j<n && button[j][dY].player == currentPlayer)
                    return  true;
                else
                    return false;

            case 7:
                for(j=dX, k=dY ; j<n && k>=0 ; j++, k--)
                {
                    if(button[j][k].player != OPPONENT)
                    {
                        break;
                    }
                }
                if(j<n && k>=0 && button[j][k].player == currentPlayer)
                    return true;
                else
                    return false;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void Hint(View view)
    {
        hintPressed = true;
        for (Integer k : hint)
        {
            int x = k/10;
            int y = k%10;
            button[x][y].setBackgroundResource(R.drawable.circle_green);
        }
    }

    public void withComputeronClick(View view)
    {
        if(gameOver)
        {
            return;
        }

        if (hintPressed)
        {
            hintPressed = false;
            for (Integer k : hint)
            {
                int x = k/10;
                int y = k%10;
                button[x][y].setBackgroundResource(R.drawable.no_player);
            }
        }

        MyButton b = (MyButton) view;

        int x = b.X;
        int y = b.Y;
        boolean flag = false;

        for(int i=0 ; i<8 ; i++)
        {
            int dX = x+direction[i][0];
            int dY = y+direction[i][1];
            if(dX >= 0 && dX < n &&  dY >= 0 && dY < n && button[dX][dY].player != NO_PLAYER) // checking limits and NO_PLAYER
            {
                if(button[dX][dY].player == PLAYER_1)
                {
                    continue;
                }
                else {                  // when the button belongs to the opponent
                    boolean moveValidity = checkMoveValidity(dX, dY, i, PLAYER_1);
                    if(moveValidity) {
                        flag = true;    // match is found
                        int discs = invertDiscs(dX, dY, i, PLAYER_1);
                        if(CURRENT_PLAYER == PLAYER_1)
                        {
                            player1Discs += discs;
                            player2Discs -= discs;
                        }
                        else if(CURRENT_PLAYER == PLAYER_2)
                        {
                            player2Discs += discs;
                            player1Discs -= discs;
                        }
                        b.setClickable(false);
                    }
                }
            }
        }

        if(!flag)       // if no disc in any direction
        {
            Toast.makeText(this, "Invalid Move!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(CURRENT_PLAYER == PLAYER_1)
        {
            b.setBackground(ContextCompat.getDrawable(this, R.drawable.player1));
            b.player = PLAYER_1;
            b.setClickable(false);
            player1Discs++;
            map.remove(""+x+y);
        }
        /*else if(CURRENT_PLAYER == PLAYER_2)
        {
            b.setBackground(ContextCompat.getDrawable(this, R.drawable.player2));
            b.player = PLAYER_2;
            b.setClickable(false);
            player2Discs++;
            map.remove(""+x+y);
        }*/

        text1.setText(""+player1Discs);
        text2.setText(""+player2Discs);

        int status = checkGameStatus();
        boolean statusHandled = statusActionHandler(status);
        if (statusHandled)
            return;

        // check whether the this player will be able to do a valid move or not
        if (CURRENT_PLAYER == PLAYER_1)
        {
            Pair<Boolean, Integer> ans = checkComputerTurnValidity();
            if (ans.first == false)     // PLAYER_2 cannot move
            {
                Pair<Boolean, ArrayList<Integer>> ans2 = checkPlayerTurnValidity(PLAYER_1);
                if (ans2.first == false) // PLAYER_1 also cannot move
                {
                    gameOver = true;
                    int Status = checkGameStatus();
                    statusActionHandler(Status);
                    Log.i("not 2 not 1", ""+Status);
                    return;
                }
                else                    // PLAYER_1 can move
                {
                    /*row1.setBackgroundResource(R.drawable.button_image);
                    row2.setBackgroundColor(ContextCompat.getColor(this, R.color.brown));*/
                    //CURRENT_PLAYER = PLAYER_1;
                    Toast.makeText(this, "Player 1 move again", Toast.LENGTH_SHORT);
                    hint = ans2.second;
                    Log.i("player 1 move again", "");
                    return;
                }
            }
            else
            {
                row2.setBackgroundResource(R.drawable.button_image);
                row1.setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
                CURRENT_PLAYER = PLAYER_2;
                computerHint = ans.second;
                computerMove(computerHint);
                Log.i("turn switched", "");
            }
        }

       /* if (CURRENT_PLAYER == PLAYER_2)
        {


        }*/
        /*else if (CURRENT_PLAYER == PLAYER_2)
        {

            Pair<Boolean, ArrayList<Integer>> ans = checkPlayerTurnValidity(PLAYER_1);
            if (ans.first == false)     // PLAYER_1 cannot move
            {
                ans = checkPlayerTurnValidity(PLAYER_2);
                if (ans.first == false) // PLAYER_2 also cannot move
                {
                    gameOver = true;
                    int Status = checkGameStatus();
                    statusActionHandler(Status);
                    Log.i("not 1 not 2", ""+Status);
                    return;
                }
                else
                {
                    row2.setBackgroundResource(R.drawable.button_image);
                    row1.setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
                    CURRENT_PLAYER = PLAYER_2;
                    Toast.makeText(this, "Player 2move again", Toast.LENGTH_SHORT);
                    hint = ans.second;
                    Log.i("player 2 move again", "");
                }
            }
            else
            {
                row1.setBackgroundResource(R.drawable.button_image);
                row2.setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
                CURRENT_PLAYER = PLAYER_1;
                hint = ans.second;
                Log.i("turn switched", "");
            }
        }*/



    }


    private void computerMove(int computerHint) {
        //MyButton b = (MyButton) v;

        final int finalComputerHint = computerHint;
        new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                int x = finalComputerHint/10;
                int y = finalComputerHint%10;

                for(int i=0 ; i<8 ; i++)
                {
                    int dX = x+direction[i][0];
                    int dY = y+direction[i][1];
                    if(dX >= 0 && dX < n &&  dY >= 0 && dY < n && button[dX][dY].player != NO_PLAYER) // checking limits and NO_PLAYER
                    {
                        if(button[dX][dY].player == PLAYER_2)
                        {
                            continue;
                        }
                        else {                  // when the button belongs to the opponent
                            boolean moveValidity = checkMoveValidity(dX, dY, i, PLAYER_2);
                            if(moveValidity) {  // match is found
                                int discs = invertDiscs(dX, dY, i, PLAYER_2);
                                player2Discs += discs;
                                player1Discs -= discs;

                                //button[x][y].setClickable(false);
                            }
                        }
                    }
                }
/*
        if(!flag)       // if no disc in any direction
        {
            Toast.makeText(this, "Invalid Move!", Toast.LENGTH_SHORT).show();
            return;
        }*/
        /*if(CURRENT_PLAYER == PLAYER_1)
        {*/
                button[x][y].setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.player2));
                button[x][y].player = PLAYER_2;
                button[x][y].setClickable(false);
                player2Discs++;
                map.remove(""+x+y);
                text1.setText(""+player1Discs);
                text2.setText(""+player2Discs);

                int status = checkGameStatus();
                boolean statusHandled = statusActionHandler(status);
                if (statusHandled)
                    return;

                Pair<Boolean, ArrayList<Integer>> ans = checkPlayerTurnValidity(PLAYER_1);
                if (ans.first == false)     // PLAYER_1 cannot move
                {
                    Pair<Boolean, Integer> ans2 = checkComputerTurnValidity();
                    if (ans.first == false) // PLAYER_2 also cannot move
                    {
                        gameOver = true;
                        int Status = checkGameStatus();
                        statusActionHandler(Status);
                        Log.i("not 1 not 2", ""+Status);
                        return;
                    }
                    else
                    {
                    /*row2.setBackgroundResource(R.drawable.button_image);
                    row1.setBackgroundColor(ContextCompat.getColor(this, R.color.brown));
                    CURRENT_PLAYER = PLAYER_2;*/
                        Toast.makeText(MainActivity.this, "Player 2 move again", Toast.LENGTH_SHORT);
                        computerMove(ans2.second);
                        Log.i("player 2 move again", "");
                    }
                }
                else
                {
                    row1.setBackgroundResource(R.drawable.button_image);
                    row2.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.brown));
                    CURRENT_PLAYER = PLAYER_1;
                    hint = ans.second;
                    Log.i("turn switched", "");
                }
            }
        }.start();



    }

    private Pair<Boolean, Integer> checkComputerTurnValidity()
    {
        boolean flag = false;
        int x;
        int y;
        int index = -1;
        int sum;
        int max = 0;
        //ArrayList<Integer> list = new ArrayList<>();
        for(String key : map.keySet())
        {
            int k = Integer.parseInt(key.toString());
            y = k%10;
            x = k/10;
            sum = 0;
            for(int i=0 ; i<8 ; i++)
            {
                int dX = x+direction[i][0];
                int dY = y+direction[i][1];
                if(dX >= 0 && dX < n &&  dY >= 0 && dY < n && button[dX][dY].player != NO_PLAYER) // checking limits and NO_PLAYER
                {
                    if(button[dX][dY].player == PLAYER_2)
                    {
                        continue;
                    }
                    else {                  // when the button belongs to the opponent
                        int moveValidity = checkComputerMoveValidity(dX, dY, i);
                        if(moveValidity != 0) {
                            flag = true;    // atleast one valid move is possible
                            //list.add(k);
                            sum += moveValidity;
                        }
                    }
                }
            }
            if (max < sum)
            {
                max = sum;
                index = k;
            }
        }

        if (flag)
        {
            return (new Pair<>(true, index));
        }
        else
        {
            return (new Pair<>(false, -1));
        }
    }

    private int checkComputerMoveValidity(int dX, int dY, int i) {
        int OPPONENT = PLAYER_1;

        int k, j;
        int discs = 0;
        switch (i)
        {
            case 0:
                for(k=dY ; k>=0 ; k--)
                {
                    if(button[dX][k].player != OPPONENT)
                    {
                        break;
                    }
                    discs++;
                }

                if(k>=0 && button[dX][k].player == PLAYER_2)/////////////////
                    return discs;
                else
                    return 0;

            case 1:
                for(j=dX, k=dY ; k>=0 && j>=0 ; k--, j--)
                {
                    if(button[j][k].player !=OPPONENT)
                    {
                        break;
                    }
                    discs++;
                }
                if(k>=0 && j>=0 && button[j][k].player == PLAYER_2)
                    return discs;
                else
                    return 0;

            case 2:
                for(j=dX ; j>=0 ; j--)
                {
                    if(button[j][dY].player != OPPONENT)
                    {
                        break;
                    }
                    discs++;
                }
                if(j>=0 && button[j][dY].player == PLAYER_2)
                    return  discs;
                else
                    return 0;

            case 3:
                for(j=dX, k=dY ; j>=0 && k<n ; j--, k++)
                {
                    if(button[j][k].player != OPPONENT)
                    {
                        break;
                    }
                    discs++;
                }
                if(j>=0 && k<n && button[j][k].player == PLAYER_2)
                    return discs;
                else
                    return 0;

            case 4:
                for(k=dY ; k<n ; k++)
                {
                    if(button[dX][k].player != OPPONENT)
                    {
                        break;
                    }
                    discs++;
                }

                if(k<n && button[dX][k].player == PLAYER_2)
                    return discs;
                else
                    return 0;

            case 5:
                for(j=dX, k=dY ; k<n && j<n ; k++, j++)
                {
                    if(button[j][k].player !=OPPONENT)
                    {
                        break;
                    }
                    discs++;
                }
                if(k<n && j<n && button[j][k].player == PLAYER_2)
                    return discs;
                else
                    return 0;

            case 6:
                for(j=dX ; j<n ; j++)
                {
                    if(button[j][dY].player != OPPONENT)
                    {
                        break;
                    }
                    discs++;
                }
                if(j<n && button[j][dY].player == PLAYER_2)
                    return  discs;
                else
                    return 0;

            case 7:
                for(j=dX, k=dY ; j<n && k>=0 ; j++, k--)
                {
                    if(button[j][k].player != OPPONENT)
                    {
                        break;
                    }
                    discs++;
                }
                if(j<n && k>=0 && button[j][k].player == PLAYER_2)
                    return discs;
                else
                    return 0;
        }

        return 0;
    }


}
