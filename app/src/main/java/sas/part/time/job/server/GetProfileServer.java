package sas.part.time.job.server;

import java.util.ArrayList;

import sas.part.time.job.dialog.SasLogoDialog;
import sas.part.time.job.pojo.Country;
import sas.part.time.job.pojo.ProfileData;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.userInterface.IProfileData;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

public class GetProfileServer extends AsyncTask<Void, Void, ProfileData> {	

	private SasLogoDialog 		cusomeDialog;
	private Activity 			mActivity;
	private IProfileData 		listener;
	private PartTimeUtils 		appConfig 	= PartTimeUtils.getSingleInstance();
	private PartTimeMessage 	appMessage 	= PartTimeMessage.getSingleInstance();
	private ArrayList<Country>	CountryList = new ArrayList<Country>();

	public GetProfileServer(Activity ac, IProfileData lis, 
			ArrayList<Country> countryList) {
		mActivity 	= ac;
		listener 	= lis;
		CountryList.addAll(countryList); 
	}

	@Override
	protected void onPostExecute(ProfileData data) {
		super.onPostExecute(data);
		cusomeDialog.dismiss();
		listener.onProfileData(data);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		cusomeDialog = new SasLogoDialog(mActivity);
		cusomeDialog.show();
	}

	@Override
	protected ProfileData doInBackground(Void... arg0) {

		ProfileData data = new ProfileData();

		try { 

			UserInfo info = new UserInfo();
			data.setEmailId(info.getEmail(mActivity));
			data.setFirstName(info.getFirstName(mActivity));
			data.setLastName(info.getLastName(mActivity));
			data.setDOB(info.getDateOfBirth(mActivity));
			data.setGender(info.getGender(mActivity));
			data.setPhoneNumber(info.getPhoneNumber(mActivity));
			data.setImageUrl(info.getImage(mActivity));
			data.setAddress(info.getAddress(mActivity));
			data.setCountry(info.getCountry(mActivity));
			data.setState(info.getState(mActivity));
			data.setCity(info.getCity(mActivity)); 
			data.setPin(info.getPin(mActivity));


			if(!TextUtils.isEmpty(data.getCountry())) {
				if(!data.getCountry().equals("null")) {
					for(int i=0;i<CountryList.size();i++) {
						if(data.getCountry().trim().equalsIgnoreCase(
								CountryList.get(i).getName())) {

							data.setZoneList(appConfig.getStateData(mActivity, 
									CountryList.get(i).getId()));
							break;
						}
					}
				}
			}
			if(!TextUtils.isEmpty(data.getState())) {
				if(!data.getState().equals("null")) {
					for(int i=0;i<data.getZoneList().size();i++) {
						if(data.getState().trim().equalsIgnoreCase(
								data.getZoneList().get(i).getName())) {

							data.setCityList(appConfig.getCityData(mActivity, 
									data.getZoneList().get(i).getId()));
							break;
						}
					}

				} 
			}
			data.setResult(true);
			
		} catch (Exception e) {
			data.setResult(false);
			data.setMessage(appMessage.getErrorMessage());
			e.printStackTrace();
		} 

		return data;
	} 
}
