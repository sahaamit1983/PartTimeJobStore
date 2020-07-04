package sas.part.time.job.utils;

public class PartTimeMessage {

	private static PartTimeMessage appConfig = null;

	private final String JobSubmitted    	= "Your job submited successfully";
	private final String ErrorMsg    		= "Some thing went wrong. Pleasetry after some time.";
	private final String NetworkFailure    	= "Network is not available";
	private final String AgeValidation    	= "Your age is below 14 years, you can't create this account";
	private final String InsertText    		= "Please insert some text";
	

	private PartTimeMessage() {}

	public static PartTimeMessage getSingleInstance() {

		if(appConfig == null) {
			appConfig = new PartTimeMessage();
		}
		return appConfig;
	}

	public String getJobPosted() {
		return JobSubmitted;
	}
	
	public String getAgeValidation() {
		return AgeValidation;
	}

	public String getNetworkFailure() {
		return NetworkFailure;
	}

	public String getInsertMessage() {
		return InsertText;
	}

	public String getErrorMessage() {
		return ErrorMsg;
	}

}