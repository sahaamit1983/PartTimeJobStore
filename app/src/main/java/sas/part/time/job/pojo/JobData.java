package sas.part.time.job.pojo;

import java.io.Serializable;
import java.util.Date;

public class JobData implements Serializable {

	/**
	 * For passing the object from one activity to another activity
	 */
	private static final long serialVersionUID = 1L;
	
	private String 	JobId;
	private String 	JobType;
	private String 	JobDetails;
	private String 	PhoneNumber;
	private String 	Amount;
	private String 	Currency;
	private String 	Address;
	private String 	City;
	private String 	State;
	private String 	Country;
	private String 	Pin;
	private Date 	PostedDate;
	private String 	Latitude;
	private String 	Longitude;
	
	public JobData() {
		JobId 		= new String("");
		JobType 	= new String("");
		JobDetails 	= new String("");
		PhoneNumber = new String("");
		Amount 		= new String("");
		Currency 	= new String("");
		Address 	= new String("");
		City		= new String("");
		State 		= new String("");
		Country 	= new String("");
		Pin 		= new String("");
		PostedDate	= new Date();
		Latitude	= new String("");
		Longitude	= new String("");
	}
	
	public String getJobId() {
		return JobId;
	}

	public void setJobId(String jobId) {
		JobId = jobId;
	}

	public String getJobType() {
		return JobType;
	}

	public void setJobType(String jobType) {
		JobType = jobType;
	}

	public String getJobDetails() {
		return JobDetails;
	}

	public void setJobDetails(String jobDetails) {
		JobDetails = jobDetails;
	}

	public String getPhoneNumber() {
		return PhoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getPin() {
		return Pin;
	}

	public void setPin(String pin) {
		Pin = pin;
	}

	public Date getPostedDate() {
		return PostedDate;
	}

	public void setPostedDate(Date postedDate) {
		PostedDate = postedDate;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
}
