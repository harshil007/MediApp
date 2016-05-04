package startup.com.mediapp;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;

/**
 * Created by Pursnani Kapil on 28-Apr-16.
 */
public class stopAlarm extends IntentService {

    public stopAlarm() {
        super("stopAlarm");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent,START_STICKY,startId);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        AlarmService as = AlarmService.getInstance();
        MediaPlayer mMediaPlayer = as.getMP();

        mMediaPlayer.stop();
        mMediaPlayer.release();



    }

}
