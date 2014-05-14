package com.example.androidhive;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ReservationViewAll extends Activity {

	JSONParser jParser = new JSONParser();
	//EditText search;
	//Button LOadData;
	ListView lv;
	String CoOrderId;
	//Button btnHomeSeaech;// Progress Dialog
	private ProgressDialog pDialog;
	@SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservationviewall);

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Bundle b = getIntent().getExtras();
		CoOrderId = (String) b.getCharSequence("id");
        
		// Create button
		Button Home = (Button) findViewById(R.id.reserhome);		
		Home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching create new product activity
				Intent i = new Intent(getApplicationContext(), MainScreenActivity.class);
				finish(); startActivity(i);
				
			}
		});
		
		
        new LoaddataInBackground().execute();
		lv = (ListView)findViewById(R.id.reserlistView1);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long Id) {
				// getting values from selected ListItem
				String ItemId = ((TextView) view.findViewById(R.id.reseromedsItemId)).getText()
						.toString();
				
				//String str=lv.getItemAtPosition((int) ItemId).toString();
				//Toast.makeText(getApplicationContext(), pid,Toast.LENGTH_LONG).show();

				Intent i = new Intent(getApplicationContext(), ReservationEditActivity.class);
		        
				//Create a bundle object
		        Bundle b = new Bundle();		 
		        //Inserts a String value into the mapping of this Bundle
		        i.putExtra("id",ItemId);		 
		        //Add the bundle to the intent.
		        i.putExtras(b);			        
		        //start the DisplayActivity
		        startActivity(i);
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
	class LoaddataInBackground extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();			
			pDialog = new ProgressDialog(ReservationViewAll.this);
			pDialog.setMessage("Loading. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		
		}	
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					
					Loaddata();
					
				}
			});
			return null;

		}		
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			pDialog.dismiss();
		} 

	}
	
	
	private void Loaddata()
	{
		// listView1
        final ListView lstView1 = (ListView)findViewById(R.id.reserlistView1);       
        String url = "http://bmgfdaycare.com/PharConnect/get_all_reservations.php";
    	// Building Parameters
        /*search = (EditText) findViewById(R.id.medstbSearchTerm);
        String term = search.getText().toString();*/
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userid",CoOrderId.toString()));
		JSONObject json = jParser.makeHttpRequest(url, "GET", params);
		
        try {
        	//JSONArray data = new JSONArray(getJSONUrl(json.toString()));
        	JSONArray data = null;
        	data = json.getJSONArray("reservations");
			
	    	final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map;
			
			for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
    			map = new HashMap<String, String>();
    			map.put("ItemId", c.getString("Id"));
    			map.put("name", c.getString("Name"));
    			map.put("description", c.getString("PurposeOfVisit"));
    			//map.put("price", c.getString("price"));
    			//map.put("path", c.getString("path"));
    			MyArrList.add(map);
			}			
			
		    lstView1.setAdapter(new ImageAdapter(this,MyArrList));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
             
    
    public class ImageAdapter extends BaseAdapter 
    {
        private Context context;
        private ArrayList<HashMap<String, String>> MyArr = new ArrayList<HashMap<String, String>>();

        public ImageAdapter(Context c, ArrayList<HashMap<String, String>> list) 
        {
        	// TODO Auto-generated method stub
            context = c;
            MyArr = list;
        }
 
        public int getCount() {
        	// TODO Auto-generated method stub
            return MyArr.size();
        }
 
        public Object getItem(int position) {
        	// TODO Auto-generated method stub
            return position;
        }
 
        public long getItemId(int position) {
        	// TODO Auto-generated method stub
            return position;
        }
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		 
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.reservation_columnview, null); 
			}
	         
	         
				
			// ColPosition
			TextView CompanyName = (TextView) convertView.findViewById(R.id.reseroMedsName);
			CompanyName.setPadding(10, 0, 0, 0);
			CompanyName.setText("      " + MyArr.get(position).get("name"));

			// ColPicname
			TextView Description = (TextView) convertView.findViewById(R.id.reseromedsDescription);
			Description.setPadding(50, 0, 0, 0);
			Description.setText("Purpose of visit: " + MyArr.get(position).get("description"));
			
			TextView ItemId = (TextView) convertView.findViewById(R.id.reseromedsItemId);
			ItemId.setPadding(50, 0, 0, 0);
			ItemId.setText("" + MyArr.get(position).get("ItemId"));
			pDialog.dismiss();
			return convertView;
				
		}

    } 
    public String getJSONUrl(String url) {
		StringBuilder str = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) { // Download OK
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
			} else {
				Log.e("Log", "Failed to download file..");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
	}
	private static final String TAG = "ERROR";
	private static final int IO_BUFFER_SIZE = 4 * 1024;
	public static Bitmap loadBitmap(String url) {
	    Bitmap bitmap = null;
	    InputStream in = null;
	    BufferedOutputStream out = null;

	    try {
	        in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

	        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
	        out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
	        copy(in, out);
	        out.flush();

	        final byte[] data = dataStream.toByteArray();
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        //options.inSampleSize = 1;

	        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
	    } catch (IOException e) {
	        Log.e(TAG, "Could not load Bitmap from: " + url);
	    } finally {
	        closeStream(in);
	        closeStream(out);
	    }

	    return bitmap;
	}

	 private static void closeStream(Closeable stream) {
	        if (stream != null) {
	            try {
	                stream.close();
	            } catch (IOException e) {
	                android.util.Log.e(TAG, "Could not close stream", e);
	            }
	        }
	    }
	 
	 private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
 

}