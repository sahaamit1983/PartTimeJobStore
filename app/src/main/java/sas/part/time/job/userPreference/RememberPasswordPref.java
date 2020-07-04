package sas.part.time.job.userPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class RememberPasswordPref {

	private static String RememberPasswordPref 	= "RememberPasswordPref";
	private static String RememberPassword 		= "RememberPassword";
	private static String RememberUsername 		= "RememberUsername";

	public static void setUserName(Context ctx, String username){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(RememberPasswordPref, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(RememberUsername, username); 
		e.commit();
	}

	public static String getUserName(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(RememberPasswordPref, Context.MODE_PRIVATE);
		return sharedPrefs.getString(RememberUsername, ""); 
	}

	public static void setPassword(Context ctx, String password){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(RememberPasswordPref, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(RememberPassword, password); 
		e.commit();
	}

	public static String getPassword(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(RememberPasswordPref, Context.MODE_PRIVATE);
		return sharedPrefs.getString(RememberPassword, ""); 
	}
}
