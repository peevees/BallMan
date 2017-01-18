package com.mikecoding.ballman;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by micha on 2017-01-18.
 */

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;

    public SoundPlayer(Context context){

        //SoundPool (int maxStreams, inte streamType, int srcQuality)

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        hitSound = soundPool.load(context, R.raw.eat, 1);
        overSound = soundPool.load(context, R.raw.gameover, 1);


    }

    public void playHitSound(){

        //play(int soundID, float leftVolume, flot rightVolume, int priority, int loop, float rate);
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);

    }
    public void playOverSound(){
        soundPool.play(overSound, 1.0f, 1.0f, 1, 0 ,1.0f);
    }

}