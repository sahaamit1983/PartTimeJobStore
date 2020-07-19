package sas.part.time.job.server;

import java.io.File;

import org.json.JSONObject;

import sas.part.time.job.R;
import sas.part.time.job.dialog.SasLogoDialog;
import sas.part.time.job.pojo.ResultMsg;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.userInterface.IRegistration;
import sas.part.time.job.utils.PartTimeMessage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

@SuppressLint("SimpleDateFormat")
public class EditProfileServer extends AsyncTask<Void, Void, ResultMsg> {

	private SasLogoDialog 			cusomeDialog;
	private Activity 				mActivity;
	private IRegistration 			CallBack;
	private String 					FName;
	private String 					LName;
	private String 					Dob;
	private String 					Gender;
	private String 					PhoneNo;
	private String 					EmailId;
	private String 					Password;
	private String 					CPassword;
	private String 					Address;
	private String 					City;
	private String 					State;
	private String 					Country;
	private String 					Pin;
	private String					PicPath;
	private File 					UserPic = null;
	private PartTimeMessage 		appMessage = PartTimeMessage.getSingleInstance();
	 
	
	public EditProfileServer(Activity ac, IRegistration callBack,String _fname, 
			String _lname, String _dob, String _gender, String _phoneNo, 
			String _emailId, String _password, String _cpassword, String _address, 
			String _city, String _state, String _country, String _pin, 
			String _picpath) {
		
		mActivity 	= ac;
		CallBack 	= callBack;
		FName		= _fname;
		LName		= _lname;
		Dob			= _dob;
		Gender		= _gender;
		PhoneNo		= _phoneNo;
		EmailId		= _emailId;
		Password	= _password;
		CPassword	= _cpassword;
		Address		= _address;
		City		= _city;
		State		= _state;
		Country		= _country;
		Pin			= _pin;
		PicPath		= _picpath;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		cusomeDialog = new SasLogoDialog(mActivity);
		cusomeDialog.show();
	}

	@Override
	protected void onPostExecute(ResultMsg data) {
		cusomeDialog.dismiss();
		CallBack.onRegistration(data);
	}

	@Override
	protected ResultMsg doInBackground(Void... params) {

		String URL = mActivity.getResources().getString(R.string.edit_profile_url);
		UserInfo info = new UserInfo();
		String mUserId = info.getUserId(mActivity);
		ResultMsg data = new ResultMsg();
		
		/*try {
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			entity.addPart("user_id", new StringBody(mUserId));
			entity.addPart("first_name", new StringBody(FName));
			entity.addPart("last_name", new StringBody(LName));
			entity.addPart("date_of_birth", new StringBody(Dob));
			entity.addPart("gender", new StringBody(Gender));
			entity.addPart("phone_no", new StringBody(PhoneNo));
			entity.addPart("email_id", new StringBody(EmailId));
			entity.addPart("address", new StringBody(Address));
			entity.addPart("city", new StringBody(City));
			entity.addPart("state", new StringBody(State));
			entity.addPart("country", new StringBody(Country));
			entity.addPart("pin_code", new StringBody(Pin));
			
			if(!TextUtils.isEmpty(Password)) {
				entity.addPart("password", new StringBody(Password));
			}
			if(!TextUtils.isEmpty(CPassword)) {
				entity.addPart("cpassword", new StringBody(CPassword));
			}
			
			if(!TextUtils.isEmpty(PicPath)) {
				UserPic = new File(PicPath);
				if(UserPic.exists()) {
					entity.addPart("image", new FileBody(UserPic));
				}
			}
			
			JSONParserFile jParser = new JSONParserFile();
			String jsonStr = jParser.getJSONFromUrl(URL,entity);
			
			JSONObject jsonObj = new JSONObject(jsonStr);
			
			data.setResult(jsonObj.getBoolean("result"));
			data.setMessage(jsonObj.getString("msg"));
			
		} catch (Exception e) {
			data.setResult(false);
			data.setMessage(appMessage.getErrorMessage());
			e.printStackTrace();
		}*/
		return data;
	}
}
