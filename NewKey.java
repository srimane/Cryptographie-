package com.example.dbconnexion;

import java.util.ArrayList;
import java.util.List;

import com.example.terfinal.MainActivity;
import com.example.terfinal.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class NewKey extends Activity{

	private ProgressDialog pDialog;
	// url to create new key
	private static String url_create_key = "http://192.168.137.1/create_key.php";
	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	JSONParser jsonParser = new JSONParser();
	String num,modulus,exponend,encoded;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_key);
		
		Bundle b = getIntent().getExtras();
		num = b.getString("num");
		encoded = b.getString("encoded");
		exponend = b.getString("exponend");
		modulus = b.getString("modulus");
		
		new CreateNewKey().execute();
		
	}

	
	
	

class CreateNewKey extends AsyncTask<String, String, String> {

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(NewKey.this);
		pDialog.setMessage("Creating key..");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	/**
	 * Creating key
	 * */
	protected String doInBackground(String... args) {
		
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("num", num));
		params.add(new BasicNameValuePair("encoded", encoded));
		params.add(new BasicNameValuePair("exponend", exponend));
		params.add(new BasicNameValuePair("modulus", modulus));

		// getting JSON Object
		JSONObject json = jsonParser.makeHttpRequest(url_create_key,
				"POST", params);
		
		// check log cat fro response
		Log.d("Create Response", json.toString());

		// check for success tag
		try {
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1) {
				// successfully created key
				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);
				finish();
			} else {
				// failed to create key
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
