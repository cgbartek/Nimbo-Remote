package com.slawdog.nimbo.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceRecognition extends Activity implements OnClickListener, OnInitListener {


    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    private ListView mList;
    TextView txt;
    TextToSpeech talker;
    String res;
    /**
     * Called with the activity is first created.
     */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // startVoiceRecognitionActivity();
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.voice_recognition);

        // Get display items for later interaction
        Button speakButton = (Button) findViewById(R.id.btn_speak);

       // mList = (ListView) findViewById(R.id.list);

         txt = (TextView) findViewById(R.id.text);
        
         talker = new TextToSpeech(this, this);
         
        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            speakButton.setOnClickListener((OnClickListener) this);
        } else {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
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
    

    /**
     * Handle the click on the start recognition button.
     */
    public void onClick(View v) {
        if (v.getId() == R.id.btn_speak) {
            startVoiceRecognitionActivity();
        }
    }

    /**
     * Fire an intent to start the speech recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
       
       
    }

    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            
          //  Toast.makeText(VoiceRecognition.this, matches.get(0), 5000).show();
        String device = "Bedroom";
          /*  mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
         matches));     
            */
            txt.setText(""+matches.get(0));
            String host = "http://yourdomain.com/web-service.php?tmote=";
            String userCommand = URLEncoder.encode(matches.get(0));
            String deviceSelected = "&org=" + device;
           res =  getServerResponse(host + userCommand + deviceSelected);
           say(""+ res);
          	Toast.makeText(VoiceRecognition.this,res ,5000).show();
          	
           
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
    
    public void say(String text2say){
    		      talker.speak(text2say, TextToSpeech.QUEUE_FLUSH, null);
    		    }

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
	//	say("Hello World");
	}
	
	@Override
		   public void onDestroy() {
	      if (talker != null) {
		         talker.stop();
		         talker.shutdown();
		      }
		 
		      super.onDestroy();
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
