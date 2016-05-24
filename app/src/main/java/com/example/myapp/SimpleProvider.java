package com.example.myapp;

import java.util.HashMap;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;




/**
 * A very simple implementation of a content provider.
 */
public class SimpleProvider extends ContentProvider {
    
    public static final class MainTable implements BaseColumns {

        // This class cannot be instantiated
        private MainTable() {}

        /**
         * The table name offered by this provider
         */
        public static final String CONTACTS_TABLE_NAME = "main";
        

        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI =  Uri.parse("content://" + AUTHORITY + "/main");

        /**
         * The content URI base for a single row of data. Callers must
         * append a numeric row id to this Uri to retrieve a row
         */
        public static final Uri CONTENT_ID_URI_BASE
                = Uri.parse("content://" + AUTHORITY + "/main/");

        /**
         * The MIME type of {@link #CONTENT_URI}.
         */
        public static final String CONTENT_TYPE
                = "vnd.android.cursor.dir/vnd.example.api-demos-throttle";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single row.
         */
        public static final String CONTENT_ITEM_TYPE
                = "vnd.android.cursor.item/vnd.example.api-demos-throttle";
        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = "data COLLATE LOCALIZED ASC";

        /**
         * Column name for the single column holding our data.
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME_DATA = "data";
    }

    /**
     * This class helps open, create, and upgrade the database file.
     */
    static class DatabaseHelper extends SQLiteOpenHelper {

       private static final String DATABASE_NAME = "loader_throttle.db";
       private static final int DATABASE_VERSION = 2;

       DatabaseHelper(Context context) {

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
           db.execSQL("CREATE TABLE " + MainTable.CONTACTS_TABLE_NAME + " ("
                   + MainTable._ID + " INTEGER PRIMARY KEY,"
                   + MainTable.COLUMN_NAME_DATA + " TEXT"
                   + ");");
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
//           Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
//                   + newVersion + ", which will destroy all old data");

           // Kills the table and existing data
           db.execSQL("DROP TABLE IF EXISTS notes");

           // Recreates the database with a new version
           onCreate(db);
       }
    }
    // A projection map used to select columns from the database
    private final HashMap<String, String> mNotesProjectionMap;
    // Uri matcher to decode incoming URIs.
    private final UriMatcher mUriMatcher;

    // The incoming URI matches the main table URI pattern
    private static final int MAIN = 1;
    // The incoming URI matches the main table row ID URI pattern
    private static final int MAIN_ID = 2;

    // Handle to a new DatabaseHelper.
    private DatabaseHelper mOpenHelper;
    public static final String AUTHORITY = "com.example.android.apis.app.test";

    /**
     * Global provider initialization.
     */
    public SimpleProvider() {
        // Create and initialize URI matcher.
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, MainTable.CONTACTS_TABLE_NAME, MAIN);
        mUriMatcher.addURI(AUTHORITY, MainTable.CONTACTS_TABLE_NAME + "/#", MAIN_ID);

        // Create and initialize projection map for all columns.  This is
        // simply an identity mapping.
        mNotesProjectionMap = new HashMap<String, String>();
        mNotesProjectionMap.put(MainTable._ID, MainTable._ID);
        mNotesProjectionMap.put(MainTable.COLUMN_NAME_DATA, MainTable.COLUMN_NAME_DATA);
    }

    /**
     * Perform provider creation.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        // Assumes that any failures will be reported by a thrown exception.
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // TODO Auto-generated method stub
        SQLiteQueryBuilder qb=new SQLiteQueryBuilder();
        
        switch(mUriMatcher.match(uri)){
            case MAIN:
                qb.setTables(MainTable.CONTACTS_TABLE_NAME);
                qb.setProjectionMap(mNotesProjectionMap);
                break;
            case MAIN_ID:
                qb.setTables(MainTable.CONTACTS_TABLE_NAME);
                qb.setProjectionMap(mNotesProjectionMap);
                /*qb.appendWhere(MainTable._ID + "="
                        + uri.getLastPathSegment());*/
                qb.appendWhere(MainTable._ID + "=?");
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                        new String[] { uri.getLastPathSegment() });
                break;
            default:
                throw new IllegalArgumentException("Unknown uri:" + uri);
        }
        
        String orderBy;
        if(TextUtils.isEmpty(sortOrder)){
            orderBy=null;
        } else {
            orderBy=sortOrder;
        }
        SQLiteDatabase db=mOpenHelper.getReadableDatabase();
        System.out.println(db.getMaximumSize());
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    /**
     * Return the MIME type for an known URI in the provider.
     */
    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case MAIN:
                return MainTable.CONTENT_TYPE;
            case MAIN_ID:
                return MainTable.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    /**
     * Handler inserting new data.
     */
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (mUriMatcher.match(uri) != MAIN) {
            // Can only insert into to main URI.
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;

        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        if (values.containsKey(MainTable.COLUMN_NAME_DATA) == false) {
            values.put(MainTable.COLUMN_NAME_DATA, "");
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        long rowId = db.insert(MainTable.CONTACTS_TABLE_NAME, null, values);

        // If the insert succeeded, the row ID exists.
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(MainTable.CONTENT_ID_URI_BASE, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    /**
     * Handle deleting data.
     */
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String finalWhere;

        int count;

        switch (mUriMatcher.match(uri)) {
            case MAIN:
                // If URI is main table, delete uses incoming where clause and args.
                count = db.delete(MainTable.CONTACTS_TABLE_NAME, where, whereArgs);
                break;

                // If the incoming URI matches a single note ID, does the delete based on the
                // incoming data, but modifies the where clause to restrict it to the
                // particular note ID.
            case MAIN_ID:
                // If URI is for a particular row ID, delete is based on incoming
                // data but modified to restrict to the given ID.
                finalWhere = DatabaseUtils.concatenateWhere(
                        MainTable._ID + " = " + ContentUris.parseId(uri), where);
                count = db.delete(MainTable.CONTACTS_TABLE_NAME, finalWhere, whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }


    /**
     * Handle updating data.
     */
    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (mUriMatcher.match(uri)) {
            case MAIN:
                // If URI is main table, update uses incoming where clause and args.
                count = db.update(MainTable.CONTACTS_TABLE_NAME, values, where, whereArgs);
                break;

            case MAIN_ID:
                // If URI is for a particular row ID, update is based on incoming
                // data but modified to restrict to the given ID.
                finalWhere = DatabaseUtils.concatenateWhere(
                        MainTable._ID + " = " + ContentUris.parseId(uri), where);
                count = db.update(MainTable.CONTACTS_TABLE_NAME, values, finalWhere, whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}