package simpleanswers.expirydatekeeper.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;

import simpleanswers.expirydatekeeper.R;

/**
 * Created by ruchadeshmukh on 3/20/15.
 */
public class ExpiryProvider extends ContentProvider{

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ExpiryDBHelper mExpiryHelper;

    static final int EXPIRY = 100;
    static final int EXPIRY_ITEM = 200;

    static  UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ExpiryContract.CONTENT_AUTHORITY, ExpiryContract.PATH_EXPIRYLIST, EXPIRY);
        uriMatcher.addURI(ExpiryContract.CONTENT_AUTHORITY, ExpiryContract.PATH_EXPIRYLIST + "/#", EXPIRY_ITEM);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mExpiryHelper = new ExpiryDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case EXPIRY:
                return ExpiryContract.ExpiryEntry.CONTENT_TYPE;
            case EXPIRY_ITEM:
                return ExpiryContract.ExpiryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        switch(sUriMatcher.match(uri)){
            case EXPIRY:
                Context context = getContext();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                if(pref.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default_value)).equalsIgnoreCase(context.getString(R.string.pref_sort_default_value)))
                    retCursor = mExpiryHelper.getReadableDatabase().query(ExpiryContract.ExpiryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                else if(pref.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default_value)).equalsIgnoreCase(context.getString(R.string.pref_sort_value_days))) {
                    String col = ExpiryContract.ExpiryEntry.COLUMN_EXP_DATE;
                    String orderBy = "date(substr("+col+", 7, 4) || substr("+col+", 1, 2) || substr("+col+", 4, 2))";
                    retCursor = mExpiryHelper.getReadableDatabase().query(ExpiryContract.ExpiryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                }else if(pref.getString(context.getString(R.string.pref_sort_key), context.getString(R.string.pref_sort_default_value)).equalsIgnoreCase(context.getString(R.string.pref_sort_value_category)))
                    retCursor = mExpiryHelper.getReadableDatabase().query(ExpiryContract.ExpiryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, ExpiryContract.ExpiryEntry.COLUMN_EXP_CATEGORY + " ASC");
                else
                    retCursor = mExpiryHelper.getReadableDatabase().query(ExpiryContract.ExpiryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            case EXPIRY_ITEM:
                retCursor = mExpiryHelper.getReadableDatabase().query(ExpiryContract.ExpiryEntry.TABLE_NAME,projection, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mExpiryHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch(match){
            case EXPIRY: {
                long _id = db.insert(ExpiryContract.ExpiryEntry.TABLE_NAME, null, values);

                if (_id > 0)
                    returnUri = ExpiryContract.ExpiryEntry.buildExpiryListItemUri(_id);
                else
                    throw new SQLException("Failed to insert row");
                break;
            }

            default:
                throw new UnsupportedOperationException("Unkown uri" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mExpiryHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        if(null==selection)
            selection= "1";

        int rowsDeleted;

        switch (match){
            case EXPIRY:
                rowsDeleted = db.delete(ExpiryContract.ExpiryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unkown URI: " + uri);
        }

        if(rowsDeleted!=0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mExpiryHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        if(null==selection)
            selection= "1";

        int rowsUpdated;

        switch (match){
            case EXPIRY:
                rowsUpdated = db.update(ExpiryContract.ExpiryEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unkown URI: " + uri);
        }

        if(rowsUpdated!=0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
