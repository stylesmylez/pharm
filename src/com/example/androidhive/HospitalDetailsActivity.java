package com.example.androidhive;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class HospitalDetailsActivity extends Activity {

	EditText hostxtName;
	//EditText hostxtPrice;
	EditText hostxtDesc;
	EditText hostxtCreatedAt;
	EditText hostxtAddress;
	EditText hostxtCompany;
	EditText hostxtContact;
	TextView hostxtadsname;
	Button hosbtnBack;
	Button hosbtnHomere;
	Button MakeRser;
	EditText Name;
	String pid;
	TextView hostxtCompanyId;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single hospital url
	private static final String url_hospital_detials ="http://bmgfdaycare.com/PharConnect/get_hospital_details.php";

	// url to update hospital
	private static final String url_update_hospital = "http://bmgfdaycare.com/PharConnect/update_hospital.php";
	
	// url to delete hospital
	private static final String url_delete_hospital = "http://bmgfdaycare.com/PharConnect/delete_hospital.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_hospital = "hospital";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	//private static final String TAG_PRICE = "price";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_ADDRRESS = "address";
	private static final String TAG_COMPANY = "company";
	private static final String TAG_CONTACT = "contact";
	private static final String TAG_adsnameh = "adsname";
	private static final String TAG_CompanyId = "CompanyId";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hospital_details);

		// Create button Home and back
		hosbtnBack = (Button) findViewById(R.id.hosbtnBack);
		MakeRser = (Button) findViewById(R.id.hosbtnOrder);
		hosbtnHomere = (Button) findViewById(R.id.hosbtnHomere);
		hostxtadsname = (TextView) findViewById(R.id.adsnameh);
		hosbtnBack.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// creating new hospital in background thread
			Intent i = new Intent(getApplicationContext(), hospiAll.class);
			finish(); startActivity(i);
			
		}
	});
		hosbtnHomere.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// creating new hospital in background thread
			Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
			finish(); startActivity(i);
			
		}
	});
		
		MakeRser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// creating new hospital in background thread
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		        
				//Create a bundle object
		        Bundle b = new Bundle();		 
		        //Inserts a String value into the mapping of this Bundle	
		        i.putExtra("coname",hostxtCompanyId.getText().toString());
		        i.putExtra("name","make");		 
		        //Add the bundle to the intent.
		        i.putExtras(b);			        
		        //start the DisplayActivity
		        startActivity(i);
				
			}
		});
		
		Bundle b = getIntent().getExtras();
		// getting specialist id (pid) from intent
		pid = (String) b.getCharSequence("name");

		// Getting complete hospital details in background thread
		new GethospitalDetails().execute();

	}

	/**
	 * Background Async Task to Get complete hospital details
	 * */
	class GethospitalDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(HospitalDetailsActivity.this);
			pDialog.setMessage("Loading hospital details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting hospital details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					// Check for success tag
					int success;
					try {
						// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("pid", pid));

						// getting hospital details by making HTTP request
						// Note that hospital details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_hospital_detials, "GET", params);

						// check your log for json response
						Log.d("Single hospital Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received hospital details
							JSONArray hospitalObj = json
									.getJSONArray(TAG_hospital); // JSON Array
							
							// get first hospital object from JSON Array
							JSONObject hospital = hospitalObj.getJSONObject(0);

							// hospital with this pid found
							// Edit Text
							hostxtName = (EditText) findViewById(R.id.hosinputName);
							//hostxtPrice = (EditText) findViewById(R.id.hosinputPrice);
							hostxtDesc = (EditText) findViewById(R.id.hosinputDesc);
							hostxtAddress = (EditText) findViewById(R.id.hosinputAddress);
							hostxtCompany = (EditText) findViewById(R.id.hosinputCompany);
							hostxtadsname = (TextView) findViewById(R.id.adsnameh);
							hostxtCompanyId = (TextView) findViewById(R.id.tbCompanyHospiOrderId);
							hostxtContact = (EditText) findViewById(R.id.hosinputContact);
							

							// display hospital data in EditText
							hostxtName.setText(hospital.getString(TAG_NAME));
							//hostxtPrice.setText(hospital.getString(TAG_PRICE));
							hostxtDesc.setText(hospital.getString(TAG_DESCRIPTION));
							hostxtAddress.setText(hospital.getString(TAG_ADDRRESS));
							hostxtCompany.setText(hospital.getString(TAG_COMPANY));
							hostxtadsname.setText(hospital.getString(TAG_adsnameh));
							hostxtCompanyId.setText(hospital.getString(TAG_CompanyId));
							hostxtContact.setText(hospital.getString(TAG_CONTACT));
							
							// Loader image - will be shown before loading image
					        int loader =1;         
					        // Imageview to show
					        ImageView image1 = (ImageView) findViewById(R.id.HosAds);         
					        //Image url
					        String Ads1 = "http://bmgfdaycare.com/PharConnect/HospitalAds/"+hostxtadsname.getText().toString()+".png";         
					        //ImageLoader class instance
					        adsImageLoader imgLoader = new adsImageLoader(getApplicationContext());
					        imgLoader.DisplayImage(Ads1, loader, image1);

						}else{
							// hospital with pid not found
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
