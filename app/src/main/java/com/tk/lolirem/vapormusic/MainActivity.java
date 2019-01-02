package com.tk.lolirem.vapormusic;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.tk.lolirem.vapormusic.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.xml.datatype.Duration;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


import static android.os.Build.VERSION.SDK_INT;
import static org.apache.commons.lang3.ObjectUtils.compare;

public class MainActivity extends AppCompatActivity {
    public int play = 0;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.tk.lolirem.vapormusic.PlayNewAudio";

    public Bitmap albumart;
    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }
    String mUser;
    public static void activityPaused() {
        activityVisible = false;
    }
    public static final int PICK_IMAGE = 1;
    private static boolean activityVisible;
    static final String STATE_USER = "user";
    private MediaPlayerService player;
    boolean serviceBound = false;
    ArrayList<Audio> audioList;
    ArrayList<Audio> audioplaylist;
    ArrayList<Audio> result;

    public String laststr ="";

    boolean  b ;
    int b1 = 0;
    public int repeatpress = 0;
    public int shufflepress = 0;
    ImageView collapsingImageView;
    RecyclerView_Adapter adapter;
    public boolean repeaton = false;
    int portrait = 1;
    public int lasttime;
    public int lastindex;
    public int lastsize;
    public boolean used = false;
    public int timetor ;
    public boolean itworks = false;
    public String lastartist ;
    public String lasttrack ;
    public String lastart;
    public String lastduration;
    public String lasttime2;
    public String lastpath;
    public int rotatelock = 1;
    public int fullscreen = 1;
    public int sortmode = 0;
    public String searchstr="";
    public int sortsaved =0;
    public int ok;
public String datapath;
    public String sortmodestr;
    public String okstr;
    public String searchstrstr;
    public String savedrotation;
    public int yeah = 0;
    public String lastradio ="" ;
    public String mem;






    int imageIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        MediaPlayer mp = MediaPlayer.create(this, R.raw.win95);
        mp.start();

        setContentView(R.layout.activity_main);
        VersionChecker versionChecker = new VersionChecker();
        Button install = findViewById(R.id.install);
       install.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String appPackageName ="com.tk.lolirem.vapormusic";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }






            }});
        Button hideupdate = findViewById(R.id.hideupdate);

        hideupdate.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
                FrameLayout update = findViewById(R.id.updatepage);
                update.setVisibility(View.INVISIBLE);






            }});
        try
        { String appVersionName = BuildConfig.VERSION_NAME;
            String mLatestVersionName = versionChecker.execute().get();
            try{
            Log.d("latestversion",mLatestVersionName);
            Log.d("current",appVersionName);}
            catch (Exception io){}
            if (compare(appVersionName, mLatestVersionName) == -1)
            {
                FrameLayout update = findViewById(R.id.updatepage);
                update.setVisibility(View.VISIBLE);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        getTotalMemory();

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mUser = savedInstanceState.getString(STATE_USER);
        } else {
            // Probably initialize members with default values for a new instance
            mUser = "NewUser";
        }
        File f4 = new File(Environment.getExternalStorageDirectory() +
                File.separator + "VaporMusic","radio");
        if (f4.exists()){
            final Button change = (Button) findViewById(R.id.button17);
            final Button changeland = (Button) findViewById(R.id.button17land);
            change.setText("Change");changeland.setText("Change");
        }
        try{
        readtext3();
        readtext4();
        readtext5();


            if (portrait == 0) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                Button rlock = findViewById(R.id.rotatelock);
                Button rlockland = findViewById(R.id.rotatelockland);
                if (rotatelock % 2 == 1) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                    rlock.setText(R.string.RotateUn);
                    rlockland.setText(R.string.RotateUnL);

                }
                if (rotatelock % 2 == 0)
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    rlock.setText(R.string.Rotate);
                    rlockland.setText(R.string.RotateL);
                }
                final FrameLayout framex = findViewById(R.id.framex);
                final FrameLayout portraitmode = findViewById(R.id.portraiter);
                final FrameLayout landscapemode = findViewById(R.id.landscaper);
                final FrameLayout frame1 = (FrameLayout) findViewById(R.id.frame1);
                final FrameLayout frame2 = (FrameLayout) findViewById(R.id.frame2);
                final FrameLayout frame3 = (FrameLayout) findViewById(R.id.frame3);
                final FrameLayout frame4 = (FrameLayout) findViewById(R.id.frame4);
                final FrameLayout frame5 = (FrameLayout) findViewById(R.id.frame5);

                final Button button4iland = (Button) findViewById(R.id.hidenpland);
