package com.example.nonroot.libertyprime;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.provider.Settings;
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

public class MainActivity extends AppCompatActivity{

    Boolean GIVE_ME_LIBERTY_OR_GIVE_ME_DEATH = Boolean.TRUE;

    int AD_VICTORIAM, PRICE_OF_FREEDOM, BRITISH_TYRANNY;

    int ONE_IF_BY_LAND[];
    int TWO_IF_BY_SEA[];

    int ARRAY_OF_DEMOCRACY[];

    String ARRAY_OF_LIBERTY[];
    ListView ARRAY_OF_FREEDOM;
    TextView SHALL_NOT_BE_INFRINGED;

    AudioManager LIVE_FREE_OR_DIE;
    Field UNJUST_TAXATION[];
    MediaPlayer LIBERTY_BELL, OLD_GLORY;
    ImageView USAUSAUSA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DECLARE_INDEPENDENCE();

        USAUSAUSA = (ImageView) findViewById(R.id.USAUSAUSA);
        ARRAY_OF_FREEDOM = (ListView) findViewById(R.id.audioList);
        SHALL_NOT_BE_INFRINGED = (TextView) findViewById(R.id.topLabel_ADVICTORIUM);

        LIVE_FREE_OR_DIE = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        LIVE_FREE_OR_DIE.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);

        final ArrayAdapter LIBERTY = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ARRAY_OF_LIBERTY);
        ARRAY_OF_FREEDOM.setAdapter(LIBERTY);

        LIBERTY_BELL = new MediaPlayer();
        OLD_GLORY = new MediaPlayer();

        ARRAY_OF_FREEDOM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                SUCCESSION();
                COLONIES(ARRAY_OF_DEMOCRACY[position]);

                BEFORE_1776();
                A_GIANT_MISTAKE();
            }
        });

        SHALL_NOT_BE_INFRINGED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SUCCESSION();
                UNALIENABLE_RIGHTS();

                BEFORE_1776();
                A_GIANT_MISTAKE();
            }
        });

        USAUSAUSA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AFTER_1776();
                BALANCE_OF_POWER();

                if (!LIBERTY_BELL.isPlaying() && GIVE_ME_LIBERTY_OR_GIVE_ME_DEATH){
                    LET_FREEDOM_RING();
                }
            }
        });
    }

    public void LET_FREEDOM_RING(){

        int currRnd;
        do{
            currRnd = new Random().nextInt(ARRAY_OF_DEMOCRACY.length);
        }while(currRnd == PRICE_OF_FREEDOM);

        PRICE_OF_FREEDOM = currRnd;

        LIBERTY_BELL = MediaPlayer.create(this, ARRAY_OF_DEMOCRACY[currRnd]);
        LIBERTY_BELL.start();

        // recursively keep playing until bool is set false
        if (currRnd < BRITISH_TYRANNY) {
            LIBERTY_BELL.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    LET_FREEDOM_RING();
                }
            });
        }

        UNALIENABLE_RIGHTS();
    }

    public void UNALIENABLE_RIGHTS(){
        OLD_GLORY.reset();
        OLD_GLORY = MediaPlayer.create(this, AD_VICTORIAM);
        OLD_GLORY.start();
    }

    public void COLONIES(int rawID){
        try{
            LIBERTY_BELL.reset();
            LIBERTY_BELL = MediaPlayer.create(this, rawID);
            LIBERTY_BELL.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void SUCCESSION(){
        if (GIVE_ME_LIBERTY_OR_GIVE_ME_DEATH == Boolean.TRUE){
            LIBERTY_BELL.reset();
            OLD_GLORY.reset();

            BEFORE_1776();
        }
    }

    public void BEFORE_1776(){
        GIVE_ME_LIBERTY_OR_GIVE_ME_DEATH = Boolean.FALSE;
    }

    public void A_GIANT_MISTAKE(){

    }

    public void AFTER_1776(){
        GIVE_ME_LIBERTY_OR_GIVE_ME_DEATH = Boolean.TRUE;
    }

    public void BALANCE_OF_POWER(){
        LIBERTY_BELL.reset();
        OLD_GLORY.reset();
    }

    public void DECLARE_INDEPENDENCE() {

        UNJUST_TAXATION = R.raw.class.getDeclaredFields();

        int startIndstr = 0;
        int mrchIndx_Start = -1;
        int weapIndx_Start = -1;

        // Bug? A "$change" string will appear if you're using the instant run feature.
        // 11/16/2016 : Found a string at Field[49] named "SerialVersionUID" which had an R.raw
        // value of 0.

        String md = "z_p";
        String wd = "z_w";

        try {
            for (int i = 0; i < UNJUST_TAXATION.length; i++) {
                String str_name = UNJUST_TAXATION[i].getName();

                if (BadStr_check(str_name)){
                    BRITISH_TYRANNY++;
                }
                if (Type_Check(str_name, md) == Boolean.TRUE && mrchIndx_Start == -1){
                    mrchIndx_Start = i;
                    System.out.println("mrchindx : " + mrchIndx_Start);
                }
                if (Type_Check(str_name, wd) == Boolean.TRUE && weapIndx_Start == -1){
                    weapIndx_Start = i;
                    System.out.println("weapindx : " + weapIndx_Start);
                }

                if (UNJUST_TAXATION[i].getName() == "z_primemarch") {
                    AD_VICTORIAM = UNJUST_TAXATION[i].getInt(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // marching audio, weapon audio
        ONE_IF_BY_LAND = new int[weapIndx_Start - mrchIndx_Start];
        TWO_IF_BY_SEA = new int[UNJUST_TAXATION.length - weapIndx_Start];

        int foo = weapIndx_Start-mrchIndx_Start;
        int bar = UNJUST_TAXATION.length - weapIndx_Start;

        System.out.println("one if by land size, index : " + foo + " " + mrchIndx_Start);
        System.out.println("two if by sea size, index : " + bar + " " + weapIndx_Start);

        // audio int rawIDs and strings for viewlist
        ARRAY_OF_DEMOCRACY = new int[BRITISH_TYRANNY];
        ARRAY_OF_LIBERTY = new String[BRITISH_TYRANNY];

        // 1 min clips of prime marching
        try {

            int mrchCount = 0;
            int weapCount = 0;

            for(int i = 0; i < UNJUST_TAXATION.length; i++) {

                if (!BadStr_check(UNJUST_TAXATION[i].getName()) && i < mrchIndx_Start){
                    continue;
                }
                if (i < mrchIndx_Start){
                    ARRAY_OF_LIBERTY[startIndstr] = UNJUST_TAXATION[i].getName();
                    ARRAY_OF_LIBERTY[startIndstr] = ARRAY_OF_LIBERTY[startIndstr].toUpperCase();

                    if (ARRAY_OF_LIBERTY[startIndstr].equals("COMMUNISM___")){
                        ARRAY_OF_LIBERTY[startIndstr] = "SOCIALISM";
                    }
                    ARRAY_OF_LIBERTY[startIndstr] = ARRAY_OF_LIBERTY[startIndstr].replace("_", " ");
                    ARRAY_OF_DEMOCRACY[startIndstr] = UNJUST_TAXATION[i].getInt(null);
                }


                if (i >= mrchIndx_Start && i < weapIndx_Start) {
                    ONE_IF_BY_LAND[mrchCount] = UNJUST_TAXATION[i].getInt(null);
                    mrchCount++;
                }
                else if (i >= weapIndx_Start && i < UNJUST_TAXATION.length) {
                    TWO_IF_BY_SEA[weapCount] = UNJUST_TAXATION[i].getInt(null);
                    weapCount++;
                }

                System.out.println(ARRAY_OF_LIBERTY[startIndstr] + " " + startIndstr + " " + ARRAY_OF_DEMOCRACY[startIndstr]);

                startIndstr++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// test 
    private Boolean BadStr_check(String a){

        // this gets rid of the random crap that is generated when you fill
        // a Field object made from R.raw.class.getDecalredFields

        // primemarch is not one of them.

        if (a == "$change" ||
            a == "serialVersionUID") {

            return Boolean.FALSE;
        }
        else {
            return Boolean.TRUE;
        }
    }

    private Boolean Type_Check(String a, String b){
        String c = a.substring(0, Math.min(a.length(), b.length()));

        if (a.contains(b)) {
            System.out.println("MATCH     " + a + "   " + b);
            return true;
        }
        return false;
    }
}
