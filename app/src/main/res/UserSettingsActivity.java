package edu.washington.norimori.quizdroidv4;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.TimeUnit;


public class UserSettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Intent alarmIntent;
    private PendingIntent pendingIntent;
    private SharedPreferences sharedPrefs;

    private String URLText;
    private String freqText;
    private Boolean alarmUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);
        alarmIntent = new Intent(UserSettingsActivity.this, AlarmReceiver.class);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        alarmUp = (PendingIntent.getBroadcast(UserSettingsActivity.this, 0, alarmIntent, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmUp) {
            Log.d("yay", "Alarm Already Exists. Now Stopping.");
            alarmStop();
        }

        URLText = sharedPrefs.getString("prefURL", "NOURL");
        freqText = sharedPrefs.getString("prefFrequency", "NOFREQ");
        Log.d("yay", "IS IT DEFAULT URL? " + URLText); //www.iamawesome.com
        Log.d("yay", "IS IT DEFAULT FREQ? " + freqText); //
        //Start alarm if values are present for URL and Frequency.
        if (URLText.isEmpty() || freqText.isEmpty() || URLText.equals("NOURL") || freqText.equals("NOFREQ")) {
            alarmStop();
        } else {
            alarmStart();
        }
    }

    //Start alarm when changes have been made to preferences
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("yay", "onSharedPreferenceChanged: " + URLText);
        Log.d("yay", "onSharedPreferenceChanged: " + freqText);
        if(alarmUp) {
            alarmStop();
        }
    }

    //Start alarm. Display URL every given interval.
    public void alarmStart() {
        alarmIntent.putExtra("URL", URLText);
        pendingIntent = PendingIntent.getBroadcast(UserSettingsActivity.this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Log.d("yay", "In alarmStart(), URL is " + URLText + " and Frequency is " + freqText);
        long interval = TimeUnit.MINUTES.toMillis(Long.valueOf(freqText));
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Log.d("yay", "Alarm Started!");
    }

    //Stop alarm
    public void alarmStop() {
        Intent intent = new Intent(UserSettingsActivity.this, AlarmReceiver.class); //Recreate Intent
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.d("yay", "Alarm Cancelled...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
