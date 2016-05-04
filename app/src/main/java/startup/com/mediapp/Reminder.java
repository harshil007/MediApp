package startup.com.mediapp;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

public class Reminder extends AppCompatActivity {

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static Reminder inst;
    TextView pill,desc;
    String dose,med,freqno,freqdays,durano,duradays,pillname;
    int day,month,year;




    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    public static Reminder instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        inst = this;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Alarm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://startup.com.mediapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        setTitle("Reminder");

        Bundle b = getIntent().getExtras();
        dose = b.getString("dose");
        med = b.getString("item");
        freqno = b.getString("freqno");
        freqdays = b.getString("freqdays");
        durano = b.getString("durano");
        duradays = b.getString("duradays");
        pillname = b.getString("pill");
        day = b.getInt("day");
        month = b.getInt("month");
        year = b.getInt("year");

        Log.d("Bundle",dose+med+freqno+freqdays+durano+duradays+pillname+day+month+year);

        desc = (TextView)findViewById(R.id.description);
        String des = "Dosage :  " + dose + " " + med + " every " + freqno + " " + freqdays + "\nDuration : " + durano + " " + duradays;
        desc.setText(des);
        pill = (TextView) findViewById(R.id.pillname);
        pill.setText(getIntent().getExtras().getString("pill"));
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(Reminder.this, PillChooser.class);
                    startActivity(i);
                return;
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Alarm Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://startup.com.mediapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {


        public TimePickerFragment() {
            super();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Log.d("MyActivity", "Alarm On");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month-1);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            long  frequency = getFrequency();
            Log.d("duration",Long.toString(frequency));
            Intent myIntent = new Intent(Reminder.this, AlarmService.class);
            pendingIntent = PendingIntent.getService(Reminder.this, 0, myIntent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),frequency, pendingIntent);

        }
    }

    public long getFrequency(){
       long frequ = 0;
        if(freqdays.equals("Days")){
            frequ = Integer.parseInt(freqno) * 24 * 3600 * 1000;

        }else if(freqdays.equals("Weeks")){

            frequ = 7 * Integer.parseInt(freqno) * 24 * 3600 * 1000;

        }else if(freqdays.equals("Months")){

            frequ = 31 * Integer.parseInt(freqno) * 24 * 3600 * 1000;

        }

        return frequ;
    }

    public void delete(View v){
        if (alarmManager!= null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(Reminder.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
        }


        return;
    }



}




