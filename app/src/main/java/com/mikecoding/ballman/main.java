package com.mikecoding.ballman;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
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

    //speed
    private int eaterSpeed;
    private int orangeSpeed;
    private int blueSpeed;
    private int ghostSpeed;

    //score
    private int score = 0;

    //Initialize class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;

    //status check
    private boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new SoundPlayer(this);

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

        //now
        //nexus4 width: 764 height: 1184
        //speed eater: 20 orange: 12 blue: 20 ghost: 16

        eaterSpeed = Math.round(screenHeight / 60F);  // 1184 / 60 = 19.733..... => 20
        orangeSpeed = Math.round(screenWidth / 60F);  // 768 / 60 = 12.8 => 13
        blueSpeed = Math.round(screenWidth / 36F);    // 768 / 36 = 21.333 => 21
        ghostSpeed = Math.round(screenWidth / 45F);   // 768 / 45 = 17.06... => 17

        // Log.v("SPEED_EATER", eaterSpeed+"");
        // Log.v("SPEED_ORANGE", orangeSpeed+"");
        // Log.v("SPEED_BLUE", blueSpeed+"");
        // Log.v("SPEED_GHOST", ghostSpeed+"");

        // move to out of screen
        orange.setX(-80f);
        orange.setY(-80f);
        blue.setX(-80f);
        blue.setY(-80f);
        ghost.setX(-80f);
        ghost.setY(-80f);

        scoreLabel.setText("Score : 0");

    }

    public void changePos(){

        hitCheck();

        //orange
        orangeX -=orangeSpeed;
        if(orangeX < 0){
            orangeX = screenWidth + 20;
            orangeY = (int) Math.floor(Math.random() * (frameHeight) - orange.getHeight());
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //ghost
        ghostX -= ghostSpeed;
        if(ghostX < 0){
            ghostX = screenWidth + 10;
            ghostY = (int) Math.floor(Math.random() * (frameHeight) - ghost.getHeight());
        }
        ghost.setX(ghostX);
        ghost.setY(ghostY);

        //blue
        blueX -= blueSpeed;
        if(blueX < 0){
            blueX = screenWidth + 5000;
            blueY = (int) Math.floor(Math.random() * (frameHeight) - blue.getHeight());
        }
        blue.setX(blueX);
        blue.setY(blueY);

        //move eater
        if(action_flg == true){
            //Touching
            eaterY -= eaterSpeed;
        } else {
            eaterY += eaterSpeed;
        }

        //check eater position
        if(eaterY < 0) eaterY=0;

        if(eaterY > frameHeight - eaterSize) eaterY = frameHeight - eaterSize;

        eater.setY(eaterY);

        scoreLabel.setText("Score : " + score);

    }

    public void hitCheck(){
        //if the center of the ball is in the hitbox of the eater, it counts as a hit

        //orange
        int orangeCenterX = orangeX + orange.getWidth() / 2;
        int orangeCenterY = orangeY + orange.getHeight() / 2;

        //0 <= orangeCenterX <= eaterWidth
        //eaterY <= orangeCenterY <= eaterY + eaterHeight

        if(0 <= orangeCenterX && orangeCenterX <= eaterSize &&
                eaterY <= orangeCenterY && orangeCenterY <= eaterY + eaterSize){

            score += 10;
            orangeX = -10;
            sound.playHitSound();

        }

        //blue
        int blueCenterX = blueX + blue.getWidth() / 2;
        int blueCenterY = blueY + blue.getHeight() / 2;

        if(0 <= blueCenterX && blueCenterX <= eaterSize &&
                eaterY <= blueCenterY && blueCenterY <= eaterY + eaterSize){

            score += 30;
            blueX = -10;
            sound.playHitSound();

        }

        //ghost
        int ghostCenterX = ghostX + ghost.getWidth() / 2;
        int ghostCenterY = ghostY + ghost.getHeight() / 2;

        if(0 <= ghostCenterX && ghostCenterX <= eaterSize &&
                eaterY <= ghostCenterY && ghostCenterY <= eaterY + eaterSize){

            //stop timer
            timer.cancel();
            timer = null;

            sound.playOverSound();

            //show results
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);

        }

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

    //disable return button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

}
