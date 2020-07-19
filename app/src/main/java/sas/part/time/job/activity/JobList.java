package sas.part.time.job.activity;

import java.util.ArrayList;

import sas.part.time.job.R;
import sas.part.time.job.dialog.UserAlertDialog;
import sas.part.time.job.list.JobSearchList;
import sas.part.time.job.pojo.JobData;
import sas.part.time.job.pojo.JobDataList;
import sas.part.time.job.server.JobListServer;
import sas.part.time.job.userInterface.IJobList;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class JobList extends Activity implements IJobList {

	private ListView JobListView;
	private TextView NoJob;
	private Button Back, Map;
	private PartTimeUtils appConfig = PartTimeUtils.getSingleInstance();
	private PartTimeMessage 	appMessage = PartTimeMessage.getSingleInstance();
	private String Subscription = "";
	private ArrayList<JobData> List = new ArrayList<JobData>();
	//private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_list);

		initView();
		listener();

		String mCountry 	= getIntent().getStringExtra("Country");
		String mState 		= getIntent().getStringExtra("State");
		String mCity 		= getIntent().getStringExtra("City");
		String mPin 		= getIntent().getStringExtra("Pin");

		Subscription = String.valueOf(appConfig.getUnSubscribed());
		
		if(appConfig.isNetworkOnline(this)) {
			JobListServer server = new JobListServer(JobList.this, JobList.this, 
					mCity, mState, mCountry, mPin, appConfig.getActiveJob());

			server.execute();

		} else {
			UserAlertDialog dialog = new UserAlertDialog(JobList.this);
			dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
					appMessage.getNetworkFailure());
		}
	}

	private void initView() {
		JobListView = (ListView)findViewById(R.id.search_job_list_view);
		NoJob 		= (TextView)findViewById(R.id.search_job_list_message);
		Back		= (Button)findViewById(R.id.search_job_list_back);
		Map			= (Button)findViewById(R.id.search_job_list_map);
		/*mAdView 	= (AdView) this.findViewById(R.id.search_job_list_adView);
		mAdView.loadAd(PartTimeUtils.getAdRequest());*/
	}
	private void listener() {

		JobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {

				//if(Subscription.equals(String.valueOf(appConfig.getSubscribed()))) {

					Intent intent = new Intent(JobList.this, JobDetails.class);
					intent.putExtra("Details", List.get(paramInt)); 
					intent.putExtra("Subscription", Subscription); 
					startActivity(intent);
				/*} else {

					UserAlertDialog dialog = new UserAlertDialog(JobList.this);
					dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
							"You are not a subscribed user. Please purchase the subscription.");
				}*/
			}
		});
		Back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		Map.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(JobList.this, MapViewActivity.class);
				intent.putExtra("Map", List);
				intent.putExtra("isDetailsvisible", false);
				intent.putExtra("Subscription", Subscription); 
				startActivity(intent);
			}
		});
	}

	@Override
	public void onJobList(JobDataList data) {

		if(data.isResult()) {

			if(data.getJobList().size()>0) {

				NoJob.setVisibility(View.GONE);
				JobListView.setVisibility(View.VISIBLE);

				List = data.getJobList();
				//Collections.sort(List, appConfig.new JobDataSortByDate());
				JobSearchList baseAdapter = new JobSearchList(JobList.this, List);
				JobListView.setAdapter(baseAdapter);

				JobListView.setVerticalScrollBarEnabled(false);
				Subscription = data.getSubscription();

				//if(Subscription.equals(String.valueOf(appConfig.getSubscribed()))) {
					Map.setVisibility(View.VISIBLE);
				//}

			} else{
				NoJob.setVisibility(View.VISIBLE);
				JobListView.setVisibility(View.GONE);
			}
		} else if(data.isException() && !data.isResult()){

			UserAlertDialog dialog = new UserAlertDialog(JobList.this);
			dialog.showDialogClose(getString(android.R.string.dialog_alert_title), data.getMessage());

		} else if(!data.isException() && !data.isResult()){

			NoJob.setVisibility(View.VISIBLE);
			JobListView.setVisibility(View.GONE);
		}
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
