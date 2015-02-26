package edu.washington.norimori.quizdroidv5;

import android.app.Application;
import android.util.Log;

/**
 * Created by midori on 2/23/15.
 */


//After extending Application, QuizApp can be accessed from any Activity using the getApplication method.
public class QuizApp extends Application {

    //Perform initializations at app startup.
    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize singletons so their instances are bound to this application's process.
        initSingleton();
        Log.i("yay", "Overriding onCreate() in custom Application class.");
    }

    //Initialize the instance of QuizAppSingleton, a singleton.
    protected void initSingleton() {
        QuizAppSingleton.initInstance();
    }

}
