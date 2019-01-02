package com.tk.lolirem.vapormusic;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.RemoteController;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.graphics.Palette;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.Duration;

import static android.content.ContentValues.TAG;
import static com.tk.lolirem.vapormusic.MainActivity.Broadcast_PLAY_NEW_AUDIO;



public class MediaPlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener,
        AudioManager.OnAudioFocusChangeListener {
    public static final String ACTION_PLAY = "com.tk.lolirem.vapormusic.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.tk.lolirem.vapormusic.ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "com.tk.lolirem.vapormusic.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.tk.lolirem.vapormusic.ACTION_NEXT";
    public static final String ACTION_STOP = "com.tk.lolirem.vapormusic.ACTION_STOP";
    private static final String CHANNEL_ID = "media_playback_channel";
    public MediaPlayer mediaPlayer;
    private String lastarts;
    private MainActivity main;
    //MediaSession
    public MediaSessionManager mediaSessionManager;
    public RemoteController remotecontroller;
    public MediaSessionCompat mediaSession;
    public MediaControllerCompat.TransportControls transportControls;
    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 147;
    //Used to pause/resume MediaPlayer
    public int resumePosition;
	public long durationboiii;
    //AudioFocus
    private AudioManager audioManager;
    // Binder given to clients
    private final IBinder iBinder = new LocalBinder();
    //List of available Audio files
    private ArrayList<Audio> audioList;
    public int audioIndex = -1;
    public Audio activeAudio; //an object on the currently playing audio
    //Handle incoming phone calls
    public Audio trueActive;
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    Boolean a2 ;
    Boolean a3 ;
    public boolean x3;
    public int shupr = 0;
    public int one = 0;
    public int timer2 =0;
    public int timer3 =500000;
    public int deadmode ;
    public int sortmode2 ;
    String channelId;
    public Bitmap lol;
    public int pausepressed = 0;
    public int pausepressed2 = 0;
    NotificationCompat.Builder notificationBuilder;
    public Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }

    // Generate palette asynchronously and use it on a different
// thread using onGenerated()
    public void createPaletteAsync(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {
                // Use generated instance
            }
        });
    }
    public int tint;
    /**
     * Service lifecycle methods
     */
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
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

            timer2 = Integer.parseInt(part3);








        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // Perform one-time setup procedures
        // Manage incoming phone calls during playback.
        // Pause MediaPlayer on incoming call,
        // Resume on hangup.
        callStateListener();
        //ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs -- BroadcastReceiver
        registerBecomingNoisyReceiver();
        //Listen for new Audio to play -- BroadcastReceiver
        register_playNewAudio();

        IntentFilter iF3x = new IntentFilter();

        iF3x.addAction("shufflereceiver");

        registerReceiver(shureceiver, iF3x);
        IntentFilter iF4x = new IntentFilter();

        iF4x.addAction("sortmode");

        registerReceiver(sortreset, iF4x);
        IntentFilter iF5x = new IntentFilter();

        iF5x.addAction("pausedtime");
