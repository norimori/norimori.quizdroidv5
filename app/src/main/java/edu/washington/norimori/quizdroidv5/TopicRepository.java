package edu.washington.norimori.quizdroidv5;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by midori on 2/17/15.
 * Interface simply stores elements in memory from a hard-coded list initialized on startup.
 */
public interface TopicRepository {
    public List<Topic> getEverything(JSONArray a);
}
