package simpleanswers.expirydatekeeper.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import simpleanswers.expirydatekeeper.R;
import simpleanswers.expirydatekeeper.data.ExpiryContract;
import simpleanswers.expirydatekeeper.util.Util;

/**
 * Created by ruchadeshmukh on 3/29/15.
 */
public class ExpiryDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int DETAIL_LOADER = 0;
    public static final String DETAIL_URI = "URI";
    private Uri mUri;

    private static final String[] DETAIL_COLUMNS = {
            ExpiryContract.ExpiryEntry.TABLE_NAME + "." + ExpiryContract.ExpiryEntry._ID,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_TITLE,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_DESCRIPTION,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_CATEGORY,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_DATE,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_TIME,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_NOTIFICATION,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_NOTIFICATION_TIME,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_IMAGE
    };

    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private TextView mCategoryView;
    private TextView mDateView;
    private TextView mTimeView;
    private TextView mDaysLeft;
    private TextView mNotificationView;
    private TextView mNotificationTimeView;

    public ExpiryDetailFragment() {setHasOptionsMenu(true);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expiry_detail, container, false);
        mTitleTextView = (TextView) rootView.findViewById(R.id.title_textView);
        mDescriptionTextView = (TextView)rootView.findViewById(R.id.description_textView);
        mCategoryView = (TextView)rootView.findViewById(R.id.category_textView);
        mDateView = (TextView) rootView.findViewById(R.id.expDate_textView);
        mTimeView = (TextView) rootView.findViewById(R.id.expDate_timeView);
        mNotificationView = (TextView)rootView.findViewById(R.id.notification_toggleView);
        mNotificationTimeView = (TextView) rootView.findViewById(R.id.notification_time);
        mDaysLeft = (TextView)rootView.findViewById(R.id.daysleft_textView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        if(savedInstanceState!=null)
            mUri = Uri.parse(savedInstanceState.getString("detail_uri"));
        else {
            Bundle arguments = getArguments();
            if (arguments != null) {
                mUri = arguments.getParcelable(ExpiryDetailFragment.DETAIL_URI);
            }
        }
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("detail_uri", mUri.toString());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(mUri!=null){
            String itemId = mUri.getLastPathSegment();
            String selection = ExpiryContract.ExpiryEntry._ID + "=?";
            String[] selectionArgs = new String[]{itemId};
            return  new CursorLoader(getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    selection,
                    selectionArgs,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        data.moveToFirst();
        mTitleTextView.setText(data.getString(data.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_TITLE)));
        mDescriptionTextView.setText(data.getString(data.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_DESCRIPTION)));
        mCategoryView.setText(data.getString(data.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_CATEGORY)));

        String date = data.getString(data.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_DATE));
        mDateView.setText(date);

        String time = data.getString(data.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_TIME));
        mTimeView.setText(time);

        String daysLeft = Util.calculateDaysLeft(data.getString(data.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_DATE)));
        mDaysLeft.setText(daysLeft);

        String notification = data.getString(data.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_NOTIFICATION));
        mNotificationView.setText(notification);

        if (notification.equalsIgnoreCase("on"))
            mNotificationTimeView.setText(" : " + data.getString(data.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_NOTIFICATION_TIME)));
        else
            mNotificationTimeView.setText("");
    }



    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
    }
}
