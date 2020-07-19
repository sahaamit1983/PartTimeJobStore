package sas.part.time.job.activity;

import java.util.ArrayList;

import sas.part.time.job.R;
import sas.part.time.job.dialog.UserAlertDialog;
import sas.part.time.job.list.JobSearchList;
import sas.part.time.job.pojo.JobData;
import sas.part.time.job.pojo.JobDataList;
import sas.part.time.job.server.HistoryServer;
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

public class History extends Activity implements IJobList {

	private ListView JobListView;
	private Button Back;
	private TextView NoJob;
	private PartTimeUtils appConfig = PartTimeUtils.getSingleInstance();
	private PartTimeMessage 	appMessage = PartTimeMessage.getSingleInstance();
	private ArrayList<JobData> List = new ArrayList<JobData>();
	//private AdView mAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		initView();
		listener();
		
		if(appConfig.isNetworkOnline(History.this)) {
			HistoryServer server = new HistoryServer(History.this, History.this,
					appConfig.getInActiveJob());

			server.execute();

		} else {
			UserAlertDialog dialog = new UserAlertDialog(History.this);
			dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
					appMessage.getNetworkFailure());
		}
	}

	private void initView() {

		JobListView = (ListView)findViewById(R.id.history_job_list_view);
		Back		= (Button)findViewById(R.id.history_back);
		NoJob 		= (TextView)findViewById(R.id.history_message);
		/*mAdView 	= (AdView) this.findViewById(R.id.history_adView);
		mAdView.loadAd(PartTimeUtils.getAdRequest());*/
	}
	private void listener() {

		JobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> paramAdapterView,
					View paramView, int paramInt, long paramLong) {

				Intent intent = new Intent(History.this, JobDetails.class);
				intent.putExtra("Details", List.get(paramInt)); 
				intent.putExtra("Subscription", String.valueOf(appConfig.getUnSubscribed())); 
				startActivity(intent);
			}
		});
		Back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
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
				JobSearchList baseAdapter = new JobSearchList(History.this, List);
				JobListView.setAdapter(baseAdapter);

				JobListView.setVerticalScrollBarEnabled(false);
			} else{
				NoJob.setVisibility(View.VISIBLE);
				JobListView.setVisibility(View.GONE);
			}
		} else if(data.isException() && !data.isResult()){

			UserAlertDialog dialog = new UserAlertDialog(History.this);
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
