package startup.com.mediapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dosage extends AppCompatActivity {

    Button done;
    RadioGroup rg;
    Spinner s1,s2,s3;
    NumberPicker n1,n2,n3;
    String dose;
    String i1="1",i2="1",i3="";
    String str1="Days",str2="Days",str3="Lifetime";
    String pill;
    static int d;
    static int m;
    static int y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Calendar c = Calendar.getInstance();
         y = c.get(Calendar.YEAR);
         m = c.get(Calendar.MONTH)+1;
         d = c.get(Calendar.DAY_OF_MONTH);

        pill = getIntent().getExtras().getString("pill");


        s1 = (Spinner) findViewById(R.id.days);

        s2 = (Spinner) findViewById(R.id.item);

        s3 = (Spinner) findViewById(R.id.frequency);

        n1 =  (NumberPicker) findViewById(R.id.number);
        n1.setMinValue(1);
        n1.setMaxValue(31);
        n1.setValue(1);
        n1.setWrapSelectorWheel(true);
        n1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                i3=Integer.toString(newVal);
            }
        });


        n2 = (NumberPicker) findViewById(R.id.quantity);
        n2.setMinValue(1);
        n2.setMaxValue(5);
        n2.setValue(1);
        n2.setWrapSelectorWheel(true);
        n2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                i1=Integer.toString(newVal);;

            }
        });


        n3 = (NumberPicker) findViewById(R.id.freq);
        n3.setMinValue(1);
        n3.setMaxValue(31);
        n3.setValue(1);
        n3.setWrapSelectorWheel(true);
        n3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker

                i2 = Integer.toString(newVal);

            }
        });

        s1.setEnabled(false);
        n1.setEnabled(false);

        rg = (RadioGroup) findViewById(R.id.duration);
        rg.check(R.id.lifetime);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if (checkedId == R.id.lifetime) {
                    s1.getSelectedView().setEnabled(false);
                    s1.setEnabled(false);
                    n1.setEnabled(false);
                    str3  = "Lifetime";
                    i3="";

                } else if (checkedId == R.id.setduration) {

                    s1.getSelectedView().setEnabled(true);
                    s1.setEnabled(true);
                    n1.setEnabled(true);
                    str3 = "Days";
                    i3 = "1";

                }

            }

        });



        done = (Button)findViewById(R.id.done);




        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Days");
        categories.add("Weeks");
        categories.add("Months");


        List<String> categories1 = new ArrayList<String>();
        categories1.add("Tablet");
        categories1.add("Capsule");
        categories1.add("Injection");
        categories1.add("Drop");
        categories1.add("Lotion");

        List<String> categories2 = new ArrayList<String>();
        categories2.add("Days");
        categories2.add("Weeks");
        categories2.add("Months");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        s1.setAdapter(dataAdapter);


        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                str3=parent.getItemAtPosition(pos).toString();


            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        });

        str3="Lifetime";




        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories1);

        // Drop down layout style - list view with radio button
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        s2.setAdapter(dataAdapter1);

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                str1=parent.getItemAtPosition(pos).toString();

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        });




        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);

        // Drop down layout style - list view with radio button
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        s3.setAdapter(dataAdapter2);

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                // parent.getItemAtPosition(pos)
                str2=parent.getItemAtPosition(pos).toString();


            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        });

    }


    public void done(View v){

        dose = "Dosage :  " + i1 +" " + str1 + " every " + i2 + " " + str2 + "\nDuration : " + i3 + " " + str3;
        Intent i = new Intent(this,Reminder.class);
        Bundle b = new Bundle();
        b.putString("dose",i1);
        b.putString("item",str1);
        b.putString("freqno",i2);
        b.putString("freqdays",str2);
        b.putString("durano",i3);
        b.putString("duradays",str3);
        b.putString("pill",pill);
        b.putInt("day", d);
        b.putInt("month", m);
        b.putInt("year",y);
        i.putExtras(b);
        startActivity(i);

        return;
    }




    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            d=day;
            m=month+1;
            y=year;

        }
    }

}
