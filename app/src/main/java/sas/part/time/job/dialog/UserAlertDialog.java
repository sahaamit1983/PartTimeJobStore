package sas.part.time.job.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class UserAlertDialog {

	private Activity activity;

	public UserAlertDialog(Activity ac) {
		activity = ac;
	}

	public void showDialogOk(String title,String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton(activity.getString(android.R.string.ok),null);

		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void showDialogClose(String title,String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton(activity.getString(android.R.string.ok),new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				activity.finish();
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
