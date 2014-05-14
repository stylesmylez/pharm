package com.example.androidhive;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReservationEditActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText edname;
	EditText edNumber;
	EditText edHealth;
	EditText edGender;
	EditText edAge;
	EditText edemail;
	EditText edPurpose;
	EditText eddate;
	EditText edtime;
	TextView eresupid;
	String CoOrderId;
	String Cancelx;
	String CouserId;
	String idname;
	List<String> doc;
	List gen;
    private Button changeDate;
    private Button changeTime;
	

	private static final String TAG_reservation = "reservation";
	private static final String TAG_ID = "Id";
	private static final String TAG_NAME = "Name";
	private static final String TAG_Gender = "Gender";
	private static final String TAG_Age = "Age";
	private static final String TAG_Email = "Email";
	private static final String TAG_Number = "Number";
	private static final String TAG_HealthInsurance = "HealthInsurance";
	private static final String TAG_DateComingIn = "DateComingIn";
	private static final String TAG_TimeComingIn = "TimeComingIn";
	private static final String TAG_PurposeOfVisit = "PurposeOfVisit";

	// url to create new product
	private static String url_Editreservation_Online = "http://bmgfdaycare.com/PharConnect/reservation_Update.php";
	private static String url_reservationDetails_Online = "http://bmgfdaycare.com/PharConnect/Get_reservation_Details.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservationedit);

		
		// Edit Text
		edname = (EditText) findViewById(R.id.reseredname);
		edNumber = (EditText) findViewById(R.id.reseredNumber);
		edHealth = (EditText) findViewById(R.id.reseredHealthInsurance);
		edGender = (EditText) findViewById(R.id.reseredGender);
		edAge = (EditText) findViewById(R.id.reseredAge);
		edemail = (EditText) findViewById(R.id.reseredemail);
		edPurpose = (EditText) findViewById(R.id.reseredPurposeOfVisit);
		eddate = (EditText) findViewById(R.id.resereddatein);
		edtime = (EditText) findViewById(R.id.reseredtimein);

        changeDate = (Button) findViewById(R.id.SetDatein);
        changeTime = (Button) findViewById(R.id.SetTimein);
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
        
		new GetProductDetails().execute();
		// Create button Home
		Button btnHomeSeaech = (Button) findViewById(R.id.reseredohome);
		Button cancel = (Button) findViewById(R.id.reseredcancelorder);
		btnHomeSeaech.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			// creating new product in background thread
			Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
			finish(); startActivity(i);
			
		}
	});
	
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				Cancelx="1".toString();
				ordersx();
				
			}
		});
	

		// Create button
		Button update = (Button) findViewById(R.id.reseredupdate);
		// button click event
		update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				Cancelx="0".toString();
				ordersx();
				
			}
		});
	}

	private void ordersx()
	{
		new EditReservation().execute();
		Toast.makeText(ReservationEditActivity.this,"Record updated, Go home & come back to see changes!", 
			    				Toast.LENGTH_LONG).show();
		
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
	
	/**
	 * Background Async Task to Create new product
	 * */
	class EditReservation extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReservationEditActivity.this);
			pDialog.setMessage("updating order..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			String name = edname.getText().toString();
			String edNumberx = edNumber.getText().toString();
			String edHealthx = edHealth.getText().toString();
			String edGenderx = edGender.getText().toString();
			String edAgex = edAge.getText().toString();
			String edemailx = edemail.getText().toString();
			String edPurposex = edPurpose.getText().toString();
			String eddatex = eddate.getText().toString();
			String edtimex = edtime.getText().toString();
			String id = eresupid.getText().toString();
			
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
			params.add(new BasicNameValuePair("Id", id));
			params.add(new BasicNameValuePair("Cancel", Cancelx));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_Editreservation_Online,
					"POST", params);
			
			
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

	class GetProductDetails extends AsyncTask<String, String, String> {


		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ReservationEditActivity.this);
			pDialog.setMessage("Loading product details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					// Check for success tag
					int success;
					try {
						// Building Parameters
						Bundle b = getIntent().getExtras();		
						// getting specialist id (pid) from intent

						CouserId = (String) b.getCharSequence("id");						
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("Id", CouserId.toString()));

						// getting product details by making HTTP request
						// Note that product details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_reservationDetails_Online, "GET", params);

						// check your log for json response
						Log.d("Single Product Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received product details
							JSONArray productObj = json
									.getJSONArray(TAG_reservation); // JSON Array
							
							// get first product object from JSON Array
							JSONObject product = productObj.getJSONObject(0);

							// Edit Text

							edname = (EditText) findViewById(R.id.reseredname);
							edNumber = (EditText) findViewById(R.id.reseredNumber);
							edHealth = (EditText) findViewById(R.id.reseredHealthInsurance);
							edGender = (EditText) findViewById(R.id.reseredGender);
							edAge = (EditText) findViewById(R.id.reseredAge);
							edemail = (EditText) findViewById(R.id.reseredemail);
							edPurpose = (EditText) findViewById(R.id.reseredPurposeOfVisit);
							eddate = (EditText) findViewById(R.id.resereddatein);
							edtime = (EditText) findViewById(R.id.reseredtimein);	
							eresupid = (TextView) findViewById(R.id.resupid);	
							// display product data in EditText							
							edname.setText(product.getString(TAG_NAME));
							edNumber.setText(product.getString(TAG_Number));
							edHealth.setText(product.getString(TAG_HealthInsurance));
							edGender.setText(product.getString(TAG_Gender));
							edAge.setText(product.getString(TAG_Age));
							edemail.setText(product.getString(TAG_Email));							
							edPurpose.setText(product.getString(TAG_PurposeOfVisit));							
							eddate.setText(product.getString(TAG_DateComingIn));							
							edtime.setText(product.getString(TAG_TimeComingIn));								
							eresupid.setText(product.getString(TAG_ID));						
							//Cancelx = (String)(product.getString(TAG_Id));	

						}else{
							// product with pid not found
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});

			return null;
		}


		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
		}
	}

}
