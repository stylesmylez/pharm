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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class OderEditActivity extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText edinputname;
	EditText edinputDrug;
	EditText edinputQuantity;
	EditText edinputHealthInsurance;
	EditText edinputDoctorInstitution;
	EditText edinputGender;
	EditText edinputAge;
	TextView updateidx;
	String CoOrderId;
	String Cancelx;
	String CouserId;
	String idname;
	List<String> doc;
	List gen;
    private Button SetOTime;
	EditText setotime;
	
	private static final String TAG_orders = "orders";
	private static final String TAG_ID = "Id";
	private static final String TAG_NAME = "Name";
	private static final String TAG_Drug = "Drug";
	private static final String TAG_Quantity = "Quantity";
	private static final String TAG_HealthInsurance = "HealthInsurance";
	private static final String TAG_DoctorInstitution = "DoctorInstitution";
	private static final String TAG_Gender = "Gender";
	private static final String TAG_Age = "Age";
	private static final String TAG_Id = "Id";
	private static final String TAG_PickUpTime = "PickUpTime";

	// url to create new product
	private static String url_EditOrder_Online = "http://bmgfdaycare.com/PharConnect/Order_Update.php";
	private static String url_OrderDetails_Online = "http://bmgfdaycare.com/PharConnect/Get_Order_Details.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderedit);
		
		// Edit Text
		edinputname = (EditText) findViewById(R.id.edxinputname);
		edinputDrug = (EditText) findViewById(R.id.edxinputDrug);
		edinputQuantity = (EditText) findViewById(R.id.edxinputQuantity);
		edinputHealthInsurance = (EditText) findViewById(R.id.Insurancexc);
		edinputDoctorInstitution = (EditText) findViewById(R.id.edxinputDoctorInstitution);
		edinputGender = (EditText) findViewById(R.id.Genderxc);
		edinputAge = (EditText) findViewById(R.id.edxinputAge);
		setotime = (EditText) findViewById(R.id.edittimein);
		
		
		new GetProductDetails().execute();
		// Create button Home
		Button btnHomeSeaech = (Button) findViewById(R.id.edohome);
		Button cancel = (Button) findViewById(R.id.edcancelorder);
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

		SetOTime = (Button) findViewById(R.id.editSetTimein);
        Startdateobjects();
        SetOTime.setOnClickListener(new OnClickListener() {
        	 
            @Override
            public void onClick(View v) {
                 
                // On button click show datepicker dialog
                showDialog(TIME_DIALOG_ID);
 
            }
 
        });
	

		// Create button
		Button update = (Button) findViewById(R.id.edupdate);
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
       //Date
       
	private void ordersx()
	{
		String IfBlankName = ((EditText) findViewById(R.id.edxinputname)).getText()
				.toString();
		// creating new product in background thread
		String IfBlankAge = ((EditText) findViewById(R.id.edxinputAge)).getText()
				.toString();
		// creating new product in background thread
		String IfBlankQuantity = ((EditText) findViewById(R.id.edxinputQuantity)).getText()
				.toString();
		// creating new product in background thread
		String IfBlankDrug = ((EditText) findViewById(R.id.edxinputDrug)).getText()
				.toString();
					//tbnewval = (EditText) findViewById(R.id.tbnewval);
		//tbnewval.setText(nm);
        if (("".equals(IfBlankName)))            
        {
        	Toast.makeText(OderEditActivity.this,"Please type your name!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }	//tbnewval.setText(nm);
        if (("".equals(IfBlankQuantity)))            
        {
        	Toast.makeText(OderEditActivity.this,"Please type the quantity needed!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }	
        if (("".equals(IfBlankAge)))            
        {
        	Toast.makeText(OderEditActivity.this,"Please type your age!", 
        				Toast.LENGTH_LONG).show();	 
        	return;
        }	//tbnewval.setText(nm);//tbnewval.setText(nm);
        if (("".equals(IfBlankDrug)))            
        {
        	Toast.makeText(OderEditActivity.this,"Please type your drug(s)!", 
        				Toast.LENGTH_LONG).show();	
        	return;
        }
			        else
			        {
			        	new EditOrder().execute();

						Toast.makeText(OderEditActivity.this,"Record updated, Go home & come back to see changes!", 
			    				Toast.LENGTH_LONG).show();
			        }
		
	
	}
	
	class EditOrder extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OderEditActivity.this);
			pDialog.setMessage("updating order..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String name = edinputname.getText().toString();
			String Drug = edinputDrug.getText().toString();
			String Quantity = edinputQuantity.getText().toString();
			String HealthInsurance = edinputHealthInsurance.getText().toString();
			String DoctorInstitution = edinputDoctorInstitution.getText().toString();
			String Gender = edinputGender.getText().toString();
			String Age = edinputAge.getText().toString();
			String id = updateidx.getText().toString();
			String PickUpTime = setotime.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("Drug", Drug));
			params.add(new BasicNameValuePair("Quantity", Quantity));
			params.add(new BasicNameValuePair("HealthInsurance", HealthInsurance));
			params.add(new BasicNameValuePair("DoctorInstitution", DoctorInstitution));
			params.add(new BasicNameValuePair("Gender", Gender));
			params.add(new BasicNameValuePair("Age", Age));
			params.add(new BasicNameValuePair("Id", id));
			params.add(new BasicNameValuePair("Cancel", Cancelx));
			params.add(new BasicNameValuePair("PickUpTime", PickUpTime));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_EditOrder_Online,
					"POST", params);
			
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}

	class GetProductDetails extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OderEditActivity.this);
			pDialog.setMessage("Loading product details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

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
						params.add(new BasicNameValuePair("Id", CouserId));

						// getting product details by making HTTP request
						// Note that product details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_OrderDetails_Online, "GET", params);

						// check your log for json response
						Log.d("Single Product Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received product details
							JSONArray productObj = json
									.getJSONArray(TAG_orders); // JSON Array
							
							// get first product object from JSON Array
							JSONObject product = productObj.getJSONObject(0);
			
							// Edit Text
							edinputname = (EditText) findViewById(R.id.edxinputname);
							edinputDrug = (EditText) findViewById(R.id.edxinputDrug);
							edinputQuantity = (EditText) findViewById(R.id.edxinputQuantity);
							edinputHealthInsurance = (EditText) findViewById(R.id.Insurancexc);
							edinputDoctorInstitution = (EditText) findViewById(R.id.edxinputDoctorInstitution);
							edinputGender = (EditText) findViewById(R.id.Genderxc);
							edinputAge = (EditText) findViewById(R.id.edxinputAge);
							updateidx = (TextView) findViewById(R.id.updateid);
							setotime = (EditText) findViewById(R.id.edittimein);
	
							// display product data in EditText							
							edinputname.setText(product.getString(TAG_NAME));
							edinputDrug.setText(product.getString(TAG_Drug));
							edinputQuantity.setText(product.getString(TAG_Quantity));
							edinputHealthInsurance.setText(product.getString(TAG_HealthInsurance));
							edinputGender.setText(product.getString(TAG_Gender));
							edinputDoctorInstitution.setText(product.getString(TAG_DoctorInstitution));							
							edinputAge.setText(product.getString(TAG_Age));								
							updateidx.setText(product.getString(TAG_Id));							
							setotime.setText(product.getString(TAG_PickUpTime));							
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

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
		}
	}

}
