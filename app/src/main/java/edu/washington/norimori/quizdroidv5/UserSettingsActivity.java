package edu.washington.norimori.quizdroidv5;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
        Log.d("yay", "Initial grab for URL & freqText:  " + URLText + " | " + freqText);

        airplaneModeMessage();
        networkMessage();

        //Start download only if values are present for URL and Frequency.
        if (URLText.isEmpty() || freqText.isEmpty() || URLText.equals("NOURL") || freqText.equals("NOFREQ")) {
            Log.d("yay", "URL and Frequency are not given.");
        } else {
            alarmStart();
        }
    }

    //Start alarm when changes have been made to preferences
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
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

    //Gets state of airplane mode.
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    //Check Airplane Mode state and display appropriate message.
    public void airplaneModeMessage() {
        if (isAirplaneModeOn(UserSettingsActivity.this)) {
            Log.d("yay", "Airplane Mode is On");
            new AlertDialog.Builder(this)
                    .setTitle("Airplane Mode")
                    .setMessage("No access to internet. Airplane Mode is On. Please go to settings to turn it off.")
                    .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) { //Take user to
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS), 0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            Log.d("yay", "Airplane Mode is Off. It's all good");
        }
    }

    //Checks if there is no signal.
    public void networkMessage() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (networkAvailable) {
            Log.d("yay", "Signal is available");
        } else {
            Log.d("yay", "Signal is not available");
            new AlertDialog.Builder(this)
            .setTitle("Signal Availability")
            .setMessage("No access to internet. Signal is not available. Leaving Application.")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
        }
    }

}
