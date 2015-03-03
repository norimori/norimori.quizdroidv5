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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MainActivity extends BaseActivity {

    private Button btnStop;
    private PendingIntent pendingIntent;

    private List<Map<String, String>> list; //ListView Quiz Title, Quiz Description
    private List<Topic> allData; //All Topic objects with their respective Question objects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        //Load JSON file data, populate ListView, and create all Topic and Question objects for all quizzes.
        JSONArray data = null;
        try {
            data = new JSONArray(loadJSONFromAsset());
            allData = QuizAppSingleton.getInstance().getEverything(data);
            //allData = new ArrayList<Topic>();
            list = new ArrayList<Map<String, String>>();
            for (int i = 0; i < data.length(); i++) { //Populate ListView
                JSONObject topic = data.getJSONObject(i);
                String title = topic.getString("title");
                String desc = topic.getString("desc");
                Map<String, String> pair = new HashMap<String, String>(2);
                pair.put("title", title);
                pair.put("descShort", desc);
                list.add(pair);
            }
            SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_expandable_list_item_2,
                    new String[] {"title", "descShort"}, new int[] {android.R.id.text1, android.R.id.text2});
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextActivity = new Intent(MainActivity.this, ActionActivity.class);
                nextActivity.putExtra("chosenTopic", allData.get(position).getName());
                Log.d("MainActivity", allData.get(position).getDescriptionShort());
                Log.d("MainActivity", allData.get(position).getDescriptionShort());
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

    //Get JSON file from assets
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("defaultquizdata.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.d("MainActivity", "Null JSON file");
            ex.printStackTrace();
            return null;
        }
        Log.d("MainActivity", "Loaded JSON file");
        return json;
    }

    //Stop alarm
    public void alarmStop() {
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class); //Recreate Intent
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.d("MainActivity", "Alarm Cancelled...");
    }
}
