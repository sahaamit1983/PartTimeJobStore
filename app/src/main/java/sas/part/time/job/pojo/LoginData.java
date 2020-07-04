package sas.part.time.job.pojo;

public class LoginData {

	private boolean Result;
	private String 	Message;
	private String 	EmailVerification;
	private String 	EmailId;
	private String 	UserId;
	private String 	FirstName;
	private String 	LastName;
	private String 	DateOfBirth;
	private String 	Gender;
	private String 	PhoneNumber;
	private String 	Image;
	private String 	Address;
	private String 	City;
	private String 	State;
	private String 	Country;
	private String 	Pin;
	private String 	UserStatus;
	
	public LoginData() {
		Result 				= false;
		Message 			= new String("");
		EmailVerification 	= new String("");
		EmailId 			= new String("");
		UserId 				= new String("");
		FirstName 			= new String("");
		LastName 			= new String("");
		DateOfBirth 		= new String("");
		Gender 				= new String("");
		PhoneNumber 		= new String("");
		Image 				= new String("");
		Address 			= new String("");
		City				= new String("");
		State 				= new String("");
		Country 			= new String("");
		Pin 				= new String("");
		UserStatus			= new String("");
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
	public String getEmailVerification() {
		return EmailVerification;
	}
	public void setEmailVerification(String emailVerification) {
		EmailVerification = emailVerification;
	}
	public String getEmailId() {
		return EmailId;
	}
	public void setEmailId(String emailId) {
		EmailId = emailId;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
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
	public String getDateOfBirth() {
		return DateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		DateOfBirth = dateOfBirth;
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
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
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
	public String getUserStatus() {
		return UserStatus;
	}

	public void setUserStatus(String userStatus) {
		UserStatus = userStatus;
	}
}
