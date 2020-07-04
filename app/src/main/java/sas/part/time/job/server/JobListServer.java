package sas.part.time.job.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import sas.part.time.job.R;
import sas.part.time.job.dialog.SasLogoDialog;
import sas.part.time.job.json.JSONParser;
import sas.part.time.job.pojo.JobData;
import sas.part.time.job.pojo.JobDataList;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.userInterface.IJobList;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.os.AsyncTask;


public class JobListServer extends AsyncTask<Void, Void, JobDataList> {	

	private SasLogoDialog 		cusomeDialog;
	private Activity 			mActivity;
	private IJobList 			listener;
	private String 				mCity;
	private String 				mState;
	private String 				mCountry;
	private String 				mPin;
	private int 				mJobStatus;
	private PartTimeMessage 	appMessage 	= PartTimeMessage.getSingleInstance();
	private PartTimeUtils 		appConfig = PartTimeUtils.getSingleInstance();
	
	public JobListServer(Activity ac, IJobList lis, String city, String state, 
			String country, String pin, int jobStatus) {
		
		mActivity 	= ac;
		mCity   	= city;
		mState 		= state;
		mCountry   	= country;
		mPin 		= pin;
		mJobStatus  = jobStatus;
		listener 	= lis;
	}

	@Override
	protected void onPostExecute(JobDataList data) {
		super.onPostExecute(data);
		cusomeDialog.dismiss();
		listener.onJobList(data);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		cusomeDialog = new SasLogoDialog(mActivity);
		cusomeDialog.show();
	}

	@Override
	protected JobDataList doInBackground(Void... arg0) {

		String URL = mActivity.getResources().getString(R.string.job_list_url);
		UserInfo info = new UserInfo();
		String mUserId = info.getUserId(mActivity);
		JobDataList list = new JobDataList();
		ArrayList<JobData> JobList = new ArrayList<JobData>();

		try { 
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", mUserId));
			nameValuePairs.add(new BasicNameValuePair("city", mCity));
			nameValuePairs.add(new BasicNameValuePair("state", mState));
			nameValuePairs.add(new BasicNameValuePair("country", mCountry));
			nameValuePairs.add(new BasicNameValuePair("pin_code", mPin));
			nameValuePairs.add(new BasicNameValuePair("job_status", String.valueOf(mJobStatus)));
			
			UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(nameValuePairs);

			JSONParser jParser = new JSONParser();
			String jsonStr = jParser.getJSONFromUrl(URL,requestEntity);
			
			JSONObject jsonObj = new JSONObject(jsonStr);
			
			list.setResult(jsonObj.getBoolean("result"));
			list.setSubscription(jsonObj.getString("subscription"));
			
			if(list.isResult()) {
				
				JSONArray array = jsonObj.getJSONArray("lists");
				
				for(int i=0;i<array.length();i++) {
					
					JSONObject obj = array.getJSONObject(i);
					JobData data = new JobData();
					
					if(obj.has("job_id"))
						data.setJobId(obj.getString("job_id"));
					
					if(obj.has("job_type"))
						data.setJobType(obj.getString("job_type"));
					
					if(obj.has("job_details"))
						data.setJobDetails(obj.getString("job_details"));
					
					if(obj.has("phone_no"))
						data.setPhoneNumber(obj.getString("phone_no"));
					
					if(obj.has("amount"))
						data.setAmount(obj.getString("amount"));
					
					if(obj.has("currency"))
						data.setCurrency(obj.getString("currency"));
					
					if(obj.has("address"))
						data.setAddress(obj.getString("address"));
					
					if(obj.has("city"))
						data.setCity(obj.getString("city"));
					
					if(obj.has("state"))
						data.setState(obj.getString("state"));
					
					if(obj.has("country"))
						data.setCountry(obj.getString("country"));
					
					if(obj.has("pin_code"))
						data.setPin(obj.getString("pin_code"));
					
					if(obj.has("modify_date"))
						data.setPostedDate(appConfig.convertStringToDate(obj.getString("modify_date")));
					
					if(obj.has("latitude"))
						data.setLatitude(obj.getString("latitude"));
					
					if(obj.has("longitude"))
						data.setLongitude(obj.getString("longitude"));
					
					JobList.add(data);
				}
			}

		} catch (Exception e) {
			list.setResult(false);
			list.setException(true);
			list.setMessage(appMessage.getErrorMessage());
			e.printStackTrace();
		} 
		
		list.setJobList(JobList);

		return list;
	} 
}
