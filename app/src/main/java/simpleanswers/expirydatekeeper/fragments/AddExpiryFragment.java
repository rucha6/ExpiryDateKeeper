package simpleanswers.expirydatekeeper.fragments;

/**
 * Created by ruchadeshmukh on 3/20/15.
 */

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import simpleanswers.expirydatekeeper.receivers.NotificationAlarmReceiver;
import simpleanswers.expirydatekeeper.R;
import simpleanswers.expirydatekeeper.util.Util;
import simpleanswers.expirydatekeeper.activities.ExpiryActivity;
import simpleanswers.expirydatekeeper.data.ExpiryContract;

public class AddExpiryFragment extends Fragment implements DatePickerDialog.OnDateSetListener,
         TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener{

    private EditText mTitleTextView;
    private EditText mDescriptionTextView;
    private TextView mDateView;
    private TextView mTimeView;
    private ToggleButton mNotification;
    private Button mSaveView;
    private Button mCancelView;
    private Spinner mCategorySpinnerView;
    private Spinner mNotificationSpinnerView;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
    private static SimpleDateFormat inTimeFormat = new SimpleDateFormat("hh:mm");
    private static SimpleDateFormat outTimeFormat = new SimpleDateFormat("hh:mm a");

    public AddExpiryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_expiry_item, container, false);

        mTitleTextView = (EditText) rootView.findViewById(R.id.title_textView);

        mDescriptionTextView = (EditText) rootView.findViewById(R.id.description_textView);

        mCategorySpinnerView = (Spinner) rootView.findViewById(R.id.category_spinner);
        mCategorySpinnerView.setOnItemSelectedListener(this);

        mDateView = (TextView) rootView.findViewById(R.id.expDate_btnView);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        mDateView.setText(sdf.format(System.currentTimeMillis()-100));
        mDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        mTimeView = (TextView) rootView.findViewById(R.id.expTime_btnView);
        Date dt = new Date();
        String currTime = dt.getHours() + ":" + dt.getMinutes();
        try {
            mTimeView.setText(outTimeFormat.format(inTimeFormat.parse(currTime)));
        }catch (ParseException e){
            mTimeView.setText(currTime);
        }
        mTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getFragmentManager(), "timePicker");
            }
        });

        mNotificationSpinnerView = (Spinner) rootView.findViewById(R.id.notification_spinner);
        mNotificationSpinnerView.setEnabled(false);
        mNotificationSpinnerView.setOnItemSelectedListener(this);

        mNotification = (ToggleButton) rootView.findViewById(R.id.notification_toggleView);
        mNotification.setBackgroundColor(Color.parseColor("#ff64c2f4"));
        mNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mNotification.getText().toString().equalsIgnoreCase("off"))
                    mNotificationSpinnerView.setEnabled(false);
                else
                    mNotificationSpinnerView.setEnabled(true);
            }
        });



        mSaveView = (Button) rootView.findViewById(R.id.saveView);
        mSaveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = mTitleTextView.getText().toString();
                if(Title.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                    return;
                }
                String Description = mDescriptionTextView.getText().toString();
                String Category = mCategorySpinnerView.getSelectedItem().toString();
                String expDate = mDateView.getText().toString();
                String expTime = mTimeView.getText().toString();
                String notificationValue = mNotification.getText().toString();
                String notificationChoice = mNotificationSpinnerView.getSelectedItem().toString();
                Context context = getActivity().getApplicationContext();
                String notificationTime = Util.calNotificationTime(context, expDate, expTime, notificationChoice);

                ContentValues values = new ContentValues();
                values.put(ExpiryContract.ExpiryEntry.COLUMN_EXP_TITLE, Title);
                values.put(ExpiryContract.ExpiryEntry.COLUMN_EXP_DESCRIPTION, Description);
                values.put(ExpiryContract.ExpiryEntry.COLUMN_EXP_CATEGORY, Category);
                values.put(ExpiryContract.ExpiryEntry.COLUMN_EXP_DATE, expDate);
                values.put(ExpiryContract.ExpiryEntry.COLUMN_EXP_TIME, expTime);
                values.put(ExpiryContract.ExpiryEntry.COLUMN_EXP_NOTIFICATION, notificationValue);
                values.put(ExpiryContract.ExpiryEntry.COLUMN_EXP_NOTIFICATION_TIME, notificationTime);

                //Notification
                if(notificationValue.equalsIgnoreCase("on")) {
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                        long time = formatter.parse(notificationTime).getTime();
                        Date d = new Date(time);

                        AlarmManager alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);

                        Intent intent = new Intent(getActivity().getApplicationContext(), NotificationAlarmReceiver.class);
                        intent.putExtra("Title", Title);
                        intent.putExtra("Description", Description);
                        final int _id = (int) System.currentTimeMillis();
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), _id, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                    } catch (ParseException e) {
                        Log.e(ExpiryActivity.logTag, e.getMessage());
                        e.printStackTrace();
                    }
                }

                getActivity().getContentResolver().insert(ExpiryContract.ExpiryEntry.CONTENT_URI, values);
                getActivity().finish();
            }
        });
        mCancelView = (Button) rootView.findViewById(R.id.cancelView);
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return rootView;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = (monthOfYear+1) + "/" + dayOfMonth + "/" + year;
        //for easier sorting
        try {
            Date d = dateFormat.parse(date);
            mDateView.setText(dateFormat.format(d));
        }catch (ParseException e){
            mDateView.setText(date);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        try{
            Date d = inTimeFormat.parse(time);
            mTimeView.setText(outTimeFormat.format(d));
        }catch (ParseException e) {
            mTimeView.setText(time);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
