package com.example.nonroot.libertyprime;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.VolumeProvider;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    int AD_VICTORIUM;
    int ARRAY_OF_DEMOCRACY[];
    String ARRAY_OF_LIBERTY[];
    ListView ARRAY_OF_FREEDOM;

    AudioManager LET_FREEDOM_RING;
    TextView DEATH_TO_THE_INSTITUTE;

    MediaPlayer LIBERTY_BELL, BETTER_DEAD_THAN_RED;

    MainActivity(){
        DECLARATION_OF_INDEPENDENCE();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DEATH_TO_THE_INSTITUTE = (TextView) findViewById(R.id.topLabel_ADVICTORIUM);

        LET_FREEDOM_RING = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        LET_FREEDOM_RING.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);

        ARRAY_OF_FREEDOM = (ListView) findViewById(R.id.audioList);
        final ArrayAdapter LIBERTY = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ARRAY_OF_LIBERTY);
        ARRAY_OF_FREEDOM.setAdapter(LIBERTY);

        LIBERTY_BELL = new MediaPlayer();
        BETTER_DEAD_THAN_RED = new MediaPlayer();

        ARRAY_OF_FREEDOM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //playAudio(ARRAY_OF_DEMOCRACY[position]);
                LIBERTY_BELL.reset();
                LIBERTY_BELL = playVoice(ARRAY_OF_DEMOCRACY[position]);
                LIBERTY_BELL.start();
            }
        });

        DEATH_TO_THE_INSTITUTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BETTER_DEAD_THAN_RED.reset();
                BETTER_DEAD_THAN_RED = playVoice(AD_VICTORIUM);
                BETTER_DEAD_THAN_RED.start();
            }
        });

    }
    
    public MediaPlayer playVoice(int rawID){

        MediaPlayer a = new MediaPlayer();
        a = MediaPlayer.create(this, rawID);
        a.setVolume(0.9f, 0.9f);

        return a;
    }
    
    public void DECLARATION_OF_INDEPENDENCE() {

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
                    //System.out.println("BAD STRING " + fields[i].getName());
                    continue;
                }

                //System.out.println(fields[i].getName() + " " + i + " " + fields[i].getLong(null));

                ARRAY_OF_LIBERTY[startIndstr] = fields[i].getName();
                ARRAY_OF_LIBERTY[startIndstr] = ARRAY_OF_LIBERTY[startIndstr].toUpperCase();
                //System.out.println(ARRAY_OF_LIBERTY[startIndstr]);
                if (ARRAY_OF_LIBERTY[startIndstr].equals("COMMUNISM___")){
                    ARRAY_OF_LIBERTY[startIndstr] = "SOCIALISM";
                    //System.out.println(ARRAY_OF_LIBERTY[startIndstr]);
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

        // list of random crap that is found in a Field
        // object made from R.raw.class.getDecalredFields

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
