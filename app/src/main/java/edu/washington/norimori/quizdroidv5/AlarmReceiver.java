package edu.washington.norimori.quizdroidv5;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by midori on 2/23/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String URL = intent.getStringExtra("URL");
        Toast.makeText(context, "Checking: " + URL, Toast.LENGTH_SHORT).show();
        Log.d("yay", "@AlarmReceiver; Checked for update at: " + URL);
    }
}