final FrameLayout textinfo = findViewById(R.id.textinfo);

                final FrameLayout frame1land = (FrameLayout) findViewById(R.id.frame1land);
                final FrameLayout frame2land = (FrameLayout) findViewById(R.id.frame2land);
                final FrameLayout frame3land = (FrameLayout) findViewById(R.id.frame3land);
                final FrameLayout frame4land = (FrameLayout) findViewById(R.id.frame4land);
                final FrameLayout frame5land = (FrameLayout) findViewById(R.id.frame5land);
                if (frame5.getVisibility() == View.VISIBLE){
                    frame1land.setVisibility(View.VISIBLE);
                    frame2land.setVisibility(View.VISIBLE);
                    frame3land.setVisibility(View.VISIBLE);
                    frame4land.setVisibility(View.VISIBLE);
                    frame5land.setVisibility(View.VISIBLE);
                    frame1.setVisibility(View.INVISIBLE);
                    frame2.setVisibility(View.INVISIBLE);
                    frame3.setVisibility(View.INVISIBLE);
                    frame4.setVisibility(View.INVISIBLE);
                    frame5.setVisibility(View.INVISIBLE);
                }
                if (framex.getVisibility() == View.VISIBLE ){
                    portraitmode.setVisibility(View.INVISIBLE);
                    landscapemode.setVisibility(View.INVISIBLE);
                } else {
                    portraitmode.setVisibility(View.INVISIBLE);
                    landscapemode.setVisibility(View.VISIBLE);}


                portrait =0;
            } else if (portrait ==1){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                final FrameLayout framex = findViewById(R.id.framex);
                final FrameLayout portraitmode = findViewById(R.id.portraiter);
                final FrameLayout landscapemode = findViewById(R.id.landscaper);
                final FrameLayout frame1 = (FrameLayout) findViewById(R.id.frame1);
                final FrameLayout frame2 = (FrameLayout) findViewById(R.id.frame2);
                final FrameLayout frame3 = (FrameLayout) findViewById(R.id.frame3);
                final FrameLayout frame4 = (FrameLayout) findViewById(R.id.frame4);
                final FrameLayout frame5 = (FrameLayout) findViewById(R.id.frame5);

                final Button button4iland = (Button) findViewById(R.id.hidenpland);


                final FrameLayout frame1land = (FrameLayout) findViewById(R.id.frame1land);
                final FrameLayout frame2land = (FrameLayout) findViewById(R.id.frame2land);
                final FrameLayout frame3land = (FrameLayout) findViewById(R.id.frame3land);
                final FrameLayout frame4land = (FrameLayout) findViewById(R.id.frame4land);
                final FrameLayout frame5land = (FrameLayout) findViewById(R.id.frame5land);
                if (frame5land.getVisibility() == View.VISIBLE){
                    frame1.setVisibility(View.VISIBLE);
                    frame2.setVisibility(View.VISIBLE);
                    frame3.setVisibility(View.VISIBLE);
                    frame4.setVisibility(View.VISIBLE);
                    frame5.setVisibility(View.VISIBLE);
                    frame1land.setVisibility(View.INVISIBLE);
                    frame2land.setVisibility(View.INVISIBLE);
                    frame3land.setVisibility(View.INVISIBLE);
                    frame4land.setVisibility(View.INVISIBLE);
                    frame5land.setVisibility(View.INVISIBLE);
                }
                if (framex.getVisibility() == View.VISIBLE ){
                    portraitmode.setVisibility(View.INVISIBLE);
                    landscapemode.setVisibility(View.INVISIBLE);
                } else {
                    portraitmode.setVisibility(View.VISIBLE);
                    landscapemode.setVisibility(View.INVISIBLE);}

                portrait = 1;
            }
        } catch(Exception loaderror){
            Log.e("oh","loaderror",loaderror);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingImageView = (ImageView) findViewById(R.id.collapsingImageView);
        FastScroller fastScroller = (FastScroller) findViewById(R.id.fastscroll);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);



        //has to be called AFTER RecyclerView.setAdapter()
        fastScroller.setRecyclerView(recyclerView);

        fastScroller.setHandleColor(0xFF404040);
        fastScroller.setBubbleTextAppearance(R.style.StyledScrollerTextAppearance);



        try{
        readtext();
            Button albumlist = findViewById(R.id.albumlist);
            Button tracklist = findViewById(R.id.tracklist);
             Button searchlist = findViewById(R.id.searchlist);
            Button artistlist = findViewById(R.id.artistlist);

            tracklist.setBackgroundResource(R.drawable.freebutton5);
            albumlist.setBackgroundResource(R.drawable.freebutton5);
            artistlist.setBackgroundResource(R.drawable.freebutton5);
            searchlist.setBackgroundResource(R.drawable.freebutton5);
            searchlist.setTextColor(Color.BLACK);
            tracklist.setTextColor(Color.BLACK);
            albumlist.setTextColor(Color.BLACK);
            artistlist.setTextColor(Color.BLACK);
            if (ok == 0){
            if (sortmode == 3){
            searchlist.setBackgroundResource(R.drawable.freebutton7);
            searchlist.setTextColor(Color.WHITE);}
            if (sortmode == 0){
                tracklist.setBackgroundResource(R.drawable.freebutton7);
                tracklist.setTextColor(Color.WHITE);}
            if (sortmode == 1){
                artistlist.setBackgroundResource(R.drawable.freebutton7);
                artistlist.setTextColor(Color.WHITE);}
            if (sortmode == 2){
                albumlist.setBackgroundResource(R.drawable.freebutton7);
                albumlist.setTextColor(Color.WHITE);}}




        } catch (Exception io ){}

        //runs without a timer by reposting this handler at the end of the runnable

        loadCollapsingImage(imageIndex);
        IntentFilter iF2 = new IntentFilter();

        iF2.addAction("NOTIFICATION_CHANGED1");

        registerReceiver(mReceiver2, iF2);
        IntentFilter iF3 = new IntentFilter();

        iF3.addAction("PLAY_TIME");

        registerReceiver(mReceiver3, iF3);
        IntentFilter iF4 = new IntentFilter();

        iF4.addAction("stopped");

        registerReceiver(stopped, iF4);


        Button button10  = (Button) findViewById(R.id.hideplaylist);

        final FrameLayout playlistlo = (FrameLayout) findViewById(R.id.playlist1);
        final RelativeLayout playlistli = (RelativeLayout) findViewById(R.id.hello3);
        button10.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                    try{
                                        if (player.mediaPlayer.getDuration() != 0){
                                            final FrameLayout textinfo = findViewById(R.id.textinfo);
                                            textinfo.setVisibility(View.INVISIBLE);
                                        }

                                    } catch (Exception io){}
                                if (playlistlo.getVisibility() == View.VISIBLE)
                                {
                                    playlistlo.setVisibility(View.INVISIBLE);

                                    playlistli.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);


                                    if(play == 1){

                                    }

                                }
                                else
                                {
                                    playlistlo.setVisibility(View.VISIBLE);
                                    playlistli.setVisibility(View.VISIBLE);


                                }
                                if (b1 == 0) {

                                      b1 = b1 + 1;
                                }

                            }
                        }
                );
        Button button11  = (Button) findViewById(R.id.button7);

        button11.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                try{
                                    if (player.mediaPlayer.getDuration() != 0){
                                        final FrameLayout textinfo = findViewById(R.id.textinfo);
                                        textinfo.setVisibility(View.INVISIBLE);
                                    }

                                } catch (Exception io){}
                                if (playlistlo.getVisibility() == View.VISIBLE)
                                {
                                    playlistlo.setVisibility(View.INVISIBLE);
                                    playlistli.setVisibility(View.INVISIBLE);


                                }
                                else
                                {
                                    playlistlo.setVisibility(View.VISIBLE);
                                    playlistli.setVisibility(View.VISIBLE);

                                }
                            }
                        }
                );
        Button button11land  = (Button) findViewById(R.id.button7land);

        button11land.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                try{
                                    if (player.mediaPlayer.getDuration() != 0){
                                        final FrameLayout textinfo = findViewById(R.id.textinfo);
                                        textinfo.setVisibility(View.INVISIBLE);
                                    }

                                } catch (Exception io){}
                                if (playlistlo.getVisibility() == View.VISIBLE)
                                {
                                    playlistlo.setVisibility(View.INVISIBLE);
                                    playlistli.setVisibility(View.INVISIBLE);


                                }
                                else
                                {
                                    playlistlo.setVisibility(View.VISIBLE);
                                    playlistli.setVisibility(View.VISIBLE);

                                }
                            }
                        }
                );

        final Button button12 = (Button) findViewById(R.id.button10);
        final Button button15 = (Button) findViewById(R.id.button9);
        final Button button12land = (Button) findViewById(R.id.button10land);
        final Button button15land = (Button) findViewById(R.id.button9land);
        button12.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (sortmode == 4){
                    player.buildNotification(PlaybackStatus.PAUSED);
                    button15.setVisibility(Button.VISIBLE);
                    button12.setVisibility(Button.INVISIBLE);
                    button15land.setVisibility(Button.VISIBLE);
                    button12land.setVisibility(Button.INVISIBLE);
                    MuteAudio();
                    try{
                    if (!player.mediaPlayer.isPlaying() || player.mediaPlayer == null){
                        playAudio(0);
                        Toast.makeText(MainActivity.this,
                                "Please wait 15 seconds... loading...", Toast.LENGTH_LONG).show();
                    }} catch (Exception io){player.skipToNext();
                    Toast.makeText(MainActivity.this,
                            "Please wait 15 seconds... loading...", Toast.LENGTH_LONG).show();}
                } else{

                if (player == null){
                    UnMuteAudio();
                    player.resumeMedia();
                    button15.setVisibility(Button.VISIBLE);
                    button12.setVisibility(Button.INVISIBLE);
                    button15land.setVisibility(Button.VISIBLE);
                    button12land.setVisibility(Button.INVISIBLE);
                    player.updateMetaData();
                    player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                            .setState(PlaybackStateCompat.STATE_PAUSED,player.timer2,0.0f).build());
                    player.buildNotification(PlaybackStatus.PAUSED);

                    player.mediaPlayer.pause();
                    player.pausepressed = 1;

                }else{
                    try{



                    if (player.mediaPlayer.isPlaying() ) {
                        UnMuteAudio();
                        player.mediaPlayer.pause();
                        player.resumePosition = player.mediaPlayer.getCurrentPosition();
                        player.buildNotification(PlaybackStatus.PAUSED);
                        button15.setVisibility(Button.VISIBLE);
                        button12.setVisibility(Button.INVISIBLE);
                        button15land.setVisibility(Button.VISIBLE);
                        button12land.setVisibility(Button.INVISIBLE);


                        player.mediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                                .setState(PlaybackStateCompat.STATE_PAUSED, player.resumePosition, 0.0f).build());
                        player.pausepressed = 1;

                } } catch (Exception io){
                        UnMuteAudio();
                        player.buildNotification(PlaybackStatus.PLAYING);
                        player.resumeMedia();
                        button12.setVisibility(Button.VISIBLE);
                        button15.setVisibility(Button.INVISIBLE);
                        button12land.setVisibility(Button.VISIBLE);
                        button15land.setVisibility(Button.INVISIBLE);
                        player.updateMetaData();
                        player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                                .setState(PlaybackStateCompat.STATE_PLAYING,player.timer2,1.0f).build());
                        player.pausepressed = 0;
                    }}}






            }});
        button12land.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (sortmode == 4){
                    player.buildNotification(PlaybackStatus.PAUSED);
                    button15.setVisibility(Button.VISIBLE);
                    button12.setVisibility(Button.INVISIBLE);
                    button15land.setVisibility(Button.VISIBLE);
                    button12land.setVisibility(Button.INVISIBLE);
                    MuteAudio();
                    try{
                        if (!player.mediaPlayer.isPlaying() || player.mediaPlayer == null){
                            playAudio(0);

                            Toast.makeText(MainActivity.this,
                                    "Please wait 15 seconds... loading...", Toast.LENGTH_LONG).show();
                        }} catch (Exception io){player.skipToNext();
                        Toast.makeText(MainActivity.this,
                                "Please wait 15 seconds... loading...", Toast.LENGTH_LONG).show();}

                } else{

                if (player == null ){
                    UnMuteAudio();
                    try{
                    player.resumeMedia();} catch (Exception io){}
                    button15.setVisibility(Button.VISIBLE);
                    button12.setVisibility(Button.INVISIBLE);
                    button15land.setVisibility(Button.VISIBLE);
                    button12land.setVisibility(Button.INVISIBLE);

                    player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                            .setState(PlaybackStateCompat.STATE_PAUSED,player.timer2,0.0f).build());
                    player.buildNotification(PlaybackStatus.PAUSED);
                    player.mediaPlayer.pause();

                    player.updateMetaData();
                    player.pausepressed = 1;

                } else{
try{

                if (player.mediaPlayer.isPlaying() ) {
                    UnMuteAudio();
                    player.mediaPlayer.pause();
                    player.resumePosition = player.mediaPlayer.getCurrentPosition();
                    player.buildNotification(PlaybackStatus.PAUSED);
                    button15.setVisibility(Button.VISIBLE);
                    button12.setVisibility(Button.INVISIBLE);
                    button15land.setVisibility(Button.VISIBLE);
                    button12land.setVisibility(Button.INVISIBLE);

                    player.pausepressed = 1;

                    player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                            .setState(PlaybackStateCompat.STATE_PAUSED,player.resumePosition,0.0f).build());


                }} catch (Exception io){
                        UnMuteAudio();
                        player.buildNotification(PlaybackStatus.PLAYING);
                        player.resumeMedia();
                        button12.setVisibility(Button.VISIBLE);
                        button15.setVisibility(Button.INVISIBLE);
                        button12land.setVisibility(Button.VISIBLE);
                        button15land.setVisibility(Button.INVISIBLE);
                        player.updateMetaData();
                        player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                                .setState(PlaybackStateCompat.STATE_PLAYING,player.timer2,1.0f).build());
    player.pausepressed = 0;
                    }}}






            }});

        final Button button16 = (Button) findViewById(R.id.button11);
        final Button button16land = (Button) findViewById(R.id.button11land);
        button16.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
            play =1;


                loadAudioList();
            loadAudio();

            boolean notused = true;
            try{
                Toast.makeText(MainActivity.this,
                        "Please wait... loading...", Toast.LENGTH_LONG).show();


                File f = new File(Environment.getExternalStorageDirectory()+
                        File.separator + "VaporMusic" ,"lastplayedmeta");

                if(!f.exists()){
                    loadAudioList();
                playAudio(0);
                    UnMuteAudio();} else{  readtext2();

                    player.updateMetaData();}

            } catch(Exception hhh){};
                checker();








            }});
        button16land.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {
                play =1;
                loadAudioList();
                loadAudio();
                try{
                    Toast.makeText(MainActivity.this,
                            "Please wait... loading...", Toast.LENGTH_LONG).show();
                    File f = new File(Environment.getExternalStorageDirectory()+
                            File.separator + "VaporMusic" ,"lastplayedmeta");

                    if(!f.exists()){
                        loadAudioList();
                        playAudio(0);
                        UnMuteAudio();} else{  readtext2();

                        player.updateMetaData();}
                } catch(Exception hhh){};
                checker();






            }});
        button15.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            { if (sortmode == 4){
                player.buildNotification(PlaybackStatus.PLAYING);
                button12.setVisibility(Button.VISIBLE);
                button15.setVisibility(Button.INVISIBLE);
                button12land.setVisibility(Button.VISIBLE);
                button15land.setVisibility(Button.INVISIBLE);
                UnMuteAudio();
                player.pausepressed = 0;
                if (!player.mediaPlayer.isPlaying() || player.mediaPlayer == null){
                    playAudio(0);
                    Toast.makeText(MainActivity.this,
                            "Please wait... loading...", Toast.LENGTH_LONG).show();
                }

            } else{
                if (player == null){
                    UnMuteAudio();
                    player.buildNotification(PlaybackStatus.PLAYING);
                    player.resumeMedia();
                    button12.setVisibility(Button.VISIBLE);
                    button15.setVisibility(Button.INVISIBLE);
                    button12land.setVisibility(Button.VISIBLE);
                    button15land.setVisibility(Button.INVISIBLE);
                    player.updateMetaData();
                    player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                            .setState(PlaybackStateCompat.STATE_PLAYING,player.timer2,1.0f).build());player.pausepressed = 0;


                } else{
try{
                if (!player.mediaPlayer.isPlaying()) {
                    UnMuteAudio();
                    player.mediaPlayer.seekTo(player.resumePosition);
                    player.mediaPlayer.start();
                    player.buildNotification(PlaybackStatus.PLAYING);
                   button12.setVisibility(Button.VISIBLE);
                   button15.setVisibility(Button.INVISIBLE);
                    button12land.setVisibility(Button.VISIBLE);
                    button15land.setVisibility(Button.INVISIBLE);
                    player.pausepressed = 0;
                    player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                            .setState(PlaybackStateCompat.STATE_PLAYING,player.resumePosition,1.0f).build());


                }}catch (Exception Io){
                    UnMuteAudio();
                    player.buildNotification(PlaybackStatus.PLAYING);
                    player.resumeMedia();
                    button12.setVisibility(Button.VISIBLE);
                    button15.setVisibility(Button.INVISIBLE);
                    button12land.setVisibility(Button.VISIBLE);
                    button15land.setVisibility(Button.INVISIBLE);
                    player.updateMetaData();player.pausepressed = 0;
                    player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                            .setState(PlaybackStateCompat.STATE_PLAYING,player.timer2,1.0f).build());
                }}}







            }});


        button15land.setOnClickListener( new View.OnClickListener()
        {
            public void onClick(View v)
            {if (sortmode == 4){
                player.buildNotification(PlaybackStatus.PLAYING);
                button12.setVisibility(Button.VISIBLE);
                button15.setVisibility(Button.INVISIBLE);
                button12land.setVisibility(Button.VISIBLE);
                button15land.setVisibility(Button.INVISIBLE);
                UnMuteAudio();player.pausepressed = 0;
                if (!player.mediaPlayer.isPlaying() || player.mediaPlayer == null){
                    playAudio(0);
                    Toast.makeText(MainActivity.this,
                            "Please wait... loading...", Toast.LENGTH_LONG).show();
                }

            } else{
                if (player == null){
                    UnMuteAudio();
                    player.buildNotification(PlaybackStatus.PLAYING);
                    player.resumeMedia();
                    button12.setVisibility(Button.VISIBLE);
                    button15.setVisibility(Button.INVISIBLE);
                    button12land.setVisibility(Button.VISIBLE);
                    button15land.setVisibility(Button.INVISIBLE);
                    player.pausepressed = 0;
                    player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                            .setState(PlaybackStateCompat.STATE_PLAYING,player.timer2,1.0f).build());


                } else{
try{
                if (!player.mediaPlayer.isPlaying()) {
                    UnMuteAudio();
                    player.mediaPlayer.seekTo(player.resumePosition);
                    player.mediaPlayer.start();
                    player.buildNotification(PlaybackStatus.PLAYING);
                    button12.setVisibility(Button.VISIBLE);
                    button15.setVisibility(Button.INVISIBLE);
                    button12land.setVisibility(Button.VISIBLE);
                    button15land.setVisibility(Button.INVISIBLE);
                    player.pausepressed = 0;
                    player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                            .setState(PlaybackStateCompat.STATE_PLAYING,player.resumePosition,1.0f).build());


                }}catch (Exception Io){
    UnMuteAudio();
    player.buildNotification(PlaybackStatus.PLAYING);
    player.resumeMedia();
    button12.setVisibility(Button.VISIBLE);
    button15.setVisibility(Button.INVISIBLE);
    button12land.setVisibility(Button.VISIBLE);
    button15land.setVisibility(Button.INVISIBLE);
    player.updateMetaData();player.pausepressed = 0;
    player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
            .setState(PlaybackStateCompat.STATE_PLAYING,player.timer2,1.0f).build());
}}}







            }});
        final Button button13 = (Button) findViewById(R.id.button9a);
        button13.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (shufflepress % 2 == 1){
                                    Random r = new Random();
                                    int i1 = r.nextInt(audioList.size() - 0) + 0;
                                    playAudio(i1);
                                } else{
                                player.skipToPrevious();}
                                player.updateMetaData();
                                player.buildNotification(PlaybackStatus.PLAYING);
                                button12land.setVisibility(Button.VISIBLE);
                                button15land.setVisibility(Button.INVISIBLE);
                                button12.setVisibility(Button.VISIBLE);
                                button15.setVisibility(Button.INVISIBLE);

                            }
                        });
        final Button button13land = (Button) findViewById(R.id.button9aland);
        button13land.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (shufflepress % 2 == 1){
                                    Random r = new Random();
                                    int i1 = r.nextInt(audioList.size() - 0) + 0;
                                    playAudio(i1);
                                } else{
                                player.skipToPrevious();}
                                player.updateMetaData();
                                player.buildNotification(PlaybackStatus.PLAYING);
                                button12.setVisibility(Button.VISIBLE);
                                button15.setVisibility(Button.INVISIBLE);
                                button12land.setVisibility(Button.VISIBLE);
                                button15land.setVisibility(Button.INVISIBLE);

                            }
                        });
        final Button button14 = (Button) findViewById(R.id.button9b);
        final Button button14land = (Button) findViewById(R.id.button9bland);
        button14.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (shufflepress % 2 == 1){
                                    Random r = new Random();
                                    int i1 = r.nextInt(audioList.size() - 0) + 0;
                                    playAudio(i1);
                                } else{
                                player.skipToNext();}
                                player.updateMetaData();
                                player.buildNotification(PlaybackStatus.PLAYING);
                                button12.setVisibility(Button.VISIBLE);
                                button15.setVisibility(Button.INVISIBLE);
                                button12land.setVisibility(Button.VISIBLE);
                                button15land.setVisibility(Button.INVISIBLE);

                            }
                        });
        button14land.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (shufflepress % 2 == 1){
                                    Random r = new Random();
                                    int i1 = r.nextInt(audioList.size() - 0) + 0;
                                    playAudio(i1);
                                } else{
                                player.skipToNext();}
                                player.updateMetaData();
                                player.buildNotification(PlaybackStatus.PLAYING);
                                button12.setVisibility(Button.VISIBLE);
                                button15.setVisibility(Button.INVISIBLE);
                                button12land.setVisibility(Button.VISIBLE);
                                button15land.setVisibility(Button.INVISIBLE);

                            }
                        });

        final Button button4i = (Button) findViewById(R.id.hidenp);
        final Button button4i2 = (Button) findViewById(R.id.shownp);

        final FrameLayout frame1 = (FrameLayout) findViewById(R.id.frame1);
        final FrameLayout frame2 = (FrameLayout) findViewById(R.id.frame2);
        final FrameLayout frame3 = (FrameLayout) findViewById(R.id.frame3);
        final FrameLayout frame4 = (FrameLayout) findViewById(R.id.frame4);
        final FrameLayout frame5 = (FrameLayout) findViewById(R.id.frame5);
        final FrameLayout framex = (FrameLayout) findViewById(R.id.framex);
        final Button button4iland = (Button) findViewById(R.id.hidenpland);


        final FrameLayout frame1land = (FrameLayout) findViewById(R.id.frame1land);
        final FrameLayout frame2land = (FrameLayout) findViewById(R.id.frame2land);
        final FrameLayout frame3land = (FrameLayout) findViewById(R.id.frame3land);
        final FrameLayout frame4land = (FrameLayout) findViewById(R.id.frame4land);
        final FrameLayout frame5land = (FrameLayout) findViewById(R.id.frame5land);

        button4i.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (frame5.getVisibility() == View.VISIBLE) {
                                    frame1.setVisibility(View.INVISIBLE);
                                    frame2.setVisibility(View.INVISIBLE);
                                    frame3.setVisibility(View.INVISIBLE);
                                    frame4.setVisibility(View.INVISIBLE);
                                    frame5.setVisibility(View.INVISIBLE);
                                    framex.setVisibility(View.VISIBLE);

                                }

                            }
                        });
        button4iland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (frame5land.getVisibility() == View.VISIBLE) {
                                    frame1land.setVisibility(View.INVISIBLE);
                                    frame2land.setVisibility(View.INVISIBLE);
                                    frame3land.setVisibility(View.INVISIBLE);
                                    frame4land.setVisibility(View.INVISIBLE);
                                    frame5land.setVisibility(View.INVISIBLE);
                                    framex.setVisibility(View.VISIBLE);
                                }

                            }
                        });
        button4i2.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                final FrameLayout portraitmode = findViewById(R.id.portraiter);
                                final FrameLayout landscapemode = findViewById(R.id.landscaper);
                                    portraitmode.setVisibility(View.VISIBLE);
                                    landscapemode.setVisibility(View.VISIBLE);
                                    if (portrait == 1){
                                        frame1land.setVisibility(View.INVISIBLE);
                                        frame2land.setVisibility(View.INVISIBLE);
                                        frame3land.setVisibility(View.INVISIBLE);
                                        frame4land.setVisibility(View.INVISIBLE);
                                        frame5land.setVisibility(View.INVISIBLE);
                                    frame1.setVisibility(View.VISIBLE);
                                    frame2.setVisibility(View.VISIBLE);
                                    frame3.setVisibility(View.VISIBLE);
                                    frame4.setVisibility(View.VISIBLE);
                                    frame5.setVisibility(View.VISIBLE);}
                                    if (portrait == 0){
                                        frame1.setVisibility(View.INVISIBLE);
                                        frame2.setVisibility(View.INVISIBLE);
                                        frame3.setVisibility(View.INVISIBLE);
                                        frame4.setVisibility(View.INVISIBLE);
                                        frame5.setVisibility(View.INVISIBLE);
                                        frame1land.setVisibility(View.VISIBLE);
                                        frame2land.setVisibility(View.VISIBLE);
                                        frame3land.setVisibility(View.VISIBLE);
                                        frame4land.setVisibility(View.VISIBLE);
                                        frame5land.setVisibility(View.VISIBLE);}

                                    framex.setVisibility(View.INVISIBLE);


                            }
                        });

        final Button about = (Button) findViewById(R.id.button8);
        final Button settings = (Button) findViewById(R.id.button6);
        final Button eq = findViewById(R.id.button12x);
        final Button selectbg = findViewById(R.id.choosebg);
        final FrameLayout aboutpage = (FrameLayout) findViewById(R.id.aboutpage);
        final FrameLayout settingpage = (FrameLayout) findViewById(R.id.settingsmenu);
        final Button hideset = findViewById(R.id.button15x);
        final Button buttonxxx = (Button) findViewById(R.id.hideabout);
        final Button resetbg = findViewById(R.id.resetbg);
        final Button aboutland = (Button) findViewById(R.id.button8land);
        final Button settingsland = (Button) findViewById(R.id.button6land);
        final Button eqland = findViewById(R.id.button12xland);
        final Button selectbgland = findViewById(R.id.choosebgland);
        final FrameLayout aboutpageland = (FrameLayout) findViewById(R.id.aboutpageland);
        final FrameLayout settingpageland = (FrameLayout) findViewById(R.id.settingsmenuland);
        final Button hidesetland = findViewById(R.id.button15xland);
        final Button buttonxxxland = (Button) findViewById(R.id.hideaboutland);
        final Button resetbgland = findViewById(R.id.resetbgland);
        aboutland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (aboutpage.getVisibility() == View.INVISIBLE) {
                                    aboutpage.setVisibility(View.VISIBLE);
                                    aboutpageland.setVisibility(View.VISIBLE);

                                }


                            }
                        });
        about.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (aboutpageland.getVisibility() == View.INVISIBLE) {
                                    aboutpage.setVisibility(View.VISIBLE);
                                    aboutpageland.setVisibility(View.VISIBLE);
                                }


                            }
                        });

       selectbg.setOnClickListener
                (

                        new View.OnClickListener()
                        {

                            public void onClick(View v)
                            {

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 1);
                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);




                            }




                        }


                        )
       ;
        selectbgland.setOnClickListener
                (

                        new View.OnClickListener()
                        {

                            public void onClick(View v)
                            {

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 1);
                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);




                            }




                        }


                )
        ;
        resetbg.setOnClickListener
                (

                        new View.OnClickListener()
                        {

                            public void onClick(View v)
                            {
                                if (Integer.parseInt(mem) < 900000){
                                    GifImageView mybg = findViewById(R.id.mainbg);
                                    mybg.setImageResource(R.drawable.bg35);
                                    try{
                                        File f = new File(Environment.getExternalStorageDirectory()+
                                                File.separator + "VaporMusic" ,"bgsaved");
                                        f.delete();} catch (Exception hhhh){}

                                } else {
                                GifImageView mybg = findViewById(R.id.mainbg);
                                mybg.setImageResource(R.drawable.bg34);
                                try{
                                File f = new File(Environment.getExternalStorageDirectory()+
                                        File.separator + "VaporMusic" ,"bgsaved");
                                f.delete();} catch (Exception hhhh){}
                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);}





                            }




                        }


                )
        ;
        resetbgland.setOnClickListener
                (

                        new View.OnClickListener()
                        {

                            public void onClick(View v)
                            {
                                if (Integer.parseInt(mem) < 900000){
                                    GifImageView mybg = findViewById(R.id.mainbg);
                                    mybg.setImageResource(R.drawable.bg35);
                                    try{
                                        File f = new File(Environment.getExternalStorageDirectory()+
                                                File.separator + "VaporMusic" ,"bgsaved");
                                        f.delete();} catch (Exception hhhh){}

                                } else{
                                GifImageView mybg = findViewById(R.id.mainbg);
                                mybg.setImageResource(R.drawable.bg34);
                                try{
                                    File f = new File(Environment.getExternalStorageDirectory()+
                                            File.separator + "VaporMusic" ,"bgsaved");
                                    f.delete();} catch (Exception hhhh){}
                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);}





                            }




                        }


                )
        ;


        buttonxxx.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                if (aboutpage.getVisibility() == View.VISIBLE) {
                                    aboutpage.setVisibility(View.INVISIBLE);
                                    aboutpageland.setVisibility(View.INVISIBLE);
                                }
                                if (aboutpageland.getVisibility() == View.VISIBLE) {
                                    aboutpage.setVisibility(View.INVISIBLE);
                                    aboutpageland.setVisibility(View.INVISIBLE);
                                }

                            }
                        });
        buttonxxxland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                if (aboutpage.getVisibility() == View.VISIBLE) {
                                    aboutpage.setVisibility(View.INVISIBLE);
                                    aboutpageland.setVisibility(View.INVISIBLE);
                                }
                                if (aboutpageland.getVisibility() == View.VISIBLE) {
                                    aboutpage.setVisibility(View.INVISIBLE);
                                    aboutpageland.setVisibility(View.INVISIBLE);
                                }

                            }
                        });
        eq.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
