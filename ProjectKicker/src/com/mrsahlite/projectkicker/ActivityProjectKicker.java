package com.mrsahlite.projectkicker;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.mrsahlite.projectkicker.ProjectFragment.OnProjectItemClickListener;
import com.mrsahlite.projectkicker.ProjectsListFragment.OnProjectsListItemClickListener;



public class ActivityProjectKicker extends FragmentActivity 
									implements OnProjectsListItemClickListener,
												OnProjectItemClickListener{
	
//	interface OnUpdateListener{
//		public abstract void update();
//	}
	
//	interface OnNotifyRole{
//		public abstract void notifyRole(int role);
//	}
	
	public static final int PROJECTS_LIST = 0;
	public static final int PROJECT = 1;
	public static final int ITEM = 2;
	
//	private int fragment;
	private long selected_project_id = -1;
//	private long selected_item_id = -1;
	
//	private OnUpdateListener updateListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_kicker);
		
		if (savedInstanceState != null) {
            return;
        }

        // Create an instance of ExampleFragment
        ProjectsListFragment firstFragment = new ProjectsListFragment();

        // In case this activity was started with special instructions from an Intent,
        // pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment, ProjectsListFragment.TAG).commit();
        
//        fragment = PROJECTS_LIST;
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_project, menu);
//		return true;
//	}

	@Override
	public void onProjectItemClick(long id) {
		// TODO Auto-generated method stub
		
		ItemFragment newFragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putLong(ItemFragment.ARG_PARENT_POSITION, selected_project_id);
        args.putLong(ItemFragment.ARG_POSITION, id);
        args.putInt(ItemFragment.ARG_ACTION, ItemFragment.ACTION_READ);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment, ItemFragment.TAG);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
	}

	@Override
	public void onProjectsListItemClick(long id) {
		// TODO Auto-generated method stub
		selected_project_id = id;
		ProjectFragment newFragment = new ProjectFragment();
		Bundle args = new Bundle();
		args.putLong(ProjectFragment.ARG_POSITION, selected_project_id);
		args.putInt(ProjectFragment.ARG_ACTION, ProjectFragment.ACTION_READ);
		newFragment.setArguments(args);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		
		// Replace whatever is in the fragment_container view with this fragment,
		// and add the transaction to the back stack so the user can navigate back
		transaction.replace(R.id.fragment_container, newFragment, ProjectFragment.TAG);
		transaction.addToBackStack(null);
		
		// Commit the transaction
		transaction.commit();
	}

	@Override
	public void onProjectCreate() {
		// TODO Auto-generated method stub
		 // Create fragment and give it an argument for the selected article
        ProjectFragment newFragment = new ProjectFragment();
        Bundle args = new Bundle();
//        args.putLong(ProjectFragment.ARG_POSITION, -1);
        args.putInt(ProjectFragment.ARG_ACTION, ProjectFragment.ACTION_INSERT);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment, ProjectFragment.TAG);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
	}

	@Override
	public void onProjectItemCreate() {
		// TODO Auto-generated method stub
		ItemFragment newFragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putLong(ItemFragment.ARG_PARENT_POSITION, selected_project_id);
//        args.putLong(ItemFragment.ARG_POSITION, selected_item_id);
        args.putInt(ItemFragment.ARG_ACTION, ItemFragment.ACTION_INSERT);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment, ItemFragment.TAG);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
	}

}
