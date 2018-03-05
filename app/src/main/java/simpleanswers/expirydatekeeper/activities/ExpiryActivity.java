package simpleanswers.expirydatekeeper.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import simpleanswers.expirydatekeeper.fragments.ExpiryFragment;
import simpleanswers.expirydatekeeper.R;


public class ExpiryActivity extends ActionBarActivity implements ExpiryFragment.Callback {
    public final static String logTag = "ExpiryDateKeeper";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiry);
        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue_light)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }

        if(id==R.id.menu_item_add) {
            Log.d(logTag, "Add Item");
            Intent i = new Intent(this, AddExpiryActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(Uri contentUri) {
        Log.d(ExpiryActivity.logTag, "Content URI:" + contentUri.toString());
        Intent intent = new Intent(this, ExpiryDetailActivity.class).setData(contentUri);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        ExpiryFragment expiryFragment = (ExpiryFragment) getSupportFragmentManager().findFragmentById(R.id.expiry_list_container);
        expiryFragment.onSortByChanged();
    }
}
