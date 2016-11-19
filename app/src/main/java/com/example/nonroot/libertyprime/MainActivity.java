package com.example.nonroot.libertyprime;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    Boolean GIVE_ME_LIBERTY_OR_GIVE_ME_DEATH = Boolean.TRUE;

    int AD_VICTORIUM, COMMONWEALTH;

    public int ARRAY_OF_DEMOCRACY[];
    public String ARRAY_OF_LIBERTY[];
    public ListView ARRAY_OF_FREEDOM;

    public ImageView REVOLUTION;
    public AudioManager LIVE_FREE_OR_DIE;
    public TextView SHALL_NOT_BE_INFRINGED;

    public MediaPlayer LIBERTY_BELL, BETTER_DEAD_THAN_RED;

    MainActivity(){
        DECLARE_INDEPENDENCE();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ARRAY_OF_FREEDOM = (ListView) findViewById(R.id.audioList);
        REVOLUTION = (ImageView) findViewById(R.id.REVOLUTION);
        SHALL_NOT_BE_INFRINGED = (TextView) findViewById(R.id.topLabel_ADVICTORIUM);

        LIVE_FREE_OR_DIE = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //LIVE_FREE_OR_DIE.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);

        final ArrayAdapter LIBERTY = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ARRAY_OF_LIBERTY);
        ARRAY_OF_FREEDOM.setAdapter(LIBERTY);

        LIBERTY_BELL = new MediaPlayer();
        BETTER_DEAD_THAN_RED = new MediaPlayer();

        initMusicPlayer();

        ARRAY_OF_FREEDOM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                BRANCHES();

                COMMONWEALTH = ARRAY_OF_DEMOCRACY[position];

                LIBERTY_BELL.reset();
                LIBERTY_BELL = playVoice(ARRAY_OF_DEMOCRACY[position]);
                LIBERTY_BELL.start();
            }
        });

        SHALL_NOT_BE_INFRINGED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRANCHES();

                BETTER_DEAD_THAN_RED.reset();
                BETTER_DEAD_THAN_RED = playVoice(AD_VICTORIUM);
                BETTER_DEAD_THAN_RED.start();
            }
        });

        // play primemarch along with primeaudio playing at random
        REVOLUTION.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AFTER_1776();
                CHECKS_AND_BALANCES();

                if (!BETTER_DEAD_THAN_RED.isLooping()){
                    BETTER_DEAD_THAN_RED.setLooping(Boolean.TRUE);
                }

                BETTER_DEAD_THAN_RED = playVoice(AD_VICTORIUM);
                BETTER_DEAD_THAN_RED.start();

                if(!LIBERTY_BELL.isPlaying()){
                    LET_FREEDOM_RING();
                }
            }
        });
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //check if song playing
        if(GIVE_ME_LIBERTY_OR_GIVE_ME_DEATH){
            int rndaudio = new Random().nextInt(ARRAY_OF_DEMOCRACY.length);
            LIBERTY_BELL.reset();
            LIBERTY_BELL = playVoice(ARRAY_OF_DEMOCRACY[rndaudio]);
            LIBERTY_BELL.start();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    public void DEATH_BEFORE_T

    public MediaPlayer playVoice(int rawID){
        MediaPlayer a = MediaPlayer.create(this, rawID);
        a.setVolume(0.9f, 0.9f);

        return a;
    }

    public void LET_FREEDOM_RING(){

        LIBERTY_BELL.reset();
        if (!LIBERTY_BELL.isPlaying()) {
            int rndaudio = new Random().nextInt(ARRAY_OF_DEMOCRACY.length);
            LIBERTY_BELL = playVoice(ARRAY_OF_DEMOCRACY[rndaudio]);
            LIBERTY_BELL.start();
        }

    }

    public void BRANCHES(){
        if (GIVE_ME_LIBERTY_OR_GIVE_ME_DEATH == Boolean.TRUE){
            CHECKS_AND_BALANCES();
            BEFORE_1776();
        }
    }

    public void BEFORE_1776(){
        GIVE_ME_LIBERTY_OR_GIVE_ME_DEATH = Boolean.FALSE;
    }

    public void AFTER_1776(){
        GIVE_ME_LIBERTY_OR_GIVE_ME_DEATH = Boolean.TRUE;
    }

    public void CHECKS_AND_BALANCES(){
        LIBERTY_BELL.reset();
        BETTER_DEAD_THAN_RED.reset();
    }

    public void initMusicPlayer(){
        LIBERTY_BELL.setAudioStreamType(AudioManager.STREAM_MUSIC);
        LIBERTY_BELL.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
        LIBERTY_BELL.setOnCompletionListener((MediaPlayer.OnCompletionListener) this);
        LIBERTY_BELL.setOnErrorListener((MediaPlayer.OnErrorListener) this);

        BETTER_DEAD_THAN_RED.setAudioStreamType(AudioManager.STREAM_MUSIC);
        BETTER_DEAD_THAN_RED.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
        BETTER_DEAD_THAN_RED.setOnCompletionListener((MediaPlayer.OnCompletionListener) this);
        BETTER_DEAD_THAN_RED.setOnErrorListener((MediaPlayer.OnErrorListener) this);
    }

    public void DECLARE_INDEPENDENCE() {

        Field fields[] = R.raw.class.getDeclaredFields();

        int flen = fields.length;
        int startIndstr = 0;

        // A "$change" string will appear if you're using the instant run feature.
        // bug? 11/16/2016 : Found a string at Field[49] named "SerialVersionUID" which had an R.raw
        // value of 0.

        try {
            for (int i = 0; i < fields.length; i++) {
                if (!BadStr_check(fields[i].getName())) {
                    flen -= 1;
                }
                if (fields[i].getName() == "primemarch") {
                    AD_VICTORIUM = fields[i].getInt(null);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ARRAY_OF_DEMOCRACY = new int[flen];
        ARRAY_OF_LIBERTY = new String[flen];

        try {
            for( int i = 0; i < fields.length; i++) {

                if (!BadStr_check(fields[i].getName())){
                    continue;
                }

                ARRAY_OF_LIBERTY[startIndstr] = fields[i].getName();
                ARRAY_OF_LIBERTY[startIndstr] = ARRAY_OF_LIBERTY[startIndstr].toUpperCase();

                if (ARRAY_OF_LIBERTY[startIndstr].equals("COMMUNISM___")){
                    ARRAY_OF_LIBERTY[startIndstr] = "SOCIALISM";
                }

                ARRAY_OF_LIBERTY[startIndstr] = ARRAY_OF_LIBERTY[startIndstr].replace("_", " ");
                ARRAY_OF_DEMOCRACY[startIndstr] = fields[i].getInt(null);

                System.out.println(ARRAY_OF_LIBERTY[startIndstr] + " " + startIndstr + " " + ARRAY_OF_DEMOCRACY[startIndstr]);

                startIndstr++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean BadStr_check(String a){

        // for some reason there's random crap that is generated when you fill
        // a Field object made from R.raw.class.getDecalredFields

        // primemarch is not one of them.

        if (a == "$change" ||
            a == "primemarch" ||
            a == "serialVersionUID") {
            return Boolean.FALSE;
        }
        else {
            return Boolean.TRUE;
        }
    }
}
