package com.mrsahlite.projectkicker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeletePromptFragment extends DialogFragment {

	public interface OnDeletePromptListener{
		void onPositiveClick();
		void onNegativeClick();
	}
	
	OnDeletePromptListener mCallback;
	
	public void setCallback(OnDeletePromptListener callback){
		mCallback = callback;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
		builder.setMessage(getString(R.string.delete_prompt))
				.setPositiveButton(getString(R.string.yes), new OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						mCallback.onPositiveClick();
					}
					
				})
				.setNegativeButton(getString(R.string.no), new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						mCallback.onNegativeClick();
					}
					
				});
		return builder.create();
	}
	
	
}
