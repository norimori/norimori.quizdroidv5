package edu.washington.norimori.quizdroidv5;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by midori on 2/23/15.
 * Singleton object form of QuizApp.
 */
public class QuizAppSingleton implements TopicRepository {

    private static QuizAppSingleton instance; //Singleton instance of QuizApp
    private List<Topic> allTopics; //Collection of all available quiz topics

    //Initialize instance.
    //Allows internal instance to be initialized only once, which is done in QuizApp class.
    public static void initInstance() {
        if (instance == null) {
            instance = new QuizAppSingleton();
        } else {
            throw new RuntimeException("You cannot instantiate more than one QuizAppSingleton");
        }
    }

    //Get instance from any part of application.
    public static QuizAppSingleton getInstance() {
        return instance;
    }

    //Constructor is private because this is a singleton (won't allow creation of multiple instances).
    private QuizAppSingleton() {}

    //Returns index of given topic in topic list.
    public int getTopicIndex(String topicName) {
        for (int i = 0; i < allTopics.size(); i++) {
            if (allTopics.get(i).getName().equalsIgnoreCase(topicName)) {
                return i;
            }
        }
        return -1; //Did not find topic in topic list.
    }

    /**
     * Methods from TopicRepository interface.
     */
    //Returns topic object of given topic name.
    public Topic getTopic(String topicName) {
        if(getTopicIndex(topicName) != -1) {
            return allTopics.get(getTopicIndex(topicName));
        }
        return null;
    }

    //Adds topic to topic list.
    public void addTopic(Topic newTopic) {
        allTopics.add(newTopic);
    }

    //Updates current topic in list with given topic. (Must have same name).
    public void updateTopic(Topic topic) {
        if(getTopicIndex(topic.getName()) != -1) {
            allTopics.set(getTopicIndex(topic.getName()), topic);
        }
    }

    //Removes topic from topic list.
    public void deleteTopic(Topic topic) {
        if(getTopicIndex(topic.getName()) != -1) {
            allTopics.remove(topic);
        }
    }

    public static String AssetJSONFile (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open("defaultquizdata.json");
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        Log.d("QuizAppSingleton", new String(formArray));

        return new String(formArray);
    }

    //Returns list of all topics.
    public List<Topic> getEverything(JSONArray data) {
        allTopics = new ArrayList<Topic>();
        for (int i = 0; i < data.length(); i++) { //Looping through topics
            JSONObject topic = null;
            try {
                topic = data.getJSONObject(i);
                String title = topic.getString("title");
                String desc = topic.getString("desc");
                //Create collection of Topics
                List<Quiz> Questions = new ArrayList<Quiz>(); //Store all questions per topic
                JSONArray questions = topic.getJSONArray("questions");
                for (int j = 0; j < questions.length(); j++) {
                    JSONObject question = questions.getJSONObject(j);
                    String text = question.getString("text");
                    int answerIndex = Integer.parseInt(question.getString("answer"));
                    Quiz q = new Quiz(answerIndex);
                    JSONArray answers = question.getJSONArray("answers");
                    q.setA1(answers.getString(0));
                    q.setA2(answers.getString(1));
                    q.setA3(answers.getString(2));
                    q.setA4(answers.getString(3));
                    q.setqText(text);
                    Questions.add(q);
                }
                Topic t = new Topic(title, desc, desc, Questions, questions.length());
                allTopics.add(t);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allTopics;
    }
}
