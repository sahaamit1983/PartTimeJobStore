package sas.part.time.job.server;

import org.json.JSONObject;

import sas.part.time.job.R;
import sas.part.time.job.dialog.SasLogoDialog;
import sas.part.time.job.pojo.ResultMsg;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.userInterface.IDelete;
import sas.part.time.job.utils.PartTimeMessage;
import android.app.Activity;
import android.os.AsyncTask;

public class DeleteJobServer extends AsyncTask<Void, Void, ResultMsg> {	

	private SasLogoDialog 		cusomeDialog;
	private Activity 			mActivity;
	private String 				JobId;
	private IDelete 			listener;
	private int 				Position;
	private PartTimeMessage 	appMessage = PartTimeMessage.getSingleInstance();

	public DeleteJobServer(Activity ac, IDelete lis, String jobId, int position) {
		mActivity 	= ac;
		JobId   	= jobId;
		listener 	= lis;
		Position	= position;
	}

	@Override
	protected void onPostExecute(ResultMsg data) {
		super.onPostExecute(data);
		cusomeDialog.dismiss();
		listener.onDelete(data, Position);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		cusomeDialog = new SasLogoDialog(mActivity);
		cusomeDialog.show();
	}

	@Override
	protected ResultMsg doInBackground(Void... arg0) {

		String URL = mActivity.getResources().getString(R.string.delete_job_url);
		UserInfo info = new UserInfo();
		String mUserId = info.getUserId(mActivity);
		ResultMsg data = new ResultMsg();

		/*try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("job_id", JobId));
			nameValuePairs.add(new BasicNameValuePair("user_id", mUserId));
			
			UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(nameValuePairs);

			JSONParser jParser = new JSONParser();
			String jsonStr = jParser.getJSONFromUrl(URL,requestEntity);

			JSONObject json = new JSONObject(jsonStr);

			data.setResult(json.getBoolean("result")); 
			data.setMessage(json.getString("msg"));
			
		} catch (Exception e) {
			data.setResult(false);
			data.setMessage(appMessage.getErrorMessage());
			e.printStackTrace();
		} */

		return data;
	} 
}
