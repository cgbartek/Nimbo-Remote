package com.slawdog.nimbo.remote;

import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Nimbomain extends Activity 

{
    /** Called when the activity is first created. */
    
	WebView webview;
    WebViewClient wc;
	Button itrcom,voice;
	
	TextView tv;
	@Override
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv=(TextView)findViewById(R.id.textDevice);
       
       itrcom=(Button)findViewById(R.id.itrcom_nimbomain);
       itrcom.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent_itr = new Intent(Nimbomain.this,Recordingvoice.class);
			startActivity(intent_itr);	
		}
	});
       voice=(Button)findViewById(R.id.voice_nimbomain);
       voice.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(Nimbomain.this,VoiceRecognition.class);
			startActivity(intent);
		}
	});
       
       //device_id=(Button)findViewById(R.id.device_id_nimbomain);
       //device_id.setOnClickListener(this);
     Intent in = getIntent();
     Bundle b = in.getExtras();
    
     String sh = "";
     String devicid="";
     if(b != null)
     {
    	sh  = b.getString("resp");
    	devicid=b.getString("deviceid");
     }
     tv.setText(devicid);
     sh = URLEncoder.encode(sh);
     String temp=sh.replace("+"," " );
       
       
        webview=(WebView)findViewById(R.id.webview_nimbomain);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(0);
        
        webview.loadData(temp, "text/html", "UTF-8");
       	 // webview.loadUrl("http://www.google.com");
       	  
       	webview.setWebViewClient(new WebViewClient()
		{

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(Nimbomain.this,"gps problem...", 4000).show();
				
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
				super.onReceivedSslError(view, handler, error);
				Toast.makeText(Nimbomain.this,"gps problem...", 4000).show();
			}
		
		
		});
       	
        
    }
	

	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	   
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) 
	    {
	    case R.id.setting: 
	    	startActivity(new Intent(this,Settingpage.class));
	    	
	    	Toast.makeText(this, "setting clicked", Toast.LENGTH_LONG).show();
        break;

	    }
		return true;
	}
	
	}

