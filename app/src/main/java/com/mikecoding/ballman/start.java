package com.mikecoding.ballman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class start extends AppCompatActivity {

    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //create the interstitial
        interstitial = new InterstitialAd(this);

        //test ids
        //for banner ads: ca-app-pub-3940256099942544/6300978111
        //For Interstitial Ads: ca-app-pub-3940256099942544/1033173712
        //For Admob NativeExpress Ads: ca-app-pub-3940256099942544/1072772517

         /*
        //add emulator and actual device ids for testing
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("TEST_DEVICE_ID")
                .build();
        adView.loadAd(adRequest);
        */

        //set your unit id, this is a test id!
        interstitial.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        //create request
        AdRequest adRequest = new AdRequest.Builder().build();

        //start loading...
        interstitial.loadAd(adRequest);

        //once request is loaded, display ad
        interstitial.setAdListener(new AdListener() {

            public void onAdLoaded(){
                displayInterstitial();
            }
        });

    }

    public void displayInterstitial(){

        if(interstitial.isLoaded()){
            interstitial.show();
        }

    }

    public void startGame(View view){
        startActivity(new Intent(getApplicationContext(), main.class));

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
