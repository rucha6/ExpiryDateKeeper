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
import android.widget.AdapterView;
import android.widget.ListView;

import simpleanswers.expirydatekeeper.R;
import simpleanswers.expirydatekeeper.adapters.ExpiryAdapter;
import simpleanswers.expirydatekeeper.data.ExpiryContract;

/**
 * Created by ruchadeshmukh on 3/20/15.
 */
public class ExpiryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    ExpiryAdapter mExpiryAdapter;
    private ListView mListView;
    static final int EXPIRY_LOADER = 0;

    public ExpiryFragment() {}

    public static final String[] EXPIRY_COLUMNS = {
            ExpiryContract.ExpiryEntry.TABLE_NAME + "." + ExpiryContract.ExpiryEntry._ID,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_TITLE,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_DESCRIPTION,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_CATEGORY,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_DATE,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_NOTIFICATION,
            ExpiryContract.ExpiryEntry.COLUMN_EXP_IMAGE
    };

    @Override
    public  void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
        getLoaderManager().initLoader(EXPIRY_LOADER, null, this);
        super.onActivityCreated(savedInstance);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expiry, container, false);
        mExpiryAdapter = new ExpiryAdapter(getActivity(), null, 0);

        mListView = (ListView) rootView.findViewById(R.id.listview_expiryFragment);
        mListView.setAdapter(mExpiryAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = mExpiryAdapter.getCursor();
                cursor.moveToPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(ExpiryContract.ExpiryEntry.buildExpiryListItemUri(
                                            (cursor.getInt(cursor.getColumnIndex(ExpiryContract.ExpiryEntry._ID)))));
                }

            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = mExpiryAdapter.getCursor();
                c.moveToPosition(position);
                long rowId = c.getLong(c.getColumnIndex(ExpiryContract.ExpiryEntry._ID));
                getActivity().getContentResolver().delete(ExpiryContract.ExpiryEntry.CONTENT_URI, ExpiryContract.ExpiryEntry._ID + "=" + rowId, null);
                mExpiryAdapter.notifyDataSetChanged();
                return true;
            }
        });

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri expiryList = ExpiryContract.ExpiryEntry.buildExpiryList();
        return new CursorLoader(getActivity(),
                expiryList,
                EXPIRY_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mExpiryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mExpiryAdapter.swapCursor(null);

    }

    public interface  Callback {
        void onItemSelected(Uri detailUri);
    }

    public void onSortByChanged(){
        getLoaderManager().restartLoader(EXPIRY_LOADER, null, this);
    }
}