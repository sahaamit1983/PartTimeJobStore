package sas.part.time.job.userPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AutoDeletePref {

	private String AutoDeletePref 	= "AutoDeletePref";
	private String AutoDelete 		= "AutoDelete";
	
	public void setAutoDelete(Context ctx, Long autoDelete){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(AutoDeletePref, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putLong(AutoDelete, autoDelete); 
		e.commit();
	}
	
	public Long getAutoDelete(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(AutoDeletePref, Context.MODE_PRIVATE);
		return sharedPrefs.getLong(AutoDelete, 0L); 
	}
}
