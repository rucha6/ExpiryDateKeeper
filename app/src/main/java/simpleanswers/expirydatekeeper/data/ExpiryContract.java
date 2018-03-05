package simpleanswers.expirydatekeeper.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import simpleanswers.expirydatekeeper.activities.ExpiryActivity;

/**
 * Created by ruchadeshmukh on 3/20/15.
 */
public class ExpiryContract {
    public static final String CONTENT_AUTHORITY = "simpleanswers.expirydatekeeper";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EXPIRYLIST = "expiry";
    public static final String PATH_EXPIRYLISTITEM = "expiry/#";

    public static final class ExpiryEntry implements BaseColumns {

        public static final String TABLE_NAME="expiry_keeper";

        public static final String COLUMN_EXP_TITLE = "exp_title";
        public static final String COLUMN_EXP_DESCRIPTION = "exp_description";
        public static final String COLUMN_EXP_CATEGORY="exp_category";
        public static final String COLUMN_EXP_DATE = "exp_date";
        public static final String COLUMN_EXP_TIME = "exp_time";
        public static final String COLUMN_EXP_NOTIFICATION = "exp_notification";
        public static final String COLUMN_EXP_NOTIFICATION_TIME="exp_notification_time";
        public static final String COLUMN_EXP_IMAGE = "exp_image";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXPIRYLIST).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPIRYLIST;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPIRYLISTITEM;

        public static Uri buildExpiryList(){
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildExpiryListItemUri(long id){
            Log.v(ExpiryActivity.logTag, "ExpiryContract:ID: " + id);
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
