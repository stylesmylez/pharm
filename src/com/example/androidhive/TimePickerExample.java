package com.example.androidhive;
import java.util.Calendar;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
 
public class TimePickerExample extends Activity {
 
    static final int TIME_DIALOG_ID = 1111;
    private TextView output;
    public Button btnClick;
 
    private int hour;
    private int minute;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stylesmylez);
 
        output = (TextView) findViewById(R.id.output);
         
     /********* display current time on screen Start ********/
         
        final Calendar c = Calendar.getInstance();
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);
         
        // set current time into output textview
        //updateTime(hour, minute);
         
     /********* display current time on screen End ********/
         
        // Add Button Click Listener
        addButtonClickListener();
 
    }
 
    public void addButtonClickListener() {
 
        btnClick = (Button) findViewById(R.id.btnClick);
 
        btnClick.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
 
                showDialog(TIME_DIALOG_ID);
 
            }
 
        });
 
    }
 
    //@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case TIME_DIALOG_ID:
             
            // set time picker as current time
            return new TimePickerDialog(this, timePickerListener, hour, minute,
                    false);
 
        }
        return null;
    }
 
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
         
 
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour   = hourOfDay;
            minute = minutes;
 
            updateTime(hour,minute);
             
         }
 
    };
 
    private static String utilTime(int value) {
         
        if (value < 10)
            return "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }
     
    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {
         
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
 
         
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
 
        // Append in a StringBuilder
         String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
 
          output.setText(aTime);
    }


}