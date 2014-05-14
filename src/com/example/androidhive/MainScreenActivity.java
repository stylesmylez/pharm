package com.example.androidhive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.ArrayList;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class MainScreenActivity extends Activity{
	
	Button imgmedication;
	Button imgregister;
	Button imgchat;
	Button ximgspecialistm;
	Button ximghospitalm;
	private Spinner SpMyNum;
	Button ximgorder;
	EditText tbnewval;
	ImageView Ads1;
	ImageView Ads2;
	ImageView Ads3;
	ImageView Ads4;
	ImageView Ads5;
	ImageView Ads6;
	ImageView Ads7;
	ImageView Ads8;
	ImageView Ads9;
	ImageView Ads10;
	String NoNetwork;
	///////
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private ViewFlipper mViewFlipper;	
	private AnimationListener mAnimationListener;
	private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

	///////
	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x);	
		
		haveNetworkConnection();
		
		updateAds();
		FetchAds();
		Toast.makeText(MainScreenActivity.this,"Welcome to pharmacy online!", Toast.LENGTH_LONG).show();
		MyDatabaseHandler();
		
		imgmedication = (Button) findViewById(R.id.ibtnmedication);
		imgregister = (Button) findViewById(R.id.ibtnregister);
		imgchat = (Button) findViewById(R.id.ibtnchat);
		ximgspecialistm = (Button) findViewById(R.id.ibtnspecialist);
		ximghospitalm = (Button) findViewById(R.id.ibtnhospital);
		ximgorder = (Button) findViewById(R.id.ibtnorder);
		imgchat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All products Activity
				 if(NoNetwork == "Network")
				    {
				Checkifgistered();
				Intent i = new Intent(getApplicationContext(), ChatAllActivity.class);
				startActivity(i);
				    }
				 else
				 {
					 Toast.makeText(MainScreenActivity.this,"No internet", 
			    				Toast.LENGTH_LONG).show();
					 return;
				 }
			}
		});		
		ximgorder.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All products Activity
				if(NoNetwork == "Network")
			    {
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				//Create a bundle object
		        Bundle b = new Bundle();		 
		        //Inserts a String value into the mapping of this Bundle
		        String no ="No".toString();
		        
		        i.putExtra("name",no);	
		        i.putExtra("coname","View Orders");		 
		        //Add the bundle to the intent.
		        i.putExtras(b);	
		        
				startActivity(i);	   }
				 else
				 {
					 Toast.makeText(MainScreenActivity.this,"No internet", 
			    				Toast.LENGTH_LONG).show();
					 return;
				 }			
			}
		});	
		imgmedication.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All products Activity
				if(NoNetwork == "Network")
			    {
				Intent i = new Intent(getApplicationContext(), MedsAll.class);
				startActivity(i);   }
				 else
				 {
					 Toast.makeText(MainScreenActivity.this,"No internet", 
			    				Toast.LENGTH_LONG).show();
					 return;
				 }
				
			}
		});
		imgregister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(NoNetwork == "Network")
			    {
				// Launching create new product activity
				Intent i = new Intent(getApplicationContext(), RegisterC.class);
				startActivity(i);   }
				 else
				 {
					 Toast.makeText(MainScreenActivity.this,"No internet", 
			    				Toast.LENGTH_LONG).show();
					 return;
				 }
				
			}
		});
		ximgspecialistm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(NoNetwork == "Network")
			    {
				// Launching create new product activity
				Intent i = new Intent(getApplicationContext(), SpecialistAll.class);
				startActivity(i);   }
				 else
				 {
					 Toast.makeText(MainScreenActivity.this,"No internet", 
			    				Toast.LENGTH_LONG).show();
					 return;
				 }
				
			}
		});
		ximghospitalm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(NoNetwork == "Network")
			    {
				// Launching create new product activity
				Intent i = new Intent(getApplicationContext(), hospiAll.class);
				startActivity(i);   }
				 else
				 {
					 Toast.makeText(MainScreenActivity.this,"No internet", 
			    				Toast.LENGTH_LONG).show();
					 return;
				 }
				
			}
		});
		AdsButtons();
		//////////////
		//mContext = this;
		mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);
				mViewFlipper.setAutoStart(true);
				mViewFlipper.setFlipInterval(6000);
				mViewFlipper.startFlipping();
		mViewFlipper.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				detector.onTouchEvent(event);
				return true;
			}
		});
		///////////
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
	
	private boolean haveNetworkConnection() {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;
	    String nowifi="No WIFI internet Connection";
	    String noMobile ="No Mobile internet Connection";

	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
    	
	    }
	    
	    if(haveConnectedWifi== false)
	    {
	    	Toast.makeText(MainScreenActivity.this,nowifi, 
    				Toast.LENGTH_LONG).show();
	    	NoNetwork="NoNetwork";
	    }
	    if(haveConnectedMobile== false)
	    {
	    	Toast.makeText(MainScreenActivity.this,noMobile, 
    				Toast.LENGTH_LONG).show();
	    	NoNetwork="NoNetwork";
	    }
	    
	    if(haveConnectedWifi== true)
	    {
	    	Toast.makeText(MainScreenActivity.this,"WIFI internet available", 
    				Toast.LENGTH_LONG).show();
	    	NoNetwork="Network";
	    }
	    if(haveConnectedMobile== true)
	    {
	    	Toast.makeText(MainScreenActivity.this,"Mobile internet available", 
    				Toast.LENGTH_LONG).show();
	    	NoNetwork="Network";
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}
	
	private void AdsButtons()
	{
		/////ADS LINK////
		Ads1 = (ImageView) findViewById(R.id.AdsLogo1);		
		Ads1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All products Activity				
				Intent i = new Intent(getApplicationContext(), Ads1.class);
		        
				//Create a bundle object
		        Bundle b = new Bundle();		 
		        //Inserts a String value into the mapping of this Bundle
		        i.putExtra("name","1");		 
		        //Add the bundle to the intent.
		        i.putExtras(b);			        
		        //start the DisplayActivity
		        startActivity(i);
				
				
				
			}
		});	
		
		Ads2 = (ImageView) findViewById(R.id.AdsLogo2);		
		Ads2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All products Activity				
				Intent i = new Intent(getApplicationContext(), Ads1.class);
		        
				//Create a bundle object
		        Bundle b = new Bundle();		 
		        //Inserts a String value into the mapping of this Bundle
		        i.putExtra("name","2");		 
		        //Add the bundle to the intent.
		        i.putExtras(b);			        
		        //start the DisplayActivity
		        startActivity(i);
				
				
				
			}
		});	
		
		Ads3 = (ImageView) findViewById(R.id.AdsLogo3);		
		Ads3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All products Activity				
				Intent i = new Intent(getApplicationContext(), Ads1.class);
		        
				//Create a bundle object
		        Bundle b = new Bundle();		 
		        //Inserts a String value into the mapping of this Bundle
		        i.putExtra("name","3");		 
		        //Add the bundle to the intent.
		        i.putExtras(b);			        
		        //start the DisplayActivity
		        startActivity(i);
				
				
				
			}
		});			
		
		Ads4 = (ImageView) findViewById(R.id.AdsLogo4);		
		Ads4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All products Activity				
				Intent i = new Intent(getApplicationContext(), Ads1.class);
		        
				//Create a bundle object
		        Bundle b = new Bundle();		 
		        //Inserts a String value into the mapping of this Bundle
		        i.putExtra("name","4");		 
		        //Add the bundle to the intent.
		        i.putExtras(b);			        
		        //start the DisplayActivity
		        startActivity(i);
				
				
				
			}
		});			
		
		Ads5 = (ImageView) findViewById(R.id.AdsLogo5);		
		Ads5.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All products Activity				
				Intent i = new Intent(getApplicationContext(), Ads1.class);
		        
				//Create a bundle object
		        Bundle b = new Bundle();		 
		        //Inserts a String value into the mapping of this Bundle
		        i.putExtra("name","5");		 
		        //Add the bundle to the intent.
		        i.putExtras(b);			        
		        //start the DisplayActivity
		        startActivity(i);
				
				
				
			}
		});			
		
		Ads6 = (ImageView) findViewById(R.id.AdsLogo6);		
		Ads6.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// Launching All products Activity				
				Intent i = new Intent(getApplicationContext(), Ads1.class);
		        
				//Create a bundle object
		        Bundle b = new Bundle();		 
		        //Inserts a String value into the mapping of this Bundle
		        i.putExtra("name","6");		 
		        //Add the bundle to the intent.
		        i.putExtras(b);			        
		        //start the DisplayActivity
		        startActivity(i);
				
				
				
			}
		});			

		/////ADS LINK////
	}
	private void updateAds()
	{
		adsImageLoader imgLoader = new adsImageLoader(getApplicationContext());
		imgLoader.fileCache.clear();
		imgLoader.memoryCache.clear();
	}

