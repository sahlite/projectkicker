package com.mrsahlite.projectkicker;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SearchView.OnQueryTextListener;

import com.mrsahlite.projectkicker.DeletePromptFragment.OnDeletePromptListener;
import com.mrsahlite.projectkicker.ProjectDBContract.Projects;


public class ProjectsListFragment extends ListFragment 
			implements OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor>, 
						OnItemLongClickListener, OnDeletePromptListener{
	
	public static final String TAG = "Projects List";
	
	public interface OnProjectsListItemClickListener{
		void onProjectsListItemClick(long id);
		void onProjectCreate();
	}
	 // This is the Adapter being used to display the list's data.
    SimpleCursorAdapter mAdapter;

//    // If non-null, this is the current filter the user has provided.
//    String mCurFilter;
    
    OnProjectsListItemClickListener mCallback;
    
    long delete_id;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// We have a menu item to show in action bar.
        setHasOptionsMenu(true);
	}

	@Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Give some text to display if there is no data.  In a real
        // application this would come from a resource.
        setEmptyText("No items!");

        // We have a menu item to show in action bar.
        setHasOptionsMenu(true);

        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.projects_list_item, null,
                new String[] { Projects.COLUMN_NAME_PROJECT, Projects.COLUMN_NAME_EXPECT_END_DATE, Projects.COLUMN_NAME_IS_FINISHED },
                new int[] { R.id.tvProjectsName, R.id.tvProjectsExpectEndDate, R.id.tvProjectsFinished }, 0);
        setListAdapter(mAdapter);

        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
        
        getListView().setOnItemLongClickListener(this);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
    	menu.clear();
        MenuItem item = menu.add(getString(R.string.create));
//        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        item.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				mCallback.onProjectCreate();
				return true;
			}
        	
        });
    }

	public boolean onQueryTextChange(String newText) {
        // Called when the action bar search text has changed.  Update
        // the search filter, and restart the loader to do a new query
        // with this filter.
//        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override public boolean onQueryTextSubmit(String query) {
        // Don't care about this.
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnProjectsListItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnItemClickListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onProjectsListItemClick(id);
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }

    // These are the Contacts rows that we will retrieve.
    static final String[] PROJECTS_SUMMARY_PROJECTION = new String[] {
        Projects._ID,
        Projects.COLUMN_NAME_PROJECT,
        Projects.COLUMN_NAME_EXPECT_END_DATE,
        Projects.COLUMN_NAME_IS_FINISHED
    };
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri = Projects.CONTENT_URI;
        
        return new CursorLoader(getActivity(), baseUri,
        		PROJECTS_SUMMARY_PROJECTION, null, null, null);
    }

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

	@Override
	public boolean onItemLongClick(AdapterView<?> adpeter, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		delete_id = id;
		DeletePromptFragment prompt = new DeletePromptFragment();
		prompt.setCallback(this);
		prompt.show(getActivity().getSupportFragmentManager(), getString(R.string.delete_prompt));
		
		return true;
	}

	@Override
	public void onPositiveClick() {
		// TODO Auto-generated method stub
		Uri uri = ContentUris.withAppendedId(Projects.CONTENT_ID_URI_BASE, delete_id);
		getActivity().getContentResolver().delete(uri, null, null);
	}

	@Override
	public void onNegativeClick() {
		// TODO Auto-generated method stub
		
	}
}
