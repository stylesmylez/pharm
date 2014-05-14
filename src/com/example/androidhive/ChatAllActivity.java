package com.example.androidhive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ChatAllActivity extends ListActivity {

	// Progress Dialog
	private ProgressDialog pDialog;
	EditText xtbMeds;
	EditText mychat;	
	private Spinner SpMyNum;
	EditText Response;
	EditText CompanyId;
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	JSONParser jsonParser = new JSONParser();
	ArrayList<HashMap<String, String>> chatsList;
	// url to get all chats list
	private static String url_all_chats = "http://bmgfdaycare.com/PharConnect/get_all_chats.php";
	// url to create new product
	private static String url_MychatSend = "http://bmgfdaycare.com/PharConnect/MychatSend.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_chats = "chats";
	private static final String TAG_PID = "pid";
	private static final String TAG_Xcompanyid = "Xcompanyid";
	private static final String TAG_Xmessage = "Xmessage";

	// chats JSONArray
	JSONArray chats = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		setContentView(R.layout.all_chat);
		
		MyDatabaseHandler();

		// Edit Text
		xtbMeds = (EditText) findViewById(R.id.tbMeds);
		mychat = (EditText) findViewById(R.id.tbmychat);
		//mychat = (EditText) findViewById(R.id.tbmychat);	
		Button btnSeachx = (Button) findViewById(R.id.btncMeds);
		CompanyId = (EditText) findViewById(R.id.tbCompanyId);
		
			Button btnHomeSeaech = (Button) findViewById(R.id.btnHome);
			btnHomeSeaech.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
				finish(); startActivity(i);
				
			}
		});
		
			// Create button Home
			Button btnSend = (Button) findViewById(R.id.btnXsends);
			btnSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				String IfComIdExist = CompanyId.getText().toString();
		        if (("-".equals(IfComIdExist)))            
	            {
		        	Toast.makeText(ChatAllActivity.this,"Please click the message you are replying to!", Toast.LENGTH_LONG).show();		    		
	            }

	        else
	            {	            				
				new SendChat().execute();					
				mychat.setText("");		
	            }
			}
		});			
		
		btnSeachx.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				// Hashmap for ListView
				chatsList = new ArrayList<HashMap<String, String>>();

				// Loading chats in Background Thread
				new LoadAllchats().execute();
				
				// Get listview
				ListView lv = getListView();
				// Get listview
				//GridView gv = getGridView();

				// on seleting single product
				// launching Edit Product Screen
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// getting values from selected ListItem
						String nm = ((TextView) view.findViewById(R.id.Xmessage)).getText()
								.toString();
						String Xid = ((TextView) view.findViewById(R.id.Xcompanyid)).getText()
								.toString();
						// display product data in EditText
						Response = (EditText) findViewById(R.id.tbResponse);
						Response.setText(nm);
						CompanyId = (EditText) findViewById(R.id.tbCompanyId);						
						CompanyId.setText(Xid);

					}
				});
			}
		});
		
		ActionStartsHere();
		Reload();

	}
	private void MyDatabaseHandler()
	{
		//My Phone Number
		DatabaseHandler db = new DatabaseHandler(this);        
        List<Contact> contacts = db.getAllContacts();
		for (Contact cn : contacts) 
		{
            String log = cn.getPhoneNumber();         
            SpMyNum = (Spinner) findViewById(R.id.SpMyNum);
		    List list = new ArrayList();
		    list.add(log);
		    ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
		    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    SpMyNum.setAdapter(dataAdapter);
        }
	}

	private void Reload()
	{
		chatsList = new ArrayList<HashMap<String, String>>();

		// Loading chats in Background Thread
		new LoadAllchats().execute();

		// Get listview
		ListView lv = getListView();
		// Get listview
		//GridView gv = getGridView();

		// on seleting single product
		// launching Edit Product Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String nm = ((TextView) view.findViewById(R.id.Xmessage)).getText()
						.toString();
				String Xid = ((TextView) view.findViewById(R.id.Xcompanyid)).getText()
						.toString();
				// display product data in EditText
				Response = (EditText) findViewById(R.id.tbResponse);
				Response.setText(nm);
				CompanyId = (EditText) findViewById(R.id.tbCompanyId);						
				CompanyId.setText(Xid);

				/*// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						ChatActivity.class);
				// sending pid to next activity
				in.putExtra(TAG_PID, pid);*/
				
				// starting new activity and expecting some response back
				//startActivityForResult(in, 100);
			}
		});
		
	}

	public void ActionStartsHere()
	{
	againStartGPSAndSendFile();
	}

	public void againStartGPSAndSendFile()
	{
	new CountDownTimer(21000,20000)
	    {
	        @Override
	        public void onTick(long millisUntilFinished)
	        {
	        	chatsList = new ArrayList<HashMap<String, String>>();
	    		new AutoLoadAllchats().execute();
	    		ListView lv = getListView();
	    		lv.setOnItemClickListener(new OnItemClickListener() {

	    			@Override
	    			public void onItemClick(AdapterView<?> parent, View view,
	    					int position, long id) {
	    				// getting values from selected ListItem
	    				String nm = ((TextView) view.findViewById(R.id.Xmessage)).getText()
	    						.toString();
	    				String Xid = ((TextView) view.findViewById(R.id.Xcompanyid)).getText()
	    						.toString();
	    				// display product data in EditText
	    				Response = (EditText) findViewById(R.id.tbResponse);
	    				Response.setText(nm);
	    				CompanyId = (EditText) findViewById(R.id.tbCompanyId);						
	    				CompanyId.setText(Xid);

	    				/*// Starting new intent
	    				Intent in = new Intent(getApplicationContext(),
	    						ChatActivity.class);
	    				// sending pid to next activity
	    				in.putExtra(TAG_PID, pid);*/
	    				
	    				// starting new activity and expecting some response back
	    				//startActivityForResult(in, 100);
	    			}
	    		});
	        }
	        @Override
	        public void onFinish()
	        {
	            ActionStartsHere();
	        }

	    }.start();
	}
	
	class SendChat extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChatAllActivity.this);
			pDialog.setMessage("Sending..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating Enquiry
		 * */
		protected String doInBackground(String... args) {
			String XSpMyNum = SpMyNum.getSelectedItem().toString();
			String XCompanyId = CompanyId.getText().toString();
			String Xmessage = mychat.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("number", XSpMyNum));
			params.add(new BasicNameValuePair("Xcompanyid", XCompanyId));
			params.add(new BasicNameValuePair("Xmessage", Xmessage));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_MychatSend,
					"POST", params);
			
			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					
					//successfully created product
					//Intent i = new Intent(getApplicationContext(), ChatAllActivity.class);
					//finish(); startActivity(i);
					// closing this screen
					//finish();
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

	// Response from Edit Product Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if result code 100
		if (resultCode == 100) {
			// if result code 100 is received 
			// means user edited/deleted product
			// reload this screen again
			Intent intent = getIntent();
			finish();
			startActivity(intent);
		}

	}
	class AutoLoadAllchats extends AsyncTask<String, String, String> {

		/**
		 * getting All chats from url
		 * */
		protected String doInBackground(String... args) {
			//String Medication = "m";
			String Medication = xtbMeds.getText().toString();
			String MyNo = SpMyNum.getSelectedItem().toString();
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Xmessage", Medication));
			params.add(new BasicNameValuePair("number", MyNo));
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_chats, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All chats: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// chats found
					// Getting Array of chats
					chats = json.getJSONArray(TAG_chats);

					// looping through All chats
					for (int i = 0; i < chats.length(); i++) {
						JSONObject c = chats.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_PID);
						String Xcompanyid = c.getString(TAG_Xcompanyid);
						String Xmessage = c.getString(TAG_Xmessage);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_PID, id);
						map.put(TAG_Xcompanyid, Xcompanyid);
						map.put(TAG_Xmessage, Xmessage);

						// adding HashList to ArrayList
						chatsList.add(map);
					}
				} else {
					// no chats found
					// Launch Add New product Activity
					/*Intent i = new Intent(getApplicationContext(),
							NewProductActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					finish(); startActivity(i);*/
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
			// dismiss the dialog after getting all chats
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							ChatAllActivity.this, chatsList,
							R.layout.chatlist_item, new String[] { TAG_PID,
									TAG_Xcompanyid,TAG_Xmessage },
							new int[] { R.id.pid,R.id.Xcompanyid,R.id.Xmessage });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}

	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllchats extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChatAllActivity.this);
			pDialog.setMessage("Loading messages. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All chats from url
		 * */
		protected String doInBackground(String... args) {
			//String Medication = "m";
			String Medication = xtbMeds.getText().toString();
			String MyNo = SpMyNum.getSelectedItem().toString();
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Xmessage", Medication));
			params.add(new BasicNameValuePair("number", MyNo));
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_chats, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All chats: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// chats found
					// Getting Array of chats
					chats = json.getJSONArray(TAG_chats);

					// looping through All chats
					for (int i = 0; i < chats.length(); i++) {
						JSONObject c = chats.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_PID);
						String Xcompanyid = c.getString(TAG_Xcompanyid);
						String Xmessage = c.getString(TAG_Xmessage);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_PID, id);
						map.put(TAG_Xcompanyid, Xcompanyid);
						map.put(TAG_Xmessage, Xmessage);

						// adding HashList to ArrayList
						chatsList.add(map);
					}
				} else {
					// no chats found
					// Launch Add New product Activity
					/*Intent i = new Intent(getApplicationContext(),
							NewProductActivity.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					finish(); startActivity(i);*/
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
			// dismiss the dialog after getting all chats
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							ChatAllActivity.this, chatsList,
							R.layout.chatlist_item, new String[] { TAG_PID,
									TAG_Xcompanyid,TAG_Xmessage },
							new int[] { R.id.pid,R.id.Xcompanyid,R.id.Xmessage });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}