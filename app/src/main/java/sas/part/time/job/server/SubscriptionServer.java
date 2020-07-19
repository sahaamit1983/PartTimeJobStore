package sas.part.time.job.server;

import org.json.JSONObject;

import sas.part.time.job.R;
import sas.part.time.job.dialog.SasLogoDialog;
import sas.part.time.job.pojo.SubscriptionMsg;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.userInterface.ISubscription;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.os.AsyncTask;

public class SubscriptionServer extends AsyncTask<Void, Void, SubscriptionMsg> {	

	private SasLogoDialog 		cusomeDialog;
	private Activity 			mActivity;
	private ISubscription 		CallBack;
	private PartTimeMessage 	appMessage 	= PartTimeMessage.getSingleInstance();
	private PartTimeUtils 		appConfig = PartTimeUtils.getSingleInstance();

	public SubscriptionServer(Activity ac, ISubscription callBack) {
		mActivity 	= ac;
		CallBack	= callBack;
	}

	@Override
	protected void onPostExecute(SubscriptionMsg data) {
		super.onPostExecute(data);
		cusomeDialog.dismiss();
		CallBack.onSubscription(data);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		cusomeDialog = new SasLogoDialog(mActivity);
		cusomeDialog.show();
	}

	@Override
	protected SubscriptionMsg doInBackground(Void... arg0) {

		String URL = mActivity.getResources().getString(R.string.get_subscription_url);

		SubscriptionMsg data = new SubscriptionMsg();
		UserInfo info = new UserInfo();
		String mUserId = info.getUserId(mActivity);

		/*try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_id", mUserId));

			UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(nameValuePairs);

			JSONParser jParser = new JSONParser();
			String jsonStr = jParser.getJSONFromUrl(URL,requestEntity);

			JSONObject json = new JSONObject(jsonStr);

			data.setMessage(json.getString("msg"));
			data.setResult(json.getBoolean("result"));
			if(data.isResult()) {
				data.setLastDate(appConfig.convertStringToDate( json.getString("lastdate")));
				data.setSystemDate(appConfig.convertStringToDate(json.getString("system_date")));
			}
		} catch (Exception e) {
			data.setMessage(appMessage.getErrorMessage());
			data.setException(true);
			data.setResult(false);
			e.printStackTrace();
		} */

		return data;
	} 
}
