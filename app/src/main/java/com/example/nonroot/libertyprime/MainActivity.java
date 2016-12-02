package com.example.nonroot.libertyprime;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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

import static android.R.attr.networkSecurityConfig;
import static android.R.attr.path;
import static android.R.id.content;
import static android.os.Environment.DIRECTORY_MUSIC;
import static android.os.Environment.DIRECTORY_RINGTONES;

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
    String original[];

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //private final File SOUND_OF_LIBERTY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
    //private final String SOUND_OF_LIBERTY = (String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES))) + "/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WE_THE_PEOPLE(this);
        DECLARE_INDEPENDENCE();
        MANIFEST_DESTINY();
        
        USAUSAUSA = (ImageView) findViewById(R.id.USAUSAUSA);
        ARRAY_OF_FREEDOM = (ListView) findViewById(R.id.audioList);
        SHALL_NOT_BE_INFRINGED = (TextView) findViewById(R.id.topLabel_ADVICTORIUM);

        LIVE_FREE_OR_DIE = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        LIVE_FREE_OR_DIE.setStreamVolume(AudioManager.STREAM_MUSIC, 100, 0);

        final ArrayAdapter LIBERTY = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, ARRAY_OF_LIBERTY);

        ARRAY_OF_FREEDOM.setAdapter(LIBERTY);
        registerForContextMenu(ARRAY_OF_FREEDOM);

        LIBERTY_BELL = new MediaPlayer();
        OLD_GLORY = new MediaPlayer();

        ARRAY_OF_FREEDOM.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String audio_filename = ARRAY_OF_LIBERTY[position];
                Log.v("long clicked",String.valueOf(audio_filename));

                return false;
            }

        });

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        menu.add(v.getId(), 1, 0, "SET TO RING TONE");
        menu.add(v.getId(), 2, 0, "some other option");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String audio_file_name = (String) ARRAY_OF_FREEDOM.getItemAtPosition(acmi.position);
        int acmi_pos = acmi.position;
        saveas(item, acmi_pos, audio_file_name);

        Log.v("long clicked",String.valueOf(audio_file_name));
        return false;
    }

    public static void WE_THE_PEOPLE(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
       // int setRingtone = ActivityCompat.checkSelfPermission(activity, Manifest.permission.U);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void MANIFEST_DESTINY(){

        File INDEPENDENCE =  Environment.getExternalStorageDirectory();
        File CONTINENTAL = new File(INDEPENDENCE+"/LibertyPrime");

        boolean exists = (new File(CONTINENTAL.toString())).exists();

        if (!exists) {
            System.out.println("FOLDER DOES NOT EXIST, CREATING FOLDER " + CONTINENTAL.toString());

            new File(CONTINENTAL.toString()).mkdirs();
        }
    }

    public boolean saveas(MenuItem item, int acmi, String a) {

        switch (item.getItemId()) {
            case 1:

                InputStream fIn = getBaseContext().getResources().openRawResource(ARRAY_OF_DEMOCRACY[acmi]);

                Toast.makeText(getApplicationContext(), "SET TO RING TONE", Toast.LENGTH_LONG).show();
                System.out.println("CLICKED RING TONE OPTION");

                String PATRIOT = ARRAY_OF_LIBERTY[acmi];
                File INDEPENDENCE =  Environment.getExternalStorageDirectory();
                File CONTINENTAL = new File(INDEPENDENCE+"/LibertyPrime");
                String SOUND_OF_LIBERTY = CONTINENTAL.getPath() + "/" + PATRIOT + ".mp3";

                boolean exists = (new File(SOUND_OF_LIBERTY.toString())).exists();
                if(!exists){
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

                    } catch (FileNotFoundException e) {
                        System.out.println("FILE FAILED TO WRITE " + SOUND_OF_LIBERTY);
                    } catch (IOException e) {
                        System.out.println("IO ERROR FAILED TO WRITE " + SOUND_OF_LIBERTY);
                    }

                    try {
                        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(SOUND_OF_LIBERTY)));
                        File k = new File(CONTINENTAL, PATRIOT);

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
                        values.put(MediaStore.MediaColumns.TITLE, PATRIOT);

                        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");

                        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
                        values.put(MediaStore.Audio.Media.IS_ALARM, true);
                        values.put(MediaStore.Audio.Media.IS_MUSIC, true);

                        //Uri uri = MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath());
                        try {
                            ContentResolver contentResolver = getContentResolver();

                            Toast.makeText(this, new StringBuilder().append("Ringtone set successfully"), Toast.LENGTH_LONG).show();
                            RingtoneManager.setActualDefaultRingtoneUri(getBaseContext(), 1, contentResolver.insert(MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath()), values));
                        } catch (Throwable th) {
                            Toast.makeText(this, new StringBuilder().append("Ringtone feature is not working"), Toast.LENGTH_LONG).show();
                        }
