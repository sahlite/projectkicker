package com.mrsahlite.projectkicker;


import java.util.Calendar;

import android.app.Activity;
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
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mrsahlite.projectkicker.ActivityProjectKicker.OnUpdateListener;
import com.mrsahlite.projectkicker.ProjectDBContract.ProjectItems;
import com.mrsahlite.projectkicker.ProjectDBContract.Projects;

public class ProjectFragment extends Fragment implements OnUpdateListener{

	public static final String TAG = "Project detail";
	
	public static final String ARG_POSITION = "project id";
	public static final String ARG_ACTION = "project action";
	
	public static final int ACTION_INSERT = 0;
	public static final int ACTION_READ = 1;
	// This is the Adapter being used to display the list's data.
    SimpleCursorAdapter mAdapter;
    
	private EditText mName;
	private TextView mStartDate;
	private TextView mEndDate;
	private TextView mExpectEndDate;
	private EditText mDescription;
	private ListView mItemsList;
	private CheckBox mFinished;
	
	private static final int PROJECT_DETAIL = 0;
	private static final int ITEMS_LIST = 1;
	private ProjectDetailManager mProjectDetailManager;
	private ProjectItemsListManager mProjectItemsListManager;
	
	private long mCurrentPosition = -1;
	private int mAction = -1;
	private OnItemClickListener mCallback = null;
	
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
		    newFragment.show(getActivity().getSupportFragmentManager(), "Actual start date");
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
		    newFragment.show(getActivity().getSupportFragmentManager(), "Actual end date");
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
		View root = inflater.inflate(R.layout.fragment_project, container, false);
		
