package sas.part.time.job.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import sas.part.time.job.R;
import sas.part.time.job.dialog.SasLogoDialog;
import sas.part.time.job.json.JSONParser;
import sas.part.time.job.pojo.ResultMsg;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.userInterface.IJobSubmit;
import sas.part.time.job.utils.PartTimeMessage;
import android.app.Activity;
import android.os.AsyncTask;


public class JobSubmitServer extends AsyncTask<Void, Void, ResultMsg> {	

	private SasLogoDialog 		cusomeDialog;
	private Activity 			mActivity;
	private IJobSubmit 			listener;
	private String 				mJobType;
	private String 				mJobDetails;
	private String 				mAmount;
	private String 				mCurrency;
	private String 				mCity;
	private String 				mState;
	private String 				mCountry;
	private String 				mPin;
	private String 				mAddress;
	private String 				mLatitude;
	private String 				mLongitude;
	private PartTimeMessage 	appMessage 	= PartTimeMessage.getSingleInstance();
	

	public JobSubmitServer(Activity ac, IJobSubmit lis, 
			String jobType, String jobDetails, String amount, String currency, 
			String city, String state, String country, String pin, String address, 
			String latitude, String longitude) {
		
		mActivity 	= ac;
		mJobType 	= jobType;
		mJobDetails = jobDetails;
		mAmount 	= amount;
		mCurrency	= currency;
		mCity   	= city;
		mState 		= state;
		mCountry   	= country;
		mPin 		= pin;
		mAddress	= address;
		mLatitude   = latitude;
		mLongitude 	= longitude;
		listener 	= lis;
	}

	@Override
	protected void onPostExecute(ResultMsg data) {
		super.onPostExecute(data);
		cusomeDialog.dismiss();
		listener.onJobSubmit(data);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		cusomeDialog = new SasLogoDialog(mActivity);
		cusomeDialog.show();
	}

	@Override
	protected ResultMsg doInBackground(Void... arg0) {

		String URL = mActivity.getResources().getString(R.string.job_submit_url);
		UserInfo info = new UserInfo();
		String mUserId = info.getUserId(mActivity);
		String mPhoneNo = info.getPhoneNumber(mActivity);
		ResultMsg data = new ResultMsg();

		try { 
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", mUserId));
			nameValuePairs.add(new BasicNameValuePair("job_type", mJobType));
			nameValuePairs.add(new BasicNameValuePair("job_details", mJobDetails));
			nameValuePairs.add(new BasicNameValuePair("amount", mAmount));
			nameValuePairs.add(new BasicNameValuePair("currency", mCurrency));
			nameValuePairs.add(new BasicNameValuePair("phone_no", mPhoneNo));
			nameValuePairs.add(new BasicNameValuePair("city", mCity));
			nameValuePairs.add(new BasicNameValuePair("state", mState));
			nameValuePairs.add(new BasicNameValuePair("country", mCountry));
			nameValuePairs.add(new BasicNameValuePair("pin_code", mPin));
			nameValuePairs.add(new BasicNameValuePair("address", mAddress));
			nameValuePairs.add(new BasicNameValuePair("latitude", mLatitude));
			nameValuePairs.add(new BasicNameValuePair("longitude", mLongitude));

			UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(nameValuePairs);

			JSONParser jParser = new JSONParser();
			String jsonStr = jParser.getJSONFromUrl(URL,requestEntity);
			
			JSONObject jsonObj = new JSONObject(jsonStr);
			
			data.setResult(jsonObj.getBoolean("result"));
			data.setMessage(jsonObj.getString("msg"));

		} catch (Exception e) {
			data.setResult(false);
			data.setMessage(appMessage.getErrorMessage());
			e.printStackTrace();
		} 

		return data;
	} 
}
