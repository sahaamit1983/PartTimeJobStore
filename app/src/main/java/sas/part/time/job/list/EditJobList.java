package sas.part.time.job.list;

import java.util.ArrayList;

import sas.part.time.job.R;
import sas.part.time.job.pojo.JobData;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditJobList extends BaseAdapter {

	private ArrayList<JobData> mData;
	private LayoutInflater mInflater;
	private PartTimeUtils appConfig = PartTimeUtils.getSingleInstance();

	public EditJobList(Context context) {
		mInflater = ((Activity) context).getLayoutInflater();
		mData = new ArrayList<JobData>();
	}
	
	public void setList(ArrayList<JobData> data) {
		mData = new ArrayList<JobData>(data);
	}
	
	public void NotifyDatasetChanged() {
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0); 
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		ViewHolder holder = null;

		if(view == null) {
			view = mInflater.
					inflate(R.layout.inflate_edit_list_super_item, parent, false);
			holder = new ViewHolder();
			holder.JobType = (TextView)view.findViewById(R.id.inflate_job_type);
			holder.JobDetails = (TextView)view.findViewById(R.id.inflate_job_details);
			holder.Location = (TextView)view.findViewById(R.id.inflate_job_location);
			holder.Date = (TextView)view.findViewById(R.id.inflate_job_date);
			holder.BackGround = (LinearLayout)view.findViewById(R.id.inflate_job_layout);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag(); 
		}
		
		if( position%4 ==0) {
			holder.BackGround.setBackgroundResource(R.drawable.joblist_box_green);
		}
		else if( position%4 ==1) {
			holder.BackGround.setBackgroundResource(R.drawable.joblist_box_blue);
		}
		else if( position%4 ==2) {
			holder.BackGround.setBackgroundResource(R.drawable.joblist_box_pink);
		}
		else if( position%4 ==3) {
			holder.BackGround.setBackgroundResource(R.drawable.joblist_box_yellow);
		}

		holder.JobType.setText(mData.get(position).getJobType());
		holder.JobDetails.setText(mData.get(position).getJobDetails());
		holder.Location.setText(mData.get(position).getCountry());
		holder.Date.setText(appConfig.convertDateToString(mData.get(position).getPostedDate()));

		return view;
	}

	private class ViewHolder {
		
		public TextView JobType;
		public TextView JobDetails;
		public TextView Location;
		public TextView	Date;
		public LinearLayout	BackGround;
	}
}
