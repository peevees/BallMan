package com.mikecoding.ballman;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by micha on 2017-01-18.
 */

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 2;

    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;

    public SoundPlayer(Context context){

        //soundpool is deprecated in api level 21 (lollipop)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(audioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        }else{

            //SoundPool (int maxStreams, inte streamType, int srcQuality)
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);

        }

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