package sas.part.time.job.pojo;

import java.util.ArrayList;

public class ProfileData {

	private boolean Result;
	private String 	Message;
	private String 	EmailId;
	private String 	FirstName;
	private String 	LastName;
	private String 	DOB;
	private String 	Gender;
	private String 	PhoneNumber;
	private String 	ImageUrl;
	private String 	Address;
	private String 	City;
	private String 	State;
	private String 	Country;
	private String 	Pin;
	private ArrayList<Zone>		ZoneList;
	private ArrayList<String>	CityList;
	
	public ProfileData() {
		
		Result 		= false;
		Message 	= new String("");
		EmailId 	= new String();
		FirstName	= new String();
		LastName	= new String();
		DOB			= new String();
		Gender		= new String();
		PhoneNumber	= new String();
		ImageUrl	= new String();
		Address		= new String();
		City		= new String();
		State		= new String();
		Country		= new String();
		Pin			= new String();
		ZoneList 	= new ArrayList<Zone>();
		CityList 	= new ArrayList<String>();
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
	public String getEmailId() {
		return EmailId;
	}
	public void setEmailId(String emailId) {
		EmailId = emailId;
	}
	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getDOB() {
		return DOB;
	}
	public void setDOB(String dOB) {
		DOB = dOB;
	}
	public String getGender() {
		return Gender;
	}
	public void setGender(String gender) {
		Gender = gender;
	}
	public String getPhoneNumber() {
		return PhoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
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
	public ArrayList<Zone> getZoneList() {
		return ZoneList;
	}
	public void setZoneList(ArrayList<Zone> zoneList) {
		ZoneList = zoneList;
	}
	public ArrayList<String> getCityList() {
		return CityList;
	}
	public void setCityList(ArrayList<String> cityList) {
		CityList = cityList;
	}
}
