package sas.part.time.job.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserInfo {

	private String UserInfoPreference 	= "UserInfoPreference";
	private String Email 				= "Email";
	private String UserId 				= "UserId";
	
	private String FirstName 			= "FirstName";
	private String LastName 			= "LastName";
	private String DateOfBirth 			= "DateOfBirth";
	private String Gender 				= "Gender";
	private String PhoneNumber 			= "PhoneNumber";
	private String Image 				= "Image";
	private String Address 				= "Address";
	private String City 				= "City";
	private String State 				= "State";
	private String Country 				= "Country";
	private String Pin 					= "Pin";
	
	
	public void setEmail(Context ctx, String email){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(Email, email); 
		e.commit();
	}
	
	public String getEmail(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Email, "");
	}
	
	public void setUserId(Context ctx, String userid){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(UserId, userid); 
		e.commit();
	}
	
	public String getUserId(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(UserId, "");
	}
	
	public void setFirstName(Context ctx, String nickName){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(FirstName, nickName); 
		e.commit();
	}
	
	public String getFirstName(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(FirstName, "");
	}
	
	public void setLastName(Context ctx, String nickName){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(LastName, nickName); 
		e.commit();
	}
	
	public String getLastName(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(LastName, "");
	}
	
	public void setDateOfBirth(Context ctx, String age){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(DateOfBirth, age); 
		e.commit();
	}
	
	public String getDateOfBirth(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(DateOfBirth, "");
	}
	
	public void setGender(Context ctx, String gender){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(Gender, gender); 
		e.commit();
	}
	
	public String getGender(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Gender, "");
	}
	
	public void setPhoneNumber(Context ctx, String unread){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(PhoneNumber, unread); 
		e.commit();
	}
	
	public String getPhoneNumber(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(PhoneNumber, "");
	}
	
	public void setImage(Context ctx, String pic){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(Image, pic); 
		e.commit();
	}
	
	public String getImage(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Image, "");
	}
	
	public void setAddress(Context ctx, String city){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(Address, city); 
		e.commit();
	}
	
	public String getAddress(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Address, "");
	}
	
	public void setCity(Context ctx, String city){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(City, city); 
		e.commit();
	}
	
	public String getCity(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(City, "");
	}
	
	public void setState(Context ctx, String state){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(State, state); 
		e.commit();
	}
	
	public String getState(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(State, "");
	}
	
	public void setCountry(Context ctx, String country){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(Country, country); 
		e.commit();
	}
	
	public String getCountry(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Country, "");
	}
	
	public void setPin(Context ctx, String country){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(Pin, country); 
		e.commit();
	}
	
	public String getPin(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(UserInfoPreference, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Pin, "");
	}
}
