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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Loginpage extends Activity {

	/** Called when the activity is first created. */
	EditText user_id,user_psw;
	CheckBox check_remember;
	Button login_btn;
	SharedPreferences myPrefs;
	Spinner spinner;
	 SharedPreferences.Editor prefsEditor;
	 String device1;
		EditTextPreference hostname;
		
		String username="";
	 String[] columns;
	 String hostName= "";
	
	// String[] string=new String[] {"bedroom","fan"};
	 
		ArrayAdapter<String> adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	setContentView(R.layout.loginpage);
	
	
	 spinner = (Spinner) findViewById(R.id.spinner);
	 hostname= (EditTextPreference)findPreference("server");
	// String j= hostname.getText();
	 
	 
	
	user_id=(EditText)findViewById(R.id.user_id);
	user_psw=(EditText)findViewById(R.id.user_psw);
	check_remember=(CheckBox)findViewById(R.id.check_remember);
	login_btn=(Button)findViewById(R.id.login_btn);
	  
	//String device1 ="http://yourdomain.com/web-service.php?devices";
	
	// device1 =servername.toString();
	SharedPreferences myPrefs=PreferenceManager.getDefaultSharedPreferences(this);
	   hostName=myPrefs.getString("server","http://yourdomain.com/web-service.php");
	   Log.e("--------------","hhhhhhhhhhhhhhhhhhhhhhhhhh"+hostName);
	  String devicc= getServerResponse(hostName + "?devices");
	   
           columns = devicc.split(",");
         
         
	
          // Toast.makeText(Loginpage.this,""+columns[0] ,5000).show();
	
	    
	
	//myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
     
	 prefsEditor  = myPrefs.edit();
	check_remember.setChecked(myPrefs.getBoolean("isChecked",false));
	
	adapter =new ArrayAdapter<String>(Loginpage.this, android.R.layout.simple_spinner_item,columns);
	      
	
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(adapter);

	
	  
	   
	
	if(check_remember.isChecked()==false)
	{
		user_id.setText("");
		user_psw.setText("");
	}
	else
	{
		String nsme=myPrefs.getString("username", "");
    	String pass=myPrefs.getString("password", "");
    	if(nsme.length()>0)
    	{
    		user_id.setText(nsme);	
    	}
    	if(pass.length()>0)
    	{
    		user_psw.setText(pass);
    	}
	}
	
	   
	login_btn.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View paramView) {
			// TODO Auto-generated method stub
			
			if(check_remember.isChecked()==true)
				{
				prefsEditor.putBoolean("isChecked", true);
				String s = user_id.getEditableText().toString().trim();
				String s1 = user_psw.getEditableText().toString().trim();
				prefsEditor.putString("username",s );
				prefsEditor.putString("password",s1 );
				prefsEditor.commit();
		 
			
			//http://yourdomain.com/web-service.php
				  String device = "Bedroom";
				
				  String host = hostName + "?mote=login";
				
			
					//http://localhost:82/?mote=login&user=username&pass=password&org=Bedroom 
	           username= "&user=" +s;
	            String password = "&pass=" +s1;
	            String deviceSelected = "&org=" + device;
		      
	            String res =  getServerResponse(host +username + password + deviceSelected);
		         
		       
   
		             
		             
		          
		          Intent i=new Intent(Loginpage.this,Nimbomain.class);   
		          i.putExtra("resp", res);
		        
		          startActivity(i);
		          
		          Toast.makeText(Loginpage.this,res ,5000).show();

	            
			}
			
			  
		}
	});
	    
	    
	    // TODO Auto-generated method stub
	}
	
	private EditTextPreference findPreference(String string) {
		// TODO Auto-generated method stub
		return null;
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
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
	}

	    
	
}




