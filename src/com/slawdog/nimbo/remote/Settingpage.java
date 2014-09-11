package com.slawdog.nimbo.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Settingpage extends PreferenceActivity
implements SharedPreferences.OnSharedPreferenceChangeListener
{
Button btn;
SharedPreferences hostName;
SharedPreferences deviceName;
SharedPreferences userName;
SharedPreferences userPassword;
String host1,device1,user1,password1;
EditTextPreference host;
EditTextPreference device;
EditTextPreference user;
EditTextPreference password;
String res;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {     
	    super.onCreate(savedInstanceState);
	 
	    addPreferencesFromResource(R.xml.settingscreen);    
	    setContentView(R.layout.buttonpreference);
	    host=(EditTextPreference)findPreference("server");
	    device=(EditTextPreference)findPreference("devicename");
	    user=(EditTextPreference)findPreference("username");
	    password=(EditTextPreference)findPreference("userpassword");
	    hostName=PreferenceManager.getDefaultSharedPreferences(Settingpage.this);   
	    deviceName=PreferenceManager.getDefaultSharedPreferences(Settingpage.this);
	    userName=PreferenceManager.getDefaultSharedPreferences(Settingpage.this);
	    userPassword=PreferenceManager.getDefaultSharedPreferences(Settingpage.this);
	   
	    btn=(Button)findViewById(R.id.buttonPrefer);      
	    btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				 host1=hostName.getString("server", "");
				    device1=deviceName.getString("devicename", "");
				    user1=userName.getString("username", "");
				    password1=userPassword.getString("userpassword", "");
				    String hosName=host1+"?mote=login";
				    String useName="&user="+user1;
				    String pass="&pass=" +password1;
				    String deviceSelected="&org="+device1;
				    res=getServerResponse(hosName+useName+pass+deviceSelected);
				
				Intent in =new Intent(Settingpage.this,Nimbomain.class);
				 in.putExtra("resp", res);
				 in.putExtra("deviceid", device1);
				startActivity(in);
			}
		});
	  
	    // TODO Auto-generated method stub  
	}  


	public String getServerResponse(String url)
    {
    	String result = "";
    	HttpClient hc = new DefaultHttpClient();
    	HttpResponse hr ;
    	HttpGet hg = new HttpGet(url);
    	try
    	{
    		hr = hc.execute(hg);
    		if(hr.getStatusLine().getStatusCode() == 200)
    		{
    			HttpEntity he = hr.getEntity();
    			if (he != null)
    			{
    				InputStream is = he.getContent();
    				  result = convertStreamToString(is);
    				  is.close();

    			}
    		}
    	}
    	
    	
    	
    	catch (Exception e) {
			// TODO: handle exception
		}
    	
    	 return result;
    }
	private String convertStreamToString(InputStream instream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
        StringBuilder sb = new StringBuilder();
  
        
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            	instream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       
        return sb.toString();
		 
    }
    
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}

}