//////ADS///////
	class SwipeGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					//mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
					//mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
					// controlling animation
					mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
					mViewFlipper.showNext();
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					//mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
					//mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.right_out));
					// controlling animation
					mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
					mViewFlipper.showPrevious();
					return true;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
	}
//////ADS///////
	private void Checkifgistered()
	{
		String IfComIdExist = ((Spinner) findViewById(R.id.MyNumberHome)).getSelectedItem()
	.toString();
		//tbnewval = (EditText) findViewById(R.id.tbnewval);
		//tbnewval.setText(nm);
        if (("Not registered".equals(IfComIdExist)))            
        {
        	Toast.makeText(MainScreenActivity.this,"Please register to chat!", Toast.LENGTH_LONG).show();		
        	Intent i = new Intent(getApplicationContext(), RegisterC.class);
			startActivity(i);
			return;
        }

    else
        {	            				
		
        }	
	}
	private void FetchAds()
	{
        // Loader image - will be shown before loading image
        int loader =1;
         
        // Imageview to show
        ImageView image1 = (ImageView) findViewById(R.id.AdsLogo1);
        ImageView image2 = (ImageView) findViewById(R.id.AdsLogo2);
        ImageView image3 = (ImageView) findViewById(R.id.AdsLogo3);
        ImageView image4 = (ImageView) findViewById(R.id.AdsLogo4);
        ImageView image5 = (ImageView) findViewById(R.id.AdsLogo5);
        ImageView image6 = (ImageView) findViewById(R.id.AdsLogo6);
        ImageView Adsi = (ImageView) findViewById(R.id.AdsSaleLogo);
         
        //Image url
        String Ads1 = "http://bmgfdaycare.com/PharConnect/Ads/Ads1.png";
        String Ads2 = "http://bmgfdaycare.com/PharConnect/Ads/Ads2.png";
        String Ads3 = "http://bmgfdaycare.com/PharConnect/Ads/Ads3.png";
        String Ads4 = "http://bmgfdaycare.com/PharConnect/Ads/Ads4.png";
        String Ads5 = "http://bmgfdaycare.com/PharConnect/Ads/Ads5.png";
        String Ads6 = "http://bmgfdaycare.com/PharConnect/Ads/Ads6.png";
        String AdsSale= "http://bmgfdaycare.com/PharConnect/Ads/AdsSale.png";
         
        //ImageLoader class instance
        adsImageLoader imgLoader = new adsImageLoader(getApplicationContext());
        imgLoader.DisplayImage(Ads1, loader, image1);
        imgLoader.DisplayImage(Ads2, loader, image2);
        imgLoader.DisplayImage(Ads3, loader, image3);
        imgLoader.DisplayImage(Ads4, loader, image4);
        imgLoader.DisplayImage(Ads5, loader, image5);
        imgLoader.DisplayImage(Ads6, loader, image6);
        imgLoader.DisplayImage(AdsSale, loader, Adsi);
	}
	private void MyDatabaseHandler()
	{
		//My Phone Number
		DatabaseHandler db = new DatabaseHandler(this);        
        List<Contact> contacts = db.getAllContacts();
		for (Contact cn : contacts) 
		{
            String log = cn.getPhoneNumber();         
            SpMyNum = (Spinner) findViewById(R.id.MyNumberHome);
		    List list = new ArrayList();
		    list.add(log);
		    ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, list);
		    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    SpMyNum.setAdapter(dataAdapter);
        }
	}
}
//}
