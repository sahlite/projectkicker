package com.mrsahlite.projectkicker;


import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mrsahlite.projectkicker.ProjectDBContract.ProjectItems;

public class ItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
	public static final String TAG = "Item detail";
	
	public static final String ARG_PARENT_POSITION = "project id";
	public static final String ARG_POSITION = "item id";
	public static final String ARG_ACTION = "item action";
	
	public static final int ACTION_INSERT = 0;
	public static final int ACTION_READ = 1;
	
	// These are the Contacts rows that we will retrieve.
    static final String[] ITEM_PROJECTION = new String[] {
        ProjectItems._ID,
        ProjectItems.COLUMN_NAME_ITEM,
        ProjectItems.COLUMN_NAME_DESCRIPTION,
        ProjectItems.COLUMN_NAME_START_DATE,
        ProjectItems.COLUMN_NAME_END_DATE,
        ProjectItems.COLUMN_NAME_EXPECT_END_DATE,
        ProjectItems.COLUMN_NAME_IS_FINISHED
    };
    
    static final int INDEX_NAME = 1;
    static final int INDEX_DESCRIPTION = 2;
    static final int INDEX_START_DATE = 3;
    static final int INDEX_END_DATE = 4;
    static final int INDEX_EXPECT_END_DATE = 5;
    static final int INDEX_EXPECT_IS_FINISHED = 6;
    
	private EditText mName;
	private TextView mStartDate;
	private TextView mEndDate;
	private TextView mExpectEndDate;
	private EditText mDescription;
	private CheckBox mFinished;
	
	long mProjectID = -1;
	long mItemID = -1;
	int mAction = -1;
	
	private OnDateSetListener mStartDateOnDateSetListener = new OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mStartDate.setText(Utils.covDateToString(year, monthOfYear, dayOfMonth));
		}
		
	};
	
	private OnClickListener mStartDateOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int[] date = Utils.covStringToDate(mStartDate.getText().toString());
			DatePickerFragment newFragment = new DatePickerFragment();
			Bundle args = new Bundle();
			args.putInt(DatePickerFragment.TAG_YEAR, date[Utils.POSITION_YEAR]);
			args.putInt(DatePickerFragment.TAG_MONTH, date[Utils.POSITION_MONTH]);
			args.putInt(DatePickerFragment.TAG_DAY, date[Utils.POSITION_DAY]);
			newFragment.setArguments(args);
			newFragment.setOnDateChangeListener(mStartDateOnDateSetListener);
		    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
		}
		
	};
	
	private OnDateSetListener mEndDateOnDateSetListener = new OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mEndDate.setText(Utils.covDateToString(year, monthOfYear, dayOfMonth));
		}
		
	};
	
	private OnClickListener mEndDateOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int[] date = Utils.covStringToDate(mEndDate.getText().toString());
			DatePickerFragment newFragment = new DatePickerFragment();
			Bundle args = new Bundle();
			args.putInt(DatePickerFragment.TAG_YEAR, date[Utils.POSITION_YEAR]);
			args.putInt(DatePickerFragment.TAG_MONTH, date[Utils.POSITION_MONTH]);
			args.putInt(DatePickerFragment.TAG_DAY, date[Utils.POSITION_DAY]);
			newFragment.setArguments(args);
			newFragment.setOnDateChangeListener(mEndDateOnDateSetListener);
		    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
		}
		
	};
	
	private OnDateSetListener mExpectEndDateOnDateSetListener = new OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			mExpectEndDate.setText(Utils.covDateToString(year, monthOfYear, dayOfMonth));
		}
		
	};
	
	private OnClickListener mExpectEndDateOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int[] date = Utils.covStringToDate(mExpectEndDate.getText().toString());
			DatePickerFragment newFragment = new DatePickerFragment();
			Bundle args = new Bundle();
			args.putInt(DatePickerFragment.TAG_YEAR, date[Utils.POSITION_YEAR]);
			args.putInt(DatePickerFragment.TAG_MONTH, date[Utils.POSITION_MONTH]);
			args.putInt(DatePickerFragment.TAG_DAY, date[Utils.POSITION_DAY]);
			newFragment.setArguments(args);
			newFragment.setOnDateChangeListener(mExpectEndDateOnDateSetListener);
		    newFragment.show(getActivity().getSupportFragmentManager(), "Expect end date");
		}
		
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View root = inflater.inflate(R.layout.fragment_item, container, false); 
		
		// If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
        	mProjectID = savedInstanceState.getLong(ARG_PARENT_POSITION);
        	mItemID = savedInstanceState.getLong(ARG_POSITION);
        	mAction = savedInstanceState.getInt(ARG_ACTION);
        }
		
		mName = (EditText)root.findViewById(R.id.etItemName);
		mStartDate = (TextView)root.findViewById(R.id.tvItemStartDate);
		mEndDate = (TextView)root.findViewById(R.id.tvItemEndDate);
		mExpectEndDate = (TextView)root.findViewById(R.id.tvItemExpectEndDate);
		mDescription = (EditText)root.findViewById(R.id.etItemDescription);
		mFinished = (CheckBox)root.findViewById(R.id.cbItemFinished);
		
		mStartDate.setOnClickListener(mStartDateOnClickListener);
		mEndDate.setOnClickListener(mEndDateOnClickListener);
		mExpectEndDate.setOnClickListener(mExpectEndDateOnClickListener);
		
		return root;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		// During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
        	mProjectID = args.getLong(ARG_PARENT_POSITION);
        	mItemID = args.getLong(ARG_POSITION);
        	mAction = args.getInt(ARG_ACTION);
        }else{
        	args = new Bundle();
        	args.putLong(ARG_PARENT_POSITION, mProjectID);
        	args.putLong(ARG_POSITION, mItemID);
        	args.putInt(ARG_ACTION, mAction);
        }
        
        if (ACTION_READ == mAction){
        	// Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            getLoaderManager().initLoader(0, args, this);
        }else{
        	Calendar c = Calendar.getInstance();
        	int year = c.get(Calendar.YEAR);
        	int month = c.get(Calendar.MONTH);
        	int day = c.get(Calendar.DAY_OF_MONTH);
        	mName.setText(getString(R.string.default_name));
	    	mDescription.setText(getString(R.string.default_name));
	    	mStartDate.setText(Utils.covDateToString(year, month, day));
	    	mEndDate.setText(Utils.covDateToString(year, month, day));
	    	mExpectEndDate.setText(Utils.covDateToString(year, month, day));
        }
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		menu.clear();
		MenuItem item = menu.add(getString(R.string.update));
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				update();
				return true;
			}
		 
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		// Save the current article selection in case we need to recreate the fragment
	    outState.putLong(ARG_PARENT_POSITION, mProjectID);
	    outState.putLong(ARG_POSITION, mItemID);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		/// This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri = ContentUris.withAppendedId(ProjectItems.CONTENT_ID_URI_BASE, args.getLong(ARG_POSITION));
        
        return new CursorLoader(getActivity(), baseUri,
        		ITEM_PROJECTION, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		if (0 < data.getCount()){
    		data.moveToFirst();
    		mName.setText(data.getString(INDEX_NAME));
	    	mDescription.setText(data.getString(INDEX_DESCRIPTION));
	    	mStartDate.setText(data.getString(INDEX_START_DATE));
	    	mEndDate.setText(data.getString(INDEX_END_DATE));
	    	mExpectEndDate.setText(data.getString(INDEX_EXPECT_END_DATE));
	    	int finish = data.getInt(INDEX_EXPECT_IS_FINISHED);
	    	mFinished.setChecked(1==finish);
    	}else{
    		Calendar c = Calendar.getInstance();
        	int year = c.get(Calendar.YEAR);
        	int month = c.get(Calendar.MONTH);
        	int day = c.get(Calendar.DAY_OF_MONTH);
        	
    		mName.setText(getString(R.string.default_name));
        	mDescription.setText(getString(R.string.default_name));
        	mStartDate.setText(Utils.covDateToString(year, month, day));
	    	mEndDate.setText(Utils.covDateToString(year, month, day));
	    	mExpectEndDate.setText(Utils.covDateToString(year, month, day));
	    	mFinished.setChecked(false);
    	}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
    	int year = c.get(Calendar.YEAR);
    	int month = c.get(Calendar.MONTH);
    	int day = c.get(Calendar.DAY_OF_MONTH);
    	
		mName.setText(getString(R.string.default_name));
    	mDescription.setText(getString(R.string.default_name));
    	mName.setText(getString(R.string.default_name));
    	mDescription.setText(getString(R.string.default_name));
    	mStartDate.setText(Utils.covDateToString(year, month, day));
    	mEndDate.setText(Utils.covDateToString(year, month, day));
    	mExpectEndDate.setText(Utils.covDateToString(year, month, day));
    	mFinished.setChecked(false);
	}

	public void update() {
		ContentValues values = new ContentValues();
		values.put(ProjectItems.COLUMN_NAME_PROJECT, mProjectID);
		values.put(ProjectItems.COLUMN_NAME_ITEM, mName.getText().toString());
		values.put(ProjectItems.COLUMN_NAME_DESCRIPTION, mDescription.getText().toString());
		values.put(ProjectItems.COLUMN_NAME_START_DATE, mStartDate.getText().toString());
		values.put(ProjectItems.COLUMN_NAME_END_DATE, mEndDate.getText().toString());
		values.put(ProjectItems.COLUMN_NAME_EXPECT_END_DATE, mExpectEndDate.getText().toString());
		int finish = mFinished.isChecked()?1:0;
		values.put(ProjectItems.COLUMN_NAME_IS_FINISHED, finish);

		if (ACTION_READ == mAction){
			Uri mUri = ContentUris.withAppendedId(ProjectItems.CONTENT_ID_URI_BASE, mItemID);

			getActivity().getContentResolver().update(mUri, values, null, null);

		}else{
			getActivity().getContentResolver().insert(ProjectItems.CONTENT_URI, values);
			mAction = ACTION_READ;
		}
	}

}
