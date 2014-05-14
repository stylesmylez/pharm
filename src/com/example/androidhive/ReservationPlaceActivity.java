package com.example.androidhive;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReservationPlaceActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText edname;
	EditText edNumber;
	Spinner edHealth;
	Spinner edGender;
	EditText edAge;
	EditText edemail;
	EditText edPurpose;
	EditText eddate;
	EditText edtime;
	String CoOrderId;
	String CouserId;
	String idname;
    private Button changeDate;
    private Button changeTime;

	// url to create new product
	private static String url_reservation_Online = "http://bmgfdaycare.com/PharConnect/reservation_Online.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservationplace);


		// Edit Text
		edname = (EditText) findViewById(R.id.presername);
		edNumber = (EditText) findViewById(R.id.preserNumber);
		edHealth = (Spinner) findViewById(R.id.preserHealthInsurance);
		edGender = (Spinner) findViewById(R.id.preserGender);
		edAge = (EditText) findViewById(R.id.preserAge);
		edemail = (EditText) findViewById(R.id.preseremail);
		edPurpose = (EditText) findViewById(R.id.preserPurposeOfVisit);
		eddate = (EditText) findViewById(R.id.preserdatein);
		edtime = (EditText) findViewById(R.id.presertimein);
					
		Bundle b = getIntent().getExtras();
		//CoOrderId = (String) b.getCharSequence("companyid");
		//CouserId = (String) b.getCharSequence("userid");
		edname.setText(b.getCharSequence("idname"));
		
		// Create button Home
		Button btnHomeSeaech = (Button) findViewById(R.id.reserhomex);
		btnHomeSeaech.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			// creating new product in background thread
			Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
			finish(); startActivity(i);
			
		}
	});
	
		changeDate = (Button) findViewById(R.id.ChooseDatein);
        changeTime = (Button) findViewById(R.id.ChooseTimein);
        Startdateobjects();
        changeDate.setOnClickListener(new OnClickListener() {
        	 
            @Override
            public void onClick(View v) {
                 
                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);
 
            }
 
        });
 	   changeTime.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
 
                showDialog(TIME_DIALOG_ID);
 
            }
 
        });

		// Create button
		Button btnCreateProduct = (Button) findViewById(R.id.reserplace);
		// button click event
		btnCreateProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				ordersx();
			}
		});
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

	    onBackPressed();
	}

	return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onBackPressed() {

	return;
	}
	
    ///Date
	private int year;
    private int month;
    private int day;
    private int Hour;
    private int minute;
	private void Startdateobjects()
	{
		final Calendar c = Calendar.getInstance();
		Hour  = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
        //updateTime(Hour, minute);
        
	}

    static final int DATE_PICKER_ID = 1111;
    static final int TIME_DIALOG_ID = 999;
  
    protected Dialog onCreateDialog(int id) {
       switch (id) {
       case DATE_PICKER_ID:
           return new DatePickerDialog(this, pickerListener, year, month,day);
       }
       switch (id){
       case TIME_DIALOG_ID:
            
           // set time picker as current time
           return new TimePickerDialog(this, timePickerListener, Hour, minute,
                   false);

       }
       return null;
   }

   private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() 
   {

       // when dialog box is closed, below method will be called.
       @Override
       public void onDateSet(DatePicker view, int selectedYear,
               int selectedMonth, int selectedDay) {
            
           year  = selectedYear;
           month = selectedMonth;
           day   = selectedDay;

           // Show selected date
           eddate.setText(new StringBuilder().append(year)
                   .append("-").append(month+1).append("-").append(day)
                   .append(" "));
    
          }
       };

       //time
       
       private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
            
    
           @Override
           public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
               // TODO Auto-generated method stub
               Hour   = hourOfDay;
               minute = minutes;
    
               updateTime(Hour,minute);
                
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
    
            edtime.setText(aTime);
       }

       ///Date

	private void ordersx()
	{
		String IfBlankName = ((EditText) findViewById(R.id.presername)).getText()
				.toString();
		String IfBlankdate = ((EditText) findViewById(R.id.preserdatein)).getText()
				.toString();
		String IfBlanktime = ((EditText) findViewById(R.id.presertimein)).getText()
				.toString();
		// creating new product in background thread
		String IfBlankAge = ((EditText) findViewById(R.id.preserAge)).getText()
				.toString();
		// creating new product in background thread
		String IfBlankPurposeOfVisit = ((EditText) findViewById(R.id.preserPurposeOfVisit)).getText()
				.toString();
					//tbnewval = (EditText) findViewById(R.id.tbnewval);
		//tbnewval.setText(nm);
        if (TextUtils.isEmpty(IfBlankName))            
        {
        	Toast.makeText(ReservationPlaceActivity.this,"Please type your name!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }	
        if (TextUtils.isEmpty(IfBlankAge))            
        {
        	Toast.makeText(ReservationPlaceActivity.this,"Please type your age!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }
        if (TextUtils.isEmpty(IfBlankPurposeOfVisit))            
        {
        	Toast.makeText(ReservationPlaceActivity.this,"Please type the purpose of your visit!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }
        if (TextUtils.isEmpty(IfBlanktime))            
        {
        	Toast.makeText(ReservationPlaceActivity.this,"Please set the time coming in!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }	//tbnewval.setText(nm);//tbnewval.setText(nm);
        if (TextUtils.isEmpty(IfBlankdate))            
        {
        	Toast.makeText(ReservationPlaceActivity.this,"Please  set the date coming in!", 
        				Toast.LENGTH_LONG).show();	
        	return;
        }
			        else
			        {
			        	new Placereservation().execute();
			        }
		
	
	}
	
	/**
	 * Background Async Task to Create new product
	 * */
	class Placereservation extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReservationPlaceActivity.this);
			pDialog.setMessage("Placing this order..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			
			Bundle b = getIntent().getExtras();
			CoOrderId = (String) b.getCharSequence("companyid");
			CouserId = (String) b.getCharSequence("userid");
			//edname.setText(b.getCharSequence("idname"));
			
			
			String name = edname.getText().toString();
			String edNumberx = edNumber.getText().toString();
			String edHealthx = edHealth.getSelectedItem().toString();
			String edGenderx = edGender.getSelectedItem().toString();
			String edAgex = edAge.getText().toString();
			String edemailx = edemail.getText().toString();
			String edPurposex = edPurpose.getText().toString();
			String eddatex = eddate.getText().toString();
			String edtimex = edtime.getText().toString();		
			
		
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Name", name));
			params.add(new BasicNameValuePair("Number", edNumberx));
			params.add(new BasicNameValuePair("HealthInsurance", edHealthx));
			params.add(new BasicNameValuePair("Gender", edGenderx));
			params.add(new BasicNameValuePair("Age", edAgex));
			params.add(new BasicNameValuePair("Email", edemailx));
			params.add(new BasicNameValuePair("PurposeOfVisit", edPurposex));
			params.add(new BasicNameValuePair("DateComingIn", eddatex));
			params.add(new BasicNameValuePair("TimeComingIn", edtimex));
			params.add(new BasicNameValuePair("CompanyId", CoOrderId.toString()));
			params.add(new BasicNameValuePair("userid", CouserId.toString()));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_reservation_Online,
					"POST", params);
			
			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
					finish(); startActivity(i);
					
					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}
}
