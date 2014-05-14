package com.example.androidhive;

import android.app.ListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class Ads1  extends Activity {
	String pid;
	EditText Name;
	String newString;
	TextView tburl;
	// Progress Dialog
	private ProgressDialog pDialog;
	// url JSONArray
	JSONArray xurlst = null;
	ListView list;
	ArrayList<HashMap<String, String>> UrlsList;
	private static final String TAG_ADS = "name";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_urls = "urls";
	private static final String TAG_urlid = "urlid";
	private static final String TAG_Url = "url";
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();
	JSONParser jsonParser = new JSONParser();
	// url to get all chats list
	private static String get_all_urls = "http://bmgfdaycare.com/PharConnect/get_all_urls.php";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ads1);	
		
		
		
		Bundle b = getIntent().getExtras();
        Name = (EditText) findViewById(R.id.AdsName); 
        Name.setText(b.getCharSequence("name"));

		Button Home = (Button) findViewById(R.id.Ads1regHome);		
		Home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching create new product activity
				Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
				startActivity(i);				
			}
		});
		
		Button VisitLink = (Button) findViewById(R.id.VisitLink);		
		VisitLink.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				tburl = (TextView) findViewById(R.id.tburl);
				list = (ListView) findViewById(R.id.list);
				
				String urls = ((TextView) list.findViewById(R.id.Xurls)).getText()
						.toString();
				
				Uri uri = Uri.parse(urls);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
				
			}
		});
		FetchAds();
		loadurls();

	}
	
	private void loadurls()
	{
		// creating new product in background thread
		// Hashmap for ListView
		UrlsList = new ArrayList<HashMap<String, String>>();

		// Loading chats in Background Thread
		new LoadAllurls().execute();
		
		// Get listview		
	    list = (ListView) findViewById(R.id.list);
		//ListView lv = getListView();
		ListView lv = list;
		//replaceurl();
		
	    
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String urls = ((TextView) view.findViewById(R.id.Xurls)).getText()
						.toString();
				// display product data in EditText
				tburl = (TextView) findViewById(R.id.tburl);						
				tburl.setText(urls);
			}
		});
	}
	
	private void replaceurl()
	
	{	
		String urls = ((TextView) list.findViewById(R.id.Xurls)).getText()
					.toString();
			// display product data in EditText
			tburl = (TextView) findViewById(R.id.tburl);						
			tburl.setText(urls);	
	}
	
	class LoadAllurls extends AsyncTask<String, String, String> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Ads1.this);
			pDialog.setMessage("Loading messages. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

	
		protected String doInBackground(String... args) {
			//String Medication = "m";
			String urlidx = Name.getText().toString();
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("urlid", urlidx));
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(get_all_urls, "GET", params);
			
			// Check your log cat for JSON reponse
			Log.d("All chats: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// chats found
					// Getting Array of chats
					xurlst = json.getJSONArray(TAG_urls);

					// looping through All chats
					for (int i = 0; i < xurlst.length(); i++) {
						JSONObject c = xurlst.getJSONObject(i);

						// Storing each json item in variable
						String urlid = c.getString(TAG_urlid);
						String url = c.getString(TAG_Url);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_urlid, urlid);
						map.put(TAG_Url, url);
						// adding HashList to ArrayList
						UrlsList.add(map);
					}
				} else {
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all chats
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					
					ListAdapter adapter = new SimpleAdapter(
							Ads1.this, UrlsList,
							R.layout.chatlist_item, new String[] {
									TAG_Url },
							new int[] {R.id.Xurls });
					// updating listview					
					list = (ListView) findViewById(R.id.list);
					list.setAdapter(adapter);
					
				}

			});

		}}
	

	private void updateAds()
	{
		adsImageLoader imgLoader = new adsImageLoader(getApplicationContext());
		imgLoader.fileCache.clear();
		imgLoader.memoryCache.clear();
	}
	private void FetchAds()
	{
        // Loader image - will be shown before loading image
        int loader = R.drawable.bigtemp;
         
        // Imageview to show
        ImageView Ads1design = (ImageView) findViewById(R.id.HosAds);
        ImageView Ads1logo = (ImageView) findViewById(R.id.Ads1logo);
         
        //Image url
        String Ads1 = 
        		"http://bmgfdaycare.com/PharConnect/AdsDetail/AdsDetail"+Name.getText().toString()+".png";
        String AdsSale = "http://bmgfdaycare.com/PharConnect/Ads/AdsSale.png";
         
        //ImageLoader class instance
        adsImageLoader imgLoader = new adsImageLoader(getApplicationContext());
        imgLoader.DisplayImage(Ads1, loader, Ads1design);
        imgLoader.DisplayImage(AdsSale, loader, Ads1logo);
	}
	}