try{
                                Intent intent2 = new Intent();
                                intent2.setAction("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL");
                                startActivityForResult(intent2, 0);
                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);} catch (Exception io){
    Toast.makeText(MainActivity.this,
            "Internal EQ missing", Toast.LENGTH_SHORT).show();
}

                            }
                        });
        eqland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                try{
                                Intent intent2 = new Intent();
                                intent2.setAction("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL");
                                startActivityForResult(intent2, 0);
                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);} catch (Exception io){
                                    Toast.makeText(MainActivity.this,
                                            "Internal EQ missing", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
        settings.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                settingpage.setVisibility(View.VISIBLE);
                                settingpageland.setVisibility(View.VISIBLE);
                            }
                        });
        settingsland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                settingpage.setVisibility(View.VISIBLE);
                                settingpageland.setVisibility(View.VISIBLE);

                            }
                        });
        hideset.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);
                            }
                        });
        hidesetland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);
                            }
                        });


        final Button fscr = findViewById(R.id.fullscr);
        fscr.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);

                                fullscreen += 1;

                                if (fullscreen % 2 == 1) {

                                    View decorView = getWindow().getDecorView();
                                    decorView.setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                    );
                                }
                                if (fullscreen % 2 == 0)
                                {
                                    View decorView = getWindow().getDecorView();
                                    decorView.setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
                                }


                            }
                        });
        final Button fscrland = findViewById(R.id.fullscrland);
        fscrland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                fullscreen += 1;

                                if (fullscreen % 2 == 1) {

                                    View decorView = getWindow().getDecorView();
                                    decorView.setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                   );
                                }
                                if (fullscreen % 2 == 0)
                                {
                                    View decorView = getWindow().getDecorView();
                                    decorView.setSystemUiVisibility(
                                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
                                }


                            }
                        });

        final Button seekbt = (Button) findViewById(R.id.button12);
        final FrameLayout seekpage = (FrameLayout) findViewById(R.id.seekpage);
        final FrameLayout modepage = (FrameLayout) findViewById(R.id.mode_menu);
        final Button seekbtland = (Button) findViewById(R.id.button12land);
        final FrameLayout seekpageland = (FrameLayout) findViewById(R.id.seekpageland);
        final FrameLayout modepageland = (FrameLayout) findViewById(R.id.mode_menuland);


        seekbt.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                if (seekpage.getVisibility() == View.INVISIBLE) {
                                    seekpage.setVisibility(View.VISIBLE);
                                    modepage.setVisibility(View.INVISIBLE);
                                }
                                if (player == null ) {
                                    seekpage.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this,
                                            "No song currenlty playing!", Toast.LENGTH_SHORT).show();


                                }
                                    else{showKeyboard(MainActivity.this);
                                    }

                            }
                        });
        seekbtland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                if (seekpageland.getVisibility() == View.INVISIBLE) {
                                    seekpageland.setVisibility(View.VISIBLE);
                                    modepageland.setVisibility(View.INVISIBLE);
                                }
                                if (player == null) {
                                    seekpageland.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this,
                                            "No song currenlty playing!", Toast.LENGTH_SHORT).show();


                                }
                                else{showKeyboard(MainActivity.this);
                                    }

                            }
                        });



        final EditText edittext = (EditText) findViewById(R.id.edittext);

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                if (edittext.getText().length() == 2) {
                    edittext.setText(edittext.getText() + ":");
                    edittext.setSelection(edittext.getText().length());
                }
                if (edittext.getText().length() == 5) {
                    seekpage.setVisibility(View.INVISIBLE);
                    if (rotatelock %2 == 0){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}


                    SimpleDateFormat seekstring = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                    String seekposition = edittext.getText().toString();
                    String[] parts = seekposition.split(":");
                    String part1 = parts[0];
                    String part2 = parts[1];
                    int minutes1 = Integer.parseInt(part1) * 60000 ;
                    int seconds1 = Integer.parseInt(part2) * 1000;
                    int time = minutes1 + seconds1;
                    String timestring = String.valueOf(time);
                    Long timeinlong = Long.parseLong(timestring);



                        if (time < player.mediaPlayer.getDuration()){
                        player.mediaPlayer.seekTo(time);
                        player.mediaPlayer.start();
                        player.buildNotification(PlaybackStatus.PLAYING);
                        button12.setVisibility(Button.VISIBLE);
                        button15.setVisibility(Button.INVISIBLE);

                        player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                                .setState(PlaybackStateCompat.STATE_PLAYING,time,1.0f).build());
                            edittext.getText().clear();
                  hideKeyboard(MainActivity.this);
                            modepage.setVisibility(View.INVISIBLE);

                    }
                     else {
                        seekpage.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this,
                                "Invalid time!", Toast.LENGTH_SHORT).show();
                            edittext.getText().clear();

                    }








                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        final EditText edittextland = (EditText) findViewById(R.id.edittextland);

        edittextland.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                if (edittextland.getText().length() == 2) {
                    edittextland.setText(edittextland.getText() + ":");
                    edittextland.setSelection(edittextland.getText().length());
                }
                if (edittextland.getText().length() == 5) {
                    seekpageland.setVisibility(View.INVISIBLE);
                    if (rotatelock % 2 == 0)
                    {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}

                    SimpleDateFormat seekstring = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
                    String seekposition = edittextland.getText().toString();
                    String[] partsland = seekposition.split(":");
                    String part1land = partsland[0];
                    String part2land = partsland[1];
                    int minutes1land = Integer.parseInt(part1land) * 60000 ;
                    int seconds1land = Integer.parseInt(part2land) * 1000;
                    int timeland = minutes1land + seconds1land;
                    String timestringland = String.valueOf(timeland);
                    Long timeinlong = Long.parseLong(timestringland);



                    if (timeland < player.mediaPlayer.getDuration()){
                        player.mediaPlayer.seekTo(timeland);
                        player.mediaPlayer.start();
                        player.buildNotification(PlaybackStatus.PLAYING);
                        button12land.setVisibility(Button.VISIBLE);
                        button15land.setVisibility(Button.INVISIBLE);
                        button12.setVisibility(Button.VISIBLE);
                        button15.setVisibility(Button.INVISIBLE);

                        player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                                .setState(PlaybackStateCompat.STATE_PLAYING,timeland,1.0f).build());
                        edittextland.getText().clear();
                        edittext.getText().clear();
                        hideKeyboard(MainActivity.this);
                        modepage.setVisibility(View.INVISIBLE);
                        modepageland.setVisibility(View.INVISIBLE);

                    }
                    else {
                        seekpageland.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this,
                                "Invalid time!", Toast.LENGTH_SHORT).show();
                        edittext.getText().clear();
                        edittextland.getText().clear();

                    }








                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        final Button rlock = findViewById(R.id.rotatelock);
        final Button rlockland = findViewById(R.id.rotatelockland);



        rlock.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                rotatelock += 1;

                                if (rotatelock % 2 == 1) {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                                    rlock.setText(R.string.RotateUn);
                                    rlockland.setText(R.string.RotateUnL);

                                }
                                if (rotatelock % 2 == 0)
                                {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                    rlock.setText(R.string.Rotate);
                                    rlockland.setText(R.string.RotateL);
                                }


                            }
                        });
        rlockland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                rotatelock += 1;

                                if (rotatelock % 2 == 1) {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                                    rlockland.setText(R.string.RotateUnL);
                                    rlock.setText(R.string.RotateUn);
                                }
                                if (rotatelock % 2 == 0)
                                {
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                                    rlockland.setText(R.string.RotateL);
                                    rlock.setText(R.string.Rotate);
                                }


                            }
                        });

        final Button repeatbutton = findViewById(R.id.button14);
        final Button repon = findViewById(R.id.rpoff);
        final Button repeatbuttonland = findViewById(R.id.button14land);
        final Button reponland = findViewById(R.id.rpoffland);
        repeatbutton.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                               modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);
                               repeatpress += 1;
                                String q = String.valueOf(repeatpress);

                               if (repeatpress % 2 == 1) {
                                   repon.setVisibility(Button.INVISIBLE);
                                   reponland.setVisibility(Button.INVISIBLE);
                               }
                               if (repeatpress % 2 == 0)
                               {
                                   repon.setVisibility(Button.VISIBLE);
                                   reponland.setVisibility(Button.VISIBLE);
                               }


                            }
                        });
        repeatbuttonland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);
                                repeatpress += 1;
                                String q = String.valueOf(repeatpress);
                                Log.d("pressed",q);
                                if (repeatpress % 2 == 1) {
                                    repon.setVisibility(Button.INVISIBLE);
                                    reponland.setVisibility(Button.INVISIBLE);
                                }
                                if (repeatpress % 2 == 0)
                                {
                                    repon.setVisibility(Button.VISIBLE);
                                    reponland.setVisibility(Button.VISIBLE);
                                }


                            }
                        });
        final Button shufflebutton = findViewById(R.id.button13);
        final Button shuffleon = findViewById(R.id.sfoff);
        final Button shufflebuttonland = findViewById(R.id.button13land);
        final Button shuffleonland = findViewById(R.id.sfoffland);
        shufflebutton.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);
                                shufflepress += 1;
                                String q = String.valueOf(shufflepress);
                                Intent intent45 = new Intent();
                                intent45.setAction("shufflereceiver");

                                Bundle bundlex2222 = new Bundle();

                                // put the song's metadata
                                String shufflepressstr = String.valueOf(shufflepress);
                                bundlex2222.putString("shuffle1234",shufflepressstr);



                                // put the playback status

                                // put your application's package


                                intent45.putExtras(bundlex2222);
                                sendBroadcast(intent45);


                                if (shufflepress % 2 == 1) {
                                    shuffleon.setVisibility(Button.INVISIBLE);
                                    shuffleonland.setVisibility(Button.INVISIBLE);
                                }
                                if (shufflepress % 2 == 0)
                                {
                                    shuffleonland.setVisibility(Button.VISIBLE);
                                    shuffleon.setVisibility(Button.VISIBLE);
                                }


                            }
                        });
        shufflebuttonland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);
                                shufflepress += 1;
                                String q = String.valueOf(shufflepress);
                                Intent intent78 = new Intent();
                                intent78.setAction("shufflereceiver");

                                Bundle bundlex111 = new Bundle();

                                // put the song's metadata
                                String shufflepressstr = String.valueOf(shufflepress);
                                bundlex111.putString("shuffle1234",shufflepressstr);




                                // put the playback status

                                // put your application's package


                                intent78.putExtras(bundlex111);
                                sendBroadcast(intent78);

                                if (shufflepress % 2 == 1) {
                                    shuffleon.setVisibility(Button.INVISIBLE);
                                    shuffleonland.setVisibility(Button.INVISIBLE);
                                }
                                if (shufflepress % 2 == 0)
                                {
                                    shuffleonland.setVisibility(Button.VISIBLE);
                                    shuffleon.setVisibility(Button.VISIBLE);
                                }


                            }
                        });

        if (checkAndRequestPermissions()) {
            loadAudioList();
        }
        final Button mode = (Button) findViewById(R.id.button7b);

        final Button closemode= (Button) findViewById(R.id.button15);
        final Button modeland = (Button) findViewById(R.id.button7bland);

        final Button closemodeland= (Button) findViewById(R.id.button15land);
        mode.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (modepage.getVisibility() == View.INVISIBLE) {
                                   modepage.setVisibility(View.VISIBLE);
                                   modepageland.setVisibility(View.VISIBLE);

                                }


                            }
                        });
        closemode.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                if (modepage.getVisibility() == View.VISIBLE) {
                                    modepage.setVisibility(View.INVISIBLE);
                                    modepageland.setVisibility(View.INVISIBLE);

                                }

                            }
                        });
        modeland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (modepage.getVisibility() == View.INVISIBLE) {
                                    modepage.setVisibility(View.VISIBLE);
                                    modepageland.setVisibility(View.VISIBLE);

                                }


                            }
                        });
        closemodeland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                if (modepage.getVisibility() == View.VISIBLE) {
                                    modepage.setVisibility(View.INVISIBLE);
                                    modepageland.setVisibility(View.INVISIBLE);

                                }

                            }
                        });






        final Button hideseek = findViewById(R.id.hideabout2);
        hideseek.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                seekpage.setVisibility(View.INVISIBLE);
                                seekpageland.setVisibility(View.INVISIBLE);
                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);
                               hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                            }
                        });
        final Button hideseekland = findViewById(R.id.hideabout2land);
        hideseekland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                seekpage.setVisibility(View.INVISIBLE);
                                seekpageland.setVisibility(View.INVISIBLE);
                                settingpage.setVisibility(View.INVISIBLE);
                                settingpageland.setVisibility(View.INVISIBLE);
                            hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                            }
                        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                playAudio("https://upload.wikimedia.org/wikipedia/commons/6/6c/Grieg_Lyric_Pieces_Kobold.ogg");
                //play the first audio in the ArrayList
