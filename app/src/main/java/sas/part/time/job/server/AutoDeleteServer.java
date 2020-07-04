package sas.part.time.job.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import sas.part.time.job.R;
import sas.part.time.job.json.JSONParser;
import sas.part.time.job.preference.UserInfo;
import android.app.Activity;

public class AutoDeleteServer extends Thread {	

	private Activity mActivity;
	
	public AutoDeleteServer(Activity ac) {
		mActivity 	= ac;
	}
	
	@Override
	public void run() {
		super.run();
	
		doInBackground();
	}

	private void doInBackground() {

		String URL = mActivity.getResources().getString(R.string.auto_delete_url);

		UserInfo info = new UserInfo();
		String mUserId = info.getUserId(mActivity);

		try { 
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", mUserId));
			
			UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(nameValuePairs);

			JSONParser jParser = new JSONParser();
			jParser.getJSONFromUrl(URL,requestEntity);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	} 
}
