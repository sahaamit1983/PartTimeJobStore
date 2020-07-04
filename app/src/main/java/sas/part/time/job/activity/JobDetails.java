package sas.part.time.job.activity;

import java.util.ArrayList;

import com.google.android.gms.ads.AdView;

import sas.part.time.job.R;
import sas.part.time.job.pojo.JobData;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JobDetails extends Activity {

	private Button Map, Back;
	private TextView Title, Description, Amount, Currency, Date, PhoneNumber, Address;
	private ArrayList<JobData> List = new ArrayList<JobData>();
	private JobData data;
	private PartTimeUtils appConfig = PartTimeUtils.getSingleInstance();
	private String Subscription = "";
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_details);

		initView();
		listener();
		data = (JobData)getIntent().getSerializableExtra("Details");
		List.add(data);
		
		Subscription = getIntent().getStringExtra("Subscription");
 
		/*if(Subscription.equals(String.valueOf(appConfig.getUnSubscribed()))){
			Map.setVisibility(View.GONE);
		} else {*/
			Map.setVisibility(View.VISIBLE);
		//}

		setValue(data);
	}

	private void initView() {

		Map 		= (Button)findViewById(R.id.job_details_map);
		Back 		= (Button)findViewById(R.id.job_details_back);
		Title		= (TextView)findViewById(R.id.job_details_title);
		Description	= (TextView)findViewById(R.id.job_details_description);
		Amount		= (TextView)findViewById(R.id.job_details_amount);
		Currency	= (TextView)findViewById(R.id.job_details_currency);
		Date		= (TextView)findViewById(R.id.job_details_date);
		PhoneNumber	= (TextView)findViewById(R.id.job_details_phoneno);
		Address		= (TextView)findViewById(R.id.job_details_address);
		mAdView 	= (AdView) this.findViewById(R.id.job_details_adView);
		mAdView.loadAd(PartTimeUtils.getAdRequest());
	}

	private void listener() {

		Map.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent i = new Intent(JobDetails.this, MapViewActivity.class);
				i.putExtra("Map", List);
				i.putExtra("isDetailsvisible", true);
				i.putExtra("Subscription", Subscription);
				startActivity(i);
			}
		});
		Back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});
	}

	private void setValue(JobData data) {

		Title.setText(data.getJobType());

		Date.setText(appConfig.convertDateToString(data.getPostedDate()));

		if(!TextUtils.isEmpty(data.getCurrency())) {
			Currency.setText(" "+data.getCurrency());
		}
		
		if(!TextUtils.isEmpty(data.getAmount())) {
			Amount.setText(" "+data.getAmount());
		}
		
		PhoneNumber.setText(getString(R.string.ph_number)+" : "+
				data.getPhoneNumber());

		Description.setText(data.getJobDetails());

		String address = "";

		if(!TextUtils.isEmpty(data.getAddress().trim())){ 
			address = data.getAddress().trim();
		}
		if(!TextUtils.isEmpty(data.getCity().trim())){ 
			address = address + ", "+ data.getCity().trim();
		}
		if(!TextUtils.isEmpty(data.getState().trim())){ 
			address = address + ", "+ data.getState().trim();
		}
		if(!TextUtils.isEmpty(data.getCountry().trim())){ 
			address = address + ", "+ data.getCountry().trim();
		}
		if(!TextUtils.isEmpty(data.getPin().trim())){ 
			address = address + ", "+ data.getPin().trim();
		}
		Address.setText(address);
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
