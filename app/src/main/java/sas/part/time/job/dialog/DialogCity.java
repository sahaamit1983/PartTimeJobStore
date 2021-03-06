package sas.part.time.job.dialog;

import java.util.ArrayList;

import sas.part.time.job.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

public class DialogCity extends Dialog {

	private ListView list;
	private EditText filterText = null;
	private CustomContact adapter = null;
	private OnCity pos;
	private ArrayList<String> mlist2;

	public DialogCity(Context context, ArrayList<String> cityList, final OnCity pos) {
		super(context);
		setContentView(R.layout.dialog_list);
		this.setTitle("Select City");
		this.pos = pos;
		filterText = (EditText) findViewById(R.id.search);
		filterText.addTextChangedListener(filterTextWatcher);
		list = (ListView) findViewById(R.id.list);
		//adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, cityList);
		mlist2 = new ArrayList<String>(cityList);
		adapter = new CustomContact(context, cityList);
		list.setAdapter(adapter);
		/*list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				pos.getId(cityList.get(position).getId());
				dismiss();
				list.getItemAtPosition(position);
			}
		});*/
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			adapter.setCustomContact(mlist2); 
			adapter.getFilter().filter(s);
		}
	};

	@Override
	public void onStop(){
		filterText.removeTextChangedListener(filterTextWatcher);
	}

	public static abstract class OnCity {
		public abstract void getCity(String city);
	}

	private class CustomContact extends BaseAdapter implements Filterable  {

		private ArrayList<String> mlist1;
		private LayoutInflater inflater;

		public CustomContact(Context context, ArrayList<String> list) {
			mlist1 = new ArrayList<String>(list);
			inflater = getLayoutInflater();
		}
		
		public void setCustomContact(ArrayList<String> list) {
			mlist1 = new ArrayList<String>(list);
		}

		@Override
		public int getCount() {
			return mlist1.size();
		}

		@Override
		public Object getItem(int position) {
			return mlist1.get(position); 
		}

		@Override
		public long getItemId(int position) {
			return position; 
		}

		@Override
		public View getView(final int position, View arg1, ViewGroup arg2) {
			View v = inflater.inflate(android.R.layout.simple_list_item_1, arg2, false);
			TextView tv = (TextView)v.findViewById(android.R.id.text1);
			tv.setText(mlist1.get(position));
			v.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					pos.getCity(mlist1.get(position));
					dismiss();
				}
			});
			return v;
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {

				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					mlist1 = (ArrayList<String>) results.values;
					notifyDataSetChanged();
				}

				@SuppressLint("DefaultLocale") @Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults results = new FilterResults();
					ArrayList<String> FilteredList = new ArrayList<String>();
					if (constraint == null || constraint.length() == 0) {
						// No filter implemented we return all the list
						results.values = mlist1;
						results.count = mlist1.size();

					} else {

						for (int i = 0; i < mlist1.size(); i++) {
							String data = mlist1.get(i);

							if (data.toLowerCase().contains(
									constraint.toString().toLowerCase())) {
								FilteredList.add(mlist1.get(i));
							}
						}
						results.values = FilteredList;
						results.count = FilteredList.size();
					}

					return results;
				}
			};
			return filter;
		}
	}
}
