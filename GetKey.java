package com.example.dbconnexion;

import java.util.ArrayList;
import java.util.List;

import com.example.terfinal.NvMsg;
import com.example.terfinal.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class GetKey extends Activity {

	Bundle be, br;
	String num;
	Intent i;
	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// single product url
	private static final String url_key_details = "http://192.168.137.1/get_key.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PUBKEY = "pubkey";
	private static final String TAG_NUM = "num";
	private static final String TAG_ENCODED = "encoded";
	private static final String TAG_EXPONEND = "exponend";
	private static final String TAG_MODULUS = "modulus";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_key);

		// getting product details from intent
		// Intent i = getIntent();

		// getting product num from intent
		br = getIntent().getExtras();

		// Getting complete product details in background thread
		new GetKeyDetails().execute();
	}

	/**
	 * Background Async Task to Get complete product details
	 * */
	class GetKeyDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(GetKey.this);
			pDialog.setMessage("Loading Public key details. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... args) {

			// updating UI from Background Thread
			int success;
			try {
				// Building Parameters
				num = br.getString("num");
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("num", num));

				// getting product details by making HTTP request
				// Note that product details url will use GET request
				JSONObject json = jsonParser.makeHttpRequest(url_key_details,
						"GET", params);

				// check your log for json response
				Log.d("Public Key Details", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					// successfully received product details
					JSONArray productObj = json.getJSONArray(TAG_PUBKEY); // JSON
																			// Array

					for (int l = 0; l < productObj.length(); l++) {
						JSONObject c = productObj.getJSONObject(l);
						// pubkey with this num found
						// Edit Text
						be = new Bundle();
						be.putString("modulus", c.getString(TAG_MODULUS));
						be.putString("exponend", c.getString(TAG_EXPONEND));
						be.putString("encoded", c.getString(TAG_ENCODED));

						i = new Intent();
						i.putExtras(be);
						setResult(RESULT_OK, i);

						startActivity(new Intent(GetKey.this, NvMsg.class));
					}

				} else {
					// key with num not found
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
			// dismiss the dialog once got all details
			pDialog.dismiss();
		}
	}
}
