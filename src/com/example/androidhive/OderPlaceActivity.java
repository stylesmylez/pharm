package com.example.androidhive;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

public class OderPlaceActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputname;
	EditText inputDrug;
	EditText inputQuantity;
	Spinner inputHealthInsurance;
	EditText inputDoctorInstitution;
	Spinner inputGender;
	EditText inputAge;
	String CoOrderId;
	String CouserId;
	String idname;
    private Button SetOTime;
	EditText setotime;

	// url to create new product
	private static String url_Order_Online = "http://bmgfdaycare.com/PharConnect/Order_Online.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderplace);

		// Edit Text
		inputname = (EditText) findViewById(R.id.xinputname);
		inputDrug = (EditText) findViewById(R.id.xinputDrug);
		inputQuantity = (EditText) findViewById(R.id.xinputQuantity);
		inputHealthInsurance = (Spinner) findViewById(R.id.xinputHealthInsurance);
		inputDoctorInstitution = (EditText) findViewById(R.id.xinputDoctorInstitution);
		inputGender = (Spinner) findViewById(R.id.xinputGender);
		inputAge = (EditText) findViewById(R.id.xinputAge);
		setotime = (EditText) findViewById(R.id.ordertimein);
		
		Bundle b = getIntent().getExtras();
		// getting specialist id (pid) from intent
		inputDrug.setText(b.getCharSequence("name"));
		//inputDoctorInstitution.setText(b.getCharSequence("coname"));
		CoOrderId = (String) b.getCharSequence("coname");
		CouserId = (String) b.getCharSequence("id");
		inputname.setText(b.getCharSequence("idname"));
		
		// Create button Home
		Button btnHomeSeaech = (Button) findViewById(R.id.ohome);
		btnHomeSeaech.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			// creating new product in background thread
			Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
			finish(); startActivity(i);
			
		}
	});
		SetOTime = (Button) findViewById(R.id.oSetTimein);
        Startdateobjects();
        SetOTime.setOnClickListener(new OnClickListener() {
        	 
            @Override
            public void onClick(View v) {
                 
                // On button click show datepicker dialog
                showDialog(TIME_DIALOG_ID);
 
            }
 
        });
	

		// Create button
		Button btnCreateProduct = (Button) findViewById(R.id.otborder);
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
    private int Hour;
    private int minute;
	private void Startdateobjects()
	{
		final Calendar c = Calendar.getInstance();
		Hour  = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
        
	}

    static final int TIME_DIALOG_ID = 999;
  
    protected Dialog onCreateDialog(int id) {
          switch (id){
       case TIME_DIALOG_ID:
            
           // set time picker as current time
           return new TimePickerDialog(this, timePickerListener, Hour, minute,
                   false);
       }
       return null;
   }


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
    
            setotime.setText(aTime);
       }

       ///Date

       
	private void ordersx()
	{
		String IfBlankName = ((EditText) findViewById(R.id.xinputname)).getText()
				.toString();
		// creating new product in background thread
		String IfBlankAge = ((EditText) findViewById(R.id.xinputAge)).getText()
				.toString();
		String IfBlanktime = ((EditText) findViewById(R.id.ordertimein)).getText()
				.toString();
		// creating new product in background thread
		String IfBlankQuantity = ((EditText) findViewById(R.id.xinputQuantity)).getText()
				.toString();
		// creating new product in background thread
		String IfBlankDrug = ((EditText) findViewById(R.id.xinputDrug)).getText()
				.toString();
					//tbnewval = (EditText) findViewById(R.id.tbnewval);
		//tbnewval.setText(nm);
        if (("".equals(IfBlankName)))            
        {
        	Toast.makeText(OderPlaceActivity.this,"Please type your name!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }	//tbnewval.setText(nm);
        if (("".equals(IfBlankQuantity)))            
        {
        	Toast.makeText(OderPlaceActivity.this,"Please type the quantity needed!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }	
        if (("".equals(IfBlankAge)))            
        {
        	Toast.makeText(OderPlaceActivity.this,"Please type your age!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }	
        if (("".equals(IfBlanktime)))            
        {
        	Toast.makeText(OderPlaceActivity.this,"Please choose pickup time!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }	//tbnewval.setText(nm);//tbnewval.setText(nm);
        if (("".equals(IfBlankDrug)))            
        {
        	Toast.makeText(OderPlaceActivity.this,"Please type your drug(s)!", 
        				Toast.LENGTH_LONG).show();	
        	return;
        }
			        else
			        {
			        	new PlaceOrder().execute();
			        }
		
	
	}
	
	/**
	 * Background Async Task to Create new product
	 * */
	class PlaceOrder extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OderPlaceActivity.this);
			pDialog.setMessage("Placing this order..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			String name = inputname.getText().toString();
			String Drug = inputDrug.getText().toString();
			String Quantity = inputQuantity.getText().toString();
			String HealthInsurance = inputHealthInsurance.getSelectedItem().toString();
			String DoctorInstitution = inputDoctorInstitution.getText().toString();
			String Gender = inputGender.getSelectedItem().toString();
			String Age = inputAge.getText().toString();
			String setotimex = setotime.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("Drug", Drug));
			params.add(new BasicNameValuePair("Quantity", Quantity));
			params.add(new BasicNameValuePair("HealthInsurance", HealthInsurance));
			params.add(new BasicNameValuePair("DoctorInstitution", DoctorInstitution));
			params.add(new BasicNameValuePair("Gender", Gender));
			params.add(new BasicNameValuePair("Age", Age));
			params.add(new BasicNameValuePair("CompanyId", CoOrderId));
			params.add(new BasicNameValuePair("userid", CouserId));
			params.add(new BasicNameValuePair("PickUpTime", setotimex));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_Order_Online,
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
