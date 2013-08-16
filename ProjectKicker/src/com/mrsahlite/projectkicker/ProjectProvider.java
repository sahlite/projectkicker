package com.mrsahlite.projectkicker;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.mrsahlite.projectkicker.ProjectDBContract.ProjectItems;
import com.mrsahlite.projectkicker.ProjectDBContract.Projects;

public class ProjectProvider extends ContentProvider{
	// Used for debugging and logging
    private static final String TAG = "ProjectProdiver";
    
    /**
     * A projection map used to select columns from the database
     */
    private static HashMap<String, String> sProjectsProjectionMap;

    /**
     * A projection map used to select columns from the database
     */
    private static HashMap<String, String> sProjectProjectionMap;
    
    /**
     * A projection map used to select columns from the database
     */
    private static HashMap<String, String> sItemsProjectionMap;

    /**
     * A projection map used to select columns from the database
     */
    private static HashMap<String, String> sItemProjectionMap;
    
    /*
     * Constants used by the Uri matcher to choose an action based on the pattern
     * of the incoming URI
     */
    // The incoming URI matches the project URI pattern
    private static final int PROJECTS = 1;

    // The incoming URI matches the project ID URI pattern
    private static final int PROJECT_ID = 2;

 // The incoming URI matches the item URI pattern
    private static final int ITEMS = 3;

    // The incoming URI matches the item ID URI pattern
    private static final int ITEM_ID = 4;
    
    /**
     * A UriMatcher instance
     */
    private static final UriMatcher sUriMatcher;
    
    // Handle to project DatabaseHelper.
    private ProjectOpenHelper mProjectOpenHelper;
    // Handle to project DatabaseHelper.
    private ProjectItemOpenHelper mProjectItemOpenHelper;
    
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(ProjectDBContract.AUTHORITY, "projects", PROJECTS);
        sUriMatcher.addURI(ProjectDBContract.AUTHORITY, "projects/#", PROJECT_ID);
        sUriMatcher.addURI(ProjectDBContract.AUTHORITY, "items", ITEMS);
        sUriMatcher.addURI(ProjectDBContract.AUTHORITY, "items/#", ITEM_ID);
        
        sProjectsProjectionMap = new HashMap<String, String>();
        sProjectsProjectionMap.put(Projects._ID, Projects._ID);
        sProjectsProjectionMap.put(Projects.COLUMN_NAME_PROJECT, Projects.COLUMN_NAME_PROJECT);
        sProjectsProjectionMap.put(Projects.COLUMN_NAME_EXPECT_END_DATE, Projects.COLUMN_NAME_EXPECT_END_DATE);
        sProjectsProjectionMap.put(Projects.COLUMN_NAME_IS_FINISHED, Projects.COLUMN_NAME_IS_FINISHED);

        sProjectProjectionMap = new HashMap<String, String>();
        sProjectProjectionMap.put(Projects._ID, Projects._ID);
        sProjectProjectionMap.put(Projects.COLUMN_NAME_PROJECT, Projects.COLUMN_NAME_PROJECT);
        sProjectProjectionMap.put(Projects.COLUMN_NAME_DESCRIPTION, Projects.COLUMN_NAME_DESCRIPTION);
        sProjectProjectionMap.put(Projects.COLUMN_NAME_START_DATE, Projects.COLUMN_NAME_START_DATE);
        sProjectProjectionMap.put(Projects.COLUMN_NAME_END_DATE, Projects.COLUMN_NAME_END_DATE);
        sProjectProjectionMap.put(Projects.COLUMN_NAME_EXPECT_END_DATE, Projects.COLUMN_NAME_EXPECT_END_DATE);
        sProjectProjectionMap.put(Projects.COLUMN_NAME_IS_FINISHED, Projects.COLUMN_NAME_IS_FINISHED);
        
        sItemsProjectionMap = new HashMap<String, String>();
        sItemsProjectionMap.put(ProjectItems._ID, ProjectItems._ID);
        sItemsProjectionMap.put(ProjectItems.COLUMN_NAME_ITEM, ProjectItems.COLUMN_NAME_ITEM);
        sItemsProjectionMap.put(ProjectItems.COLUMN_NAME_EXPECT_END_DATE, ProjectItems.COLUMN_NAME_EXPECT_END_DATE);
        sItemsProjectionMap.put(ProjectItems.COLUMN_NAME_IS_FINISHED, ProjectItems.COLUMN_NAME_IS_FINISHED);
        