//                playAudio(2);
                if (imageIndex == 4) {
                    imageIndex = 0;
                    loadCollapsingImage(imageIndex);
                } else {
                    loadCollapsingImage(++imageIndex);
                }
            }
        });

       final Button artistlist = findViewById(R.id.artistlist);
        final Button albumlist = findViewById(R.id.albumlist);
        final Button tracklist = findViewById(R.id.tracklist);
        final Button searchlist = findViewById(R.id.searchlist);
        final FrameLayout searchbar = findViewById(R.id.searchbar);
        final EditText edittext2 = (EditText) findViewById(R.id.edittext2);
        final Button  button = findViewById(R.id.ok);
        final Button  button2 = findViewById(R.id.clearsearch);
        searchlist.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                        final FrameLayout textinfo = findViewById(R.id.textinfo);
                                        textinfo.setVisibility(View.INVISIBLE);

                                sortmode=3;
                                searchbar.setVisibility(View.VISIBLE);





                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                                searchlist.setBackgroundResource(R.drawable.freebutton7);
                                searchlist.setTextColor(Color.WHITE);





                            }
                        });


        button.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {try{

                                ok =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                loadAudio2();
                                storage.storeAudio(audioList);

                                laststr = searchstr;
                   hideKeyboard(MainActivity.this);


                                loadAudio2();
                                storage.storeAudio(audioList);
                                initRecyclerView2();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);




                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);
                            } catch (Exception io){}


                            }
                        });
        button2.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                ok =0;
                                edittext2.getText().clear();

                                try{
                                    } catch (Exception io){}
                                laststr = searchstr;
            hideKeyboard(MainActivity.this);
                                StorageUtil storage = new StorageUtil(getApplicationContext());



                                loadAudio2();
                                storage.storeAudio(audioList);
                                initRecyclerView2();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);




                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);



                            }
                        });

        edittext2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                searchstr = edittext2.getText().toString();












            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchstr = edittext2.getText().toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchstr = edittext2.getText().toString();

            }
        });
        tracklist.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                final FrameLayout textinfo = findViewById(R.id.textinfo);
                                textinfo.setVisibility(View.INVISIBLE);
                                try{
                                    } catch (Exception io){}
                                searchbar.setVisibility(View.VISIBLE);
                                sortmode =0; ok =0;
                                StorageUtil storage = new StorageUtil(getApplicationContext());

                                tracklist.setBackgroundResource(R.drawable.freebutton7);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.WHITE);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);

                                loadAudio2();storage.storeAudio(audioList);
                                initRecyclerView2();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);




                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);



                            }
                        });
        artistlist.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                final FrameLayout textinfo = findViewById(R.id.textinfo);
                                textinfo.setVisibility(View.INVISIBLE);
                                try{
                                    } catch (Exception io){}
                                searchbar.setVisibility(View.VISIBLE);
                                ok = 0;
                                StorageUtil storage = new StorageUtil(getApplicationContext());

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton7);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.WHITE);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);

                                sortmode =1;
                                loadAudio2()
                                ;storage.storeAudio(audioList);
                                initRecyclerView2();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);




                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                            }
                        });

        albumlist.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {ok =0;
                            final FrameLayout textinfo = findViewById(R.id.textinfo);
                                textinfo.setVisibility(View.INVISIBLE);
                                try{
                                audioplaylist.clear();} catch (Exception io){}
                                searchbar.setVisibility(View.VISIBLE);
                                StorageUtil storage = new StorageUtil(getApplicationContext());

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton7);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);

                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);

                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.WHITE);
                                artistlist.setTextColor(Color.BLACK);
                                sortmode =2;
                                loadAudio2();
                                storage.storeAudio(audioList);
                                initRecyclerView2();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);




                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);





                            }
                        });
