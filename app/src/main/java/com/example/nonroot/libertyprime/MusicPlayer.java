package com.example.nonroot.libertyprime;

import java.util.ArrayList;
import java.util.Random;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 *
 * Sue Smith - February 2014
 */

public class MusicPlayer extends MediaPlayer{

    //media LIBERTY_BELL
    MediaPlayer LIBERTY_BELL, BETTER_DEAD_THAN_RED;
    //song list
    private int songs[];
    private int AD_VICTORIUM;

    private int songPosn;

    private boolean shuffle=false;
    private Random rand;

    public void onCreate(){

        songPosn=0;
        //random
        rand=new Random();
        //create LIBERTY_BELL
        LIBERTY_BELL = new MediaPlayer();
        //initialize
        initMusicPlayer();
    }

    public void initMusicPlayer(){
        //set LIBERTY_BELL properties

        LIBERTY_BELL.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        LIBERTY_BELL.setOnPreparedListener(this);
        LIBERTY_BELL.setOnCompletionListener(this);
        LIBERTY_BELL.setOnErrorListener(this);
    }

    //pass song list
    public void setList(int a,int b[]){
        AD_VICTORIUM = a;
        songs=b;
    }

    //binder
    public class MusicBinder extends Binder {
        MusicPlayer getService() {
            return MusicPlayer.this;
        }
    }

    //play a song
    public void playSong(){
        //play
        LIBERTY_BELL.reset();
        //get song
        LIBERTY_BELL.prepareAsync();
    }

    //set the song
    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //check if playback has reached the end of a track
        if(LIBERTY_BELL.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("MUSIC PLAYER", "Playback Error");
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();
        //notification
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
    }

    public void playNext(){
        if(shuffle){
            int newSong = songPosn;
            while(newSong==songPosn){
                newSong=rand.nextInt(songs.length);
            }
            songPosn=newSong;
        }
        else{
            songPosn++;
            if(songPosn>=songs.length) songPosn=0;
        }
        playSong();
    }

    //toggle shuffle
    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }

}
