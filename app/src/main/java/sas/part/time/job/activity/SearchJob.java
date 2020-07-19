package sas.part.time.job.activity;

import java.util.ArrayList;

import sas.part.time.job.R;
import sas.part.time.job.dialog.DialogCity;
import sas.part.time.job.dialog.DialogCountry;
import sas.part.time.job.dialog.DialogState;
import sas.part.time.job.pojo.Country;
import sas.part.time.job.pojo.Zone;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchJob extends Activity  {

	private Button Search, Country, State, City, Back;
	private EditText Pin;
	private ArrayList<Country>	CountryList = new ArrayList<Country>();
	private String[]			CountryNames = new String[]{};
	private String[]			ZoneNames = new String[]{};
	private ArrayList<Zone>		ZoneList = new ArrayList<Zone>();
	private ArrayList<String>	CityList = new ArrayList<String>();
	private PartTimeUtils 		appConfig = PartTimeUtils.getSingleInstance();
	private DialogCountry 		dialog = null;
	//private AdView 				mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_job);

		initView();
		listener();

		CountryList = appConfig.getCountryData(this);
		CountryNames = new String[CountryList.size()];
		for(int i=0; i<CountryList.size();i++) {
			CountryNames[i] = CountryList.get(i).getName();
		}
	}

	private void initView() {

		Search 	= (Button)findViewById(R.id.search_job_search);
		Country = (Button)findViewById(R.id.search_job_country);
		State 	= (Button)findViewById(R.id.search_job_state);
		City 	= (Button)findViewById(R.id.search_job_city);
		Back 	= (Button)findViewById(R.id.search_job_back);
		Pin 	= (EditText)findViewById(R.id.search_job_pincode);
		/*mAdView = (AdView) this.findViewById(R.id.search_job_adView);
		mAdView.loadAd(PartTimeUtils.getAdRequest());*/
	}

	private void listener() {

		Back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
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
		Search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String mCountry = Country.getText().toString().trim();
				if(!TextUtils.isEmpty(mCountry)){

					String mState = State.getText().toString().trim();

					if(!TextUtils.isEmpty(mState) || State.getVisibility() == View.GONE){

						String mCity = City.getText().toString().trim();
						if(!TextUtils.isEmpty(mCity) || City.getVisibility() == View.GONE){

							String mPin = Pin.getText().toString().trim();

							Intent intent = new Intent(SearchJob.this, JobList.class);
							intent.putExtra("Country", mCountry);
							intent.putExtra("State", mState);
							intent.putExtra("City", mCity);
							intent.putExtra("Pin", mPin);

							startActivity(intent);

						}  else {
							City.setError(getString(R.string.error_field_required));
						}
					} else {
						State.setError(getString(R.string.error_field_required));
					}
				} else {
					Country.setError(getString(R.string.error_field_required));
				}
			}
		});
	}

	private void setCountry() {
		/*AlertDialog.Builder builder = new AlertDialog.Builder(SearchJob.this);
		builder.setTitle("Select your country");
		builder.setItems(CountryNames, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Country.setError(null);
				Country.setText(CountryList.get(which).getName());

				ZoneList = appConfig.getStateData(SearchJob.this, 
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
				} else {
					State.setVisibility(View.GONE);
					City.setVisibility(View.GONE);
				}
			}
		});
		builder.show();*/
		DialogCountry dialog = new DialogCountry(SearchJob.this, CountryList, new DialogCountry.OnCountry() {

			@Override
			public void getCountry(Country country) {
				Country.setText(country.getName());
				
				ZoneList = appConfig.getStateData(SearchJob.this, country.getId());

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
				} else {

					State.setVisibility(View.GONE);
					City.setVisibility(View.GONE);
				}
			}
		});
		dialog.show();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
		if(dialog!=null) {
			
		}
		
	}

	private void setState() {
		/*AlertDialog.Builder builder = new AlertDialog.Builder(SearchJob.this);
		builder.setTitle("Select your state");
		builder.setItems(ZoneNames, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				State.setError(null);
				State.setText(ZoneNames[which]);

				CityList = appConfig.getCityData(SearchJob.this, 
						ZoneList.get(which).getId());

				if(CityList.size()>0){
					City.setEnabled(true);
					City.setText("");
				}  else {
					City.setVisibility(View.GONE);
				}

			}
		});
		builder.show();*/
		DialogState dialog = new DialogState(SearchJob.this, ZoneList, new DialogState.OnZone() {

			@Override
			public void getZone(Zone zone) {
				State.setText(zone.getName());

				CityList = appConfig.getCityData(SearchJob.this, zone.getId());

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
		/*AlertDialog.Builder builder = new AlertDialog.Builder(SearchJob.this);
		builder.setTitle("Select your city");
		builder.setItems(CityList.toArray(new String[]{}), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				City.setError(null);
				City.setText(CityList.get(which));
			}
		});
		builder.show();*/
		DialogCity dialog = new DialogCity(SearchJob.this, CityList, new DialogCity.OnCity() {

			@Override
			public void getCity(String city) {
				City.setText(city);
			}
		});
		dialog.show();
	}
	
	/*@Override
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
	}*/
}
