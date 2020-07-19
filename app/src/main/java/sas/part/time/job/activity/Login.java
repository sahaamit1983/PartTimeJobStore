package sas.part.time.job.activity;

import sas.part.time.job.R;
import sas.part.time.job.dialog.UserAlertDialog;
import sas.part.time.job.passwordProtection.AsteriskPasswordTransformationMethod;
import sas.part.time.job.pojo.LoginData;
import sas.part.time.job.preference.UserInfo;
import sas.part.time.job.server.ForgotPasswordServer;
import sas.part.time.job.server.LoginServer;
import sas.part.time.job.userInterface.ILogin;
import sas.part.time.job.userPreference.RememberPasswordPref;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

//import com.google.android.gms.ads.AdView;

public class Login extends Activity implements ILogin {

	private Button 				Login;
	private TextView 			SignUp, ForgotPassword;
	private EditText 			Username, Password;
	private PartTimeUtils 		appConfig = PartTimeUtils.getSingleInstance();
	private PartTimeMessage 	appMessage = PartTimeMessage.getSingleInstance();
	private ToggleButton		RememberPass;
	//private AdView 				mAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();
		listener();
		
		String username = RememberPasswordPref.getUserName(Login.this);
		String password = RememberPasswordPref.getPassword(Login.this);
		if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			Username.setText(username);
			Password.setText(password);
			RememberPass.setChecked(true);
		}
		Password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		//mAdView.loadAd(PartTimeUtils.getAdRequest());
		//Username.setText("saha.amit1983@gmail.com");
		//Password.setText("12345678");
		
	}

	private void initView() {
		Login 			= (Button)findViewById(R.id.login_user);
		SignUp 			= (TextView)findViewById(R.id.login_registration);
		ForgotPassword 	= (TextView)findViewById(R.id.login_forgot_password);
		Username 		= (EditText)findViewById(R.id.login_username);
		Password 		= (EditText)findViewById(R.id.login_password);
		RememberPass  	= (ToggleButton)findViewById(R.id.login_toggle_on_off);
		//mAdView 		= (AdView) findViewById(R.id.login_adView);
	}

	private void listener() {
		Login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String _Username = Username.getText().toString().trim();
				String _Password = Password.getText().toString().trim();

				if(!TextUtils.isEmpty(_Username)) {
					if(!TextUtils.isEmpty(_Password)) {
						if(appConfig.isNetworkOnline(Login.this)) {
							LoginServer server = new LoginServer(Login.this, Login.this, 
									_Username, _Password);
							server.execute();
						} else {
							UserAlertDialog dialog = new UserAlertDialog(Login.this);
							dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
									appMessage.getNetworkFailure());
						}
					} else {
						Toast.makeText(Login.this, "Password can't be empty", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(Login.this, "User ID/Email can't be empty", Toast.LENGTH_LONG).show();
				}
			}
		});

		SignUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Login.this, Registration.class);
				Login.this.startActivity(intent);
			}
		});
		ForgotPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showAlert();
			}
		});
	}

	@Override
	public void onLogin(LoginData data) {

		if(data.isResult()) {

			UserInfo info = new UserInfo();
			info.setEmail(Login.this, data.getEmailId());
			info.setUserId(Login.this, data.getUserId());

			info.setFirstName(Login.this, data.getFirstName());
			info.setLastName(Login.this, data.getLastName());
			info.setDateOfBirth(Login.this, data.getDateOfBirth());
			info.setGender(Login.this, data.getGender());
			info.setPhoneNumber(Login.this, data.getPhoneNumber());
			info.setImage(Login.this, data.getImage());
			info.setAddress(Login.this, data.getAddress());
			info.setCity(Login.this, data.getCity());
			info.setState(Login.this, data.getState());
			info.setCountry(Login.this, data.getCountry());
			info.setPin(Login.this, data.getPin());

			Intent intent = new Intent(Login.this, Home.class);
			Login.this.startActivity(intent);
			
			if(RememberPass.isChecked()) {
				RememberPasswordPref.setUserName(Login.this, Username.getText().toString().trim());
				RememberPasswordPref.setPassword(Login.this, Password.getText().toString().trim());
			} else {
				RememberPasswordPref.setUserName(Login.this, "");
				RememberPasswordPref.setPassword(Login.this, "");
			}
		} else {
			UserAlertDialog dialog = new UserAlertDialog(Login.this);
			dialog.showDialogOk(getString(android.R.string.dialog_alert_title), data.getMessage());
		}
	}

	private void showAlert() {

		final EditText edit = new EditText(Login.this);
		AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
		builder.setView(edit);
		builder.setTitle("Enter your email id");
		builder.setCancelable(false);
		builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface param, int paramInt) {

				if(edit.getText().toString().length() >0) {
					if(appConfig.isValidEmail(edit.getText().toString())) {

						ForgotPasswordServer server = new ForgotPasswordServer(Login.this, edit.getText().toString());
						server.execute();

					} else {
						param.dismiss();
						Toast.makeText(Login.this, "Please enter valid email id", Toast.LENGTH_LONG).show();
					}
				} else {
					param.dismiss();
					Toast.makeText(Login.this, "Please enter email id", Toast.LENGTH_LONG).show();
				}
			}
		});
		builder.setNegativeButton(getString(android.R.string.cancel),null);

		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	/*@Override
	public void onPause() {
		if (mAdView != null) {
			mAdView.pause();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdView != null) {
			mAdView.resume();
		}
	}

	@Override
	public void onDestroy() {
		if (mAdView != null) {
			mAdView.destroy();
		}
		super.onDestroy();
	}*/
}
