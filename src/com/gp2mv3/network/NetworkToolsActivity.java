package com.gp2mv3.network;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.whois.WhoisClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NetworkToolsActivity extends Activity
{
	private String ip;
	private EditText txtIp;
	private EditText output;
	private Button btPing;
	private Button btWhois;
	private Button btDown;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        output = (EditText) findViewById(R.id.output);
        txtIp = (EditText) findViewById(R.id.etIp);
        txtIp.setText("google.com");   
        
        btPing = (Button) findViewById(R.id.btPing);
        btPing.setOnClickListener(new pingListener()); 

        btDown= (Button) findViewById(R.id.btDown);
        btDown.setOnClickListener(new downListener()); 
        
        btWhois = (Button) findViewById(R.id.btWhois);
        btWhois.setOnClickListener(new whoisListener()); 
    }
    
    private class pingListener implements android.view.View.OnClickListener
    {
    	private boolean enabled = false;
    	private Ping ping;
    	
    	public pingListener()
    	{
    		ping = new Ping(txtIp.getText().toString(), output);
    	}
    	
		@Override
		public void onClick(View v)
		{
			if(enabled)
			{
				ping.pause();
				btPing.setText(R.string.ping);
			}
			else
			{
				ping.resume(txtIp.getText().toString());
				btPing.setText(R.string.pingS);
			}
			
			enabled = !enabled;
		}	
    }    
    
    private class whoisListener implements android.view.View.OnClickListener
    {
		@Override
		public void onClick(View v)
		{
			ip = txtIp.getText().toString().trim();

			String nameToQuery = ip;

			WhoisClient whoisClient = new WhoisClient();
			try {
				whoisClient.connect(WhoisClient.DEFAULT_HOST);
				String results = whoisClient.query("="+nameToQuery+"\r\n");

				output.setText(results);
				whoisClient.disconnect();
			} catch (SocketException e) {
				output.setText("SocketException: "+e.toString());
			} catch (IOException e) {
				output.setText("IOException: "+e.toString());
			}
		}
			
    }    
    
    private class downListener implements android.view.View.OnClickListener
    {
		@Override
		public void onClick(View arg0) {
		    ip = txtIp.getText().toString().trim();
			Network net = new Network("http://www.downforeveryoneorjustme.com/"+ip);
			try {
				net.execute(Network.RequestMethod.GET);
				String xml = net.getResponse();

				if(xml.contains("It's just you."))
					output.setText("It's just you.");
				else
					output.setText(ip+" is down for everyone.");
				
			} catch (Exception e) {
				output.setText(e.toString());
			}	
		}
    }

}