String id3344 ="abdc";
        registerReceiver(pausemedia, iF5x);
        Intent notificationIntent = new Intent(this, MediaPlayerService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";// The id of the channel.
            CharSequence name = "Vapor Music";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        notification = new Notification.Builder(this,id3344)
                .setContentTitle("App is running")

                .setSmallIcon(android.R.drawable.stat_sys_headset)
                .setContentIntent(pendingIntent)
                .setTicker("Vapor Music")
                .setChannelId(CHANNEL_ID)
                .build();
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

        } else {  notification = new Notification.Builder(this)
            .setContentTitle("App is running")

            .setSmallIcon(android.R.drawable.stat_sys_headset)
            .setContentIntent(pendingIntent)
            .setTicker("Vapor Music")
            .setPriority(Notification.PRIORITY_MIN)
            .build();}

        startForeground(15, notification);
    }
    public BroadcastReceiver shureceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {

            String shuffle = intent.getStringExtra("shuffle1234");
            shupr = Integer.parseInt(shuffle);
            Log.d("heyboi",shuffle);




        }
    };
    public BroadcastReceiver pausemedia = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {

            String shuffle = intent.getStringExtra("paused1234time");
            try{
            timer2 = Integer.parseInt(shuffle);} catch (Exception io){}





        }
    };
    public BroadcastReceiver sortreset = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            //Load data from SharedPreferences
            String shuffle = intent.getStringExtra("shuffle123456");
            try{
            sortmode2 = Integer.parseInt(shuffle);} catch (Exception io ){}
            StorageUtil storage = new StorageUtil(getApplicationContext());

            audioList = storage.loadAudio();
            audioIndex = storage.loadAudioIndex();
            if (audioIndex != -1 && audioIndex < audioList.size()) {
                //index is in a valid range
                activeAudio = audioList.get(audioIndex);
            } else {
                stopSelf();
            }


            //Request audio focus
            if (requestAudioFocus() == false) {
                //Could not gain focus
                stopSelf();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                if (mediaSessionManager == null) {
                    try {
                        initMediaSession();
                        initMediaPlayer();

                    } catch (RemoteException e) {
                        e.printStackTrace();
                        stopSelf();
                    }}
                buildNotification(PlaybackStatus.PLAYING);
            } else{
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
                try {
                    initMediaSession();
                    initMediaPlayer();
                    UnMuteAudio();
                    buildNotification(PlaybackStatus.PLAYING);

                } catch (RemoteException e) {
                    e.printStackTrace();
                    stopSelf();
                }}



            }





        }
    };
    //The system calls this method when an activity, requests the service be started
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            //Load data from SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            audioList = storage.loadAudio();
            audioIndex = storage.loadAudioIndex();
            if (audioIndex != -1 && audioIndex < audioList.size()) {
                //index is in a valid range
                activeAudio = audioList.get(audioIndex);
            } else {
                stopSelf();
            }
        } catch (NullPointerException e) {
            stopSelf();
        }
        //Request audio focus
        if (requestAudioFocus() == false) {
            //Could not gain focus
            stopSelf();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        if (mediaSessionManager == null) {
            try {
                initMediaSession();
                initMediaPlayer();


            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }}
            buildNotification(PlaybackStatus.PLAYING);
        } else{
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
            try {
                initMediaSession();
                initMediaPlayer();
                UnMuteAudio();
                buildNotification(PlaybackStatus.PLAYING);

            } catch (RemoteException e) {
                e.printStackTrace();
                stopSelf();
            }}



        }
        //Handle Intent action from MediaSession.TransportControls
        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaSession.release();
        removeNotification();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            stopMedia();
            mediaPlayer.release();
        }
        removeNotification();

        //Disable the PhoneStateListener
        if (phoneStateListener != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        UnMuteAudio();
        //unregister BroadcastReceivers
        unregisterReceiver(becomingNoisyReceiver);
        unregisterReceiver(playNewAudio);
        unregisterReceiver(pausemedia);
        unregisterReceiver(shureceiver);
        unregisterReceiver(sortreset);

        //clear cached playlist
        new StorageUtil(getApplicationContext()).clearCachedAudioPlaylist();
    }

    /**
     * Service Binder
     */
    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MediaPlayerService.this;
        }
    }

    /**
     * MediaPlayer callback methods
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        //Invoked indicating buffering status of
        //a media resource being streamed over the network.
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //Invoked when playback of a media source has completed.
        stopMedia();

        //stop the service
        stopSelf();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //Invoked when there has been an error during an asynchronous operation
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                Log.d("MediaPlayer Error", "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.d("MediaPlayer Error", "MEDIA ERROR SERVER DIED " + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.d("MediaPlayer Error", "MEDIA ERROR UNKNOWN " + extra);
                break;
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //Invoked to communicate some info
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //Invoked when the media source is ready for playback.
        playMedia();
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        //Invoked indicating the completion of a seek operation.
    }

    @Override
    public void onAudioFocusChange(int focusState) {
        //Invoked when the audio focus of the system is updated.
        switch (focusState) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                try { Log.d("manuallypaused",String.valueOf(pausepressed));
                if (mediaPlayer == null) initMediaPlayer();
                else if (!mediaPlayer.isPlaying() && pausepressed == 0) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                String yt3 = "yes";
                Log.d("yoube1",yt3);} catch (Exception io){}
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                try{
                // Lost focus for an unbounded amount of time: stop playback and release media player
                if (mediaPlayer.isPlaying() || !mediaPlayer.isPlaying() && mediaPlayer.getCurrentPosition() != 0) {
                    timer2 = mediaPlayer.getCurrentPosition();
                    timer3 = mediaPlayer.getDuration();
                    Intent intent = new Intent();
                    intent.setAction("stcpped");
                }
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;

                String yt = "yes";
                Log.d("yoube2",yt);
                buildNotification(PlaybackStatus.PAUSED);


                mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                        .setState(PlaybackStateCompat.STATE_PAUSED,timer2,1.0f).build());

                deadmode =1;} catch (Exception io){}
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                try{
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();

                String yt1 = "yes";
                Log.d("yoube3",yt1);} catch (Exception io){}


                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                try{
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                String yt2 = "yes";
                Log.d("yoube4",yt2);} catch (Exception io) {}
                break;
        }
    }

    /**
     * AudioFocus
     */
    public boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    public boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

    /**
     * MediaPlayer actions
     */
    public void initMediaPlayer() {
        try{
        if (mediaPlayer == null) mediaPlayer = new MediaPlayer();//new MediaPlayer instance
        //Set up MediaPlayer event listeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnInfoListener(this);


        //Reset so that the MediaPlayer is not pointing to another data source
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            // Set the data source to the mediaFile location
            mediaPlayer.setDataSource(activeAudio.getData());
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
        }
        mediaPlayer.prepareAsync();}
        catch (Exception io){
            Toast.makeText(this,
                    "Invalid address!", Toast.LENGTH_LONG).show();
            AudioManager mAlramMAnager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,0);

            } else {

                mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, false);

            }

        }

    }

    public void playMedia() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            a3 = false;

mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                    .setState(PlaybackStateCompat.STATE_PLAYING,1,1.0f).build());

        }
    }

    public void stopMedia() {
        if (mediaPlayer == null) return;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void pauseMedia() {
try{
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
            a2 = true;
            a3 = false;
mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                            .setState(PlaybackStateCompat.STATE_PAUSED,resumePosition,0.0f).build());

        }} catch (Exception io){
    mediaPlayer.stop();
    Toast.makeText(this,"Audio stopped due to error",Toast.LENGTH_SHORT).show();
}
    }

    public void resumeMedia() {
        if (mediaPlayer != null) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(resumePosition);
mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                    .setState(PlaybackStateCompat.STATE_PLAYING,resumePosition,1.0f).build());
            mediaPlayer.start();
            a2 = false;
            a3 = false;}
        } else {skipToNext2();
        updateMetaData();
            mediaPlayer.seekTo(timer2);
            mediaSession.setPlaybackState(new 	PlaybackStateCompat.Builder ()
                    .setState(PlaybackStateCompat.STATE_PLAYING,timer2,1.0f).build());
            mediaPlayer.start();
            requestAudioFocus();
        }
    }

    public void skipToNext() {
        pausepressed = 0;
        if (audioIndex == audioList.size() - 1) {
            //if last in playlist
            audioIndex = 0;
            activeAudio = audioList.get(audioIndex);
        } else {
            try{
            //get next in playlist
            activeAudio = audioList.get(++audioIndex);} catch (Exception oo){
                audioIndex = 0;
                activeAudio = audioList.get(audioIndex);
            }
        }
        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);
        stopMedia();
        //reset mediaPlayer
        try{
        mediaPlayer.reset();} catch( Exception ene){};

        initMediaPlayer();
        requestAudioFocus();



    }
    public void skipToNext2() {
        pausepressed = 0;
        if (audioIndex == audioList.size() - 1) {
            //if last in playlist
            audioIndex = 0;
            activeAudio = trueActive;
        } else {
            //get next in playlist
            activeAudio = trueActive;
        }
        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);
        stopMedia();
        //reset mediaPlayer
        try{
            mediaPlayer.reset();} catch( Exception ene){};

        initMediaPlayer();


    }


    public void playthefirstsong () {
        audioIndex = new StorageUtil(getApplicationContext()).loadAudioIndex();
        audioIndex = 0;
        activeAudio = audioList.get(audioIndex);
        stopMedia();
        mediaPlayer.reset();
        initMediaPlayer();
        updateMetaData();
        buildNotification(PlaybackStatus.PLAYING);

        a3 = false;

    }

    public void skipToPrevious() {
        pausepressed = 0;
        if (audioIndex == 0) {
            //if first in playlist
            //set index to the last of audioList
            audioIndex = audioList.size() - 1;
            activeAudio = audioList.get(audioIndex);
        } else {
            //get previous in playlist
            activeAudio = audioList.get(--audioIndex);
        }
        //Update stored index
        new StorageUtil(getApplicationContext()).storeAudioIndex(audioIndex);
        stopMedia();
        //reset mediaPlayer
        try {
        mediaPlayer.reset();} catch (Exception enee){};

        initMediaPlayer();
        requestAudioFocus();
 a2 = false;
        a3 = false;


    }

    /**
     * ACTION_AUDIO_BECOMING_NOISY -- change in audio outputs
     */
    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //pause audio on ACTION_AUDIO_BECOMING_NOISY
            pauseMedia();
            buildNotification(PlaybackStatus.PAUSED);
        }
    };

    private void registerBecomingNoisyReceiver() {
        //register after getting audio focus
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
    }

    /**
     * Handle PhoneState changes
     */
    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (mediaPlayer != null) {
                            pauseMedia();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (mediaPlayer != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                resumeMedia();
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * MediaSession and Notification actions
     */
    public void initMediaSession() throws RemoteException {

        if (mediaSessionManager != null) return; //mediaSessionManager exists
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
        mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);}

        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "VaporMusic");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        //Set mediaSession's MetaData
        updateMetaData();
        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                pausepressed = 0;
                super.onPlay();
                if ( sortmode2 == 4){

                    a2 = false;
                    a3 = false;
                    x3 = false;
                    a3 = false;
                    buildNotification(PlaybackStatus.PLAYING);
                    sendnotiplayback();
                    if (mediaPlayer == null){
                        skipToNext();
                        Toast.makeText(MediaPlayerService.this,
                                "Please wait 15 seconds... loading...", Toast.LENGTH_LONG).show();
                    }

                } else{
                resumeMedia();
                buildNotification(PlaybackStatus.PLAYING);
                sendnotiplayback();
                x3 = false;
                a3 = false;}
                UnMuteAudio();
            }

            @Override
            public void onPause() {
                super.onPause();
                if (mediaPlayer.getDuration() < 0){sortmode2 = 4;}
Log.d("sm2",String.valueOf(sortmode2));
                pausepressed = 1;
                if (mediaPlayer == null){



                    skipToNext2();
                        buildNotification(PlaybackStatus.PLAYING);


                    mediaPlayer.seekTo(timer2);
                    mediaPlayer.start();


                } else{
                    if ( sortmode2 == 4 ||mediaPlayer.getDuration() < 0){
                        MuteAudio();
                        buildNotification(PlaybackStatus.PAUSED);
                        a2 = true;
                        a3 = false;
                        sendnotiplayback();
                    } else{
                pauseMedia();
                buildNotification(PlaybackStatus.PAUSED);}}
                sendnotiplayback();
                x3 = true;
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                if (shupr % 2 == 1){
                    Random r = new Random();
                    int i1 = r.nextInt(audioList.size() - 0) + 0;
                    hihi(i1);
                } else{
                skipToNext();}
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
				sendnotiplayback();
 				a3 = false; UnMuteAudio();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                if (shupr % 2 == 1){

                    Random r = new Random();
                    int i1 = r.nextInt(audioList.size() - 0) + 0;
                    hihi(i1);
                } else{
                skipToPrevious();}
                updateMetaData();
                buildNotification(PlaybackStatus.PLAYING);
 sendnotiplayback(); UnMuteAudio();
                
                a3 = false;
            }

            @Override
            public void onStop() {
                super.onStop();
                if (mediaPlayer.isPlaying() != true){
                removeNotification();}
                //Stop the service
                stopSelf();
                UnMuteAudio();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }


        });
    }

    private Uri saveImage(Bitmap image) {

        //TODO - Should be processed in another thread
        File imagesFolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagesFolder.mkdirs();
            File file = new File(imagesFolder, activeAudio.getTitle()+"_"+activeAudio.getArtist()+".jpeg");
			try{
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(this, "com.mydomain.fileprovider", file);}
				catch (NullPointerException et) {
                Log.d(TAG, "imageerror " + et.getMessage());
			}

        } catch (IOException e) {
            Log.d(TAG, "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }


    public void updateMetaData() {
        Bitmap albumArt;
        String length = null;
		Long durationlong = null;
        try {
            MediaMetadataRetriever meta = new MediaMetadataRetriever();
            meta.setDataSource(activeAudio.getData());
            String durationStr = meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int millSecond = Integer.parseInt(durationStr);
            length =String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millSecond),
                    TimeUnit.MILLISECONDS.toSeconds(millSecond) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millSecond))
            );
            byte imgdata[] = meta.getEmbeddedPicture();
            albumArt = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
        } catch (Exception e) {
            albumArt = BitmapFactory.decodeResource(getResources(), R.drawable.image5);
        }



                            try{
        String index = String.valueOf(audioIndex);
        String size =  String.valueOf(audioList.size());

        Log.d("indexnum", index);
		durationboiii= Long.valueOf(activeAudio.getDuration());
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)

                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, activeAudio.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, activeAudio.getAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, activeAudio.getTitle())
				.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, durationboiii)
                .build());
        trueActive = activeAudio;

        Uri uri = saveImage(albumArt);
        String stringUri = null;
        try{
        stringUri = uri.toString();} catch (Exception hhh){}
               Intent intent = new Intent();
        intent.setAction("datatransfer");

        Bundle bundlex = new Bundle();

        // put the song's metadata
        String sortnum = String.valueOf(activeAudio.getMode());
        String oknum = String.valueOf(activeAudio.getSearched());
        String searchnum = String.valueOf(activeAudio.getSearch());
        bundlex.putString("size", size);
        bundlex.putString("index", index);
        bundlex.putString("track1", activeAudio.getTitle());
        bundlex.putString("artist1", activeAudio.getArtist());
        bundlex.putString("album1", activeAudio.getAlbum());
        bundlex.putString("uri1", stringUri);
        bundlex.putString("length1",length);
        bundlex.putString("sortlast1234",sortnum);
        bundlex.putString("searchedlast1234",oknum);
        bundlex.putString("searchlast1234",searchnum);



        // put the playback status
        
        // put your application's package
        bundlex.putString("scrobbling_source", "com.tk.lolirem.vapormusic");

        intent.putExtras(bundlex);
        sendBroadcast(intent);} catch (Exception io){}









    }

    public void buildNotification(PlaybackStatus playbackStatus) {
        /**
         * Notification actions -> playbackAction()
         *  0 -> Play
         *  1 -> Pause
         *  2 -> Next track
         *  3 -> Previous track
         */


        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;
        Bitmap notificationIcon;
        Palette.Swatch vibrantSwatch;


        try {
            MediaMetadataRetriever meta = new MediaMetadataRetriever();
            meta.setDataSource(trueActive.getData());
            byte imgdata[] = meta.getEmbeddedPicture();
            notificationIcon = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
            lol = notificationIcon;

        } catch (Exception e) {
            notificationIcon = BitmapFactory.decodeResource(getResources(), R.drawable.image5);



        }



        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        in.setAction(Intent.ACTION_MAIN);
        in.addCategory(Intent.CATEGORY_LAUNCHER);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
   channelId = "test-channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel newIncidentChannel = new NotificationChannel(channelId,
                    "Music Playback",
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(newIncidentChannel);
        }



        // Create a new Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {




   notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this,channelId)
		.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                // Hide the timestamp
               
                .setShowWhen(false)
                // Set the Notification style
                .setStyle(new MediaStyle()
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(playbackAction(4))
                        // Attach our MediaSession token
                        .setMediaSession(mediaSession.getSessionToken())
                        // Show our playback controls in the compat view
                        .setShowActionsInCompactView(0, 1, 2))
                // Set the Notification color
                .setColor(tint)
                // Set the large and small icons
                .setLargeIcon(notificationIcon)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                // Set Notification content information
                .setContentText(trueActive.getAlbum())
                .setContentTitle(trueActive.getTitle())
                .setContentInfo(trueActive.getArtist())

                    .setContentIntent(pendingIntent);
        //Set notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING) {
            try{
            notificationAction = android.R.drawable.ic_media_pause;

            //create the pause action
            play_pauseAction = playbackAction(1);
            notificationBuilder.setOngoing(true);} catch (Exception io){}
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            try{
            notificationAction = android.R.drawable.ic_media_play;
            //create the play action
            play_pauseAction = playbackAction(0);
            notificationBuilder.setOngoing(false);} catch (Exception io){}
        }
        // Add playback actions
        notificationBuilder.addAction(android.R.drawable.ic_media_previous, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(android.R.drawable.ic_media_next, "next", playbackAction(2))
        ;
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());}

    }

    public PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, MediaPlayerService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 4:
                playbackAction.setAction(ACTION_STOP);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    public void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }

    /**
     * Play new Audio
     */
    private BroadcastReceiver playNewAudio = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            //Get the new media index form SharedPreferences
            audioIndex = new StorageUtil(getApplicationContext()).loadAudioIndex();

            if (audioIndex != -1 && audioIndex < audioList.size()) {

                //index is in a valid range
                activeAudio = audioList.get(audioIndex);

            } else {
                stopSelf();
            }
            //A PLAY_NEW_AUDIO action received
            //reset mediaPlayer to play the new Audio
            stopMedia();
            try{
            mediaPlayer.reset();} catch (Exception damn){}
            initMediaPlayer();
            updateMetaData();
            buildNotification(PlaybackStatus.PLAYING);

            a3 = false;
        }
    };

    public void timer(){

        final Handler timerHandler = new Handler();
        Runnable timerRunnable = new Runnable() {
            String a = null;
            String b = null;
            @Override
            public void run() {
                StorageUtil storage = new StorageUtil(getApplicationContext());
                audioList = storage.loadAudio();
                audioIndex = storage.loadAudioIndex();



            }};

        timerHandler.postDelayed(timerRunnable, 0);



    }


    private void register_playNewAudio() {
        //Register playNewMedia receiver
        IntentFilter filter = new IntentFilter(Broadcast_PLAY_NEW_AUDIO);
        registerReceiver(playNewAudio, filter);
    }

    private void sendnotiplayback() {
        Intent intent = new Intent();
        intent.setAction("NOTIFICATION_CHANGED1");
        Bundle bundle = new Bundle();

        // put the song's metadata
        String str = String.valueOf(a2);
        bundle.putString("playmode", str);

        String str2 = String.valueOf(a3);
        bundle.putString("playmode2", str2);

        // put your application's package


        intent.putExtras(bundle);
        sendBroadcast(intent);

    }
public void hihi(int audioIndex) {
        //Store the new audioIndex to SharedPreferences
        StorageUtil storage = new StorageUtil(getApplicationContext());
        storage.storeAudioIndex(audioIndex);


        //Service is active
        //Send a broadcast to the service -> PLAY_NEW_AUDIO
        Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
        sendBroadcast(broadcastIntent);

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


    }

