package sas.part.time.job.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sas.part.time.job.BuildConfig;
import sas.part.time.job.R;
import sas.part.time.job.datepicker.PartDatePicker;
import sas.part.time.job.dialog.DialogCity;
import sas.part.time.job.dialog.DialogCountry;
import sas.part.time.job.dialog.DialogState;
import sas.part.time.job.dialog.UserAlertDialog;
import sas.part.time.job.info.GPSTracker;
import sas.part.time.job.passwordProtection.AsteriskPasswordTransformationMethod;
import sas.part.time.job.pojo.Country;
import sas.part.time.job.pojo.User;
import sas.part.time.job.pojo.ResultMsg;
import sas.part.time.job.pojo.Zone;
import sas.part.time.job.server.RegistrationServer;
import sas.part.time.job.userInterface.IRegistration;
import sas.part.time.job.utils.PartTimeMessage;
import sas.part.time.job.utils.PartTimeUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@SuppressLint("SimpleDateFormat")
public class Registration extends Activity implements IRegistration {

    private Button Dob, Gender, Registration, City, State, Country;
    private TextView CameraButt, GalleryButt;
    private EditText FName, LName, PhNumber, EmailId, Password, RePassword, Address, Pincode;
    private ImageView Pic;
    private ScrollView SC;
    private Calendar Cal = Calendar.getInstance();
    private final String GengerArray[] = new String[]{"Male", "Female"};
    private final int GALLERY_REQUEST_CODE = 100;
    private final int CAMERA_REQUEST_CODE = 101;
    private Uri mCapturedImageURI = null;
    private String GenderStr = "M";
    private PartTimeMessage appMessage = PartTimeMessage.getSingleInstance();
    private PartTimeUtils appConfig = PartTimeUtils.getSingleInstance();
    private ArrayList<Country> CountryList = new ArrayList<>();
    private String[] CountryNames = new String[]{};
    private String[] ZoneNames = new String[]{};
    private ArrayList<Zone> ZoneList = new ArrayList<>();
    private ArrayList<String> CityList = new ArrayList<>();
    private GPSTracker gps;
    private RelativeLayout TopGray;
    private final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // IMEI number is pending
        initView();
        listener();

