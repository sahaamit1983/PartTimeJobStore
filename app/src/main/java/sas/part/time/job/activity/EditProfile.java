package sas.part.time.job.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sas.part.time.job.R;
import sas.part.time.job.datepicker.PartDatePicker;
import sas.part.time.job.dialog.DialogCity;
import sas.part.time.job.dialog.DialogCountry;
import sas.part.time.job.dialog.DialogState;
import sas.part.time.job.dialog.UserAlertDialog;
import sas.part.time.job.imageLoader.ImageLoader;
import sas.part.time.job.passwordProtection.AsteriskPasswordTransformationMethod;
import sas.part.time.job.pojo.Country;
import sas.part.time.job.pojo.ProfileData;
import sas.part.time.job.pojo.ResultMsg;
import sas.part.time.job.pojo.Zone;
import sas.part.time.job.server.EditProfileServer;
import sas.part.time.job.server.GetProfileServer;
import sas.part.time.job.userInterface.IProfileData;
import sas.part.time.job.userInterface.IRegistration;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class EditProfile extends Activity implements IRegistration, IProfileData {

	private Button 				Dob, Gender, Submit, City, State, Country, Back;
	private TextView 			CameraButt, GalleryButt, EmailId;
	private EditText 			FName, LName, PhNumber, Password, RePassword, 
	Address, Pincode;
	private ImageView 			Pic;
	private	ScrollView 			SC;
	private Calendar 			Cal = Calendar.getInstance();
	private final String 		GengerArray[] = new String[]{"Male", "Female"};
	private final int 			GALLERY_REQUEST_CODE = 100;
	private final int 			CAMERA_REQUEST_CODE = 101;
	private Uri 				mCapturedImageURI 	= null;
	private String 				mImagePath = null;
	private String 				GenderStr = "M";
	private PartTimeMessage 	appMessage = PartTimeMessage.getSingleInstance();
	private PartTimeUtils 		appConfig = PartTimeUtils.getSingleInstance();
	private ArrayList<Country>	CountryList = new ArrayList<Country>();
	private String[]			CountryNames = new String[]{};
	private String[]			ZoneNames = new String[]{};
	private ArrayList<Zone>		ZoneList = new ArrayList<Zone>();
	private ArrayList<String>	CityList = new ArrayList<String>();
	private ImageLoader 		imageLoader;
	private ProgressBar			Pb;
	private RelativeLayout		TopGray;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editprofile);

		// IMEI number is pending
		initView();
		listener();

		SC.setVerticalScrollBarEnabled(false);
		CountryList = appConfig.getCountryData(this);
		CountryNames = new String[CountryList.size()];
		for(int i=0; i<CountryList.size();i++) {
			CountryNames[i] = CountryList.get(i).getName();
		}

		Password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		RePassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());

		imageLoader = new ImageLoader(EditProfile.this,R.drawable.reg_image_default);

		if(appConfig.isNetworkOnline(EditProfile.this)) {
			GetProfileServer getProServer = new GetProfileServer(EditProfile.this, 
					EditProfile.this, CountryList);
			getProServer.execute();
		} else {
			UserAlertDialog dialog = new UserAlertDialog(EditProfile.this);
			dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
					appMessage.getNetworkFailure());
		}
	}

	private void initView() {
		Pic				= (ImageView)findViewById(R.id.edit_profile_pic);
		CameraButt		= (TextView)findViewById(R.id.edit_profile_pic_from_camera);
		GalleryButt		= (TextView)findViewById(R.id.edit_profile_pic_from_gallery);
		Gender			= (Button)findViewById(R.id.edit_profile_gender);
		Dob 			= (Button)findViewById(R.id.edit_profile_dob);
		Submit			= (Button)findViewById(R.id.edit_profile_submit);
		EmailId 		= (TextView)findViewById(R.id.edit_profile_email);
		Password 		= (EditText)findViewById(R.id.edit_profile_password);
		RePassword 		= (EditText)findViewById(R.id.edit_profile_repassword);
		FName 			= (EditText)findViewById(R.id.edit_profile_fname);
		LName			= (EditText)findViewById(R.id.edit_profile_lname);
		PhNumber 		= (EditText)findViewById(R.id.edit_profile_ph_number);
		Address 		= (EditText)findViewById(R.id.edit_profile_address_line);
		City			= (Button)findViewById(R.id.edit_profile_city);
		State 			= (Button)findViewById(R.id.edit_profile_state);
		Country 		= (Button)findViewById(R.id.edit_profile_country);
		Back 			= (Button)findViewById(R.id.edit_profile_back);
		Pincode 		= (EditText)findViewById(R.id.edit_profile_pincode);
		SC 				= (ScrollView)findViewById(R.id.edit_profile_scroll);
		Pb				= (ProgressBar)findViewById(R.id.edit_profile_pic_loader);
		TopGray			= (RelativeLayout)findViewById(R.id.edit_profile_top_gray);

		EditTextChangeListener(Password);
		EditTextChangeListener(RePassword);
		EditTextChangeListener(FName);
		EditTextChangeListener(LName);
		EditTextChangeListener(PhNumber);
		EditTextChangeListener(Address);
		EditTextChangeListener(Pincode);
		ButtonTextChangeListener(Gender);
		ButtonTextChangeListener(Dob);
		ButtonTextChangeListener(City);
		ButtonTextChangeListener(State);
		ButtonTextChangeListener(Country);
	}

	private void listener() {
		Dob.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				PartDatePicker picker = new PartDatePicker(EditProfile.this, 
						setStartDate, 
						Cal.get(Calendar.YEAR), 
						Cal.get(Calendar.MONTH), 
						Cal.get(Calendar.DAY_OF_MONTH));

				picker.show();
			}
		});
		GalleryButt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_PICK, 
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(Intent.createChooser(intent,"Select Picture"), 
						GALLERY_REQUEST_CODE);
			}
		});
		CameraButt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, 
						System.currentTimeMillis()+".jpg");
				mCapturedImageURI = getContentResolver().
						insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
				startActivityForResult(intentPicture, CAMERA_REQUEST_CODE);
			}
		});
		Submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				attemptSubmit();
			}
		});
		Gender.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setGender();
			}
		});
		Country.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				setCountry();
			}
		});
		State.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				setState();
			}
		});
		City.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				setCity();
			}
		});
		Back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});
		Pic.setOnClickListener(null);
		TopGray.setOnClickListener(null);
	}

	private void attemptSubmit() {

		// Reset errors.
		FName.setError(null);
		LName.setError(null);
		EmailId.setError(null);
		Password.setError(null);
		RePassword.setError(null);
		PhNumber.setError(null);
		Gender.setError(null);
		Dob.setError(null);
		Address.setError(null);
		State.setError(null);
		Country.setError(null);
		Pincode.setError(null);

		boolean cancel = false;
		View focusView = null;

		if (TextUtils.isEmpty(Pincode.getText().toString().trim())) {
			Pincode.setError(getString(R.string.error_field_required));
			focusView = Pincode;
			cancel = true;
		}
		if (TextUtils.isEmpty(Country.getText().toString().trim())) {
			Country.setError(getString(R.string.error_field_required));
			focusView = Country;
			cancel = true;
		}
		if (TextUtils.isEmpty(State.getText().toString().trim())) {
			State.setError(getString(R.string.error_field_required));
			focusView = State;
			cancel = true;
		}
		if (TextUtils.isEmpty(City.getText().toString().trim())) {
			City.setError(getString(R.string.error_field_required));
			focusView = City;
			cancel = true;
		}
		if (TextUtils.isEmpty(Address.getText().toString().trim())) {
			Address.setError(getString(R.string.error_field_required));
			focusView = Address;
			cancel = true;
		}
		if (TextUtils.isEmpty(Dob.getText().toString().trim())) {
			Dob.setError(getString(R.string.error_field_required));
			focusView = Dob;
			cancel = true;
		} else {

			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c2.setTime(Cal.getTime());
			c2.set(Calendar.YEAR, c2.get(Calendar.YEAR)+14);

			if(c2.after(c1)) {  
				cancel = true;
				UserAlertDialog dialog = new UserAlertDialog(EditProfile.this);
				dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
						appMessage.getAgeValidation());
			}
		}
		if (TextUtils.isEmpty(RePassword.getText().toString().trim())) {
			RePassword.setError(getString(R.string.error_field_required));
			focusView = RePassword;
			cancel = true;
		}

		if (TextUtils.isEmpty(Password.getText().toString().trim())) {
			Password.setError(getString(R.string.error_field_required));
			focusView = Password;
			cancel = true;
		} else if (Password.getText().toString().trim().length() < 5) {
			Password.setError(getString(R.string.error_invalid_password));
			focusView = Password;
			cancel = true;
		} else if (!Password.getText().toString().trim().
				equals(RePassword.getText().toString().trim())) {
			RePassword.setError(getString(R.string.error_mismatch_password));
			focusView = RePassword;
			cancel = true;
		}

		if (TextUtils.isEmpty(EmailId.getText().toString().trim())) {
			EmailId.setError(getString(R.string.error_field_required));
			focusView = EmailId;
			cancel = true;
		} else if (!PartTimeUtils.getSingleInstance().
				isValidEmail(EmailId.getText().toString().trim())) {
			EmailId.setError(getString(R.string.error_invalid_email));
			focusView = EmailId;
			cancel = true;
		}
		if (TextUtils.isEmpty(PhNumber.getText().toString().trim())) {
			PhNumber.setError(getString(R.string.error_field_required));
			focusView = PhNumber;
			cancel = true;
		}
		if (TextUtils.isEmpty(LName.getText().toString().trim())) {
			LName.setError(getString(R.string.error_field_required));
			focusView = LName;
			cancel = true;
		} 
		if (TextUtils.isEmpty(FName.getText().toString().trim())) {
			FName.setError(getString(R.string.error_field_required));
			focusView = FName;
			cancel = true;
		} 

		boolean isNetworkProblem = false;
		if(!PartTimeUtils.getSingleInstance().
				isNetworkOnline(getApplicationContext())) {
			cancel = true;
			isNetworkProblem = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			if(focusView!=null) {
				focusView.requestFocus();
			} else if(isNetworkProblem) {
				UserAlertDialog dialog = new UserAlertDialog(EditProfile.this);
				dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
						appMessage.getNetworkFailure());
			}
		} else {
			String mFName 			= FName.getText().toString().trim();
			String mLName 			= LName.getText().toString().trim();
			String mPhNumber		= PhNumber.getText().toString().trim();
			String mEmailId			= EmailId.getText().toString().trim();
			String mPassword 		= Password.getText().toString().trim();
			String mRePassword 		= RePassword.getText().toString().trim();
			String mAddress			= Address.getText().toString().trim();
			String mCity			= City.getText().toString().trim();
			String mState			= State.getText().toString().trim();
			String mCountry 		= Country.getText().toString().trim();
			String mPincode 		= Pincode.getText().toString().trim();

			if(mPassword.equals("******") && mRePassword.equals("******")) { 
				mPassword = null;
				mRePassword = null;
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String sdt = formatter.format(Cal.getTime());

			EditProfileServer editServer = new EditProfileServer(EditProfile.this,
					EditProfile.this,mFName,mLName,sdt,GenderStr,mPhNumber,mEmailId,
					mPassword, mRePassword, mAddress, mCity, mState,mCountry,
					mPincode,mImagePath);
			editServer.execute();

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && 
				data!=null && resultCode!= RESULT_CANCELED) {

			Uri uri = data.getData();
			String path = getPath(uri);

			if(path != null) {
				String picturePath = new String(path);
				File newfile = new File(picturePath);

				if(newfile.exists()) {
					mImagePath = new String(picturePath);
					processImage(picturePath);
				} else {
					mImagePath = null;
					Toast.makeText(getApplicationContext(),"Image not found", 
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(),"Image not found", 
						Toast.LENGTH_SHORT).show();
			}
		}

		if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && 
				resultCode!= RESULT_CANCELED) {

			String	picturePath = new String(getPath(mCapturedImageURI));
			File newfile = new File(picturePath);

			if(newfile.exists()) {
				mImagePath = new String(picturePath);
				processImage(picturePath);
			} else {
				mImagePath = null;
				Toast.makeText(getApplicationContext(),"Image not found", 
						Toast.LENGTH_SHORT).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void processImage(String originalpath) {

		Bitmap bitmap = PartTimeUtils.getSingleInstance().
				makeScaling(EditProfile.this, originalpath);
		Pic.setBackgroundResource(android.R.color.transparent);
		Pic.setImageBitmap(bitmap);
	}

	private String getPath(Uri uri) {

		String picturePath = null;

		try {
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().
					query(uri, projection, null, null, null);
			int column_index = cursor.
					getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			picturePath = cursor.getString(column_index);
			if(cursor!=null)
				cursor.close();
		} catch(Exception e) {
			e.printStackTrace();
			return uri.getPath();
		}
		return picturePath;
	}

	private PartDatePicker.OnDateSetListener setStartDate 
	= new PartDatePicker.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			Cal.set(Calendar.YEAR, year); 
			Cal.set(Calendar.MONTH, monthOfYear); 
			Cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			Dob.setText(dayOfMonth+"/"+getMonth(monthOfYear)+"/"+year);
		}

		@Override
		public void onDateChanged(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
		}
	};

	@Override
	public void onProfileData(ProfileData data) {
		// TODO Auto-generated method stub
		if(data.isResult()) {

			Password.setText("******");
			RePassword.setText("******");

			if(!TextUtils.isEmpty(data.getGender())) {
				GenderStr = data.getGender();
				if(GenderStr.equals("M")) {
					Gender.setText(GengerArray[0]);
				}else if(GenderStr.equals("F")){
					Gender.setText(GengerArray[1]);
				}
			}

			if(!TextUtils.isEmpty(data.getDOB())) {
				if(!data.getDOB().equals("null")) {

					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Cal.setTime(formatter.parse(data.getDOB()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					Dob.setText(Cal.get(Calendar.DAY_OF_MONTH)
							+"/"+getMonth(Cal.get(Calendar.MONTH))
							+"/"+Cal.get(Calendar.YEAR));
				}
			}

			if(!TextUtils.isEmpty(data.getFirstName())) {
				if(!data.getFirstName().equals("null")) {
					FName.setText(data.getFirstName());
				}
			}
			if(!TextUtils.isEmpty(data.getLastName())) {
				if(!data.getLastName().equals("null")) {
					LName.setText(data.getLastName());
				}
			}
			if(!TextUtils.isEmpty(data.getPhoneNumber())) {
				if(!data.getPhoneNumber().equals("null")) {
					PhNumber.setText(data.getPhoneNumber());
				}
			}
			if(!TextUtils.isEmpty(data.getEmailId())) {
				if(!data.getEmailId().equals("null")) {
					EmailId.setText(data.getEmailId());
				}
			}
			if(!TextUtils.isEmpty(data.getAddress())) {
				if(!data.getAddress().equals("null")) {
					Address.setText(data.getAddress());
				}
			}
			if(!TextUtils.isEmpty(data.getPin())) {
				if(!data.getPin().equals("null")) {
					Pincode.setText(data.getPin());
				}
			}
			if(!TextUtils.isEmpty(data.getImageUrl())) {
				if(!data.getImageUrl().equals("null")) {
					Pb.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(data.getImageUrl(), Pic, Pb);
				}
			}
			if(!TextUtils.isEmpty(data.getCity())) {
				if(!data.getCity().equals("null")) {
					City.setText(data.getCity());
				} else {
					City.setEnabled(false);
				}
			} else {
				City.setEnabled(false);
			}

			if(!TextUtils.isEmpty(data.getCountry())) {
				if(!data.getCountry().equals("null")) {
					Country.setText(data.getCountry());
				}
			}
			if(!TextUtils.isEmpty(data.getState())) {
				if(!data.getState().equals("null")) {
					State.setText(data.getState());
				} else {
					State.setEnabled(false);
				}
			} else {
				State.setEnabled(false);
			}

			ZoneList = data.getZoneList();
			CityList = data.getCityList();
			ZoneNames = new String[ZoneList.size()];
			for(int j=0; j<ZoneList.size();j++) {
				ZoneNames[j] = ZoneList.get(j).getName();
			}

		} else {
			UserAlertDialog dialog = new UserAlertDialog(EditProfile.this);
			dialog.showDialogOk(getString(android.R.string.dialog_alert_title), 
					data.getMessage());
		}
	}

	@Override
	public void onRegistration(ResultMsg data) {

		if(data.isResult()) {
			Toast.makeText(getApplicationContext(), data.getMessage(), 
					Toast.LENGTH_LONG).show();

			Intent i = new Intent();
			setResult(RESULT_OK, i);
			finish();

		} else {
			UserAlertDialog dialog = new UserAlertDialog(EditProfile.this);
			dialog.showDialogOk(getString(android.R.string.dialog_alert_title),
					data.getMessage());
		}
	}

	private String getMonth(int month) {

		String mon = "Jan";
		switch (month) {
		case 0:
			mon = "Jan";
			break;
		case 1:
			mon = "Feb";
			break;
		case 2:
			mon = "Mar";
			break;
		case 3:
			mon = "Apr";
			break;
		case 4:
			mon = "May";
			break;
		case 5:
			mon = "Jun";
			break;
		case 6:
			mon = "Jul";
			break;
		case 7:
			mon = "Aug";
			break;
		case 8:
			mon = "Sep";
			break;
		case 9:
			mon = "Oct";
			break;
		case 10:
			mon = "Nov";
			break;
		case 11:
			mon = "Dec";
			break;
		}
		return mon;
	}

	private void setGender() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select your gender");
		builder.setItems(GengerArray, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(which == 0) {
					GenderStr = "M";
				} else {
					GenderStr = "F";
				}
				Gender.setText(GengerArray[which]);
			}
		});
		builder.show();
	}

	/*private void setCountry() {
		AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
		builder.setTitle("Select your country");
		builder.setItems(CountryNames, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Country.setText(CountryList.get(which).getName());

				ZoneList = appConfig.getStateData(EditProfile.this, 
						CountryList.get(which).getId());

				ZoneNames = new String[ZoneList.size()];
				for(int i=0; i<ZoneList.size();i++) {
					ZoneNames[i] = ZoneList.get(i).getName();
				}
				State.setEnabled(true);
				City.setEnabled(false);
				State.setText("");
				City.setText("");
			}
		});
		builder.show();
	}*/

	private void setCountry() {

		DialogCountry dialog = new DialogCountry(EditProfile.this, CountryList, new DialogCountry.OnCountry() {

			@Override
			public void getCountry(Country country) {
				Country.setText(country.getName());

				ZoneList = appConfig.getStateData(EditProfile.this, 
						country.getId());

				if(ZoneList.size()>0){

					ZoneNames = new String[ZoneList.size()];
					for(int i=0; i<ZoneList.size();i++) {
						ZoneNames[i] = ZoneList.get(i).getName();
					}
					State.setEnabled(true);
					City.setEnabled(false);
					State.setText("");
					City.setText("");
					Pincode.setText("");
					Address.setText("");
				} else {

					State.setVisibility(View.GONE);
					City.setVisibility(View.GONE);
				}
			}
		});
		dialog.show();
	}

	private void setState() {
		/*AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
		builder.setTitle("Select your state");
		builder.setItems(ZoneNames, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				State.setText(ZoneNames[which]);

				CityList = appConfig.getCityData(EditProfile.this, 
						ZoneList.get(which).getId());

				City.setEnabled(true);
				City.setText("");
			}
		});
		builder.show();*/
		
		DialogState dialog = new DialogState(EditProfile.this, ZoneList, new DialogState.OnZone() {

			@Override
			public void getZone(Zone zone) {
				State.setText(zone.getName());

				CityList = appConfig.getCityData(EditProfile.this, zone.getId());

				if(CityList.size()>0){
					City.setEnabled(true);
					City.setText("");
				}  else {
					City.setVisibility(View.GONE);
				}
			}
		});
		dialog.show();
	}

	private void setCity() {
		/*AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
		builder.setTitle("Select your city");
		builder.setItems(CityList.toArray(new String[]{}), 
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				City.setText(CityList.get(which));
			}
		});
		builder.show();*/
		DialogCity dialog = new DialogCity(EditProfile.this, CityList, new DialogCity.OnCity() {

			@Override
			public void getCity(String city) {
				City.setText(city);
			}
		});
		dialog.show();
	}

	private void EditTextChangeListener(final EditText editText) {

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, 
					int arg1, int arg2, int arg3) {
				if(editText.length()>0 && editText.getError()!=null) {
					editText.setError(null);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, 
					int arg1, int arg2, int arg3) {}

			@Override
			public void afterTextChanged(Editable arg0) {}
		});
	}
	private void ButtonTextChangeListener(final Button button) {

		button.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, 
					int arg1, int arg2, int arg3) {
				if(button.length()>0 && button.getError()!=null) {
					button.setError(null);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, 
					int arg1, int arg2, int arg3) {}

			@Override
			public void afterTextChanged(Editable arg0) {}
		});
	}
}
