package sas.part.time.job.activity;

import java.util.ArrayList;


import sas.part.time.job.R;
import sas.part.time.job.dialog.DialogCity;
import sas.part.time.job.dialog.DialogCountry;
import sas.part.time.job.dialog.DialogState;
import sas.part.time.job.dialog.UserAlertDialog;
import sas.part.time.job.info.GPSTracker;
import sas.part.time.job.pojo.Country;
import sas.part.time.job.pojo.JobData;
import sas.part.time.job.pojo.ResultMsg;
import sas.part.time.job.pojo.Zone;
import sas.part.time.job.server.JobEditServer;
import sas.part.time.job.userInterface.IJobSubmit;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class EditJob extends Activity implements IJobSubmit {

	private Button 				Submit, Back;
	private TextView			CurrencyCode, Title;
	private EditText 			JobType;
	private EditText 			JobDescription;
	private EditText 			Amount;
	private Button 				City;
	private Button 				State;
	private Button 				Country;
	private EditText 			Pin;
	private EditText 			Address;
	private	ScrollView 			SC;
	private PartTimeMessage 	appMessage = PartTimeMessage.getSingleInstance();
	private GPSTracker 			gps;
	private ArrayList<Country>	CountryList = new ArrayList<>();
	private String[]			CountryNames = new String[]{};
	private String[]			ZoneNames = new String[]{};
	private ArrayList<Zone>		ZoneList = new ArrayList<>();
	private ArrayList<String>	CityList = new ArrayList<>();
	private PartTimeUtils 		appConfig = PartTimeUtils.getSingleInstance();
	private JobData 			data;
	private AdView 				mAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_job);

		initView();
		listener();

		SC.setVerticalScrollBarEnabled(false);
		CountryList = appConfig.getCountryData(this);
		CountryNames = new String[CountryList.size()];
		for(int i=0; i<CountryList.size();i++) {
			CountryNames[i] = CountryList.get(i).getName();
		}

		Title.setText(getString(R.string.edit_your_job)); 
		data = (JobData)getIntent().getSerializableExtra("Details");
		if(data != null) {
			JobType.setText(data.getJobType());
			JobDescription.setText(data.getJobDetails());
			Amount.setText(data.getAmount());
			CurrencyCode.setText(data.getCurrency());
			City.setText(data.getCity());
			State.setText(data.getState());
			Country.setText(data.getCountry());
			Pin.setText(data.getPin());
			Address.setText(data.getAddress());
		}
	}

	private void initView() {

		Submit 			= (Button)findViewById(R.id.submit_job);
		Back 			= (Button)findViewById(R.id.submit_back);
		JobType			= (EditText)findViewById(R.id.submit_job_type);
		JobDescription	= (EditText)findViewById(R.id.submit_job_description);
		Amount			= (EditText)findViewById(R.id.submit_amount);
		CurrencyCode	= (TextView)findViewById(R.id.submit_currency);
		Title			= (TextView)findViewById(R.id.submit_title);
		City			= (Button)findViewById(R.id.submit_city);
		State			= (Button)findViewById(R.id.submit_state);
		Country			= (Button)findViewById(R.id.submit_country);
		Pin				= (EditText)findViewById(R.id.submit_pincode);
		Address			= (EditText)findViewById(R.id.submit_address);
		SC 				= (ScrollView)findViewById(R.id.submit_scroll);
		mAdView 		= (AdView) findViewById(R.id.submit_adView);
		mAdView.loadAd(PartTimeUtils.getAdRequest());
	}

	private void listener() {
		Submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				gps = new GPSTracker(EditJob.this);
				if (gps.canGetLocation()) {

					attemptSubmit();
				} else {
					gps.showSettingsAlert();
				}
			}
		});
		Country.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				setCountry();
			}
		});
		State.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				setState();
			}
		});
		City.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				setCity();
			}
		});
		Back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});

	}

	private void attemptSubmit() {

		JobType.setError(null);
		JobDescription.setError(null);
		Amount.setError(null);
		City.setError(null);
		State.setError(null);
		Country.setError(null);
		Pin.setError(null);

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(Address.getText().toString().trim())) {
			Address.setError(getString(R.string.error_field_required));
			focusView = Pin;
			cancel = true;
		}

		if (TextUtils.isEmpty(Pin.getText().toString().trim())) {
			Pin.setError(getString(R.string.error_field_required));
			focusView = Pin;
			cancel = true;
		}
		if (TextUtils.isEmpty(Country.getText().toString().trim())) {
			Country.setError(getString(R.string.error_field_required));
			focusView = Country;
			cancel = true;
		}
		if (State.getVisibility() == View.VISIBLE && TextUtils.isEmpty(State.getText().toString().trim())) {
			State.setError(getString(R.string.error_field_required));
			focusView = State;
			cancel = true;
		}
		if (City.getVisibility() == View.VISIBLE &&TextUtils.isEmpty(City.getText().toString().trim())) {
			City.setError(getString(R.string.error_field_required));
			focusView = City;
			cancel = true;
		}

		if (TextUtils.isEmpty(Amount.getText().toString().trim())) {
			Amount.setError(getString(R.string.error_field_required));
			focusView = Amount;
			cancel = true;
		}
		if (TextUtils.isEmpty(JobDescription.getText().toString().trim())) {
			JobDescription.setError(getString(R.string.error_field_required));
			focusView = JobDescription;
			cancel = true;
		}
		if (TextUtils.isEmpty(JobType.getText().toString().trim())) {
			JobType.setError(getString(R.string.error_field_required));
			focusView = JobType;
			cancel = true;
		}

		if(!PartTimeUtils.getSingleInstance().isNetworkOnline(getApplicationContext())) {
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			if(focusView!=null) {
				focusView.requestFocus();
			} else {
				UserAlertDialog dialog = new UserAlertDialog(EditJob.this);
				dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
						appMessage.getNetworkFailure());
			}
		} else {

			String mJobType			= JobType.getText().toString().trim();
			String mJobDescription 	= JobDescription.getText().toString().trim();
			String mAmount 			= Amount.getText().toString().trim();
			String mCurrency		= CurrencyCode.getText().toString().trim();
			String mCity			= City.getText().toString().trim();
			String mState			= State.getText().toString().trim();
			String mCountry 		= Country.getText().toString().trim();
			String mPin 			= Pin.getText().toString().trim();
			String mAddress 		= Address.getText().toString().trim();
			String mLatitude		= String.valueOf(gps.getLatitude());
			String mLongitude		= String.valueOf(gps.getLongitude());

			gps.stopUsingGPS();

			JobEditServer server = new JobEditServer(EditJob.this, EditJob.this,
					data.getJobId(),mJobType,mJobDescription,mAmount,mCurrency, 
					mCity,mState,mCountry,mPin,mAddress,mLatitude,mLongitude);
			server.execute();
		}
	}

	@Override
	public void onJobSubmit(ResultMsg result) {

		if(result.isResult()) {

			Toast.makeText(EditJob.this, appMessage.getJobPosted(), Toast.LENGTH_LONG).show();
			Intent i = new Intent();
			setResult(RESULT_OK, i);
			finish();

		} else {

			UserAlertDialog dialog = new UserAlertDialog(EditJob.this);
			dialog.showDialogOk(getString(android.R.string.dialog_alert_title), result.getMessage());
		}
	}

	/*private void setCountry() {
		AlertDialog.Builder builder = new AlertDialog.Builder(EditJob.this);
		builder.setTitle("Select your country");
		builder.setItems(CountryNames, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Country.setText(CountryList.get(which).getName());
				CurrencyCode.setText(CountryList.get(which).getCurrency());

				ZoneList = appConfig.getStateData(EditJob.this, 
						CountryList.get(which).getId());

				if(ZoneList.size()>0){

					ZoneNames = new String[ZoneList.size()];
					for(int i=0; i<ZoneList.size();i++) {
						ZoneNames[i] = ZoneList.get(i).getName();
					}
					State.setEnabled(true);
					City.setEnabled(false);
					State.setText("");
					City.setText("");
					Pin.setText("");
					Address.setText("");

				} else {

					State.setVisibility(View.GONE);
					City.setVisibility(View.GONE);
				}
			}
		});
		builder.show();
	}*/

	private void setCountry() {

		DialogCountry dialog = new DialogCountry(EditJob.this, CountryList, new DialogCountry.OnCountry() {

			@Override
			public void getCountry(Country country) {
				Country.setText(country.getName());
				CurrencyCode.setText(country.getCurrency());

				ZoneList = appConfig.getStateData(EditJob.this, 
						country.getId());

				if(ZoneList.size()>0){

					ZoneNames = new String[ZoneList.size()];
					for(int i=0; i<ZoneList.size();i++) {
						ZoneNames[i] = ZoneList.get(i).getName();
					}
					State.setEnabled(true);
					City.setEnabled(false);
					State.setText("");
					City.setText("");
					Pin.setText("");
					Address.setText("");
				} else {

					State.setVisibility(View.GONE);
					City.setVisibility(View.GONE);
				}
			}
		});
		dialog.show();
	}

	private void setState() {
		/*AlertDialog.Builder builder = new AlertDialog.Builder(EditJob.this);
		builder.setTitle("Select your state");
		builder.setItems(ZoneNames, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				State.setText(ZoneNames[which]);

				CityList = appConfig.getCityData(EditJob.this, 
						ZoneList.get(which).getId());

				if(CityList.size()>0){
					City.setEnabled(true);
					City.setText("");
					Pin.setText("");
					Address.setText("");
				}  else {

					City.setVisibility(View.GONE);
				}
			}
		});
		builder.show();*/
		DialogState dialog = new DialogState(EditJob.this, ZoneList, new DialogState.OnZone() {

			@Override
			public void getZone(Zone zone) {
				State.setText(zone.getName());

				CityList = appConfig.getCityData(EditJob.this, zone.getId());

				if(CityList.size()>0){
					City.setEnabled(true);
					City.setText("");
				}  else {
					City.setVisibility(View.GONE);
				}
			}
		});
		dialog.show();
	}

	private void setCity() {
		/*AlertDialog.Builder builder = new AlertDialog.Builder(EditJob.this);
		builder.setTitle("Select your city");
		builder.setItems(CityList.toArray(new String[]{}), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				City.setText(CityList.get(which));
				Pin.setText("");
			}
		});
		builder.show();*/
		DialogCity dialog = new DialogCity(EditJob.this, CityList, new DialogCity.OnCity() {

			@Override
			public void getCity(String city) {
				City.setText(city);
			}
		});
		dialog.show();
	}
	
	@Override
	public void onPause() {
		if (mAdView != null) {
			mAdView.pause();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdView != null) {
			mAdView.resume();
		}
	}

	@Override
	public void onDestroy() {
		if (mAdView != null) {
			mAdView.destroy();
		}
		super.onDestroy();
	}
}
