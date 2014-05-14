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

public class ProductDetailsActivity extends Activity {

	EditText txtName;
	EditText txtPrice;
	EditText txtDesc;
	EditText txtCreatedAt;
	EditText txtAddress;
	EditText txtCompany;
	EditText txtContact;
	TextView txtadsname;
	TextView txtCompanyId;
	Button btnBack;
	Button btnbtnOrder;
	Button btnHomere;
	EditText Name;
	String pid;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single product url
	private static final String url_product_detials ="http://bmgfdaycare.com/PharConnect/get_product_details.php";

	// url to update product
	private static final String url_update_product = "http://bmgfdaycare.com/PharConnect/update_product.php";
	
	// url to delete product
	private static final String url_delete_product = "http://bmgfdaycare.com/PharConnect/delete_product.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCT = "product";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	private static final String TAG_PRICE = "price";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_ADDRRESS = "address";
	private static final String TAG_COMPANY = "company";
	private static final String TAG_CONTACT = "contact";
	private static final String TAG_adsnamep = "adsname";
	private static final String TAG_CompanyId = "CompanyId";
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_details);

		// Create button Home and back
		btnBack = (Button) findViewById(R.id.btnBack);
		btnHomere = (Button) findViewById(R.id.btnHomere);
		btnbtnOrder = (Button) findViewById(R.id.btnOrder);
		txtadsname = (TextView) findViewById(R.id.adsnamep);
		btnBack.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// creating new product in background thread
			Intent i = new Intent(getApplicationContext(), MedsAll.class);
			startActivity(i);
			
		}
	});
		btnHomere.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// creating new product in background thread
			Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
			startActivity(i);
			
		}
	});
		

		btnbtnOrder.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// creating new product in background thread
			Intent i = new Intent(getApplicationContext(), LoginActivity.class);
	        
			//Create a bundle object
	        Bundle b = new Bundle();		 
	        //Inserts a String value into the mapping of this Bundle
	        i.putExtra("name",txtName.getText().toString());	
	        i.putExtra("coname",txtCompanyId.getText().toString());		 
	        //Add the bundle to the intent.
	        i.putExtras(b);			        
	        //start the DisplayActivity
	        startActivity(i);
			
		}
	});
		
		
	
		Bundle b = getIntent().getExtras();
		// getting specialist id (pid) from intent
		pid = (String) b.getCharSequence("name");

		// Getting complete product details in background thread
		new GetProductDetails().execute();

	}

	/**
	 * Background Async Task to Get complete product details
	 * */
	class GetProductDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ProductDetailsActivity.this);
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
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("pid", pid));

						// getting product details by making HTTP request
						// Note that product details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_product_detials, "GET", params);

						// check your log for json response
						Log.d("Single Product Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received product details
							JSONArray productObj = json
									.getJSONArray(TAG_PRODUCT); // JSON Array
							
							// get first product object from JSON Array
							JSONObject product = productObj.getJSONObject(0);

							// product with this pid found
							// Edit Text
							txtName = (EditText) findViewById(R.id.inputName);
							txtPrice = (EditText) findViewById(R.id.inputPrice);
							txtDesc = (EditText) findViewById(R.id.inputDesc);
							txtAddress = (EditText) findViewById(R.id.inputAddress);
							txtCompany = (EditText) findViewById(R.id.inputCompany);
							txtContact = (EditText) findViewById(R.id.inputContact);
							txtadsname = (TextView) findViewById(R.id.adsnamep);
							txtCompanyId = (TextView) findViewById(R.id.tbCompanyOrderId);

							// display product data in EditText
							txtName.setText(product.getString(TAG_NAME));
							txtPrice.setText(product.getString(TAG_PRICE));
							txtDesc.setText(product.getString(TAG_DESCRIPTION));
							txtAddress.setText(product.getString(TAG_ADDRRESS));
							txtCompany.setText(product.getString(TAG_COMPANY));
							txtContact.setText(product.getString(TAG_CONTACT));
							txtadsname.setText(product.getString(TAG_adsnamep));
							txtCompanyId.setText(product.getString(TAG_CompanyId));
							
							
							// Loader image - will be shown before loading image
					        int loader =1;         
					        // Imageview to show
					        ImageView image1 = (ImageView) findViewById(R.id.ProAds);         
					        //Image url
					        String Ads1 = "http://bmgfdaycare.com/PharConnect/MedicationAds/"+txtadsname.getText().toString()+".png";         
					        //ImageLoader class instance
					        adsImageLoader imgLoader = new adsImageLoader(getApplicationContext());
					        imgLoader.DisplayImage(Ads1, loader, image1);

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

	/**
	 * Background Async Task to  Save product Details
	 * */
	class SaveProductDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ProductDetailsActivity.this);
			pDialog.setMessage("Saving product ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving product
		 * */
		protected String doInBackground(String... args) {

			// getting updated data from EditTexts
			String name = txtName.getText().toString();
			String price = txtPrice.getText().toString();
			String description = txtDesc.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_PID, pid));
			params.add(new BasicNameValuePair(TAG_NAME, name));
			params.add(new BasicNameValuePair(TAG_PRICE, price));
			params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));

			// sending modified data through http request
			// Notice that update product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_update_product,
					"POST", params);

			// check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);
				
				if (success == 1) {
					// successfully updated
					Intent i = getIntent();
					// send result code 100 to notify about product update
					setResult(100, i);
					finish();
				} else {
					// failed to update product
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
			// dismiss the dialog once product uupdated
			pDialog.dismiss();
		}
	}

	/*****************************************************************
	 * Background Async Task to Delete Product
	 * */
	class DeleteProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ProductDetailsActivity.this);
			pDialog.setMessage("Deleting Product...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Deleting product
		 * */
		protected String doInBackground(String... args) {

			// Check for success tag
			int success;
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("pid", pid));

				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(
						url_delete_product, "POST", params);

				// check your log for json response
				Log.d("Delete Product", json.toString());
				
				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// product successfully deleted
					// notify previous activity by sending code 100
					Intent i = getIntent();
					// send result code 100 to notify about product deletion
					setResult(100, i);
					finish();
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
			// dismiss the dialog once product deleted
			pDialog.dismiss();

		}

	}
}