final Button radio = findViewById(R.id.radio);




        final FrameLayout radiopage = (FrameLayout) findViewById(R.id.radiopage);

        final Button radioland = (Button) findViewById(R.id.radioland);
        final Button change = (Button) findViewById(R.id.button17);
        final Button changeland = (Button) findViewById(R.id.button17land);
        final FrameLayout radiopageland = (FrameLayout) findViewById(R.id.radiopageland);

        final EditText edittext3 = (EditText) findViewById(R.id.edittext3);
        final EditText edittext3land = (EditText) findViewById(R.id.edittext3);
        radio.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {change.setVisibility(View.VISIBLE);
                                changeland.setVisibility(View.VISIBLE);
                                File f = new File(Environment.getExternalStorageDirectory() +
                                        File.separator + "VaporMusic","radio");

                                if (f.exists()) {
                                    try{
                                        File f4 = new File(Environment.getExternalStorageDirectory() +
                                                File.separator + "VaporMusic","radio");
                                        FileInputStream is = new FileInputStream(f4);
                                        int size = is.available();
                                        byte[] buffer = new byte[size];
                                        is.read(buffer);
                                        is.close();
                                        lastradio= new String(buffer);
                                        edittext3.getText().clear();
                                        edittext3land.getText().clear();
                                    } catch (Exception io){}
                                    Toast.makeText(MainActivity.this,
                                            "Click OK to listen to saved playlist ", Toast.LENGTH_LONG).show();
                            }
                                if (radiopage.getVisibility() == View.INVISIBLE) {
                                    radiopage.setVisibility(View.VISIBLE);
                                    radiopageland.setVisibility(View.VISIBLE);
                                    modepage.setVisibility(View.INVISIBLE);
                                }
                              showKeyboard(MainActivity.this);


                            }
                        });
      radioland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {change.setVisibility(View.VISIBLE);
                                changeland.setVisibility(View.VISIBLE);
                                File f = new File(Environment.getExternalStorageDirectory() +
                                        File.separator + "VaporMusic","radio");

                                if (f.exists()) {
                                    try{
                                        File f4 = new File(Environment.getExternalStorageDirectory() +
                                                File.separator + "VaporMusic","radio");
                                        FileInputStream is = new FileInputStream(f4);
                                        int size = is.available();
                                        byte[] buffer = new byte[size];
                                        is.read(buffer);
                                        is.close();
                                        lastradio= new String(buffer);
                                    edittext3.getText().clear();
                                        edittext3land.getText().clear();
                                    } catch (Exception io){}

                                    Toast.makeText(MainActivity.this,
                                            "Click OK to listen to saved playlist ", Toast.LENGTH_LONG).show();
                                }
                                if (radiopageland.getVisibility() == View.INVISIBLE) {
                                    radiopage.setVisibility(View.VISIBLE);
                                    radiopageland.setVisibility(View.VISIBLE);
                                    modepage.setVisibility(View.INVISIBLE);
                                }
                                showKeyboard(MainActivity.this);


                            }
                        });





        edittext3.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                lastradio = edittext3.getText().toString();










            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                lastradio = edittext3.getText().toString();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                lastradio = edittext3.getText().toString();
            }
        });


        edittext3land.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                lastradio = edittext3land.getText().toString();










            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lastradio = edittext3land.getText().toString();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastradio = edittext3land.getText().toString();
            }
        });
        final Button hideradio = findViewById(R.id.button16);
        hideradio.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modeland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                                searchbar.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this,
                                        "Please wait 15 seconds...buffering...", Toast.LENGTH_LONG).show();
                                sortmode =4; ok =1; yeah =1;
                                play =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                storage.killthefile();


                                loadAudio();
                                storage.storeAudio(audioList);
                                initRecyclerView();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);



                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                                playAudio(0);
                                Button albumlist = findViewById(R.id.albumlist);
                                Button tracklist = findViewById(R.id.tracklist);
                                Button searchlist = findViewById(R.id.searchlist);
                                Button artistlist = findViewById(R.id.artistlist);

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                            }
                        });
        final Button hideradioland = findViewById(R.id.button16land);
        hideradioland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {


                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                                searchbar.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this,
                                        "Please wait 15 seconds...buffering...", Toast.LENGTH_LONG).show();
                                sortmode =4; ok =1; yeah =1;
                                play =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                storage.killthefile();


                                loadAudio();
                                storage.storeAudio(audioList);
                                initRecyclerView();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle1234",shufflepressstr);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);



                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                                playAudio(0);
                                Button albumlist = findViewById(R.id.albumlist);
                                Button tracklist = findViewById(R.id.tracklist);
                                Button searchlist = findViewById(R.id.searchlist);
                                Button artistlist = findViewById(R.id.artistlist);

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                            }
                        });

        final Button hideradio2 = findViewById(R.id.hideabout2o);
        hideradio2.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                            }
                        });

        changeland.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                changeland.setVisibility(View.INVISIBLE);}

                        });

        change.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {

                                change.setVisibility(View.INVISIBLE);}

                        });
          final  Button lofi1 = findViewById(R.id.lofi1);
        final  Button lofi2 = findViewById(R.id.lofi2);
        final  Button lofi3 = findViewById(R.id.lofi3);
        final  Button lofi4 = findViewById(R.id.lofi4);
        final  Button vapor1 = findViewById(R.id.vapor1);
        final  Button vapor2 = findViewById(R.id.vapor2);
        final  Button vapor3 = findViewById(R.id.vapor3);
        final  Button vapor4 = findViewById(R.id.vapor4);

        lofi1.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                lastradio ="http://vapormusic.herokuapp.com/hHW1oY26kxQ";

                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modeland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                                searchbar.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this,
                                        "Please wait 15 seconds...buffering...", Toast.LENGTH_LONG).show();
                                sortmode =4; ok =1; yeah =1;
                                play =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                storage.killthefile();


                                loadAudio();
                                storage.storeAudio(audioList);
                                initRecyclerView();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);



                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                                playAudio(0);
                                Button albumlist = findViewById(R.id.albumlist);
                                Button tracklist = findViewById(R.id.tracklist);
                                Button searchlist = findViewById(R.id.searchlist);
                                Button artistlist = findViewById(R.id.artistlist);

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                            }
                        });
        lofi2.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                lastradio ="http://vapormusic.herokuapp.com/5xVymfmXKko";
                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modeland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                                searchbar.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this,
                                        "Please wait 15 seconds ...buffering...", Toast.LENGTH_LONG).show();
                                sortmode =4; ok =1; yeah =1;
                                play =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                storage.killthefile();


                                loadAudio();
                                storage.storeAudio(audioList);
                                initRecyclerView();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);



                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                                playAudio(0);
                                Button albumlist = findViewById(R.id.albumlist);
                                Button tracklist = findViewById(R.id.tracklist);
                                Button searchlist = findViewById(R.id.searchlist);
                                Button artistlist = findViewById(R.id.artistlist);

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                            }
                        });
        lofi3.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                lastradio ="http://vapormusic.herokuapp.com/LsBrT6vbQa8";
                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modeland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                                searchbar.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this,
                                        "Please wait 15 seconds...buffering...", Toast.LENGTH_LONG).show();
                                sortmode =4; ok =1; yeah =1;
                                play =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                storage.killthefile();


                                loadAudio();
                                storage.storeAudio(audioList);
                                initRecyclerView();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);



                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                                playAudio(0);
                                Button albumlist = findViewById(R.id.albumlist);
                                Button tracklist = findViewById(R.id.tracklist);
                                Button searchlist = findViewById(R.id.searchlist);
                                Button artistlist = findViewById(R.id.artistlist);

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                            }
                        });
        lofi4.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                lastradio ="http://vapormusic.herokuapp.com/jUa7BHoyfDk";
                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modeland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                                searchbar.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this,
                                        "Please wait 15 seconds...buffering...", Toast.LENGTH_LONG).show();
                                sortmode =4; ok =1; yeah =1;
                                play =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                storage.killthefile();


                                loadAudio();
                                storage.storeAudio(audioList);
                                initRecyclerView();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);



                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                                playAudio(0);
                                Button albumlist = findViewById(R.id.albumlist);
                                Button tracklist = findViewById(R.id.tracklist);
                                Button searchlist = findViewById(R.id.searchlist);
                                Button artistlist = findViewById(R.id.artistlist);

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                            }
                        });
        vapor1.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                    lastradio ="http://radio.plaza.one/mp3";
                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modeland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                                searchbar.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this,
                                        "Please wait 15 seconds...buffering...", Toast.LENGTH_LONG).show();
                                sortmode =4; ok =1; yeah =1;
                                play =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                storage.killthefile();


                                loadAudio();
                                storage.storeAudio(audioList);
                                initRecyclerView();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);



                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                                playAudio(0);
                                Button albumlist = findViewById(R.id.albumlist);
                                Button tracklist = findViewById(R.id.tracklist);
                                Button searchlist = findViewById(R.id.searchlist);
                                Button artistlist = findViewById(R.id.artistlist);

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                            }
                        });
        vapor2.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                lastradio ="http://vapormusic.herokuapp.com/fxn8p26WTR4";
                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modeland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                                searchbar.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this,
                                        "Please wait 15 seconds...buffering...", Toast.LENGTH_LONG).show();
                                sortmode =4; ok =1; yeah =1;
                                play =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                storage.killthefile();


                                loadAudio();
                                storage.storeAudio(audioList);
                                initRecyclerView();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);



                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                                playAudio(0);
                                Button albumlist = findViewById(R.id.albumlist);
                                Button tracklist = findViewById(R.id.tracklist);
                                Button searchlist = findViewById(R.id.searchlist);
                                Button artistlist = findViewById(R.id.artistlist);

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                            }
                        });
        vapor3.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                lastradio ="http://currentcondition.org/";
                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modeland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                                searchbar.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this,
                                        "Please wait 15 seconds...buffering...", Toast.LENGTH_LONG).show();
                                sortmode =4; ok =1; yeah =1;
                                play =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                storage.killthefile();


                                loadAudio();
                                storage.storeAudio(audioList);
                                initRecyclerView();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);



                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                                playAudio(0);
                                Button albumlist = findViewById(R.id.albumlist);
                                Button tracklist = findViewById(R.id.tracklist);
                                Button searchlist = findViewById(R.id.searchlist);
                                Button artistlist = findViewById(R.id.artistlist);

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                            }
                        });
        vapor4.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                lastradio ="http://vapormusic.herokuapp.com/ET6657PH9gQ";
                                radiopage.setVisibility(View.INVISIBLE);
                                radiopageland.setVisibility(View.INVISIBLE);
                                modepage.setVisibility(View.INVISIBLE);
                                modeland.setVisibility(View.INVISIBLE);
                                hideKeyboard(MainActivity.this);
                                if (rotatelock % 2 == 0){
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);}
                                searchbar.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this,
                                        "Please wait 15 seconds...buffering...", Toast.LENGTH_LONG).show();
                                sortmode =4; ok =1; yeah =1;
                                play =1;
                                StorageUtil storage = new StorageUtil(getApplicationContext());
                                storage.killthefile();


                                loadAudio();
                                storage.storeAudio(audioList);
                                initRecyclerView();
                                Intent intenta= new Intent();
                                intenta.setAction("sortmode");
                                Bundle bundlex111 = new Bundle();
                                String shufflepressstr = String.valueOf(sortmode);
                                bundlex111.putString("shuffle123456",shufflepressstr);
                                modepage.setVisibility(View.INVISIBLE);
                                modepageland.setVisibility(View.INVISIBLE);



                                // put the playback status

                                // put your application's package


                                intenta.putExtras(bundlex111);
                                sendBroadcast(intenta);


                                playAudio(0);
                                Button albumlist = findViewById(R.id.albumlist);
                                Button tracklist = findViewById(R.id.tracklist);
                                Button searchlist = findViewById(R.id.searchlist);
                                Button artistlist = findViewById(R.id.artistlist);

                                tracklist.setBackgroundResource(R.drawable.freebutton5);
                                albumlist.setBackgroundResource(R.drawable.freebutton5);
                                artistlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setBackgroundResource(R.drawable.freebutton5);
                                searchlist.setTextColor(Color.BLACK);
                                tracklist.setTextColor(Color.BLACK);
                                albumlist.setTextColor(Color.BLACK);
                                artistlist.setTextColor(Color.BLACK);
                            }
                        });



































        IntentFilter iF = new IntentFilter();
        iF.addAction("datatransfer");





        registerReceiver(mReceiver, iF);



    }

    private void loadAudioList() {
        loadAudio();
        initRecyclerView();

    }




    public void hidebutton() {
        if (play == 1){
            try{
        if (player.a2 == null) return;
        if (player.a2 == true) {
            Button button12a = (Button) findViewById(R.id.button10);
            Button button15a = (Button) findViewById(R.id.button9);
            Button button16a = (Button) findViewById(R.id.button11);
            Button button12aland = (Button) findViewById(R.id.button10land);
            Button button15aland = (Button) findViewById(R.id.button9land);
            Button button16aland = (Button) findViewById(R.id.button11land);
            button15a.setVisibility(Button.VISIBLE);
            button12a.setVisibility(Button.INVISIBLE);
            button16a.setVisibility(Button.INVISIBLE);
            button15aland.setVisibility(Button.VISIBLE);
            button12aland.setVisibility(Button.INVISIBLE);
            button16aland.setVisibility(Button.INVISIBLE);

        }
        if (player.a2 == false) {
            Button button12a = (Button) findViewById(R.id.button10);
            Button button15a = (Button) findViewById(R.id.button9);
            Button button16a = (Button) findViewById(R.id.button11);
            Button button12aland = (Button) findViewById(R.id.button10land);
            Button button15aland = (Button) findViewById(R.id.button9land);
            Button button16aland = (Button) findViewById(R.id.button11land);
            button15a.setVisibility(Button.INVISIBLE);
            button12a.setVisibility(Button.VISIBLE);
            button16a.setVisibility(Button.INVISIBLE);
            button15aland.setVisibility(Button.INVISIBLE);
            button12aland.setVisibility(Button.VISIBLE);
            button16aland.setVisibility(Button.INVISIBLE);
        }} catch (Exception hhh){}

    }}

    private boolean checkAndRequestPermissions() {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            int permissionReadPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionWriteStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            List<String> listPermissionsNeeded = new ArrayList<>();

            if (permissionReadPhoneState != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }


            if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        String TAG = "LOG_PERMISSION";
        Log.d(TAG, "Permission callback called-------");

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);

                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions

                    if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            ) {
                        Log.d(TAG, "Phone state and storage permissions granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                        loadAudioList();
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                      //shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                ) {
                            showDialogOK("Phone state and storage permissions required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    private void initRecyclerView() {
        if (audioList != null && audioList.size() > 0) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

            RecyclerView_Adapter adapter = new RecyclerView_Adapter(audioList, getApplication());
            recyclerView.setAdapter(adapter);

                FastScroller fastScroller = (FastScroller) findViewById(R.id.fastscroll);






                //has to be called AFTER RecyclerView.setAdapter()
                fastScroller.setRecyclerView(recyclerView);

                fastScroller.setHandleColor(0xFF404040);
                fastScroller.setBubbleTextAppearance(R.style.StyledScrollerTextAppearance);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addOnItemTouchListener(new CustomTouchListener(this, new onItemClickListener() {
                @Override
                public void onClick(View view, int index) {
                    yeah =0;
                    playAudio(index);
                }
            }));
        }
    }

    private void initRecyclerView2() {
        if (audioplaylist != null && audioplaylist.size() > 0) {
            final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

            RecyclerView_Adapter adapter = new RecyclerView_Adapter(audioplaylist, getApplication());
            recyclerView.setAdapter(adapter);
            FastScroller fastScroller = (FastScroller) findViewById(R.id.fastscroll);






            //has to be called AFTER RecyclerView.setAdapter()
            fastScroller.setRecyclerView(recyclerView);

            fastScroller.setHandleColor(0xFF404040);
            fastScroller.setBubbleTextAppearance(R.style.StyledScrollerTextAppearance);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addOnItemTouchListener(new CustomTouchListener(this, new onItemClickListener() {
                @Override
                public void onClick(View view, int index) {
                    audioList = audioplaylist;  StorageUtil storage = new StorageUtil(getApplicationContext());

                    storage.storeAudio(audioList);
                    Intent intenta= new Intent();
                    intenta.setAction("sortmode");

                    sendBroadcast(intenta);

                    playAudio(index);

                }
            }));
        }
    }
    private void loadCollapsingImage(int i) {
        TypedArray array = getResources().obtainTypedArray(R.array.images);
        collapsingImageView.setImageDrawable(array.getDrawable(i));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("serviceStatus", serviceBound);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("serviceStatus");
    }

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {



            String artist = intent.getStringExtra("artist1");
            String album = intent.getStringExtra("album1");
            String track = intent.getStringExtra("track1");
            String length = intent.getStringExtra("length1");
            String stringUri = intent.getStringExtra("uri1");
            sortmodestr = intent.getStringExtra("sortlast1234");
            okstr = intent.getStringExtra("searchedlast1234");
            searchstrstr = intent.getStringExtra("searchlast1234");

            lastartist = artist;
            lasttrack = track;
            lastart = stringUri;
            Log.d("Music",artist+":"+album+":"+track+":"+stringUri+":"+length);
            TextView textView1 = (TextView) findViewById(R.id.track1);
            textView1.setText(track);
            TextView textView2 =  (TextView)  findViewById(R.id.artist1);
            textView2.setText(artist);
            TextView textView3 =  (TextView)  findViewById(R.id.time2);
            textView3.setText(length);

            //get the song's id from intent



            TextView textView1land = (TextView) findViewById(R.id.track1land);
            textView1land.setText(track);
            TextView textView2land =  (TextView)  findViewById(R.id.artist1land);
            textView2land.setText(artist);
            TextView textView3land =  (TextView)  findViewById(R.id.time2land);
            textView3land.setText(length);

            ImageView mainImage = (ImageView) findViewById(R.id.albumart);
            //get the song's id from intent
            ImageView mainImageland = (ImageView) findViewById(R.id.albumartland);
            try{
            Uri uri;
            uri = Uri.parse(stringUri);
            mainImage.setImageURI(uri);
            mainImageland.setImageURI(uri);}catch (Exception jjj){
                ImageView mainImagex = (ImageView) findViewById(R.id.albumart);

                //get the song's id from intent
                ImageView mainImagelandx = (ImageView) findViewById(R.id.albumartland);
                mainImage.setImageResource(R.drawable.image5);
                mainImageland.setImageResource(R.drawable.image5);
            }}





    };
    private BroadcastReceiver mReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive (Context context, Intent intent)
        {

            String pause = intent.getStringExtra("playmode");
            String hideplaybutton = intent.getStringExtra("playmode2");





            hidebutton();



        }

    };
    private BroadcastReceiver mReceiver3 = new BroadcastReceiver() {

        @Override
        public void onReceive (Context context, Intent intent)
        {
            Boolean playing;
            String pause = intent.getStringExtra("playmode");
            String playtime = intent.getStringExtra("position1");
            String length = intent.getStringExtra("duration1");


            TextView tv = (TextView) findViewById(R.id.time);
            tv.setText(playtime);
            TextView tvland = (TextView) findViewById(R.id.timeland);
            tvland.setText(playtime);










        }
    };
    private BroadcastReceiver stopped = new BroadcastReceiver() {

        @Override
        public void onReceive (Context context, Intent intent)
        {
            final Button button12 = (Button) findViewById(R.id.button10);
            final Button button15 = (Button) findViewById(R.id.button9);
            final Button button12land = (Button) findViewById(R.id.button10land);
            final Button button15land = (Button) findViewById(R.id.button9land);
            button15.setVisibility(Button.VISIBLE);
            button12.setVisibility(Button.INVISIBLE);
            button15land.setVisibility(Button.VISIBLE);
            button12land.setVisibility(Button.INVISIBLE);









        }
    };

    public void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            Log.d("aloha","no");
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());

            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            startForegroundService(playerIntent);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            } else {
                Intent playerIntent = new Intent(this, MediaPlayerService.class);
                startService(playerIntent);
                bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            }

        } else {
            //Store the new audioIndex to SharedPreferences
            Log.d("aloha","yes");
            StorageUtil storage = new StorageUtil(getApplicationContext());





            storage.storeAudioIndex(audioIndex);



            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);






            sendBroadcast(broadcastIntent);

        }
        UnMuteAudio();
        Button button13a = (Button) findViewById(R.id.button9a);
        Button button13a2 = (Button) findViewById(R.id.button9a2);
        Button button13a3 = (Button) findViewById(R.id.button9a3);
        Button button13a4 = (Button) findViewById(R.id.button9a4);
        Button button13b = (Button) findViewById(R.id.button9b);
        Button button13b2 = (Button) findViewById(R.id.button9b2);
        Button button13b3 = (Button) findViewById(R.id.button9b3);
        Button button13b4 = (Button) findViewById(R.id.button9b4);
        Button button12x = (Button) findViewById(R.id.button10);
        Button button15x = (Button) findViewById(R.id.button9);
        Button button16b = (Button) findViewById(R.id.button11);
        Button button16b2 = (Button) findViewById(R.id.button12a);
        Button button16b3 = (Button) findViewById(R.id.button12a2);
        Button button16b4 = (Button) findViewById(R.id.button12a3);
        Button button18 = (Button) findViewById(R.id.button18);
        Button button18land = (Button) findViewById(R.id.button18land);
        button18.setVisibility(Button.INVISIBLE);
        button18land.setVisibility(Button.INVISIBLE);
        button13a.setVisibility(Button.VISIBLE);
        button13a2.setVisibility(Button.VISIBLE);
        button13a3.setVisibility(Button.VISIBLE);
        button13a4.setVisibility(Button.VISIBLE);


        button13b.setVisibility(Button.VISIBLE);
        button13b2.setVisibility(Button.VISIBLE);
        button13b3.setVisibility(Button.VISIBLE);
        button13b4.setVisibility(Button.VISIBLE);

        button12x.setVisibility(Button.VISIBLE);
        button15x.setVisibility(Button.INVISIBLE);

        button16b.setVisibility(Button.INVISIBLE);
        button16b2.setVisibility(Button.INVISIBLE);
        button16b3.setVisibility(Button.INVISIBLE);
        button16b4.setVisibility(Button.INVISIBLE);




        Button button13al= (Button) findViewById(R.id.button9aland);
        Button button13a2l = (Button) findViewById(R.id.button9a2land);
        Button button13a3l = (Button) findViewById(R.id.button9a3land);
        Button button13a4l = (Button) findViewById(R.id.button9a4land);
        Button button13bl = (Button) findViewById(R.id.button9bland);
        Button button13b2l = (Button) findViewById(R.id.button9b2land);
        Button button13b3l = (Button) findViewById(R.id.button9b3land);
        Button button13b4l = (Button) findViewById(R.id.button9b4land);
        Button button12xl = (Button) findViewById(R.id.button10land);
        Button button15xl = (Button) findViewById(R.id.button9land);
        Button button16bl = (Button) findViewById(R.id.button11land);
        Button button16b2l = (Button) findViewById(R.id.button12aland);
        Button button16b3l = (Button) findViewById(R.id.button12a2land);
        Button button16b4l = (Button) findViewById(R.id.button12a3land);

        button13al.setVisibility(Button.VISIBLE);
        button13a2l.setVisibility(Button.VISIBLE);
        button13a3l.setVisibility(Button.VISIBLE);
        button13a4l.setVisibility(Button.VISIBLE);


        button13bl.setVisibility(Button.VISIBLE);
        button13b2l.setVisibility(Button.VISIBLE);
        button13b3l.setVisibility(Button.VISIBLE);
        button13b4l.setVisibility(Button.VISIBLE);

        button12xl.setVisibility(Button.VISIBLE);
        button15xl.setVisibility(Button.INVISIBLE);

        button16bl.setVisibility(Button.INVISIBLE);
        button16b2l.setVisibility(Button.INVISIBLE);
        button16b3l.setVisibility(Button.INVISIBLE);
        button16b4l.setVisibility(Button.INVISIBLE);
        if (yeah ==1){
            button13al.setVisibility(Button.INVISIBLE);
            button13a2l.setVisibility(Button.INVISIBLE);
            button13a3l.setVisibility(Button.INVISIBLE);
            button13a4l.setVisibility(Button.INVISIBLE);


            button13bl.setVisibility(Button.INVISIBLE);
            button13b2l.setVisibility(Button.INVISIBLE);
            button13b3l.setVisibility(Button.INVISIBLE);
            button13b4l.setVisibility(Button.INVISIBLE);

            button13a.setVisibility(Button.INVISIBLE);
            button13a2.setVisibility(Button.INVISIBLE);
            button13a3.setVisibility(Button.INVISIBLE);
            button13a4.setVisibility(Button.INVISIBLE);


            button13b.setVisibility(Button.INVISIBLE);
            button13b2.setVisibility(Button.INVISIBLE);
            button13b3.setVisibility(Button.INVISIBLE);
            button13b4.setVisibility(Button.INVISIBLE);




        }

        timeonnp();
        play = 1;


    }

    public void timeonnp(){

        final Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            String a = null;
            String b = null;
            @Override
            public void run() {
                int length = 10000;
                int millis = 0;
                try{
                    length = player.mediaPlayer.getDuration();
                    millis = player.mediaPlayer.getCurrentPosition();} catch
                        (Exception haha){
                     length = 10000;
                     millis = 0;
                     try{
                    length = player.timer3;
                    millis = player.timer2;}  catch (Exception io){}
                };

                lasttime2  = a;
                lastduration  = b;






                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                int seconds1 = (int) (length / 1000);
                int minutes1 = seconds1 / 60;
                seconds1 = seconds1 % 60;

                a =String.format("%02d:%02d", minutes, seconds);
                b =String.format("%02d:%02d", minutes1, seconds1);
                try{
                if (player.mediaPlayer.getDuration() <= 0){
                    b = "--:--";
                }} catch (Exception io){}

                TextView acc = (TextView) findViewById(R.id.time);
                acc.setText(a);
                TextView acc2 = (TextView) findViewById(R.id.time2);
                acc2.setText(b);
                TextView acc3 = (TextView) findViewById(R.id.timeland);
                acc3.setText(a);
                TextView acc4 = (TextView) findViewById(R.id.time2land);
                acc4.setText(b);
                try{
                if (yeah == 0){
                if (a.equals(b)){
                    if(repeatpress % 2 == 1) {
                        player.mediaPlayer.seekTo(0);
                        player.mediaPlayer.start();
                    } else{
                        player.skipToNext();
                        player.updateMetaData();}
                    player.buildNotification(PlaybackStatus.PLAYING);
                }}



                timerHandler.postDelayed(this, 500);
                } catch (Exception io){}









            }};

        timerHandler.postDelayed(timerRunnable, 0);



    }
    public void checker(){

        final Handler timerHandler = new Handler();
        Runnable timerRunnable2 = new Runnable() {

            @Override
            public void run() {
                if(!itworks  && player != null){
                player.mediaPlayer.seekTo(timetor);

                player.mediaPlayer.start();

                    if (player!= null){
                        AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        UnMuteAudio();
                        itworks = true;
                        player.updateMetaData();
                        player.mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                                .setState(PlaybackStateCompat.STATE_PLAYING,timetor,1.0f).build());
                        hideplay();
                    }
                }








                timerHandler.postDelayed(this, 500);










            }};

        timerHandler.postDelayed(timerRunnable2, 0);

    }
    public void checker2(){

        final Handler timerHandler = new Handler();
        Runnable timerRunnable2 = new Runnable() {

            @Override
            public void run() {
                if(!itworks  && player != null){
                    player.mediaPlayer.seekTo(timetor);

                    player.mediaPlayer.start();

                    if (player!= null){
                        AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        UnMuteAudio();
                        itworks = true;
                    }
                }








                timerHandler.postDelayed(this, 500);










            }};

        timerHandler.postDelayed(timerRunnable2, 0);

    }


    /**
     * Load audio files using {@link ContentResolver}
     *
     * If this don't works for you, load the audio files to audioList Array your oun way
     */
    private void loadAudio() {

        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder ="lower(" + MediaStore.Audio.Media.TITLE+ ") ASC" ;
        if (sortmode ==0) {
        sortOrder = "lower(" + MediaStore.Audio.Media.TITLE+ ") ASC" ;}
        if (sortmode ==1) {
            sortOrder = "lower(" + MediaStore.Audio.Media.ARTIST+ ") ASC" ;}
        if (sortmode ==2) {
            sortOrder = "lower(" + MediaStore.Audio.Media.ALBUM+ ") ASC" ;}



        Cursor cursor1 = contentResolver.query(uri, null, selection, null, sortOrder);
        cursor1.close();
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                int mode = sortmode;
                int searched = ok;
                String search = searchstr;
                // Save to audioList

                audioList.add(new Audio(data, title, album, artist, duration, mode, searched,search));

                if (ok == 1){

                 result = new ArrayList<>();

                for (Audio person : audioList) {
                    Pattern p = Pattern.compile(searchstr, Pattern.CASE_INSENSITIVE);
                    String searchstr1 = searchstr.toLowerCase();
                    if (sortmode == 1){

                    if (
                            person.getArtist().toLowerCase().contains(searchstr1)


                            ) {
                        result.add(person);
                    }}
                    if (sortmode == 3){

                        if (
                                person.getArtist().toLowerCase().contains(searchstr1)||
                                        person.getTitle().toLowerCase().contains(searchstr1)||
                                        person.getAlbum().toLowerCase().contains(searchstr1)


                                ) {
                            result.add(person);
                        }}
                    if (sortmode == 0){
                        if (
                                person.getTitle().toLowerCase().contains(searchstr1)


                                ) {
                            result.add(person);
                        }}
                    if (sortmode == 2){
                        if (
                                person.getAlbum().toLowerCase().contains(searchstr1)


                                ) {
                            result.add(person);
                        }}



                    if (sortmode == 4){

                        result = new ArrayList<>();
                        String data1 = lastradio;
                        String title1 = "Radio";
                        String album1 = " ";
                        String artist1 = " ";
                        String duration1 = "999999999999";
                        int mode1 = sortmode;
                        int searched1 = ok;
                        String search1 = searchstr;

                        result.add(new Audio(data1, title1, album1, artist1, duration1, mode1, searched1,search1));}
                    audioList = result;






                }

            }}
        }
        if (cursor != null)
            cursor.close();
    }
    private void loadAudio2() {

        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder ="lower(" + MediaStore.Audio.Media.TITLE+ ") ASC" ;
        if (sortmode ==0) {
            sortOrder = "lower(" + MediaStore.Audio.Media.TITLE+ ") ASC" ;}
        if (sortmode ==1) {
            sortOrder = "lower(" + MediaStore.Audio.Media.ARTIST+ ") ASC" ;}
        if (sortmode ==2) {
            sortOrder = "lower(" + MediaStore.Audio.Media.ALBUM+ ") ASC" ;}



        Cursor cursor1 = contentResolver.query(uri, null, selection, null, sortOrder);
        cursor1.close();
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            audioplaylist = new ArrayList<>();

            while (cursor.moveToNext()) {

                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                int mode = sortmode;
                int searched = ok;
                String search = searchstr;
                // Save to audioList

                audioplaylist.add(new Audio(data, title, album, artist, duration, mode, searched,search));

                if (ok == 1){

                    result = new ArrayList<>();

                    for (Audio person : audioplaylist) {
                        Pattern p = Pattern.compile(searchstr, Pattern.CASE_INSENSITIVE);
                        String searchstr1 = searchstr.toLowerCase();
                        if (sortmode == 1){

                            if (
                                    person.getArtist().toLowerCase().contains(searchstr1)


                                    ) {
                                result.add(person);
                            }}
                        if (sortmode == 3){

                            if (
                                    person.getArtist().toLowerCase().contains(searchstr1)||
                                            person.getTitle().toLowerCase().contains(searchstr1)||
                                            person.getAlbum().toLowerCase().contains(searchstr1)


                                    ) {
                                result.add(person);
                            }}
                        if (sortmode == 0){
                            if (
                                    person.getTitle().toLowerCase().contains(searchstr1)


                                    ) {
                                result.add(person);
                            }}
                        if (sortmode == 2){
                            if (
                                    person.getAlbum().toLowerCase().contains(searchstr1)


                                    ) {
                                result.add(person);
                            }}



                        if (sortmode == 4){

                            result = new ArrayList<>();
                            String data1 = lastradio;
                            String title1 = "Radio";
                            String album1 = " ";
                            String artist1 = " ";
                            String duration1 = "999999999999";
                            int mode1 = sortmode;


                            int searched1 = ok;
                            String search1 = searchstr;

                            result.add(new Audio(data1, title1, album1, artist1, duration1, mode1, searched1,search1));}
                        audioplaylist= result;






                    }

                }}
        }
        if (cursor != null)
            cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.activityResumed();
        if (fullscreen % 2 == 0){
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
        play = 1;


    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.activityPaused();
        hideKeyboard(MainActivity.this);
        play = 0;


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void showPhoto(Uri photoUri){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_CANCELED)
        {

            // action cancelled
        }
        if(resultCode==RESULT_OK)
        {
            try{
            Uri selectedimg = data.getData();
            GifImageView mybg = findViewById(R.id.mainbg);
            mybg.setImageURI(selectedimg);
                File directory = new File(selectedimg.getPath());
                File dst = new File(Environment.getDataDirectory(), "image");

                String filename = "bgsaved";
                String fileContents = selectedimg.toString();
                FileOutputStream outputStream;
                File folder = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "VaporMusic");
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                File file = new File(Environment.getExternalStorageDirectory()+
                        File.separator + "VaporMusic", filename);
                file.delete();
                file.createNewFile();
                String id = DocumentsContract.getDocumentId(selectedimg);
                InputStream inputStream = getContentResolver().openInputStream(selectedimg);

                try {
                    File file1 = new File(Environment.getExternalStorageDirectory()+
                            File.separator + "VaporMusic"+File.separator+"thebackground");
                    writeFile(inputStream, file1);
                    String filePath = file1.getAbsolutePath();
                    FileWriter filewriter = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(filewriter);

                    out.write(filePath);

                    out.close();
                    filewriter.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
            catch (Exception yehithappens){}

        }
    }

    public void writeFile(InputStream in, File file) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if ( out != null ) {
                    out.close();
                }
                in.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
    public void readtext() {
        try {
            File f = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "VaporMusic","bgsaved");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);

            Uri newbg = Uri.parse(text);
            String heello = newbg.toString();

            GifImageView mybg = findViewById(R.id.mainbg);
            mybg.setImageURI(newbg);
            GifDrawable gifFromPath = new GifDrawable(heello);
            mybg.setImageDrawable(gifFromPath);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void readtext2() {
        try {
            File f = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "VaporMusic","lastplayed");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            String[] parts = text.split(":");
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];
            int index2 = Integer.parseInt(part1);
            int time2= Integer.parseInt(part2);
            int size2= Integer.parseInt(part3);
            Log.d("timehuh",index2+":"+time2+":"+size2);

                //Check is service is active
              playAudio(index2);
              showplay();



                    AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
            MuteAudio();
            used = true;
               timetor = time2;

                Log.d("resumed","yes");




        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void readtext3() {
        try {
            File f = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "VaporMusic","lastplayedmeta");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            String[] parts = text.split(":::");
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];
            String part4 = parts[3];
            String part5 = parts[4];
            String part6 = parts[5];
            String part7 = parts[6];
            String part8 = parts[7];
            String part9 = parts[8];
            String part10 = parts[9];
            String part11 = parts[10];
            String part12 = parts[11];
            String part13 = parts[12];


            String artist = part1;
            String track= part2;
           String time= part3;
            String duration = part4;
            Uri art = Uri.parse(part5);
            searchstr = part7;
            sortmode = Integer.parseInt(part8);
            ok = Integer.parseInt(part9);
            datapath = part10;
            portrait = Integer.parseInt(part11);
            rotatelock = Integer.parseInt(part12);
            yeah = Integer.parseInt(part13);
            if (sortmode == 4 ){
                sortmode =0;
            }






            fullscreen = Integer.parseInt(part6);
            if (fullscreen % 2 == 1) {

                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
            }
            if (fullscreen % 2 == 0)
            {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
            final TextView artistlast = findViewById(R.id.artist1);
            final TextView tracklast = findViewById(R.id.track1);
            final TextView timelast = findViewById(R.id.time);
            final TextView durationlast = findViewById(R.id.time2);
            final ImageView albumartlast = findViewById(R.id.albumart);
            final TextView artistlastland = findViewById(R.id.artist1land);
            final TextView tracklastland = findViewById(R.id.track1land);
            final TextView timelastland = findViewById(R.id.timeland);
            final TextView durationlastland = findViewById(R.id.time2land);
            final ImageView albumartlastland = findViewById(R.id.albumartland);
            artistlast.setText(artist);
            artistlastland.setText(artist);
            tracklast.setText(track);
            tracklastland.setText(track);
            timelast.setText(time);
            timelastland.setText(time);
            durationlast.setText(duration);
            durationlastland.setText(duration);
            albumartlast.setImageURI(art);
            albumartlastland.setImageURI(art);
            Log.d("loaded","yes");








        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void readtext4() {
        try {
            File f = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "VaporMusic","settings");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            String[] parts = text.split(":::");
            String part1 = parts[0];
            String part2 = parts[1];




            portrait = Integer.parseInt(part1);
            rotatelock = Integer.parseInt(part2);













        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void readtext5() {
        try {
            File f = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "VaporMusic","radio");
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String text = new String(buffer);
            String[] parts = text.split(":::");
            String part1 = parts[0];





            lastradio = part1;














        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            final FrameLayout framex = findViewById(R.id.framex);
            final FrameLayout portraitmode = findViewById(R.id.portraiter);
            final FrameLayout landscapemode = findViewById(R.id.landscaper);
            final FrameLayout frame1 = (FrameLayout) findViewById(R.id.frame1);
            final FrameLayout frame2 = (FrameLayout) findViewById(R.id.frame2);
            final FrameLayout frame3 = (FrameLayout) findViewById(R.id.frame3);
            final FrameLayout frame4 = (FrameLayout) findViewById(R.id.frame4);
            final FrameLayout frame5 = (FrameLayout) findViewById(R.id.frame5);

            final Button button4iland = (Button) findViewById(R.id.hidenpland);


            final FrameLayout frame1land = (FrameLayout) findViewById(R.id.frame1land);
            final FrameLayout frame2land = (FrameLayout) findViewById(R.id.frame2land);
            final FrameLayout frame3land = (FrameLayout) findViewById(R.id.frame3land);
            final FrameLayout frame4land = (FrameLayout) findViewById(R.id.frame4land);
            final FrameLayout frame5land = (FrameLayout) findViewById(R.id.frame5land);
            if (frame5.getVisibility() == View.VISIBLE){
                frame1land.setVisibility(View.VISIBLE);
                frame2land.setVisibility(View.VISIBLE);
                frame3land.setVisibility(View.VISIBLE);
                frame4land.setVisibility(View.VISIBLE);
                frame5land.setVisibility(View.VISIBLE);
                frame1.setVisibility(View.INVISIBLE);
                frame2.setVisibility(View.INVISIBLE);
                frame3.setVisibility(View.INVISIBLE);
                frame4.setVisibility(View.INVISIBLE);
                frame5.setVisibility(View.INVISIBLE);
            }
            if (framex.getVisibility() == View.VISIBLE ){
                portraitmode.setVisibility(View.INVISIBLE);
                landscapemode.setVisibility(View.INVISIBLE);
            } else {
            portraitmode.setVisibility(View.INVISIBLE);
            landscapemode.setVisibility(View.VISIBLE);}


            portrait =0;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            final FrameLayout framex = findViewById(R.id.framex);
            final FrameLayout portraitmode = findViewById(R.id.portraiter);
            final FrameLayout landscapemode = findViewById(R.id.landscaper);
            final FrameLayout frame1 = (FrameLayout) findViewById(R.id.frame1);
            final FrameLayout frame2 = (FrameLayout) findViewById(R.id.frame2);
            final FrameLayout frame3 = (FrameLayout) findViewById(R.id.frame3);
            final FrameLayout frame4 = (FrameLayout) findViewById(R.id.frame4);
            final FrameLayout frame5 = (FrameLayout) findViewById(R.id.frame5);

            final Button button4iland = (Button) findViewById(R.id.hidenpland);


            final FrameLayout frame1land = (FrameLayout) findViewById(R.id.frame1land);
            final FrameLayout frame2land = (FrameLayout) findViewById(R.id.frame2land);
            final FrameLayout frame3land = (FrameLayout) findViewById(R.id.frame3land);
            final FrameLayout frame4land = (FrameLayout) findViewById(R.id.frame4land);
            final FrameLayout frame5land = (FrameLayout) findViewById(R.id.frame5land);
            if (frame5land.getVisibility() == View.VISIBLE){
                frame1.setVisibility(View.VISIBLE);
                frame2.setVisibility(View.VISIBLE);
                frame3.setVisibility(View.VISIBLE);
                frame4.setVisibility(View.VISIBLE);
                frame5.setVisibility(View.VISIBLE);
                frame1land.setVisibility(View.INVISIBLE);
                frame2land.setVisibility(View.INVISIBLE);
                frame3land.setVisibility(View.INVISIBLE);
                frame4land.setVisibility(View.INVISIBLE);
                frame5land.setVisibility(View.INVISIBLE);
            }
            if (framex.getVisibility() == View.VISIBLE ){
                portraitmode.setVisibility(View.INVISIBLE);
                landscapemode.setVisibility(View.INVISIBLE);
            } else {
            portraitmode.setVisibility(View.VISIBLE);
            landscapemode.setVisibility(View.INVISIBLE);}

            portrait = 1;
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        savemeta();
        savemeta2();
        savemeta3();
        try{

            lastindex = player.audioIndex;
            lasttime  = player.mediaPlayer.getCurrentPosition();
            lastsize = audioList.size();

            String lastindexstr = String.valueOf(lastindex);
            String lasttimestr = String.valueOf(lasttime);
            String lastsizestr = String.valueOf(lastsize);
            Log.d("sumup", lastindexstr +":"+ lasttimestr +":"+ lastsizestr);

            String filename = "lastplayed";
            String fileContents = lastindexstr +":"+ lasttimestr +":"+ lastsizestr;
            String fileContents2 = lastindexstr +":"+ lastsizestr +':'+lasttrack;
            FileOutputStream outputStream;

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "VaporMusic");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            File file = new File(Environment.getExternalStorageDirectory()+
                    File.separator + "VaporMusic", filename);
            if (fileContents2.equals("0:1:Radio") ){} else{
            if (lastindex > -1 ){
            file.delete();
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(fileContents);

            myOutWriter.close();

            fOut.flush();
            fOut.close();}

        }}
        catch (Exception hhh ) {};

    }
    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    public void savemeta(){
        try{








        String filename = "lastplayedmeta";
        String fullscreenstr = String.valueOf(fullscreen);
        Log.d("fcsr",fullscreenstr);
        Log.d("string",laststr);
        String laststrstr = String.valueOf(laststr);
        String positionstr = String.valueOf(portrait);
        String rlockstr = String.valueOf(rotatelock);
        String lastartstr = lastart;
        String fileContents = lastartist+":::"+lasttrack+":::"+ lasttime2 +":::"+ lastduration+":::"+ lastart+":::"+fullscreenstr+":::"+searchstrstr+":::"+sortmodestr+":::"+okstr+":::"+datapath+":::"+positionstr+":::"+rlockstr+":::"+yeah+":::"+lastradio;
            String fileContents2 = lastartist+":::"+lasttrack;
        FileOutputStream outputStream;
Log.d("savedinfo",fileContents);
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "VaporMusic");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()+
                File.separator + "VaporMusic", filename);
        if (fileContents2.equals("null:::null") || fileContents2.equals(" :::Radio")){
            Log.d("blocked","yes");
        } else{
        file.delete();
        file.createNewFile();
        FileOutputStream fOut = new FileOutputStream(file);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myOutWriter.append(fileContents);

        myOutWriter.close();

        fOut.flush();
        fOut.close();}}


    catch (Exception ripmy ) {
        Log.e("error1234","damn",ripmy);
    };


    }

    public void savemeta2(){
        try{







            String filename = "settings";
            String fullscreenstr = String.valueOf(fullscreen);
            Log.d("fcsr",fullscreenstr);
            Log.d("string",laststr);
            String laststrstr = String.valueOf(laststr);
            String positionstr = String.valueOf(portrait);
            String rlockstr = String.valueOf(rotatelock);
            String fileContents = positionstr+":::"+rlockstr;
            FileOutputStream outputStream;

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "VaporMusic");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            File file = new File(Environment.getExternalStorageDirectory()+
                    File.separator + "VaporMusic", filename);

            file.delete();
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(fileContents);

            myOutWriter.close();

            fOut.flush();
            fOut.close();}


        catch (Exception ripmy ) {
            Log.e("error1234","damn",ripmy);
        };

    }

    public void MuteAudio(){
        AudioManager mAlramMAnager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);

        } else {

            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, true);

        }
    }

    public void UnMuteAudio(){
        AudioManager mAlramMAnager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,0);

        } else {

            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, false);

        }
    }

    public void hideplay(){

        Button button16b = (Button) findViewById(R.id.button11);
        Button button16b2 = (Button) findViewById(R.id.button12a);
        Button button16b3 = (Button) findViewById(R.id.button12a2);
        Button button16b4 = (Button) findViewById(R.id.button12a3);
        Button button16bl = (Button) findViewById(R.id.button11land);
        Button button16b2l = (Button) findViewById(R.id.button12aland);
        Button button16b3l = (Button) findViewById(R.id.button12a2land);
        Button button16b4l = (Button) findViewById(R.id.button12a3land);

        button16b2.setVisibility(Button.INVISIBLE);
        button16b3.setVisibility(Button.INVISIBLE);
        button16b4.setVisibility(Button.INVISIBLE);

        button16b2l.setVisibility(Button.INVISIBLE);
        button16b3l.setVisibility(Button.INVISIBLE);
        button16b4l.setVisibility(Button.INVISIBLE);
        Button button18 = (Button) findViewById(R.id.button18);
        Button button18land = (Button) findViewById(R.id.button18land);
        button18.setVisibility(Button.INVISIBLE);
        button18land.setVisibility(Button.INVISIBLE);


    };
    public void showplay(){

        Button button16b = (Button) findViewById(R.id.button11);
        Button button16b2 = (Button) findViewById(R.id.button12a);
        Button button16b3 = (Button) findViewById(R.id.button12a2);
        Button button16b4 = (Button) findViewById(R.id.button12a3);
        Button button16bl = (Button) findViewById(R.id.button11land);
        Button button16b2l = (Button) findViewById(R.id.button12aland);
        Button button16b3l = (Button) findViewById(R.id.button12a2land);
        Button button16b4l = (Button) findViewById(R.id.button12a3land);

        button16b2.setVisibility(Button.VISIBLE);
        button16b3.setVisibility(Button.VISIBLE);
        button16b4.setVisibility(Button.VISIBLE);

        button16b2l.setVisibility(Button.VISIBLE);
        button16b3l.setVisibility(Button.VISIBLE);
        button16b4l.setVisibility(Button.VISIBLE);
        Button button18 = (Button) findViewById(R.id.button18);
        Button button18land = (Button) findViewById(R.id.button18land);
        button18.setVisibility(Button.VISIBLE);
        button18land.setVisibility(Button.VISIBLE);



    };
    public void savemeta3(){
        try{



           String filename = "radio";
            String fileContents = lastradio;
            String fileContents2 = lastradio;
            FileOutputStream outputStream;
            Log.d("savedinfo",fileContents);
            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "VaporMusic");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            File file = new File(Environment.getExternalStorageDirectory()+
                    File.separator + "VaporMusic", filename);
            if (fileContents2.equals("")){
                Log.d("blocked","yes");
            } else{
                file.delete();
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(fileContents);

                myOutWriter.close();

                fOut.flush();
                fOut.close();}}


        catch (Exception ripmy ) {
            Log.e("error1234","damn",ripmy);
        }}
    public static void clearSharedPreferences(Context ctx){
        File dir = new File(ctx.getFilesDir().getParent() + "/shared_prefs/");
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            // clear each of the prefrances
            ctx.getSharedPreferences(children[i].replace(".xml", ""), Context.MODE_PRIVATE).edit().clear().commit();
        }
        // Make sure it has enough time to save all the commited changes
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        for (int i = 0; i < children.length; i++) {
            // delete the files
            new File(dir, children[i]).delete();
        }
    }
    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }
    public void getTotalMemory() {
        {
            String str1 = "/proc/meminfo";
            String str2;
            String[] arrayOfString;
            long initial_memory = 0;
            try {
                FileReader localFileReader = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(    localFileReader, 8192);
                str2 = localBufferedReader.readLine();//meminfo
                arrayOfString = str2.split("\\s+");
                String memreal =  arrayOfString[1];
                mem = memreal;
                if (Integer.parseInt(memreal) < 900000){
                    GifImageView mybg = findViewById(R.id.mainbg);
                    mybg.setImageResource(R.drawable.bg35);
                    try{
                        File f = new File(Environment.getExternalStorageDirectory()+
                                File.separator + "VaporMusic" ,"bgsaved");
                        f.delete();} catch (Exception hhhh){}

                }
                for (String num : arrayOfString) {
                    Log.i(str2, num + "\t");
                    Log.d("memsize",memreal );
                }
                //total Memory
                initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
                localBufferedReader.close();
            }
            catch (IOException e)
            {
            }
        }}
    @Override
    protected void onDestroy() {
        try{
            unregisterReceiver(mReceiver);
            unregisterReceiver(mReceiver2);
            unregisterReceiver(mReceiver3);
        } catch (Exception io){}
        try{
        Log.d("ouch", "appdestroyed");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();} catch (Exception io){}
        super.onDestroy();
        try{
            player.mediaSession.release();
        UnMuteAudio();

        player.removeNotification();
        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
        cancelNotification(this,147);} catch ( Exception io){}
        if (serviceBound) {
            unbindService(serviceConnection);

            //service is active
            player.stopSelf();
        }}



}
