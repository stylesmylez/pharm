package com.example.androidhive;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	EditText txtName;
	EditText txtPrice;
	EditText txtDesc;
	EditText txtCreatedAt;
	EditText txtAddress;
	EditText txtCompany;
	EditText txtContact;
	TextView txtadsname;
	TextView txtCompanyId;
	Button login;
	Button reserlogin;
	Button member;
	EditText Name;
	EditText xusername;
	EditText xpassword;
	String id;
	String idname;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single product url
	private static final String url_user_detials ="http://bmgfdaycare.com/PharConnect/get_user_details.php";
	// url to create new product
	private static String url_Create_Online = "http://bmgfdaycare.com/PharConnect/Create_Online.php";
	
	private static String url_Check_If_User_Exist = "http://bmgfdaycare.com/PharConnect/Check_If_User_Exist.php";

	
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_register = "register";
	private static final String TAG_id = "id";
	private static final String TAG_name = "name"; 
	

	String CoOrderId;
	String CoOrderDrug;
	EditText xmbusername;
	EditText name;
	EditText username;
	EditText email;
	EditText password;
	EditText contactx;
	EditText addressx;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		 // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		//Get
		Bundle b = getIntent().getExtras();
		CoOrderId = (String) b.getCharSequence("coname");
		CoOrderDrug = (String) b.getCharSequence("name");
		// Edit Text

		xusername = (EditText) findViewById(R.id.tbusername);
		xpassword = (EditText) findViewById(R.id.tbpassword);
		name = (EditText) findViewById(R.id.mbname);
		username = (EditText) findViewById(R.id.mbusername);
		email = (EditText) findViewById(R.id.mbemail);
		password = (EditText) findViewById(R.id.mbpassword);
		contactx = (EditText) findViewById(R.id.mbcontact);
		addressx = (EditText) findViewById(R.id.mbaddress);
				

		// Create button Home and back
		login = (Button) findViewById(R.id.mblogin);
		reserlogin = (Button) findViewById(R.id.resermblogin);		
			
		member = (Button) findViewById(R.id.mbmember);
		
		if (("No".equals(CoOrderDrug.toString())))            
	        {
			 	login.setText(b.getCharSequence("coname"));
	        }
		login.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		

			String un = xusername.getText().toString();
			String pw = xpassword.getText().toString();
			if (TextUtils.isEmpty(un))           
	        {
	        	Toast.makeText(LoginActivity.this,"Please type your username!", 
	        				Toast.LENGTH_LONG).show();	   
	        	return;
	        }
			if (TextUtils.isEmpty(pw))            
	        {
	        	Toast.makeText(LoginActivity.this,"Please type your password!", 
	        				Toast.LENGTH_LONG).show();	   
	        	return;
	        }
			
		// Getting complete product details in background thread
		new GetProductDetails().execute();
		}
	});
		reserlogin.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		

			String un = xusername.getText().toString();
			String pw = xpassword.getText().toString();
			if (TextUtils.isEmpty(un))           
	        {
	        	Toast.makeText(LoginActivity.this,"Please type your username!", 
	        				Toast.LENGTH_LONG).show();	   
	        	return;
	        }
			if (TextUtils.isEmpty(pw))            
	        {
	        	Toast.makeText(LoginActivity.this,"Please type your password!", 
	        				Toast.LENGTH_LONG).show();	   
	        	return;
	        }
			
		// Getting complete product details in background thread
		new resGetProductDetails().execute();
		}
	});
		
		member.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// creating new product in background thread
			
			String nm = name.getText().toString();
			String un = username.getText().toString();
			String em = email.getText().toString();
			String pw = password.getText().toString();
			String cn = contactx.getText().toString();
			String ad = addressx.getText().toString();

			if (TextUtils.isEmpty(cn))           
	        {
	        	Toast.makeText(LoginActivity.this,"Please type your contact!", 
	        				Toast.LENGTH_LONG).show();	   
	        	return;
	        }
			if (TextUtils.isEmpty(ad))           
	        {
	        	Toast.makeText(LoginActivity.this,"Please type your address!", 
	        				Toast.LENGTH_LONG).show();	   
	        	return;
	        }
			if (TextUtils.isEmpty(nm))           
	        {
	        	Toast.makeText(LoginActivity.this,"Please type your name!", 
	        				Toast.LENGTH_LONG).show();	   
	        	return;
	        }
			if (TextUtils.isEmpty(un))           
	        {
	        	Toast.makeText(LoginActivity.this,"Please type your username!", 
	        				Toast.LENGTH_LONG).show();	   
	        	return;
	        }
			if (TextUtils.isEmpty(em))            
	        {
	        	Toast.makeText(LoginActivity.this,"Please type your email!", 
	        				Toast.LENGTH_LONG).show();	   
	        	return;
	        }
			if (TextUtils.isEmpty(pw))            
	        {
	        	Toast.makeText(LoginActivity.this,"Please type your password!", 
	        				Toast.LENGTH_LONG).show();	   
	        	return;
	        }
			Pattern pattern;
			    Matcher matcher;
			    String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			    pattern = Pattern.compile(EMAIL_PATTERN);
			    matcher = pattern.matcher(em);
			    
			    if(matcher.matches()==false)
			    {
			    	Toast.makeText(LoginActivity.this,"Invalid email address!", 
	        				Toast.LENGTH_LONG).show();	
			    			return;
			    }
	        else
	        {
			new CheckIfuserExist().execute();
	        }
			
		}
	});
		

	}

	class GetProductDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Logging in. Please wait...");
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

						String un = xusername.getText().toString();
						String pw = xpassword.getText().toString();
						// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("username", un));
						params.add(new BasicNameValuePair("password", pw));

						// getting hospital details by making HTTP request
						// Note that hospital details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_user_detials, "GET", params);

						// check your log for json response
						Log.d("Single Product Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received product details
							JSONArray productObj = json
									.getJSONArray(TAG_register); // JSON Array
							
							// get first product object from JSON Array
							JSONObject product = productObj.getJSONObject(0);

							id = (String) (product.getString(TAG_id));
							idname = (String) (product.getString(TAG_name));
							 if (("No".equals(CoOrderDrug.toString())))            
						        {
						        	Toast.makeText(LoginActivity.this,"Here you can manage your transactions!", 						        			
						        				Toast.LENGTH_LONG).show();	
						        	Intent i = new Intent(getApplicationContext(), OrdersViewAll.class);					        
									//Send
							        Bundle b = new Bundle();		 
							        i.putExtra("id",id.toString()); 	 
							        //Add the bundle to the intent.
							        i.putExtras(b);			        
							        //start the DisplayA
							        finish(); startActivity(i);
						        	return;
						        }
						        else
						        {
							Intent i = new Intent(getApplicationContext(), OderPlaceActivity.class);					        
							//Send
					        Bundle b = new Bundle();		 
					        //Inserts a String value into the mapping of this Bundle
					        i.putExtra("name",CoOrderDrug.toString());	
					        i.putExtra("coname",CoOrderId.toString());	
					        i.putExtra("id",id.toString());	
							i.putExtra("idname",idname.toString());		 
					        //Add the bundle to the intent.
					        i.putExtras(b);			        
					        //start the DisplayA
					        finish(); startActivity(i);	
					        }						
							

						}else{
							Toast.makeText(LoginActivity.this,"Login failed, please try again!", 
			        				Toast.LENGTH_LONG).show();	
			        	
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

	class resGetProductDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Logging in. Please wait...");
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

						String un = xusername.getText().toString();
						String pw = xpassword.getText().toString();
						// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("username", un));
						params.add(new BasicNameValuePair("password", pw));

						// getting hospital details by making HTTP request
						// Note that hospital details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_user_detials, "GET", params);

						// check your log for json response
						Log.d("Single Product Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received product details
							JSONArray productObj = json
									.getJSONArray(TAG_register); // JSON Array
							
							// get first product object from JSON Array
							JSONObject product = productObj.getJSONObject(0);

							id = (String) (product.getString(TAG_id));
							idname = (String) (product.getString(TAG_name));
							if (("make".equals(CoOrderDrug.toString())))            
					        {
								Intent i = new Intent(getApplicationContext(), ReservationPlaceActivity.class);					        
								//Send
						        Bundle b = new Bundle();		 
						        //Inserts a String value into the mapping of this Bundle
						       //Company id
						        i.putExtra("companyid",CoOrderId.toString());	
						        //User Id
						        i.putExtra("userid",id.toString());	
						        //User full name
								i.putExtra("idname",idname.toString());	
								/*
								Toast.makeText(LoginActivity.this,CoOrderId.toString(), 						        			
				        				Toast.LENGTH_LONG).show();	
								Toast.makeText(LoginActivity.this,id.toString(), 						        			
				        				Toast.LENGTH_LONG).show();	
								Toast.makeText(LoginActivity.this,idname.toString(), 						        			
				        				Toast.LENGTH_LONG).show();	*/
						        //Add the bundle to the intent.
						        i.putExtras(b);			        
						        //start the DisplayA
						        finish(); startActivity(i);	
						        return;
					        }
							 if (("No".equals(CoOrderDrug.toString())))            
						        {
						        	Toast.makeText(LoginActivity.this,"Here you can manage your transactions!", 						        			
						        				Toast.LENGTH_LONG).show();	
						        	Intent i = new Intent(getApplicationContext(), ReservationViewAll.class);					        
									//Send
							        Bundle b = new Bundle();		 
							        i.putExtra("id",id.toString()); 	 
							        //Add the bundle to the intent.
							        i.putExtras(b);			        
							        //start the DisplayA
							        finish(); startActivity(i);
						        	return;
						        }
						        else
						        {
							/*Intent i = new Intent(getApplicationContext(), OderPlaceActivity.class);					        
							//Send
					        Bundle b = new Bundle();		 
					        //Inserts a String value into the mapping of this Bundle
					        i.putExtra("name",CoOrderDrug.toString());	
					        i.putExtra("coname",CoOrderId.toString());	
					        i.putExtra("id",id.toString());	
							i.putExtra("idname",idname.toString());		 
					        //Add the bundle to the intent.
					        i.putExtras(b);			        
					        //start the DisplayA
					        finish(); startActivity(i);	*/
					        }						
							

						}else{
							Toast.makeText(LoginActivity.this,"Login failed, please try again!", 
			        				Toast.LENGTH_LONG).show();	
			        	
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
	
	class CheckIfuserExist extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Checking usernames. Please wait...");
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

						String un = username.getText().toString();
						// Building Parameters
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("username", un));

						// getting hospital details by making HTTP request
						// Note that hospital details url will use GET request
						JSONObject json = jsonParser.makeHttpRequest(
								url_Check_If_User_Exist, "GET", params);

						// check your log for json response
						Log.d("Single Product Details", json.toString());
						
						// json success tag
						success = json.getInt(TAG_SUCCESS);
						if (success == 1) {
							// successfully received product details
							Toast.makeText(LoginActivity.this,"Username already exist! try another!", 
			        				Toast.LENGTH_LONG).show();	
							return;

						}else{
							int dur=200;
							Toast.makeText(LoginActivity.this,"Member created, now you can manage orders online!", 
									dur).show();	
							new Createmember().execute();
			        	
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

	class Createmember extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Creating member..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String xnamex = name.getText().toString();
			String xusernamex = username.getText().toString();
			String xemailx = email.getText().toString();
			String xpasswordx = password.getText().toString();
			String xcontactx = contactx.getText().toString();
			String xaddress = addressx.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", xnamex.toString()));
			params.add(new BasicNameValuePair("username", xusernamex.toString()));
			params.add(new BasicNameValuePair("email", xemailx.toString()));
			params.add(new BasicNameValuePair("password", xpasswordx.toString()));
			params.add(new BasicNameValuePair("contact", xcontactx.toString()));
			params.add(new BasicNameValuePair("Address", xaddress.toString()));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_Create_Online,
					"POST", params);
			
			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
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
