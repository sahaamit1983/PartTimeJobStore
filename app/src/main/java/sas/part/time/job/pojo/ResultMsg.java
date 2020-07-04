package sas.part.time.job.pojo;

public class ResultMsg {

	private boolean Result;
	private String 	Message;
	
	public ResultMsg() {
		Result 	= false;
		Message = new String("");
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
}
