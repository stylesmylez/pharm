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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ChatActivity extends Activity {

	EditText txtrereply;
	private Spinner SpChatNumber;
	/*Button btnBack;
	Button btnHomere;*/

	String pid;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single chats url
	private static final String url_chats_detials ="http://bmgfdaycare.com/PharConnect/get_chat_details.php";

	// url to update chats
	private static final String url_update_chats = "http://bmgfdaycare.com/PharConnect/update_chats.php";
	
	// url to delete chats
	private static final String url_delete_chats = "http://bmgfdaycare.com/PharConnect/delete_chats.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_chats = "chats";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		//My Phone Number
		DatabaseHandler db = new DatabaseHandler(this);        
        List<Contact> contacts = db.getAllContacts();
		for (Contact cn : contacts) 
		{
            String log = cn.getPhoneNumber();         
            SpChatNumber = (Spinner) findViewById(R.id.SpChatNumber);
		    List list = new ArrayList();
		    list.add(log);
		    ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
		    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    SpChatNumber.setAdapter(dataAdapter);
        }

		/*// Create button Home and back
		btnBack = (Button) findViewById(R.id.btnBack);
		btnHomere = (Button) findViewById(R.id.btnHomere);

		btnBack.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// creating new chats in background thread
			Intent i = new Intent(getApplicationContext(), AllchatssActivity.class);
			finish(); startActivity(i);
			
		}
	});
		btnHomere.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// creating new chats in background thread
			Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
			finish(); startActivity(i);
			
		}
	});*/
	
		// getting chats details from intent
		Intent i = getIntent();
		
		// getting chats id (pid) from intent
		pid = i.getStringExtra(TAG_PID);

		// Getting complete chats details in background thread
		new GetchatsDetails().execute();


	}

	/**
	 * Background Async Task to Get complete chats details
	 * */
	class GetchatsDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChatActivity.this);
			pDialog.setMessage("Loading chats details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting chats details in background thread
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
						params.add(new BasicNameValuePair("pid", "1"));

						// getting chats details by making HTTP request
						// Note that chats details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_chats_detials, "GET", params);

						// check your log for json response
						Log.d("Single chats Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received chats details
							JSONArray chatsObj = json
									.getJSONArray(TAG_chats); // JSON Array
							
							// get first chats object from JSON Array
							JSONObject chats = chatsObj.getJSONObject(0);

							// chats with this pid found
							// Edit Text
							txtrereply = (EditText) findViewById(R.id.tbrequery);

							// display chats data in EditText
							txtrereply.setText(chats.getString(TAG_NAME));

						}else{
							// chats with pid not found
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
	 * Background Async Task to  Save chats Details
	 * */
	class SavechatsDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChatActivity.this);
			pDialog.setMessage("Saving chats ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Saving chats
		 * */
		protected String doInBackground(String... args) {

			// getting updated data from EditTexts
			String name = txtrereply.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(TAG_PID, pid));
			params.add(new BasicNameValuePair(TAG_NAME, name));

			// sending modified data through http request
			// Notice that update chats url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_update_chats,
					"POST", params);

			// check json success tag
			try {
				int success = json.getInt(TAG_SUCCESS);
				
				if (success == 1) {
					// successfully updated
					Intent i = getIntent();
					// send result code 100 to notify about chats update
					setResult(100, i);
					finish();
				} else {
					// failed to update chats
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
			// dismiss the dialog once chats uupdated
			pDialog.dismiss();
		}
	}

	/*****************************************************************
	 * Background Async Task to Delete chats
	 * */
	class Deletechats extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChatActivity.this);
			pDialog.setMessage("Deleting chats...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Deleting chats
		 * */
		protected String doInBackground(String... args) {

			// Check for success tag
			int success;
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("pid", pid));

				// getting chats details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(
						url_delete_chats, "POST", params);

				// check your log for json response
				Log.d("Delete chats", json.toString());
				
				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// chats successfully deleted
					// notify previous activity by sending code 100
					Intent i = getIntent();
					// send result code 100 to notify about chats deletion
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
			// dismiss the dialog once chats deleted
			pDialog.dismiss();

		}

	}
}
