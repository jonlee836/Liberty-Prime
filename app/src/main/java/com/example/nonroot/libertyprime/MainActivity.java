package com.example.nonroot.libertyprime;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.VolumeProvider;
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

    int ARRAY_OF_DEMOCRACY[];
    String ARRAY_OF_LIBERTY[];
    ListView ARRAY_OF_FREEDOM;

    AudioManager LET_FREEDOM_RING;
    TextView DEATH_TO_THE_INSTITUTE;
    MainActivity(){
        DECLARATION_OF_INDEPENDENCE();
    }

    private MediaPlayer LIBERTY_BELL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DEATH_TO_THE_INSTITUTE = (TextView) findViewById(R.id.topLabel_ADVICTORIUM);

        LET_FREEDOM_RING = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        LET_FREEDOM_RING.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);

        ARRAY_OF_FREEDOM = (ListView) findViewById(R.id.audioList);
        ArrayAdapter LIBERTY = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ARRAY_OF_LIBERTY);
        ARRAY_OF_FREEDOM.setAdapter(LIBERTY);

        LIBERTY_BELL = new MediaPlayer();

        ARRAY_OF_FREEDOM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                playSong(ARRAY_OF_DEMOCRACY[position]);
            }
        });

        DEATH_TO_THE_INSTITUTE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong(ARRAY_OF_DEMOCRACY[0]);
            }
        });

    }

    public void playSong(int rawID){
        LIBERTY_BELL.reset();
        LIBERTY_BELL = MediaPlayer.create(this, rawID);
        LIBERTY_BELL.setVolume(0.9f, 0.9f);
        LIBERTY_BELL.start();
    }

    public void DECLARATION_OF_INDEPENDENCE() {

        Field fields[] = R.raw.class.getDeclaredFields();

        int flen = fields.length;
        int startIndstr = 0;

        // A "$change" string will appear if you're using the instant run feature.
        // bug? 11/16/2016 : Found a string at Field[49] named "SerialVersionUID" which had an R.raw
        // value of 0.

        for (int i = 0; i < fields.length; i++){
            if (!BadStr_check(fields[i].getName())){
                flen -= 1;
            }
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
            a == "serialVersionUID") {
            return Boolean.FALSE;
        }
        else {
            return Boolean.TRUE;
        }
    }
}
