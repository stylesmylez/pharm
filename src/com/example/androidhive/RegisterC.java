package com.example.androidhive;

import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


	public class RegisterC extends Activity {
	
	// Progress Dialog
	private ProgressDialog pDialog;
	EditText xtbnumber;
	EditText xtbeage;
	EditText xtbecontact;
	EditText xtbeissue;
	EditText xtbename;
	Spinner xdpegender;
	private Spinner SpNumber;

	JSONParser jsonParser = new JSONParser();

	// url to create new product
	private static String url_Makeenquiry = "http://bmgfdaycare.com/PharConnect/Enquiry.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	
	private void FormControls()
{
				String IfBlankExist = ((EditText) findViewById(R.id.tbnumber)).getText()
				.toString();
					//tbnewval = (EditText) findViewById(R.id.tbnewval);
					//tbnewval.setText(nm);
					if (TextUtils.isEmpty(IfBlankExist))           
			        {
			        	Toast.makeText(RegisterC.this,"Please type your phone number to register!", 
			        				Toast.LENGTH_LONG).show();	   
			        	return;
			        }
			        else
			        {
			        	pDialog = new ProgressDialog(RegisterC.this);
			        	pDialog.setMessage("Registering..");
			        	pDialog.setIndeterminate(false);
			        	pDialog.setCancelable(true);
			        	pDialog.show();
			        	Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
			        	finish(); startActivity(i);
			        	DatabaseHandler dbm = new DatabaseHandler(this);
			        	dbm.addContact(new Contact("Phone Number", xtbnumber.getText().toString()));
			        }
}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regisme);
		
		DatabaseHandler db = new DatabaseHandler(this);

        //db.addContact(new Contact("Phone Number", "6544")); 
        List<Contact> contacts = db.getAllContacts();
		for (Contact cn : contacts) 
		{
            String log = cn.getPhoneNumber();         
            SpNumber = (Spinner) findViewById(R.id.SpNumber);
		    List list = new ArrayList();
		    list.add(log);
		    ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
		    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    SpNumber.setAdapter(dataAdapter);
        }
        
		// Edit Text
		xtbnumber = (EditText) findViewById(R.id.tbnumber);
		xtbeage = (EditText) findViewById(R.id.tbeage);
		xtbecontact = (EditText) findViewById(R.id.tbecontact);
		xtbeissue = (EditText) findViewById(R.id.tbeissue);
		xtbename = (EditText) findViewById(R.id.tbename);
		xdpegender = (Spinner) findViewById(R.id.SpGender);
		
		// Create button
		Button btnSubmit = (Button) findViewById(R.id.btnesubmit);
		// button click event
		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				//new MakeEnquiry().execute();
				//Checkifgistered();
				String IfComIdExist = ((Spinner) findViewById(R.id.SpNumber)).getSelectedItem()
						.toString();
				String nm = ((EditText) findViewById(R.id.tbename)).getText()
						.toString();
				String is = ((EditText) findViewById(R.id.tbeissue)).getText()
						.toString();
							//tbnewval = (EditText) findViewById(R.id.tbnewval);
							//tbnewval.setText(nm);
				
				
					        if (("Not registered".equals(IfComIdExist)))            
					        {
					        	Toast.makeText(RegisterC.this,"Please register your phone number to make an enquiry!", 
					        				Toast.LENGTH_LONG).show();	        	
					        }
					        if (TextUtils.isEmpty(nm))            
					        {
					        	Toast.makeText(RegisterC.this,"Please type your name!", 
					        				Toast.LENGTH_LONG).show();	        	
					        }
					        if (TextUtils.isEmpty(is))            
					        {
					        	Toast.makeText(RegisterC.this,"Please type your issue!", 
					        				Toast.LENGTH_LONG).show();	        	
					        }

					    else
					        {
					    	
					    		new MakeEnquiry().execute();
					    	
					        }
			}
		});
		
		// Create button
		Button btnRegisternumber = (Button) findViewById(R.id.btnRegnum);
		// button click event
		btnRegisternumber.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				FormControls();
				
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


	
    		class MakeEnquiry extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterC.this);
			pDialog.setMessage("Meaking enquiry..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating Enquiry
		 * */
		protected String doInBackground(String... args) {
			String age = xtbeage.getText().toString();
			String contact = xtbecontact.getText().toString();
			String issue = xtbeissue.getText().toString();
			String name = xtbename.getText().toString();
			String gender = xdpegender.getSelectedItem().toString();
			String number = SpNumber.getSelectedItem().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("age", age));
			params.add(new BasicNameValuePair("contact", contact));
			params.add(new BasicNameValuePair("issue", issue));
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("gender", gender));
			params.add(new BasicNameValuePair("number", number));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_Makeenquiry,
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

