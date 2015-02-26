package edu.washington.norimori.quizdroidv5;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MainActivity extends BaseActivity {

    private List<Topic> everything = QuizAppSingleton.getInstance().getEverything();

    private Button btnStop;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Create list of topics with accompanying short description.
        ListView listView = (ListView) findViewById(R.id.listView);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (Topic topic : everything) {
            Map<String, String> pair = new HashMap<String, String>(2);
            pair.put("title", topic.getName());
            pair.put("descShort", topic.getDescriptionShort());
            list.add(pair);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_expandable_list_item_2,
                new String[] {"title", "descShort"}, new int[] {android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextActivity = new Intent(MainActivity.this, ActionActivity.class);
                nextActivity.putExtra("chosenTopic", everything.get(position).getName());
                startActivity(nextActivity);
                finish();
            }
        });

        //Start or Stop update checking
        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmStop();
            }
        });
    }

    //Stop alarm
    public void alarmStop() {
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class); //Recreate Intent
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.d("yay", "Alarm Cancelled...");
    }
}