        sItemProjectionMap = new HashMap<String, String>();
        sItemProjectionMap.put(ProjectItems._ID, ProjectItems._ID);
        sItemProjectionMap.put(ProjectItems.COLUMN_NAME_ITEM, ProjectItems.COLUMN_NAME_ITEM);
        sItemProjectionMap.put(ProjectItems.COLUMN_NAME_DESCRIPTION, ProjectItems.COLUMN_NAME_DESCRIPTION);
        sItemProjectionMap.put(ProjectItems.COLUMN_NAME_START_DATE, ProjectItems.COLUMN_NAME_START_DATE);
        sItemProjectionMap.put(ProjectItems.COLUMN_NAME_END_DATE, ProjectItems.COLUMN_NAME_END_DATE);
        sItemProjectionMap.put(ProjectItems.COLUMN_NAME_EXPECT_END_DATE, ProjectItems.COLUMN_NAME_EXPECT_END_DATE);
        sItemProjectionMap.put(ProjectItems.COLUMN_NAME_IS_FINISHED, ProjectItems.COLUMN_NAME_IS_FINISHED);
    }

    
    /**
    *
    * This class helps open, create, and upgrade the database file. Set to package visibility
    * for testing purposes.
    */
   static class ProjectOpenHelper extends SQLiteOpenHelper {
	   /**
	     * The database that the provider uses as its underlying data store
	     */
	    private static final String DATABASE_NAME = "project.db";

	    /**
	     * The database version
	     */
	    private static final int DATABASE_VERSION = 2;

	   ProjectOpenHelper(Context context) {

           // calls the super constructor, requesting the default cursor factory.
           super(context, DATABASE_NAME, null, DATABASE_VERSION);
       }

       /**
        *
        * Creates the underlying database with table name and column names taken from the
        * NotePad class.
        */
       @Override
       public void onCreate(SQLiteDatabase db) {
           db.execSQL(SQL_CREATE_ENTRIES);
       }

       /**
        *
        * Demonstrates that the provider must consider what happens when the
        * underlying datastore is changed. In this sample, the database is upgraded the database
        * by destroying the existing data.
        * A real application should upgrade the database in place.
        */
       @Override
       public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

           // Logs that the database is being upgraded
           Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                   + newVersion + ", which will destroy all old data");

           // Kills the table and existing data
           db.execSQL(SQL_DELETE_ENTRIES);

           // Recreates the database with a new version
           onCreate(db);
       }
   
		private static final String TEXT_TYPE = " TEXT";
		private static final String INTEGER_TYPE = " INTEGER";
//		private static final String REAL_TYPE = " REAL";
		private static final String COMMA_SEP = ", ";
		private static final String SQL_CREATE_ENTRIES = 
						"CREATE TABLE " + Projects.TABLE_NAME + " (" +
						Projects._ID + " INTEGER PRIMARY KEY, " +
						Projects.COLUMN_NAME_PROJECT + TEXT_TYPE + COMMA_SEP +
						Projects.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
						Projects.COLUMN_NAME_START_DATE + TEXT_TYPE + COMMA_SEP +
						Projects.COLUMN_NAME_END_DATE + TEXT_TYPE + COMMA_SEP +
						Projects.COLUMN_NAME_EXPECT_END_DATE + TEXT_TYPE + COMMA_SEP +
						Projects.COLUMN_NAME_IS_FINISHED + INTEGER_TYPE + 
						"); ";
		private static final String SQL_DELETE_ENTRIES =
						"DROP TABLE IF EXISTS " + Projects.TABLE_NAME;
   }
   
   /**
   *
   * This class helps open, create, and upgrade the database file. Set to package visibility
   * for testing purposes.
   */
  static class ProjectItemOpenHelper extends SQLiteOpenHelper {
	   /**
	     * The database that the provider uses as its underlying data store
	     */
	    private static final String DATABASE_NAME = "project_item.db";

	    /**
	     * The database version
	     */
	    private static final int DATABASE_VERSION = 2;

	    ProjectItemOpenHelper(Context context) {

          // calls the super constructor, requesting the default cursor factory.
          super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      /**
       *
       * Creates the underlying database with table name and column names taken from the
       * NotePad class.
       */
      @Override
      public void onCreate(SQLiteDatabase db) {
          db.execSQL(SQL_CREATE_ENTRIES);
      }

      /**
       *
       * Demonstrates that the provider must consider what happens when the
       * underlying datastore is changed. In this sample, the database is upgraded the database
       * by destroying the existing data.
       * A real application should upgrade the database in place.
       */
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

          // Logs that the database is being upgraded
          Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                  + newVersion + ", which will destroy all old data");

          // Kills the table and existing data
          db.execSQL(SQL_DELETE_ENTRIES);

          // Recreates the database with a new version
          onCreate(db);
      }
  
		private static final String TEXT_TYPE = " TEXT";
		private static final String INTEGER_TYPE = " INTEGER";
