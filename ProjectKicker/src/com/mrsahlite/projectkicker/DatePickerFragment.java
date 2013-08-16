package com.mrsahlite.projectkicker;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {
	public static final String TAG = "DatePickerFragment";
	public static final String TAG_YEAR = TAG+".year";
	public static final String TAG_MONTH = TAG+".month";
	public static final String TAG_DAY = TAG+".day";
	
//	interface OnDateChangeListener{
//		void onDateChange(int year, int monthOfYear, int dayOfMonth);
//	}
	
	OnDateSetListener mListener;
	
	public void setOnDateChangeListener(OnDateSetListener listener){
		mListener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle args = getArguments();
		int year, month, day;
        if (null == args){
        	final Calendar c = Calendar.getInstance();
        	year = c.get(Calendar.YEAR);
        	month = c.get(Calendar.MONTH);
        	day = c.get(Calendar.DAY_OF_MONTH);
        }else{
        	year = args.getInt(TAG_YEAR);
            month = args.getInt(TAG_MONTH);
            day = args.getInt(TAG_DAY);
        }
        
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), mListener, year, month, day);
	}

//	@Override
//	public void onDateSet(DatePicker view, int year, int monthOfYear,
//			int dayOfMonth) {
//		// TODO Auto-generated method stub
//		if (null != mListener)
//			mListener.onDateChange(year, monthOfYear, dayOfMonth);
//	}

}
