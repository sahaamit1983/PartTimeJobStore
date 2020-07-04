package sas.part.time.job.pojo;

import java.util.Date;

public class SubscriptionMsg {

	private boolean Result;
	private String 	Message;
	private Date 	LastDate;
	private Date 	SystemDate;
	private boolean Exception;
	
	public SubscriptionMsg() {
		Result 		= false;
		Exception 	= false;
		Message 	= new String("");
		LastDate 	= new Date();
		SystemDate 	= new Date();
	}
	public boolean isResult() {
		return Result;
	}
	public void setResult(boolean result) {
		Result = result;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public Date getLastDate() {
		return LastDate;
	}
	public void setLastDate(Date lastDate) {
		LastDate = lastDate;
	}
	public boolean isException() {
		return Exception;
	}
	public void setException(boolean exception) {
		Exception = exception;
	}
	public Date getSystemDate() {
		return SystemDate;
	}
	public void setSystemDate(Date systemDate) {
		SystemDate = systemDate;
	}
}
