package startup.com.mediapp;

/**
 * Created by Vishu on 22-04-2016.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by c on 4/8/2016.
 */
public class AlarmService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private NotificationManager alarmNotificationManager;
    public static AlarmService inst;
    MediaPlayer mMediaPlayer;
    Uri alarmUri;

    public AlarmService() {
        super("AlarmService");
        inst = this;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent,START_STICKY,startId);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            playRingtone(intent.getExtras().getString("uri"));
        } catch (IOException e) {
            e.printStackTrace();

            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }


            mMediaPlayer = new MediaPlayer();

            try {
                mMediaPlayer.setDataSource(this, alarmUri);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);

            try {
                mMediaPlayer.prepare();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            mMediaPlayer.setLooping(true);

            mMediaPlayer.start();
        }
        sendNotification("Touch to cancel");

    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getService(this, 0,
                new Intent(this, stopAlarm.class), 0);

        NotificationCompat.Builder alamNotificationBuilder =
                new NotificationCompat.Builder(
                        this).setContentTitle("Pill Time").setSmallIcon(R.drawable.ic_alarm_add_black_24dp)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setContentText(msg);


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
        return;
    }

    public void playRingtone(String uri) throws IOException {

        SharedPreferences sharedPref = getApplicationContext().
                getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.Default);
        String almuri = sharedPref.getString("uri", defaultValue);
        Log.d("almuri",almuri);


        if(!almuri.equals("false")){
            alarmUri = Uri.parse(almuri);
        }
        else {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
        }

        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }


        mMediaPlayer = new MediaPlayer();

        mMediaPlayer.setDataSource(this, alarmUri);

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);

        mMediaPlayer.prepare();
        mMediaPlayer.setLooping(true);

        mMediaPlayer.start();
        return;
    }

    public MediaPlayer getMP(){
        return this.mMediaPlayer;
    }
    public static AlarmService getInstance()
    {
        return inst;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

