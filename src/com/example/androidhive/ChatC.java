package com.example.androidhive;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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


	public class ChatC extends Activity {
	
	// Progress Dialog
	private ProgressDialog pDialog;
	EditText tbrequery;
	EditText tbmychat;
	private Spinner SpChatNumber;

	JSONParser jsonParser = new JSONParser();

	// url to create new product
	private static String url_chat = "http://bmgfdaycare.com/PharConnect/chat.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registernumber);
		
		DatabaseHandler db = new DatabaseHandler(this);
		List<Contact> contacts = db.getAllContacts();
        for (Contact cn : contacts) 
        {
            String log = cn.getPhoneNumber();         
            SpChatNumber = (Spinner) findViewById(R.id.SpChatNumber);
			    List list = new ArrayList();
			    list.add(log);
			    ArrayAdapter dataAdapter = 
			    		new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
			    dataAdapter.setDropDownViewResource
			    (android.R.layout.simple_spinner_dropdown_item);
			    SpChatNumber.setAdapter(dataAdapter);
        }
        
		// Edit Text
        tbmychat = (EditText) findViewById(R.id.tbmychat);
        tbrequery = (EditText) findViewById(R.id.tbrequery);
		SpChatNumber = (Spinner) findViewById(R.id.SpChatNumber);
		
		// Create button
		Button btnSendx = (Button) findViewById(R.id.btnSendx);
		// button click event
		btnSendx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				new MakeEnquiry().execute();
			}
		});
		
		// Create button
		Button btnQueryx = (Button) findViewById(R.id.btnQueryx);
		// button click event
		btnQueryx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread

		        //db.addContact(new Contact("Karthik", "9533333333")); 
				
			}
		});

		// Create button
		Button Home = (Button) findViewById(R.id.regHome);		
		Home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching create new product activity
				Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
				finish(); startActivity(i);
				
			}
		});

	}
	
	class MakeEnquiry extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChatC.this);
			pDialog.setMessage("Meaking enquiry..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating Enquiry
		 * */
		protected String doInBackground(String... args) {
			String chat = tbmychat.getText().toString();
			String ChatNumber = SpChatNumber.getSelectedItem().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("chat", chat));
			params.add(new BasicNameValuePair("ChatNumber", ChatNumber));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_chat,
					"POST", params);
			
			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					//Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
					//finish(); startActivity(i);
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

