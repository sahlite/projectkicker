package com.mrsahlite.projectkicker;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mrsahlite.projectkicker.ProjectDBContract.ProjectItems;
import com.mrsahlite.projectkicker.ProjectDBContract.Projects;



public class ActivityProjectKicker extends FragmentActivity implements OnItemClickListener{
	
	interface OnUpdateListener{
		public abstract void update();
	}
	
	interface OnNotifyRole{
		public abstract void notifyRole(int role);
	}
	
	public static final int PROJECTS_LIST = 0;
	public static final int PROJECT = 1;
	public static final int ITEM = 2;
	
//	private int fragment;
	private long selected_project_id = -1;
	private long selected_item_id = -1;
	
	private OnUpdateListener updateListener;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_project, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		FragmentManager manager = getSupportFragmentManager();
		int fragment = PROJECTS_LIST;
		if (null != manager.findFragmentByTag(ItemFragment.TAG)){
			fragment = ITEM;
		}else if (null != manager.findFragmentByTag(ProjectFragment.TAG)){
			fragment = PROJECT;
		}
		switch(item.getItemId()){
		case R.id.item_read:
			if (PROJECTS_LIST == fragment){
				// If the frag is not available, we're in the one-pane layout and must swap frags...

	            // Create fragment and give it an argument for the selected article
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
	            
	            updateListener = (OnUpdateListener)newFragment;
	            fragment = PROJECT;
			}else if (PROJECT == fragment){
				// If the frag is not available, we're in the one-pane layout and must swap frags...

	            // Create fragment and give it an argument for the selected article
	            ItemFragment newFragment = new ItemFragment();
	            Bundle args = new Bundle();
	            args.putLong(ItemFragment.ARG_PARENT_POSITION, selected_project_id);
	            args.putLong(ItemFragment.ARG_POSITION, selected_item_id);
	            args.putInt(ItemFragment.ARG_ACTION, ItemFragment.ACTION_READ);
	            newFragment.setArguments(args);
	            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

	            // Replace whatever is in the fragment_container view with this fragment,
	            // and add the transaction to the back stack so the user can navigate back
	            transaction.replace(R.id.fragment_container, newFragment, ItemFragment.TAG);
	            transaction.addToBackStack(null);

	            // Commit the transaction
	            transaction.commit();
	            
	            updateListener = (OnUpdateListener)newFragment;
	            fragment = ITEM;
			}
			break;
		case R.id.item_update:
			updateListener.update();
			break;
		case R.id.item_create:
			if (PROJECTS_LIST == fragment){
				// If the frag is not available, we're in the one-pane layout and must swap frags...

	            // Create fragment and give it an argument for the selected article
	            ProjectFragment newFragment = new ProjectFragment();
	            Bundle args = new Bundle();
	            args.putLong(ProjectFragment.ARG_POSITION, selected_project_id);
	            args.putInt(ProjectFragment.ARG_ACTION, ProjectFragment.ACTION_INSERT);
	            newFragment.setArguments(args);
	            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

	            // Replace whatever is in the fragment_container view with this fragment,
	            // and add the transaction to the back stack so the user can navigate back
	            transaction.replace(R.id.fragment_container, newFragment, ProjectFragment.TAG);
	            transaction.addToBackStack(null);

	            // Commit the transaction
	            transaction.commit();
	            
	            updateListener = (OnUpdateListener)newFragment;
	            fragment = PROJECT;
			}else if (PROJECT == fragment){
				// If the frag is not available, we're in the one-pane layout and must swap frags...

	            // Create fragment and give it an argument for the selected article
	            ItemFragment newFragment = new ItemFragment();
	            Bundle args = new Bundle();
	            args.putLong(ItemFragment.ARG_PARENT_POSITION, selected_project_id);
	            args.putLong(ItemFragment.ARG_POSITION, selected_item_id);
	            args.putInt(ItemFragment.ARG_ACTION, ItemFragment.ACTION_INSERT);
	            newFragment.setArguments(args);
	            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

	            // Replace whatever is in the fragment_container view with this fragment,
	            // and add the transaction to the back stack so the user can navigate back
	            transaction.replace(R.id.fragment_container, newFragment, ItemFragment.TAG);
	            transaction.addToBackStack(null);

	            // Commit the transaction
	            transaction.commit();
	            
	            updateListener = (OnUpdateListener)newFragment;
	            fragment = ITEM;
			}
			break;
		case R.id.item_delete:
			if (PROJECTS_LIST == fragment){
				Uri uri = ContentUris.withAppendedId(Projects.CONTENT_ID_URI_BASE, selected_project_id);
				getContentResolver().delete(uri, null, null);
			}else if (PROJECT == fragment){
				Uri uri = ContentUris.withAppendedId(ProjectItems.CONTENT_ID_URI_BASE, selected_item_id);
				getContentResolver().delete(uri, null, null);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int item, long id) {
		// TODO Auto-generated method stub
		FragmentManager manager = getSupportFragmentManager();
		int fragment = PROJECTS_LIST;
		if (null != manager.findFragmentByTag(ItemFragment.TAG)){
			fragment = ITEM;
		}else if (null != manager.findFragmentByTag(ProjectFragment.TAG)){
			fragment = PROJECT;
		}
		if (PROJECTS_LIST == fragment){
			selected_project_id = id;
		}else if (PROJECT == fragment){
			selected_item_id = id;	
		}
		
		
	}

}
