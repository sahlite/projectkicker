package com.mrsahlite.projectkicker;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines a contract between the ProjectKicker content provider and its clients. A contract defines the
 * information that a client needs to access the provider as one or more data tables. A contract
 * is a public, non-extendable (final) class that contains constants defining column names and
 * URIs. A well-written client depends only on the constants in the contract.
 */
public final class ProjectDBContract {
	public static final String AUTHORITY = "com.mrsahlite.projectkicker.provider.project";
	
	 // This class cannot be instantiated
	private ProjectDBContract() {
		
	}
	
	/**
     * Projects table contract
     */
    public static final class Projects implements BaseColumns {

        // This class cannot be instantiated
        private Projects() {}
        
        /**
         * The table name offered by this provider
         */
        public static final String TABLE_NAME = "projects";

        /*
         * URI definitions
         */

        /**
         * The scheme part for this provider's URI
         */
        private static final String SCHEME = "content://";

        /**
         * Path parts for the URIs
         */

        /**
         * Path part for the Projects URI
         */
        private static final String PATH_PROJECTS = "/projects";

        /**
         * Path part for the Project ID URI
         */
        private static final String PATH_PROJECT_ID = "/projects/";

        /**
         * 0-relative position of a note ID segment in the path part of a project ID URI
         */
        public static final int PROJECT_ID_PATH_POSITION = 1;
        
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_PROJECTS);

        /**
         * The content URI base for a single project. Callers must
         * append a numeric project id to this Uri to retrieve a note
         */
        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_PROJECT_ID);

        /**
         * The content URI match pattern for a single project, specified by its ID. Use this to match
         * incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN
            = Uri.parse(SCHEME + AUTHORITY + PATH_PROJECT_ID + "/#");
        
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of projects.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mrsahlite.project";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single project.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mrsahlite.project";
        
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        /*
         * Column definitions
         */

        /**
         * Column name for the name of project
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_PROJECT = "name";

        /**
         * Column name for the description of project
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        /**
         * Column name for the start date 
         * <P>Type: TEXT (format as year/month/day)</P>
         */
        public static final String COLUMN_NAME_START_DATE = "start";

        /**
         * Column name for the end date
         * <P>Type: TEXT (format as year/month/day)</P>
         */
        public static final String COLUMN_NAME_END_DATE = "stop";
        
        /**
         * Column name for the end date expected
         * <P>Type: TEXT (format as year/month/day)</P>
         */
        public static final String COLUMN_NAME_EXPECT_END_DATE = "expect";
        
        /**
         * Column name for the end date
         * <P>Type: BOOLEAN</P>
         */
        public static final String COLUMN_NAME_IS_FINISHED = "finished";
    }
    
    /**
     * Project items table contract
     */
    public static final class ProjectItems implements BaseColumns {

        // This class cannot be instantiated
        private ProjectItems() {}
        
        /**
         * The table name offered by this provider
         */
        public static final String TABLE_NAME = "items";

        /*
         * URI definitions
         */

        /**
         * The scheme part for this provider's URI
         */
        private static final String SCHEME = "content://";

        /**
         * Path parts for the URIs
         */

        /**
         * Path part for the Projects URI
         */
        private static final String PATH_ITEMS= "/items";

        /**
         * Path part for the Project ID URI
         */
        private static final String PATH_ITEM_ID = "/items/";

        /**
         * 0-relative position of a note ID segment in the path part of a project ID URI
         */
        public static final int ITEM_ID_PATH_POSITION = 1;
        
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + PATH_ITEMS);

        /**
         * The content URI base for a single project. Callers must
         * append a numeric project id to this Uri to retrieve a note
         */
        public static final Uri CONTENT_ID_URI_BASE
            = Uri.parse(SCHEME + AUTHORITY + PATH_ITEM_ID);

        /**
         * The content URI match pattern for a single project, specified by its ID. Use this to match
         * incoming URIs or to construct an Intent.
         */
        public static final Uri CONTENT_ID_URI_PATTERN
            = Uri.parse(SCHEME + AUTHORITY + PATH_ITEM_ID + "/#");
        
        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of items.
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mrsahlite.item";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single item.
         */
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mrsahlite.item";
        
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        /*
         * Column definitions
         */
        
        /**
         * Column name for the ID of project the item belongs to
         * <P>Type: INTEGER </P>
         */
        public static final String COLUMN_NAME_PROJECT = "project_id";

        /**
         * Column name for the name of project item
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_ITEM = "name";

        /**
         * Column name for the description of project item
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        /**
         * Column name for the start date 
         * <P>Type: TEXT (format as year/month/day)</P>
         */
        public static final String COLUMN_NAME_START_DATE = "start";

        /**
         * Column name for the end date
         * <P>Type: TEXT (format as year/month/day)</P>
         */
        public static final String COLUMN_NAME_END_DATE = "stop";
        
        /**
         * Column name for the end date expected
         * <P>Type: TEXT (format as year/month/day)</P>
         */
        public static final String COLUMN_NAME_EXPECT_END_DATE = "expect";
        
        /**
         * Column name for the end date
         * <P>Type: BOOLEAN</P>
         */
        public static final String COLUMN_NAME_IS_FINISHED = "finished";
    }
}