//		private static final String REAL_TYPE = " REAL";
		private static final String COMMA_SEP = ", ";
		private static final String SQL_CREATE_ENTRIES = 
						"CREATE TABLE " + ProjectItems.TABLE_NAME + " (" +
						ProjectItems._ID + " INTEGER PRIMARY KEY, " +
						ProjectItems.COLUMN_NAME_PROJECT + INTEGER_TYPE + COMMA_SEP +
						ProjectItems.COLUMN_NAME_ITEM + TEXT_TYPE + COMMA_SEP +
						ProjectItems.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
						ProjectItems.COLUMN_NAME_START_DATE + TEXT_TYPE + COMMA_SEP +
						ProjectItems.COLUMN_NAME_END_DATE + TEXT_TYPE + COMMA_SEP +
						ProjectItems.COLUMN_NAME_EXPECT_END_DATE + TEXT_TYPE + COMMA_SEP +
						ProjectItems.COLUMN_NAME_IS_FINISHED + INTEGER_TYPE + 
						"); ";
		private static final String SQL_DELETE_ENTRIES =
						"DROP TABLE IF EXISTS " + ProjectItems.TABLE_NAME;
  }


	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		String id;	
        int count;
        
        switch (sUriMatcher.match(uri)) {
        case PROJECT_ID:
        	db = mProjectOpenHelper.getWritableDatabase();
        	id = uri.getPathSegments().get(1);
            count = db.delete(Projects.TABLE_NAME, Projects._ID + "=" + id
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
            break;

        case ITEM_ID:
        	db = mProjectItemOpenHelper.getWritableDatabase();
        	id = uri.getPathSegments().get(1);
            count = db.delete(ProjectItems.TABLE_NAME, ProjectItems._ID + "=" + id
                    + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (sUriMatcher.match(uri)) {
        case PROJECTS:
        	return Projects.CONTENT_TYPE;
        	
        case PROJECT_ID:
            return Projects.CONTENT_ITEM_TYPE;

        case ITEMS:
            return ProjectItems.CONTENT_TYPE;
            
        case ITEM_ID:
            return ProjectItems.CONTENT_ITEM_TYPE;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		// Validate the requested uri
        if ((sUriMatcher.match(uri) != PROJECTS)&&(sUriMatcher.match(uri) != ITEMS)) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        
        if (sUriMatcher.match(uri) == PROJECTS){
        	SQLiteDatabase db = mProjectOpenHelper.getWritableDatabase();
            long rowId = db.insert(Projects.TABLE_NAME, null, values);
            if (rowId > 0) {
                Uri noteUri = ContentUris.withAppendedId(Projects.CONTENT_URI, rowId);
                getContext().getContentResolver().notifyChange(noteUri, null);
                return noteUri;
            }
        }else if (sUriMatcher.match(uri) == ITEMS){
        	SQLiteDatabase db = mProjectItemOpenHelper.getWritableDatabase();
            long rowId = db.insert(ProjectItems.TABLE_NAME, null, values);
            if (rowId > 0) {
                Uri noteUri = ContentUris.withAppendedId(ProjectItems.CONTENT_URI, rowId);
                getContext().getContentResolver().notifyChange(noteUri, null);
                return noteUri;
            }
        }
        
        throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mProjectOpenHelper = new ProjectOpenHelper(getContext());
		mProjectItemOpenHelper = new ProjectItemOpenHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        

        switch (sUriMatcher.match(uri)) {
        case PROJECTS:
        	qb.setTables(Projects.TABLE_NAME);
            qb.setProjectionMap(sProjectsProjectionMap);
            break;

        case PROJECT_ID:
        	qb.setTables(Projects.TABLE_NAME);
            qb.setProjectionMap(sProjectProjectionMap);
            qb.appendWhere(Projects._ID + "=" + uri.getPathSegments().get(1));
            break;

        case ITEMS:
        	qb.setTables(ProjectItems.TABLE_NAME);
            qb.setProjectionMap(sItemsProjectionMap);
            break;

        case ITEM_ID:
        	qb.setTables(ProjectItems.TABLE_NAME);
            qb.setProjectionMap(sItemProjectionMap);
            qb.appendWhere(ProjectItems._ID + "=" + uri.getPathSegments().get(1));
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // If no sort order is specified use the default
//        String orderBy;
//        if (TextUtils.isEmpty(sortOrder)) {
//            orderBy = RecorderContract.DEFAULT_SORT_ORDER;
//        } else {
//            orderBy = sortOrder;
//        }

        // Get the database and run the query
        SQLiteDatabase db = null;
        switch (sUriMatcher.match(uri)) {
        case PROJECTS:
        case PROJECT_ID:
        	db = mProjectOpenHelper.getReadableDatabase();
            break;

        case ITEMS:
        case ITEM_ID:
        	db = mProjectItemOpenHelper.getReadableDatabase();
            break;
            
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, null);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db;
		String id;
        int count;
        
        switch (sUriMatcher.match(uri)) {
        case PROJECT_ID:
        	db = mProjectOpenHelper.getWritableDatabase();
        	id = uri.getPathSegments().get(1);
            count = db.update(Projects.TABLE_NAME, values, Projects._ID + "=" + id
        			+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
            break;

        case ITEM_ID:
        	db = mProjectItemOpenHelper.getWritableDatabase();
        	id = uri.getPathSegments().get(1);
            count = db.update(ProjectItems.TABLE_NAME, values, ProjectItems._ID + "=" + id
        			+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}
}
