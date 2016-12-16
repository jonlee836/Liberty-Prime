package com.example.nonroot.LibertyPrime;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Random;

// Source code for the Fallout 4 Liberty Prime sound board.

public class MainActivity extends AppCompatActivity{

    Boolean playAllaudio_Bool = Boolean.TRUE;

    Field rawItems[];

    int marchAudio_Main, currRawID;
    int voiceIDs[], marchIDs[], weaponIDs[];

    String audioFileNames[];
    ListView ListView_audioFileNames;

    TextView TextView_audioName;
    MediaPlayer mediaPlayer_VOICE, mediaPlayer_MARCH;
    ImageView playAll_Icon;

    AudioManager audioMngr;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionCheck(this);
        folderCheck();

        rawItems = R.raw.class.getDeclaredFields();
        AudioSort a = new AudioSort();
        a.init_RAWfiles();

        voiceIDs = a.get_rawIDs();
        audioFileNames = a.get_StrFileNames();
        weaponIDs = a.get_weaponIDs();
        marchIDs = a.get_marchIDs();

        marchAudio_Main = a.get_marchAudio();

        playAll_Icon = (ImageView) findViewById(R.id.playAll_Icon);
        ListView_audioFileNames = (ListView) findViewById(R.id.audioList);
        TextView_audioName = (TextView) findViewById(R.id.topLabel_ADVICTORIUM);

        // create AudioManager and set the volume to max on app launch.
        audioMngr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioMngr.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);

        final ArrayAdapter adapter_for_List = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, audioFileNames);

        ListView_audioFileNames.setAdapter(adapter_for_List);
        registerForContextMenu(ListView_audioFileNames);

        mediaPlayer_VOICE = new MediaPlayer();
        mediaPlayer_MARCH = new MediaPlayer();

        ListView_audioFileNames.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String audio_filename = audioFileNames[position];
                System.out.println("Long Clicked");

                return false;
            }
        });

        ListView_audioFileNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                playVoice(voiceIDs[position]);
                set_playAll_false();
            }
        });

        TextView_audioName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer_MARCH.isPlaying()){
                    mediaPlayer_MARCH.reset();
                }
                else if (!mediaPlayer_MARCH.isPlaying()){
                    playMarch();
                }
                set_playAll_false();
            }
        });

        playAll_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_playAll_true();

                mediaPlayer_VOICE.reset();
                mediaPlayer_MARCH.reset();

                if (!mediaPlayer_VOICE.isPlaying()){
                    LET_FREEDOM_RING();
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        menu.add(v.getId(), 1, 0, "Download as Ringtone");
        //menu.add(v.getId(), 2, 0, "Send as voice mail");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String audio_file_name = (String) ListView_audioFileNames.getItemAtPosition(acmi.position);
        int acmi_pos = acmi.position;
        download_Ringtone(item, acmi_pos, audio_file_name);

        return false;
    }

        public static void permissionCheck(Activity activity) {
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void folderCheck(){

        File INDEPENDENCE =  Environment.getExternalStorageDirectory();
        File CONTINENTAL = new File(INDEPENDENCE+"/LibertyPrime");

        boolean exists = (new File(CONTINENTAL.toString())).exists();

        if (!exists) {
            System.out.println("FOLDER DOES NOT EXIST, CREATING FOLDER " + CONTINENTAL.toString());

            new File(CONTINENTAL.toString()).mkdirs();
        }
    }

    public void LET_FREEDOM_RING(){

        if (playAllaudio_Bool) {
            // This is to stop it from playing the same voice file consecutively.
            int currRnd;
            do {
                currRnd = new Random().nextInt(voiceIDs.length);
            } while (currRnd == currRawID);

            currRawID = currRnd;

            mediaPlayer_VOICE = MediaPlayer.create(this, voiceIDs[currRnd]);
            mediaPlayer_VOICE.start();

            // Recursively keep playing until bool is set false by onClick listeners with playAll_Check
            if (currRnd < voiceIDs.length) {
                mediaPlayer_VOICE.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        LET_FREEDOM_RING();
                    }
                });
            }

            if (!mediaPlayer_MARCH.isPlaying()) {
                playMarch();
            }
        }
    }

    public boolean download_Ringtone(MenuItem item, int acmi, String a) {

        switch (item.getItemId()) {
            case 1:
                InputStream fIn = getBaseContext().getResources().openRawResource(voiceIDs[acmi]);

                String PATRIOT = audioFileNames[acmi];
                File INDEPENDENCE = new File(Environment.getExternalStorageDirectory()+"/LibertyPrime");
                String SOUND_OF_LIBERTY = INDEPENDENCE.getAbsolutePath() + "/" + PATRIOT + ".mp3";

                boolean exists = (new File(SOUND_OF_LIBERTY.toString())).exists();
                if(!exists && isExternalStorageWritable() && isExternalStorageReadable()){
                    byte[] buffer = null;
                    int size = 0;

                    // CREATE INPUT STREAM FROM AUDIO FILE IN RAW FOLDER, WRITE TO PUBLIC STORAGE
                    try {
                        size = fIn.available();
                        buffer = new byte[size];
                        System.out.println("buffer size " + size);

                        fIn.read(buffer);
                        fIn.close();

                        System.out.println("INPUT STREAM AVAILABLE " + fIn.toString());
                    } catch (IOException e) {
                        return false;
                    }

                    FileOutputStream save;

                    try {
                        System.out.println("FILE PATH + NAME : " + SOUND_OF_LIBERTY);

                        save = new FileOutputStream(SOUND_OF_LIBERTY);

                        save.write(buffer);
                        save.flush();
                        save.close();

                        System.out.println("WROTE TO DISK");

                    } catch (FileNotFoundException e) {
                        System.out.println("FILE FAILED TO WRITE " + SOUND_OF_LIBERTY);
                    } catch (IOException e) {
                        System.out.println("IO ERROR FAILED TO WRITE " + SOUND_OF_LIBERTY);
                    }
                }

                Toast.makeText(getApplicationContext(), "Downloaded Ringtone!", Toast.LENGTH_LONG).show();

                break;
//            case 2:
//                Toast.makeText(getApplicationContext(), "some other option", Toast.LENGTH_LONG).show();
//                System.out.println("CLICKED OTHER OPTION");
//                break;
        }
        return true;
    }

    public void playVoice(int rawID){
        try{
            mediaPlayer_VOICE.reset();
            mediaPlayer_VOICE = MediaPlayer.create(this, rawID);
            mediaPlayer_VOICE.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void set_playAll_false(){
        playAllaudio_Bool = Boolean.FALSE;
    }

    public void set_playAll_true(){
        playAllaudio_Bool = Boolean.TRUE;
    }

    public void playMarch(){
        mediaPlayer_MARCH.reset();
        mediaPlayer_MARCH = MediaPlayer.create(this, marchAudio_Main);
        mediaPlayer_MARCH.start();
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
