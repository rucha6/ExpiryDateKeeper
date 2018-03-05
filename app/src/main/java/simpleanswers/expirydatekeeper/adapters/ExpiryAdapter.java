package simpleanswers.expirydatekeeper.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import simpleanswers.expirydatekeeper.R;
import simpleanswers.expirydatekeeper.data.ExpiryContract;
import simpleanswers.expirydatekeeper.util.Util;


/**
 * Created by ruchadeshmukh on 3/29/15.
 */
public class ExpiryAdapter extends CursorAdapter {

    public ExpiryAdapter(Context context, Cursor c, int flags){super(context, c, flags);}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = R.layout.list_item_expiry;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.daysLeft.setText(Util.calculateDaysLeft(
                cursor.getString(cursor.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_DATE))));
        viewHolder.titleView.setText(
                cursor.getString(cursor.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_TITLE)));
//        viewHolder.descriptionView.setText(
//                cursor.getString(cursor.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_DESCRIPTION)));
        viewHolder.dateView.setText(
                cursor.getString(cursor.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_DATE)));
        viewHolder.categoryView.setText(
                cursor.getString(cursor.getColumnIndex(ExpiryContract.ExpiryEntry.COLUMN_EXP_CATEGORY)));
    }

    public static class ViewHolder {
        public final TextView daysLeft;
        public final TextView titleView;
        public final TextView categoryView;
//        public final TextView descriptionView;
        public final TextView dateView;

        public ViewHolder(View view){
            daysLeft = (TextView) view.findViewById(R.id.list_item_daysLeft);
            titleView = (TextView) view.findViewById(R.id.list_item_expiry_title);
            categoryView = (TextView)view.findViewById(R.id.list_item_expiry_category);
//            descriptionView = (TextView)view.findViewById(R.id.list_item_expiry_description);
            dateView = (TextView) view.findViewById(R.id.list_item_expiry_date);
        }
    }
}
