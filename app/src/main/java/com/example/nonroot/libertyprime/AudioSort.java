package com.example.nonroot.libertyprime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Field;

/**
 * Created by nonroot on 12/10/2016.
 */

public class AudioSort {

    int marchAudio_Main, currRawID, voiceSize;
    int voiceIDs[], marchIDs[], weaponIDs[];
    String audioFileNames[];

    Field rawItems[];
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

    public String[] get_StrFileNames(){
        return audioFileNames;
    }

    public int[] get_rawIDs(){
        return voiceIDs;
    }

    public int[] get_marchIDs(){
        return marchIDs;
    }

    public int[] get_weaponIDs(){
        return weaponIDs;
    }

    public void init_RAWfiles() {

        rawItems = R.raw.class.getDeclaredFields();
        int voiceSize = 0;

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
                    voiceSize++;
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
        voiceSize -= marchIDs.length + weaponIDs.length;

        // int writepath = weapIndx_Start-mrchIndx_Start;
        // int bar = rawItems.length - weapIndx_Start;

        // audio int rawItems and strings for viewlist
        voiceIDs = new int[voiceSize];
        audioFileNames = new String[voiceSize];

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
                    voiceIDs[startIndstr] = rawItems[i].getInt(null);

                    System.out.println(audioFileNames[startIndstr] + " " + startIndstr + " " + voiceIDs[startIndstr]);
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
        if (a.contains(b)) {
            System.out.println("MATCH     " + a + "   " + b);
            return true;
        }
        return false;
    }

    public int get_marchAudio(){
        return marchAudio_Main;
    }

}
