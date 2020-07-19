package sas.part.time.job.pojo;

public class User {

    private String 					FirstName;
    private String 					LastName;
    private String 					DOB;
    private String 					Gender;
    private String 					PhoneNo;
    private String 					Address;
    private String 					City;
    private String 					State;
    private String 					Country;
    private String 					Pin;
    private String 					Latitude;
    private String 					Longitude;
    private String                  imageReference;
    private String                  Key;

    public User(String FirstName, String LastName, String DOB, String gender, String phoneNo, String address, String city, String state,
                String country, String pin, String Latitude, String Longitude, String imageReference) {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.DOB = DOB;
        this.Gender = gender;
        this.PhoneNo = phoneNo;
        this.Address = address;
        this.City = city;
        this.State = state;
        this.Country = country;
        this.Pin = pin;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.imageReference = imageReference;
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

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
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

    public String getImageReference() {
        return imageReference;
    }

    public void setImageReference(String imageReference) {
        this.imageReference = imageReference;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
