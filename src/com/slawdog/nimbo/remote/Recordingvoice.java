package com.slawdog.nimbo.remote;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Recordingvoice extends Activity {

	
	Button record,recordstop;
	Spinner spinerrecord;
	ArrayAdapter<String> adapter;
	 SharedPreferences.Editor prefsEditor;
	 SharedPreferences myPrefs;
	 String hostName= "";
	 String[] columns;
	 File externalStorage;  
	 String path = "";
	 MediaRecorder recorder;
	 Timer time;
	 String filename1="";
	 boolean s = false;
	 
	 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	setContentView(R.layout.recording);
	 record=(Button)findViewById(R.id.record);
	 recordstop=(Button)findViewById(R.id.recordstop);
	 spinerrecord=(Spinner)findViewById(R.id.spinnerrecord);
		externalStorage = Environment.getExternalStorageDirectory();
		String sdCardPath = externalStorage.getAbsolutePath();
		 recorder = new MediaRecorder();
			path = sdCardPath + "/" ;
		    Date dt = new Date();
            int hours = dt.getHours();
            int minutes = dt.getMinutes();
            int seconds = dt.getSeconds();
            String curTime = hours + ":"+minutes + ":"+ seconds;
           

			 filename1 = "phonecall_at" + curTime + ".mp4";	

	record.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					String state = android.os.Environment.getExternalStorageState();
				    if(!state.equals(android.os.Environment.MEDIA_MOUNTED))  {
				        try {
							throw new IOException("SD Card is not mounted.  It is " + state + ".");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    }
					
					
					
				Date dt = new Date();
	                int hours = dt.getHours();
	                int minutes = dt.getMinutes();
	                int seconds = dt.getSeconds();
	                String curTime = hours + "_"+minutes + "_"+ seconds;
	               

					 filename1 = "phonecall_at" + curTime + ".mp4";
					if(recorder == null)
					{
						 recorder = new MediaRecorder();
					}
		
					

					 File f=new File(path, filename1);
						try {
							s = f.createNewFile();
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							Toast.makeText(Recordingvoice.this,"hiiiii...."+e1.getMessage(),2000).show();
							e1.printStackTrace();
						}
					//MediaRecorder.AudioSource.VOICE_CALL + MediaRecorder.AudioSource.MIC 
					 //recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_UPLINK + MediaRecorder.AudioSource.VOICE_DOWNLINK );
						if(s==true)
						{
					 recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					 recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
					 recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		  		
					 recorder.setOutputFile(f.getAbsolutePath());
					// record.setText("stop");
					// record.setBackgroundColor( Color.BLUE);
					 Toast.makeText(Recordingvoice.this, String.valueOf(s), 3000).show();
					try {
						recorder.prepare();
						Toast.makeText(Recordingvoice .this,"Recording starts",5000).show();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e(".................................",""+ e.toString());
					}
					recorder.start();
						}
						else
						{
							Toast.makeText(getApplicationContext(), "No space Left on device",2000 ).show();
						}
				/* try {
					recorder.prepare();
					 Toast.makeText(Recordingvoice .this,"Recording starts",5000).show();
					recorder.start(); 
					
					  
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {   
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}*/
				}
			});
	 
			recordstop.setOnClickListener(new OnClickListener() {         
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(s==true)   
					{
					Toast.makeText(Recordingvoice .this,"Recording stoped",2000).show();
					if(recorder == null)
					{
						 recorder = new MediaRecorder();
					}
					if(recorder!=null)
					{
					recorder.stop();
					recorder.release();
					}
					recorder=null;
					time.cancel();
					uploadCode();
					}
					 
					
				}
			});
	 
	 SharedPreferences myPrefs=PreferenceManager.getDefaultSharedPreferences(this);
	   hostName=myPrefs.getString("server","http://yourdomain.com/web-service.php");
	   
		
	   Log.e("--------------","hhhhhhhhhhhhhhhhhhhhhhhhhh"+hostName);
	  String devicc= getServerResponse(hostName+ "?devices");
	   
         columns = devicc.split(",");
       
     
	adapter =new ArrayAdapter<String>(Recordingvoice.this, android.R.layout.simple_spinner_item,columns);
	      
	
  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
  spinerrecord.setAdapter(adapter);

  
  
	
	    // TODO Auto-generated method stub
  
  
  
  
  
  time =new Timer();
  TimerTask moniterthread=new TimerTask() {
  	
  	@Override
  	public void run() {
  		// TODO Auto-generated method stub
  		Recordingvoice.this.runOnUiThread(new Runnable() {
			public void run() {
				if(recorder!=null)
				{  
					recorder.stop();
					 recorder.release();
				}
				 
				
				 recorder=null;
				
		  		time.cancel();
		  		uploadCode();
			}
		});
 
  		
  	}
  };
  time.scheduleAtFixedRate(moniterthread, 20000, 20000);

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
	
	  String host = hostName + "?mote=login";
	  String res =getServerResponse(host);




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

public void uploadCode()
{
	HttpURLConnection connection = null;
	DataOutputStream outputStream = null;
	DataInputStream inputStream = null;
	
	//String pathToOurFile = "/data/file_to_send.mp3";
	String urlServer = "http://yourdomain.com/uploaddata.php";
	String lineEnd = "\r\n";
	String twoHyphens = "--";
	String boundary =  "*****";

	int bytesRead, bytesAvailable, bufferSize;
	byte[] buffer;
	int maxBufferSize = 1*1024*1024;

	try
	{
		FileInputStream fileInputStream = new FileInputStream(new File(path+filename1) );

		URL url = new URL(urlServer);
		connection = (HttpURLConnection) url.openConnection();

		// Allow Inputs & Outputs
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);

		// Enable POST method
		connection.setRequestMethod("POST");

		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

		outputStream = new DataOutputStream( connection.getOutputStream() );
		outputStream.writeBytes(twoHyphens + boundary + lineEnd);
		outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + filename1 +"\"" + lineEnd);
		outputStream.writeBytes(lineEnd);

		bytesAvailable = fileInputStream.available();
		bufferSize = Math.min(bytesAvailable, maxBufferSize);
		buffer = new byte[bufferSize];  

		// Read file
		bytesRead = fileInputStream.read(buffer, 0, bufferSize);

		while (bytesRead > 0)
		{
			outputStream.write(buffer, 0, bufferSize);
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		}

		outputStream.writeBytes(lineEnd);
		outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

		// Responses from the server (code and message)
		int serverResponseCode;
		String serverResponseMessage;

		serverResponseCode = connection.getResponseCode();
		serverResponseMessage = connection.getResponseMessage();
		Toast.makeText(Recordingvoice.this,"sending data to server",2000).show();
		
		 Log.v("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii",serverResponseCode + "/////  " +serverResponseMessage );
		fileInputStream.close();
		outputStream.flush();
		outputStream.close();
	}
	catch (Exception ex)
	{
		Toast.makeText(Recordingvoice.this,"hiiiiiii"+ex.getMessage(),2000).show();
		//Exception handling
	}

  


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

}