		// If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getLong(ARG_POSITION);
            mAction = savedInstanceState.getInt(ARG_ACTION);
        }
        
		mName = (EditText)root.findViewById(R.id.etProjectName);
		mStartDate = (TextView)root.findViewById(R.id.tvProjectStartDate);
		mEndDate = (TextView)root.findViewById(R.id.tvProjectEndDate);
		mExpectEndDate = (TextView)root.findViewById(R.id.tvProjectExpectEndDate);
		mDescription = (EditText)root.findViewById(R.id.etProjectDescription);
		mItemsList = (ListView)root.findViewById(R.id.lvItemsList);
		mFinished = (CheckBox)root.findViewById(R.id.cbProjectFinished);
		
		mStartDate.setOnClickListener(mStartDateOnClickListener);
		mEndDate.setOnClickListener(mEndDateOnClickListener);
		mExpectEndDate.setOnClickListener(mExpectEndDateOnClickListener);
		
		// Create an empty adapter we will use to display the loaded data.
		 mAdapter = new SimpleCursorAdapter(getActivity(),
	                R.layout.items_list_item, null,
	                new String[] { ProjectItems.COLUMN_NAME_ITEM, ProjectItems.COLUMN_NAME_EXPECT_END_DATE, ProjectItems.COLUMN_NAME_IS_FINISHED },
	                new int[] { R.id.tvItemsName, R.id.tvItemsExpectEndDate, R.id.tvItemsFinished }, 0);
        
		mItemsList.setAdapter(mAdapter);
		mItemsList.setOnItemClickListener(mCallback);
		return root;
	}
	
	@Override
    public void onStart() {
        super.onStart();

        
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
        	mCurrentPosition = args.getLong(ARG_POSITION);
        	mAction = args.getInt(ARG_ACTION);
        }else{
        	args = new Bundle();
        	args.putLong(ARG_POSITION, mCurrentPosition);
        	args.putInt(ARG_ACTION, mAction);
        }
        
        if (ACTION_READ == mAction){
        	mProjectDetailManager = new ProjectDetailManager();
    		mProjectItemsListManager = new ProjectItemsListManager();
    		
    		// Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            getLoaderManager().initLoader(PROJECT_DETAIL, args, mProjectDetailManager);
            getLoaderManager().initLoader(ITEMS_LIST, args, mProjectItemsListManager);
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
//            mCallback = (OnItemClickListener) activity;
        	mCallback = (OnItemClickListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemClickListener");
        }
    }
	 
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	
	    // Save the current article selection in case we need to recreate the fragment
	    outState.putLong(ARG_POSITION, mCurrentPosition);
	}

	class ProjectDetailManager implements LoaderManager.LoaderCallbacks<Cursor>{
		
		// These are the Contacts rows that we will retrieve.
	    final String[] PROJECT_PROJECTION = new String[] {
	        Projects._ID,
	        Projects.COLUMN_NAME_PROJECT,
	        Projects.COLUMN_NAME_DESCRIPTION,
	        Projects.COLUMN_NAME_START_DATE,
	        Projects.COLUMN_NAME_END_DATE,
	        Projects.COLUMN_NAME_EXPECT_END_DATE,
	        Projects.COLUMN_NAME_IS_FINISHED
	    };
	    
	    final int INDEX_NAME = 1;
	    final int INDEX_DESCRIPTION = 2;
	    final int INDEX_START_DATE = 3;
	    final int INDEX_END_DATE = 4;
	    final int INDEX_EXPECT_END_DATE = 5;
	    final int INDEX_IS_FINISHED= 6;

	    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
	        // This is called when a new Loader needs to be created.  This
	        // sample only has one Loader, so we don't care about the ID.
	        // First, pick the base URI to use depending on whether we are
	        // currently filtering.
	        Uri baseUri = ContentUris.withAppendedId(Projects.CONTENT_ID_URI_BASE, args.getLong(ARG_POSITION));
	        
	        return new CursorLoader(getActivity(), baseUri,
	        		PROJECT_PROJECTION, null, null, null);
	    }

	    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	        // Swap the new cursor in.  (The framework will take care of closing the
	        // old cursor once we return.)
	        //mAdapter.swapCursor(data);
	    	if (0 < data.getCount()){
	    		data.moveToFirst();
	    		mName.setText(data.getString(INDEX_NAME));
		    	mDescription.setText(data.getString(INDEX_DESCRIPTION));
		    	mStartDate.setText(data.getString(INDEX_START_DATE));
		    	mEndDate.setText(data.getString(INDEX_END_DATE));
		    	mExpectEndDate.setText(data.getString(INDEX_EXPECT_END_DATE));
		    	int finish = data.getInt(INDEX_IS_FINISHED);
    			mFinished.setChecked(1 == finish);
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

	    public void onLoaderReset(Loader<Cursor> loader) {
	        // This is called when the last Cursor provided to onLoadFinished()
	        // above is about to be closed.  We need to make sure we are no
	        // longer using it.
	        //mAdapter.swapCursor(null);
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
		
	}
	
	class ProjectItemsListManager implements LoaderManager.LoaderCallbacks<Cursor>{
		// These are the Contacts rows that we will retrieve.
	    final String[] ITEMS_SUMMERY_PROJECTION = new String[] {
	        ProjectItems._ID,
	        ProjectItems.COLUMN_NAME_ITEM,
	        ProjectItems.COLUMN_NAME_EXPECT_END_DATE,
	        ProjectItems.COLUMN_NAME_IS_FINISHED
	    };

		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			// Swap the new cursor in.  (The framework will take care of closing the
	        // old cursor once we return.)
	        mAdapter.swapCursor(data);
		}

		public void onLoaderReset(Loader<Cursor> loader) {
			// This is called when the last Cursor provided to onLoadFinished()
	        // above is about to be closed.  We need to make sure we are no
	        // longer using it.
	        mAdapter.swapCursor(null);
		}

		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// This is called when a new Loader needs to be created.  This
	        // sample only has one Loader, so we don't care about the ID.
	        // First, pick the base URI to use depending on whether we are
	        // currently filtering.
	        Uri baseUri = ProjectItems.CONTENT_URI;
	        
        	// Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            String select = "(" + ProjectItems.COLUMN_NAME_PROJECT + "=" + String.valueOf(mCurrentPosition) + ")";
	        
	        return new CursorLoader(getActivity(), baseUri,
	        		ITEMS_SUMMERY_PROJECTION, select, null, null);
		}
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put(Projects.COLUMN_NAME_PROJECT, mName.getText().toString());
		values.put(Projects.COLUMN_NAME_DESCRIPTION, mDescription.getText().toString());
		values.put(Projects.COLUMN_NAME_START_DATE, mStartDate.getText().toString());
		values.put(Projects.COLUMN_NAME_END_DATE, mEndDate.getText().toString());
		values.put(Projects.COLUMN_NAME_EXPECT_END_DATE, mExpectEndDate.getText().toString());
		int finish = mFinished.isChecked()?1:0;
		values.put(ProjectItems.COLUMN_NAME_IS_FINISHED, finish);

		if (ACTION_READ == mAction){
			Uri mUri = ContentUris.withAppendedId(Projects.CONTENT_ID_URI_BASE, mCurrentPosition);

			getActivity().getContentResolver().update(mUri, values, null, null);

		}else{
			getActivity().getContentResolver().insert(Projects.CONTENT_URI, values);
		}

	}

}
