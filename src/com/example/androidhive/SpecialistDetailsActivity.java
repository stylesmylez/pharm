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

public class SpecialistDetailsActivity extends Activity {

	EditText spetxtName;
	//EditText spetxtPrice;
	EditText spetxtDesc;
	EditText spetxtCreatedAt;
	EditText spetxtAddress;
	EditText spetxtCompany;
	EditText spetxtContact;
	TextView spetxtadsname;
	Button spebtnBack;
	Button spebtnHomere;
	Button MakeRser;
	EditText Name;
	TextView spetxtCompanyId;

	String pid;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single specialist url
	private static final String url_specialist_detials ="http://bmgfdaycare.com/PharConnect/get_specialist_details.php";

	// url to update specialist
	private static final String url_update_specialist = "http://bmgfdaycare.com/PharConnect/update_specialist.php";
	
	// url to delete specialist
	private static final String url_delete_specialist = "http://bmgfdaycare.com/PharConnect/delete_specialist.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_specialist = "specialist";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	//private static final String TAG_PRICE = "price";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_ADDRRESS = "address";
	private static final String TAG_COMPANY = "company";
	private static final String TAG_CONTACT = "contact";
	private static final String TAG_adsnames = "adsname";
	private static final String TAG_CompanyId = "CompanyId";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specialist_details);

       // Name = (EditText) findViewById(R.id.AdsName); 
        //Name.setText(b.getCharSequence("name"));
        
		// Create button Home and back
		spebtnBack = (Button) findViewById(R.id.spebtnBack);
		spebtnHomere = (Button) findViewById(R.id.spebtnHomere);
		MakeRser = (Button) findViewById(R.id.spebtnOrder);
		spebtnBack.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// creating new specialist in background thread
			Intent i = new Intent(getApplicationContext(), SpecialistAll.class);
			finish(); startActivity(i);
			
		}
	});
		spebtnHomere.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// creating new specialist in background thread
			Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
			finish(); startActivity(i);
			
		}
	});
		MakeRser.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// creating new hospital in background thread
				
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		        
				Bundle b = new Bundle();		 
		        //Inserts a String value into the mapping of this Bundle	
		        i.putExtra("coname",spetxtCompanyId.getText().toString());
		        i.putExtra("name","make");		 
		        //Add the bundle to the intent.
		        i.putExtras(b);			        
		        //start the DisplayActivity
		        startActivity(i);
				
			}
		});
	
		// getting specialist details from intent
		//Intent i = getIntent();

		Bundle b = getIntent().getExtras();
		// getting specialist id (pid) from intent
		pid = (String) b.getCharSequence("name");

		// Getting complete specialist details in background thread
		new GetspecialistDetails().execute();
		

	}

	/**
	 * Background Async Task to Get complete specialist details
	 * */
	class GetspecialistDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SpecialistDetailsActivity.this);
			pDialog.setMessage("Loading specialist details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting specialist details in background thread
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

						// getting specialist details by making HTTP request
						// Note that specialist details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_specialist_detials, "GET", params);

						// check your log for json response
						Log.d("Single specialist Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received specialist details
							JSONArray specialistObj = json
									.getJSONArray(TAG_specialist); // JSON Array
							
							// get first specialist object from JSON Array
							JSONObject specialist = specialistObj.getJSONObject(0);

							// specialist with this pid found
							// Edit Text
							spetxtName = (EditText) findViewById(R.id.speinputName);
							//spetxtPrice = (EditText) findViewById(R.id.speinputPrice);
							spetxtDesc = (EditText) findViewById(R.id.speinputDesc);
							spetxtAddress = (EditText) findViewById(R.id.speinputAddress);
							spetxtCompany = (EditText) findViewById(R.id.speinputCompany);
							spetxtContact = (EditText) findViewById(R.id.speinputContact);
							spetxtadsname = (TextView) findViewById(R.id.adsnames);
							spetxtCompanyId = (TextView) findViewById(R.id.tbCompanyspeciOrderId);

							// display specialist data in EditText
							spetxtName.setText(specialist.getString(TAG_NAME));
							//spetxtPrice.setText(specialist.getString(TAG_PRICE));
							spetxtDesc.setText(specialist.getString(TAG_DESCRIPTION));
							spetxtAddress.setText(specialist.getString(TAG_ADDRRESS));
							spetxtCompany.setText(specialist.getString(TAG_COMPANY));
							spetxtContact.setText(specialist.getString(TAG_CONTACT));
							spetxtadsname.setText(specialist.getString(TAG_adsnames));
							spetxtCompanyId.setText(specialist.getString(TAG_CompanyId));
							
							

							// Loader image - will be shown before loading image
					        int loader =1;         
					        // Imageview to show
					        ImageView image1 = (ImageView) findViewById(R.id.SpeAds);         
					        //Image url
					        String Ads1 = "http://bmgfdaycare.com/PharConnect/SpecialistAds/"+spetxtadsname.getText().toString()+".png";         
					        //ImageLoader class instance
					        adsImageLoader imgLoader = new adsImageLoader(getApplicationContext());
					        imgLoader.DisplayImage(Ads1, loader, image1);

						}else{
							// specialist with pid not found
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
