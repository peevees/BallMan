package com.mikecoding.ballman;

import android.graphics.Point;
import android.media.Image;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView eater;
    private ImageView orange;
    private ImageView blue;
    private ImageView ghost;

    //Size
    private int frameHeight;
    private int eaterSize;
    private int screenWidth;
    private int screenHeight;

    //position
    private int eaterY;
    private int orangeX;
    private int orangeY;
    private int blueX;
    private int blueY;
    private int ghostX;
    private int ghostY;

    //Initialize class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //status check
    private boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        eater = (ImageView) findViewById(R.id.eater);
        orange = (ImageView) findViewById(R.id.orange);
        blue = (ImageView) findViewById(R.id.blue);
        ghost = (ImageView) findViewById(R.id.ghost);

        //get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        // move to out of screen
        orange.setX(-80f);
        orange.setY(-80f);
        blue.setX(-80f);
        blue.setY(-80f);
        ghost.setX(-80f);
        ghost.setY(-80f);

    }

    public void changePos(){

        //orange
        orangeX -=12;
        if(orangeX < 0){
            orangeX = screenWidth + 20;
            orangeY = (int) Math.floor(Math.random() * (frameHeight) - orange.getHeight());
        }
        orange.setX(orangeX);
        orange.setY(orangeY);


        //move eater
        if(action_flg == true){
            //Touching
            eaterY -= 20;
        } else {
            eaterY += 20;
        }

        //check eater position
        if(eaterY < 0) eaterY=0;

        if(eaterY > frameHeight - eaterSize) eaterY = frameHeight - eaterSize;

        eater.setY(eaterY);
    }

    public boolean onTouchEvent(MotionEvent me){

        if(start_flg == false){

            start_flg = true;

            //why get frame height and eater height here?
            //because the ui has not benn set on the screen in OnCreate()!

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            eaterY = (int) eater.getY();
            //the eater is a square, (height and width are the same) it just looks like a circle
            eaterSize = eater.getHeight();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0, 20);

        } else {

            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }

        }

        return true;
    }
}
