package simpleanswers.expirydatekeeper.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import simpleanswers.expirydatekeeper.activities.ExpiryActivity;

/**
 * Created by ruchadeshmukh on 3/20/15.
 */
public class ExpiryDBHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME="expiry_keeper.db";
    private static final int DATABASE_VERSION=6;

    public ExpiryDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            final String SQL_CREATE_TABLE = "CREATE TABLE " + ExpiryContract.ExpiryEntry.TABLE_NAME + " (" +
                    ExpiryContract.ExpiryEntry._ID + " INTEGER PRIMARY KEY, " +
                    ExpiryContract.ExpiryEntry.COLUMN_EXP_TITLE + " TEXT NOT NULL, " +
                    ExpiryContract.ExpiryEntry.COLUMN_EXP_DESCRIPTION + " TEXT NOT NULL, " +
                    ExpiryContract.ExpiryEntry.COLUMN_EXP_CATEGORY + " TEXT NOT NULL," +
                    ExpiryContract.ExpiryEntry.COLUMN_EXP_DATE + " TEXT NOT NULL, " +
                    ExpiryContract.ExpiryEntry.COLUMN_EXP_TIME + " TEXT NOT NULL, " +
                    ExpiryContract.ExpiryEntry.COLUMN_EXP_NOTIFICATION + " TEXT NOT NULL, " +
                    ExpiryContract.ExpiryEntry.COLUMN_EXP_NOTIFICATION_TIME + " TEXT , " +
                    ExpiryContract.ExpiryEntry.COLUMN_EXP_IMAGE + " BLOB NULL);";
            Log.d(ExpiryActivity.logTag, SQL_CREATE_TABLE);
            db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(ExpiryActivity.logTag, "In Upgrade Database: Old Version: " + oldVersion + " New Version: " + newVersion);
        if(newVersion==6) {
            String sql1 = "ALTER TABLE " + ExpiryContract.ExpiryEntry.TABLE_NAME + " ADD COLUMN " + ExpiryContract.ExpiryEntry.COLUMN_EXP_CATEGORY + " TEXT NOT NULL DEFAULT 'OTHER';";
            String sql2 = "ALTER TABLE " + ExpiryContract.ExpiryEntry.TABLE_NAME + " ADD COLUMN " + ExpiryContract.ExpiryEntry.COLUMN_EXP_NOTIFICATION_TIME + " TEXT;";
            db.execSQL(sql1);
            db.execSQL(sql2);
        }
    }
}
