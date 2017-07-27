package com.example.aditi.othello;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Created by Aditi on 6/18/2017.
 */

class MyButton extends android.support.v7.widget.AppCompatImageButton{

    int player;
    int X, Y;
    public MyButton(Context context) {
        super(context);

        player = MainActivity.NO_PLAYER;
    }
}
