package sas.part.time.job.pojo;

import java.util.ArrayList;

public class JobDataList {

	private boolean Result;
	private boolean Exception;
	private String 	Message;
	private String 	Subscription;
	private ArrayList<JobData> JobList;
	
	public JobDataList() {
		Result 		= false;
		Exception	= false;
		Message 	= new String("");
		JobList 	= new ArrayList<JobData>();
	}

	public boolean isResult() {
		return Result;
	}

	public void setResult(boolean result) {
		Result = result;
	}
	
	public boolean isException() {
		return Exception;
	}

	public void setException(boolean exception) {
		Exception = exception;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getSubscription() {
		return Subscription;
	}

	public void setSubscription(String subscription) {
		Subscription = subscription;
	}

	public ArrayList<JobData> getJobList() {
		return JobList;
	}

	public void setJobList(ArrayList<JobData> jobList) {
		JobList = jobList;
	}
}