        SC.setVerticalScrollBarEnabled(false);
        CountryList = appConfig.getCountryData(this);
        CountryNames = new String[CountryList.size()];
        for (int i = 0; i < CountryList.size(); i++) {
            CountryNames[i] = CountryList.get(i).getName();
        }
        Password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        RePassword.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        requestPermission();
    }

    private boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_NETWORK_STATE, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void initView() {
        Pic = (ImageView) findViewById(R.id.registration_pic);
        CameraButt = (TextView) findViewById(R.id.registration_pic_from_camera);
        GalleryButt = (TextView) findViewById(R.id.registration_pic_from_gallery);
        Gender = (Button) findViewById(R.id.registration_gender);
        Dob = (Button) findViewById(R.id.registration_dob);
        Registration = (Button) findViewById(R.id.registration_user);
        EmailId = (EditText) findViewById(R.id.registration_email);
        Password = (EditText) findViewById(R.id.registration_password);
        RePassword = (EditText) findViewById(R.id.registration_repassword);
        FName = (EditText) findViewById(R.id.registration_fname);
        LName = (EditText) findViewById(R.id.registration_lname);
        PhNumber = (EditText) findViewById(R.id.registration_ph_number);
        Address = (EditText) findViewById(R.id.registration_address_line);
        City = (Button) findViewById(R.id.registration_city);
        State = (Button) findViewById(R.id.registration_state);
        Country = (Button) findViewById(R.id.registration_country);
        Pincode = (EditText) findViewById(R.id.registration_pincode);
        SC = (ScrollView) findViewById(R.id.registration_scroll);
        TopGray = (RelativeLayout) findViewById(R.id.registration_top_gray);
    }

    private void listener() {
        Dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PartDatePicker picker = new PartDatePicker(Registration.this,
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
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
            }
        });
        CameraButt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis() + ".jpg");
                    mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(intentPicture, CAMERA_REQUEST_CODE);
                } else {
                    requestPermission();
                }
            }
        });
        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkStoragePermission()) {
                    gps = new GPSTracker(Registration.this);
                    if (gps.canGetLocation()) {
                        attemptLogin();
                    } else {
                        gps.showSettingsAlert();
                    }
                } else {
					requestPermission();
				}
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
        Pic.setOnClickListener(null);
        TopGray.setOnClickListener(null);
    }

    private void attemptLogin() {
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
        if (TextUtils.isEmpty(State.getText().toString().trim())
                && !(State.getVisibility() == View.GONE)) {
            State.setError(getString(R.string.error_field_required));
            focusView = State;
            cancel = true;
        }
        if (TextUtils.isEmpty(City.getText().toString().trim())
                && !(City.getVisibility() == View.GONE)) {
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
            c2.set(Calendar.YEAR, c2.get(Calendar.YEAR) + 14);

            if (c2.after(c1)) {
                cancel = true;
                UserAlertDialog dialog = new UserAlertDialog(Registration.this);
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
        } else if (!PartTimeUtils.getSingleInstance().isValidEmail(EmailId.getText().toString().trim())) {
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
        if (!PartTimeUtils.getSingleInstance().isNetworkOnline(getApplicationContext())) {
            cancel = true;
            isNetworkProblem = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null) {
                focusView.requestFocus();
            } else if (isNetworkProblem) {
                UserAlertDialog dialog = new UserAlertDialog(Registration.this);
                dialog.showDialogOk(getString(android.R.string.dialog_alert_title), appMessage.getNetworkFailure());
            }
        } else {
            String mFName = FName.getText().toString().trim();
            String mLName = LName.getText().toString().trim();
            String mPhNumber = PhNumber.getText().toString().trim();
            String mEmailId = EmailId.getText().toString().trim();
            String mPassword = Password.getText().toString().trim();
            String mAddress = Address.getText().toString().trim();
            String mCity = City.getText().toString().trim();
            String mState = State.getText().toString().trim();
            String mCountry = Country.getText().toString().trim();
            String mPincode = Pincode.getText().toString().trim();
            String mLatitude = String.valueOf(gps.getLatitude());
            String mLongitude = String.valueOf(gps.getLongitude());

            gps.stopUsingGPS();
            String path = getPath(mCapturedImageURI);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String sdt = formatter.format(Cal.getTime());
            File imageFile = getImageFile();
            User user = new User(mFName, mLName, sdt, GenderStr, mPhNumber, mAddress, mCity, mState, mCountry, mPincode, mLatitude, mLongitude, "");
            RegistrationServer registrationServer = new RegistrationServer(this, this, mEmailId, mPassword, user, imageFile);
            registrationServer.execute();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (BuildConfig.DEBUG) {
            FName.setText("ABC");
            LName.setText("XYZ");
            PhNumber.setText("1234567890");
            EmailId.setText("a@gmail.com");
            Password.setText("123456");
            RePassword.setText("123456");
            Address.setText("sdfbsjbjbsdc");
            City.setText("Kolkata");
            State.setText("West Bengal");
            Country.setText("India");
            Pincode.setText("711106");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mCapturedImageURI = data.getData();
            if (mCapturedImageURI != null) {
                processImage();
            } else {
                Toast.makeText(getApplicationContext(), "Image not found", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            if (mCapturedImageURI != null) {
                processImage();
            } else {
                Toast.makeText(getApplicationContext(), "Image not found", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processImage() {
        Pic.setImageURI(mCapturedImageURI);
    }

    private File getImageFile() {
        String picturePath = getPath(mCapturedImageURI);
        if(!TextUtils.isEmpty(picturePath)) {
            return new File(picturePath);
        }
        return null;
    }

    private String getPath(Uri uri) {
        String picturePath = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                picturePath = cursor.getString(column_index);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return uri.getPath();
        }
        return picturePath;
    }

    private PartDatePicker.OnDateSetListener setStartDate = new PartDatePicker.OnDateSetListener() {

        @SuppressLint("SetTextI18n")
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Cal.set(Calendar.YEAR, year);
            Cal.set(Calendar.MONTH, monthOfYear);
            Cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Dob.setText(dayOfMonth + "/" + getMonth(monthOfYear) + "/" + year);
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        }
    };

    @Override
    public void onRegistration(ResultMsg data) {
        if (data.isResult()) {
            finish();
        }
        Toast.makeText(getApplicationContext(), data.getMessage(), Toast.LENGTH_LONG).show();
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

                if (which == 0) {
                    GenderStr = "M";
                } else {
                    GenderStr = "F";
                }
                Gender.setText(GengerArray[which]);
            }
        });
        builder.show();
    }

    private void setCountry() {
        DialogCountry dialog = new DialogCountry(Registration.this, CountryList, new DialogCountry.OnCountry() {

            @Override
            public void getCountry(Country country) {
                Country.setText(country.getName());
                ZoneList = appConfig.getStateData(Registration.this, country.getId());
                if (ZoneList.size() > 0) {
                    ZoneNames = new String[ZoneList.size()];
                    for (int i = 0; i < ZoneList.size(); i++) {
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
        DialogState dialog = new DialogState(Registration.this, ZoneList, new DialogState.OnZone() {

            @Override
            public void getZone(Zone zone) {
                State.setText(zone.getName());
                CityList = appConfig.getCityData(Registration.this, zone.getId());
                if (CityList.size() > 0) {
                    City.setEnabled(true);
                    City.setText("");
                } else {
                    City.setVisibility(View.GONE);
                }
            }
        });
        dialog.show();
    }

    private void setCity() {
        DialogCity dialog = new DialogCity(Registration.this, CityList, new DialogCity.OnCity() {

            @Override
            public void getCity(String city) {
                City.setText(city);
            }
        });
        dialog.show();
    }
}
