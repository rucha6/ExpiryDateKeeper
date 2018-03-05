package simpleanswers.expirydatekeeper.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

import simpleanswers.expirydatekeeper.R;


/**
 * Created by ruchadeshmukh on 4/20/15.
 */
public class DatePickerFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog =  new DatePickerDialog(getActivity(), (AddExpiryFragment)(getFragmentManager().findFragmentById(R.id.expiry_add_container)), year, month, day);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        return  dialog;
    }

}