//                        Uri uri = MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath());
//
//                        System.out.println("old uri " + uri);
//
//                        MainActivity.this.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + path + "\"", null);
//                        MainActivity.this.getContentResolver().insert(uri, values);

//                        System.out.println("new uri " + uri);
//                        Uri uri = MediaStore.Audio.Media.getContentUriForPath(path);
//
//                        getActivity().getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + path + "\"", null);
//                        getActivity().getContentResolver().insert(uri, values);
//                        Log.i("TAG", "the absolute path of the file is :"+ SOUND_OF_LIBERTY);
//
//                        Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), ringtone);
//                        mp.start();
//
//                        Uri uri = MediaStore.Audio.Media.getContentUriForPath(SOUND_OF_LIBERTY);
//                        //getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + SOUND_OF_LIBERTY + "\"", null);
//                        Uri newUri = getContentResolver().insert(uri, values);
//
//                        //grantUriPermission("com.example.nonroot.libertyprime", newUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                        System.out.println("uri=="+uri);
//                        Log.i("TAG","the ringtone uri is :"+newUri);

                        //RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this, RingtoneManager.TYPE_RINGTONE, newUri);
                    }
                    catch (Throwable t) {
                        Log.d("test", "FILE NOT FOUND, THIS IS AFTER IT WAS WRITTEN TO STORAGE");
                    }
                }
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "some other option", Toast.LENGTH_LONG).show();
                System.out.println("CLICKED OTHER OPTION");
                break;
        }
        return true;
    }

    public void LET_FREEDOM_RING(){

        int currRnd;
        do{
            currRnd = new Random().nextInt(ARRAY_OF_DEMOCRACY.length);
        }while(currRnd == PRICE_OF_FREEDOM);

        PRICE_OF_FREEDOM = currRnd;

        LIBERTY_BELL = MediaPlayer.create(this, ARRAY_OF_DEMOCRACY[currRnd]);
        LIBERTY_BELL.start();

        // recursively keep playing until bool is set false by other onClicks
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

    public void DECLARE_INDEPENDENCE() {

        UNJUST_TAXATION = R.raw.class.getDeclaredFields();
        BRITISH_TYRANNY = 0;

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
                }
                if (Type_Check(str_name, wd) == Boolean.TRUE && weapIndx_Start == -1){
                    weapIndx_Start = i;
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
        BRITISH_TYRANNY -= ONE_IF_BY_LAND.length + TWO_IF_BY_SEA.length;

        int writepath = weapIndx_Start-mrchIndx_Start;
        int bar = UNJUST_TAXATION.length - weapIndx_Start;

        // audio int rawIDs and strings for viewlist
        ARRAY_OF_DEMOCRACY = new int[BRITISH_TYRANNY];
        ARRAY_OF_LIBERTY = new String[BRITISH_TYRANNY];
        original = new String[BRITISH_TYRANNY];

        // for now it's just 1 min clips of prime marching
        // eventually make the weapons firing and marching random so you get a unique
        // experience every time?

        try {

            int mrchCount = 0;
            int weapCount = 0;

            for(int i = 0; i < UNJUST_TAXATION.length; i++) {

                if (!BadStr_check(UNJUST_TAXATION[i].getName()) && i < mrchIndx_Start){
                    continue;
                }
                if (i < mrchIndx_Start){
                    ARRAY_OF_LIBERTY[startIndstr] = UNJUST_TAXATION[i].getName();
                    original[startIndstr] = ARRAY_OF_LIBERTY[startIndstr];
                    ARRAY_OF_LIBERTY[startIndstr] = ARRAY_OF_LIBERTY[startIndstr].toUpperCase();

                    if (ARRAY_OF_LIBERTY[startIndstr].equals("COMMUNISM___")){
                        ARRAY_OF_LIBERTY[startIndstr] = "SOCIALISM";
                    }
                    ARRAY_OF_LIBERTY[startIndstr] = ARRAY_OF_LIBERTY[startIndstr].replace("_", " ");
                    ARRAY_OF_DEMOCRACY[startIndstr] = UNJUST_TAXATION[i].getInt(null);

                    System.out.println(ARRAY_OF_LIBERTY[startIndstr] + " " + startIndstr + " " + ARRAY_OF_DEMOCRACY[startIndstr]);
                }

                if (i >= mrchIndx_Start && i < weapIndx_Start) {
                    ONE_IF_BY_LAND[mrchCount] = UNJUST_TAXATION[i].getInt(null);
                    mrchCount++;
                }
                else if (i >= weapIndx_Start && i < UNJUST_TAXATION.length) {
                    TWO_IF_BY_SEA[weapCount] = UNJUST_TAXATION[i].getInt(null);
                    weapCount++;
                }

                startIndstr++;
            }
        } catch (Exception e) {
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
}
