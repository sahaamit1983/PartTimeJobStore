package sas.part.time.job.server;

import org.json.JSONArray;
import org.json.JSONObject;

import sas.part.time.job.R;
import sas.part.time.job.dialog.SasLogoDialog;
import sas.part.time.job.pojo.LoginData;
import sas.part.time.job.userInterface.ILogin;
import sas.part.time.job.utils.PartTimeMessage;
import android.app.Activity;
import android.os.AsyncTask;

public class LoginServer extends AsyncTask<Void, Void, LoginData> {	

	private SasLogoDialog 		cusomeDialog;
	private Activity 			mActivity;
	private String 				UserName,Password;
	private ILogin 				listener;
	private PartTimeMessage 	appMessage 	= PartTimeMessage.getSingleInstance();

	public LoginServer(Activity ac, ILogin lis, String username, String password) {
		mActivity 	= ac;
		UserName   	= username;
		Password 	= password;
		listener 	= lis;
	}

	@Override
	protected void onPostExecute(LoginData data) {
		super.onPostExecute(data);
		cusomeDialog.dismiss();
		listener.onLogin(data);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		cusomeDialog = new SasLogoDialog(mActivity);
		cusomeDialog.show();
	}

	@Override
	protected LoginData doInBackground(Void... arg0) {

		String URL = mActivity.getResources().getString(R.string.login_url);

		LoginData data = new LoginData();

		/*try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email_id", UserName));
			nameValuePairs.add(new BasicNameValuePair("password", Password));

			UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(nameValuePairs);

			JSONParser jParser = new JSONParser();
			String jsonStr = jParser.getJSONFromUrl(URL,requestEntity);

			JSONObject json = new JSONObject(jsonStr);

			data.setResult(json.getBoolean("result")); 
			data.setMessage(json.getString("msg"));
			
			if(data.isResult()) {
				
				data.setEmailVerification(json.getString("email_verification"));
				data.setEmailId(json.getString("email_id"));
				JSONArray array = json.getJSONArray("data");
				JSONObject jsonObj = array.getJSONObject(0);
				
				data.setUserId(jsonObj.getString("user_id"));
				data.setFirstName(jsonObj.getString("first_name"));
				data.setLastName(jsonObj.getString("last_name"));
				data.setDateOfBirth(jsonObj.getString("date_of_birth"));
				data.setGender(jsonObj.getString("gender"));
				data.setPhoneNumber(jsonObj.getString("phone_no"));
				data.setImage(jsonObj.getString("image"));
				data.setAddress(jsonObj.getString("address"));
				data.setCountry(jsonObj.getString("country"));
				data.setState(jsonObj.getString("state"));
				data.setCity(jsonObj.getString("city"));
				data.setPin(jsonObj.getString("pin_code"));
				data.setUserStatus(jsonObj.getString("user_status"));
			}
		} catch (Exception e) {
			data.setResult(false);
			data.setMessage(appMessage.getErrorMessage());
			e.printStackTrace();
		} */

		return data;
	} 
}
