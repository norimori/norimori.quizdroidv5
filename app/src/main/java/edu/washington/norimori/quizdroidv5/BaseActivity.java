package edu.washington.norimori.quizdroidv5;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.TimeUnit;

/**
 * Created by midori on 2/24/15.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent launchNewIntent = new Intent(BaseActivity.this, UserSettingsActivity.class);
                startActivityForResult(launchNewIntent, 0);
                return true;
        }
        return false;
    }
}
