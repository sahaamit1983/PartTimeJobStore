package sas.part.time.job.activity;

import java.util.ArrayList;

import sas.part.time.job.R;
import sas.part.time.job.dialog.UserAlertDialog;
import sas.part.time.job.list.EditJobList;
import sas.part.time.job.list.ListViewSwipeGesture;
import sas.part.time.job.pojo.JobData;
import sas.part.time.job.pojo.JobDataList;
import sas.part.time.job.pojo.ResultMsg;
import sas.part.time.job.server.DeleteJobServer;
import sas.part.time.job.server.HistoryServer;
import sas.part.time.job.userInterface.IDelete;
import sas.part.time.job.userInterface.IJobList;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditList extends Activity implements IJobList, IDelete {

	private ListView JobListView;
	private Button Back;
	private PartTimeUtils 		appConfig = PartTimeUtils.getSingleInstance();
	private PartTimeMessage 	appMessage = PartTimeMessage.getSingleInstance();
	private TextView NoJob, Title;
	private ArrayList<JobData> List = new ArrayList<JobData>();
	private EditJobList baseAdapter;
	private final int REQUEST_CODE = 103;
	//private AdView 				mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		initView();
		listener();

		if(appConfig.isNetworkOnline(this)) {
			HistoryServer server = new HistoryServer(EditList.this, EditList.this,
					appConfig.getActiveJob());

			server.execute();
		} else {
			UserAlertDialog dialog = new UserAlertDialog(this);
			dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
					appMessage.getNetworkFailure());
		}
		Title.setText(getString(R.string.edit_job_list)); 

		baseAdapter = new EditJobList(EditList.this);
		JobListView.setAdapter(baseAdapter);

		ListViewSwipeGesture touchListener = new ListViewSwipeGesture(JobListView, swipeListener, EditList.this);
		JobListView.setOnTouchListener(touchListener);
	}

	private void initView() {

		JobListView = (ListView)findViewById(R.id.history_job_list_view);
		Back		= (Button)findViewById(R.id.history_back);
		NoJob 		= (TextView)findViewById(R.id.history_message);
		Title		= (TextView)findViewById(R.id.history_title);
		/*mAdView 	= (AdView) findViewById(R.id.history_adView);
		mAdView.loadAd(PartTimeUtils.getAdRequest());*/
	}
	private void listener() {

		Back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {

		@Override
		public void DeleteTaskListView(int position) {

			if(appConfig.isNetworkOnline(EditList.this)) {
				DeleteJobServer server = new DeleteJobServer(EditList.this,EditList.this,
						List.get(position).getJobId(), position);
				server.execute();
			} else {
				UserAlertDialog dialog = new UserAlertDialog(EditList.this);
				dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
						appMessage.getNetworkFailure());
			}
		}

		@Override
		public void OnClickListView(int position) {
			Intent intent = new Intent(EditList.this, EditJob.class);
			intent.putExtra("Details", List.get(position));
			startActivityForResult(intent, REQUEST_CODE);
		}
	};

	@Override
	public void onDelete(ResultMsg data, int position) {

		if(data.isResult()) {
			List.remove(position);
			baseAdapter.setList(List);
			baseAdapter.NotifyDatasetChanged();
		} 
		Toast.makeText(EditList.this, data.getMessage(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onJobList(JobDataList data) {

		if(data.isResult()) {

			if(data.getJobList().size()>0) {

				NoJob.setVisibility(View.GONE);
				JobListView.setVisibility(View.VISIBLE);

				List = data.getJobList();
				//Collections.sort(List, appConfig.new JobDataSortByDate());
				baseAdapter.setList(List);
				baseAdapter.NotifyDatasetChanged();

				JobListView.setVerticalScrollBarEnabled(false);
			} else{
				NoJob.setVisibility(View.VISIBLE);
				JobListView.setVisibility(View.GONE);
				NoJob.setText(getString(R.string.no_job));
			}
		} else if(data.isException() && !data.isResult()){

			UserAlertDialog dialog = new UserAlertDialog(EditList.this);
			dialog.showDialogClose(getString(android.R.string.dialog_alert_title), 
					data.getMessage());

		} else if(!data.isException() && !data.isResult()){

			NoJob.setVisibility(View.VISIBLE);
			JobListView.setVisibility(View.GONE);
			NoJob.setText(getString(R.string.no_job));
		}
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		if(requestCode == REQUEST_CODE && resultCode == RESULT_OK
				&& resultCode!= RESULT_CANCELED) {
			
			if(appConfig.isNetworkOnline(this)) {
				HistoryServer server = new HistoryServer(EditList.this, EditList.this,
						appConfig.getActiveJob());

				server.execute();
			} else {
				UserAlertDialog dialog = new UserAlertDialog(this);
				dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
						appMessage.getNetworkFailure());
			}
		}
	}

	@Override
	protected void onDestroy() {
		/*if (mAdView != null) {
			mAdView.destroy();
		}*/
		super.onDestroy();

		if(List!=null) {
			List.clear();
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
	}*/
}
