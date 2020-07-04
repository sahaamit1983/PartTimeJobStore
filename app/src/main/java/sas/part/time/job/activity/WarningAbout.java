package sas.part.time.job.activity;

import sas.part.time.job.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WarningAbout extends Activity {

	private Button 		Back;
	private TextView 	Title, Message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warning_about);

		initView();
		listener();

		if(getIntent().getBooleanExtra("Warning", true)) {

			Title.setText(getString(R.string.warning));
			Message.setText(getString(R.string.warning_message));

		} else {

			Title.setText(getString(R.string.about));
			Message.setText(getString(R.string.about_message));
		}
	}

	private void initView() {

		Back 	= (Button)findViewById(R.id.warning_about_back);
		Title 	= (TextView)findViewById(R.id.warning_about_title);
		Message = (TextView)findViewById(R.id.warning_about_message);
	}
	private void listener() {


		Back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
