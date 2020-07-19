package sas.part.time.job.server;

import org.json.JSONObject;

import sas.part.time.job.R;
import sas.part.time.job.dialog.SasLogoDialog;
import sas.part.time.job.utils.PartTimeMessage;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

public class ForgotPasswordServer extends AsyncTask<Void, Void, String> {	

	private SasLogoDialog 		cusomeDialog;
	private Activity 			mActivity;
	private String 				EmailId;
	private PartTimeMessage 	appMessage = PartTimeMessage.getSingleInstance();
	
	public ForgotPasswordServer(Activity ac, String emailId) {
		mActivity 	= ac;
		EmailId   	= emailId;
	}

	@Override
	protected void onPostExecute(String data) {
		super.onPostExecute(data);
		cusomeDialog.dismiss();
		
		Toast.makeText(mActivity, data, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		cusomeDialog = new SasLogoDialog(mActivity);
		cusomeDialog.show();
	}

	@Override
	protected String doInBackground(Void... arg0) {

		String URL = mActivity.getResources().getString(R.string.forgotpassword_url);

		String data = new String();

		/*try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email_id", EmailId));
			
			UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(nameValuePairs);

			JSONParser jParser = new JSONParser();
			String jsonStr = jParser.getJSONFromUrl(URL,requestEntity);

			JSONObject json = new JSONObject(jsonStr);

			data = json.getString("msg");
			
			
		} catch (Exception e) {
			data = appMessage.getErrorMessage();
			e.printStackTrace();
		} */

		return data;
	} 
}
