package com.example.nonroot.libertyprime;
import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

// Source code for the Liberty Prime sound board.
// All variable names are intentional for obvious reasons.

public class MainActivity extends AppCompatActivity{

    int marchAudio_Main, currRawID, currAudio_compare;
    int rawIDs[], marchIDs[], weaponIDs[];

    Field rawItems[];
    String audioFileNames[];
    ListView ListView_audioFileNames;
    Boolean playAllaudio_Bool = Boolean.TRUE;

    TextView TextView_audioName;
    AudioManager audioMngr;
    MediaPlayer mediaPlayer_VOICE, mediaPlayer_MARCH;
    ImageView playAll_Icon;

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
            init_RAWfiles();
            folderCheck();

            playAll_Icon = (ImageView) findViewById(R.id.playAll_Icon);
            ListView_audioFileNames = (ListView) findViewById(R.id.audioList);
            TextView_audioName = (TextView) findViewById(R.id.topLabel_ADVICTORIUM);

            // create AudioManager and set the volume to max on app launch.
            //audioMngr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            //audioMngr.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);

            final ArrayAdapter adapter_for_List = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, audioFileNames);

            ListView_audioFileNames.setAdapter(adapter_for_List);
            registerForContextMenu(ListView_audioFileNames);

            mediaPlayer_VOICE = new MediaPlayer();
            mediaPlayer_MARCH = new MediaPlayer();

            ListView_audioFileNames.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String audio_filename = audioFileNames[position];
                    Log.v("long clicked", String.valueOf(audio_filename));

                    return false;
                }

            });

            ListView_audioFileNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    playAll_Check();
                    playVoice(rawIDs[position]);

                    set_playAll_false();
                }
            });

            TextView_audioName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playAll_Check();
                    playMarch();

                    set_playAll_false();
                }
            });

            playAll_Icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    set_playAll_true();
                    stopAll_Audio();

                    if (!mediaPlayer_VOICE.isPlaying()){
                        LET_FREEDOM_RING();
                    }
                }
            });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        menu.add(v.getId(), 1, 0, "Download as Ringtone");
        menu.add(v.getId(), 2, 0, "Send as voice mail");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String audio_file_name = (String) ListView_audioFileNames.getItemAtPosition(acmi.position);
        int acmi_pos = acmi.position;
        download_Ringtone(item, acmi_pos, audio_file_name);

        Log.v("long clicked",String.valueOf(audio_file_name));
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
                currRnd = new Random().nextInt(rawIDs.length);
            } while (currRnd == currRawID);

            currRawID = currRnd;

            mediaPlayer_VOICE = MediaPlayer.create(this, rawIDs[currRnd]);
            mediaPlayer_VOICE.start();

            // Recursively keep playing until bool is set false by onClick listeners with playAll_Check
            if (currRnd < currAudio_compare) {
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
                InputStream fIn = getBaseContext().getResources().openRawResource(rawIDs[acmi]);

                String PATRIOT = audioFileNames[acmi];
                File INDEPENDENCE = new File(Environment.getExternalStorageDirectory()+"/LibertyPrime");
                String SOUND_OF_LIBERTY = INDEPENDENCE.getAbsolutePath() + "/" + PATRIOT + ".mp3";

                boolean exists = (new File(SOUND_OF_LIBERTY.toString())).exists();
                if(!exists && isExternalStorageWritable() && isExternalStorageReadable()){
                    byte[] buffer = null;
                    int size = 0;

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

                // Not sure why I can't directly set the ringtone after downloading.
                // setRingtone(INDEPENDENCE, PATRIOT);

                break;
            case 2:
                Toast.makeText(getApplicationContext(), "some other option", Toast.LENGTH_LONG).show();
                System.out.println("CLICKED OTHER OPTION");
                break;
        }
        return true;
    }

    public void init_RAWfiles() {

        rawItems = R.raw.class.getDeclaredFields();
        currAudio_compare = 0;

        int startIndstr = 0;
        int mrchIndx_Start = -1;
        int weapIndx_Start = -1;

        // "$change" will appear if you're using the instant run feature.
        // 11/16/2016 : Found a string at Field[49] named "SerialVersionUID" which had an R.raw
        // value of 0.

        String md = "z_p";
        String wd = "z_w";

        try {
            for (int i = 0; i < rawItems.length; i++) {
                String str_name = rawItems[i].getName();

                if (BadStr_check(str_name)){
                    currAudio_compare++;
                }
                if (Type_Check(str_name, md) == Boolean.TRUE && mrchIndx_Start == -1){
                    mrchIndx_Start = i;
                }
                if (Type_Check(str_name, wd) == Boolean.TRUE && weapIndx_Start == -1){
                    weapIndx_Start = i;
                }

                if (rawItems[i].getName() == "z_primemarch") {
                    marchAudio_Main = rawItems[i].getInt(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // marching audio, weapon audio
        marchIDs = new int[weapIndx_Start - mrchIndx_Start];
        weaponIDs = new int[rawItems.length - weapIndx_Start];
        currAudio_compare -= marchIDs.length + weaponIDs.length;

//        int writepath = weapIndx_Start-mrchIndx_Start;
//        int bar = rawItems.length - weapIndx_Start;

        // audio int rawItems and strings for viewlist
        rawIDs = new int[currAudio_compare];
        audioFileNames = new String[currAudio_compare];

        // for now it's just 1 min clips of prime marching
        // eventually i'll make the gun fire and marching random so you get a unique
        // experience every time.

        try {

            int mrchCount = 0;
            int weapCount = 0;

            for(int i = 0; i < rawItems.length; i++) {

                if (!BadStr_check(rawItems[i].getName()) && i < mrchIndx_Start){
                    continue;
                }
                if (i < mrchIndx_Start){
                    audioFileNames[startIndstr] = rawItems[i].getName();
                    audioFileNames[startIndstr] = audioFileNames[startIndstr].toUpperCase();

                    if (audioFileNames[startIndstr].equals("COMMUNISM___")){
                        audioFileNames[startIndstr] = "SOCIALISM";
                    }
                    audioFileNames[startIndstr] = audioFileNames[startIndstr].replace("_", " ");
                    rawIDs[startIndstr] = rawItems[i].getInt(null);

                    System.out.println(audioFileNames[startIndstr] + " " + startIndstr + " " + rawIDs[startIndstr]);
                }

                if (i >= mrchIndx_Start && i < weapIndx_Start) {
                    marchIDs[mrchCount] = rawItems[i].getInt(null);
                    mrchCount++;
                }
                else if (i >= weapIndx_Start && i < rawItems.length) {
                    weaponIDs[weapCount] = rawItems[i].getInt(null);
                    weapCount++;
                }

                startIndstr++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMarch(){
        mediaPlayer_MARCH.reset();
        mediaPlayer_MARCH = MediaPlayer.create(this, marchAudio_Main);
        mediaPlayer_MARCH.start();
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

    public void playAll_Check(){
        if (playAllaudio_Bool == Boolean.TRUE){
            mediaPlayer_VOICE.reset();
            mediaPlayer_MARCH.reset();

            set_playAll_false();
        }
    }

    public void set_playAll_false(){
        playAllaudio_Bool = Boolean.FALSE;
    }

    public void set_playAll_true(){
        playAllaudio_Bool = Boolean.TRUE;
    }

    public void stopAll_Audio(){
        mediaPlayer_VOICE.reset();
        mediaPlayer_MARCH.reset();
    }

    private Boolean BadStr_check(String a){

        // this gets rid of the random crap that is generated when you fill
        // a Field object made from R.raw.class.getDecalredFields

        // primemarch is not one of them.

        if (a == "$change" ||
            a == "serialVersionUID") {

            System.out.println("BADSTR_CHECK : " + a);
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

    public void setRingtone(File INDEPENDENCE, String PATRIOT){
        try {
            File k = new File(INDEPENDENCE, PATRIOT);

            ContentValues contentValues = new ContentValues();
            contentValues.put("_data", k.getAbsolutePath());
            contentValues.put("title", PATRIOT);
            contentValues.put("mime_type", "audio/mp3");
            contentValues.put("_size", Long.valueOf(k.length()));
            contentValues.put("artist", Integer.valueOf(R.string.app_name));
            contentValues.put("is_ringtone", Boolean.valueOf(true));
            contentValues.put("is_notification", Boolean.valueOf(true));
            contentValues.put("is_alarm", Boolean.valueOf(false));
            contentValues.put("is_music", Boolean.valueOf(false));

            try {
                //Uri parse = Uri.parse(this.PATH_TO_FREEDOM);
                //ContentResolver contentResolver = getContentResolver();
                //RingtoneManager.setActualDefaultRingtoneUri(getBaseContext(), 1, contentResolver.insert(MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath()), contentValues));
                //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(SOUND_OF_LIBERTY)));
                System.out.println("working!");
                Toast.makeText(this, new StringBuilder().append("Downloaded as Ringtone!"), Toast.LENGTH_LONG).show();

            } catch (Throwable th) {
                System.out.println("NOT WORKING");
                Toast.makeText(this, new StringBuilder().append("Ringtone feature is not working"), Toast.LENGTH_LONG).show();
            }
        }
        catch (Throwable t) {
            Log.d("test", "FILE NOT FOUND, THIS IS AFTER IT WAS WRITTEN TO STORAGE");
        }
    }

}
