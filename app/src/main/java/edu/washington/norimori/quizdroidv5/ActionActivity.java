package edu.washington.norimori.quizdroidv5;

import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ActionActivity extends BaseActivity {

    private String chosenTopicName; //Name of chosen topic from list
    private List<Topic> allTopics; //ArrayList of all available Topic objects
    private int indexOfCurrQ = 0; //Keep track of current Question
    private int totalCorrQ = 0; //Keep track of number of correctly answered questions

    private RadioGroup radioGroup; //Question choices for current quiz
    private Button btnSubmit; //Submit answer to proceed to Answer Summary fragment
    private Quiz currQuiz; //Current quiz in topic
    private Topic topic; //Topic object of chosen quiz
    private int radioId; //Chosen radio button for one quiz
    private String chosenAText; //Text of chosen answer

    private Button btnAction; //"Next" or "Finish" action during quiz

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        //Display initial Topic Overview fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new topicOverviewFragment())
                    .commit();
        }

        //Retrieve chosen topic name
        Intent launchedMe = getIntent();
        chosenTopicName = launchedMe.getStringExtra("chosenTopic");
    }

    //Adjust visiblity of "Submit" button when radio button is selected
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            btnSubmit.setVisibility(btnSubmit.VISIBLE);
        } else {
            btnSubmit.setVisibility(btnSubmit.INVISIBLE);
        }
    }

    //Returns true if all questions have been answered.
    public boolean finished() {
        return indexOfCurrQ == (topic.getTotalQ());
    }

    //Fragment displaying quiz topic overview.
    public class topicOverviewFragment extends Fragment {

        public topicOverviewFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_topicoverview, container, false);

            //Display chosen topic overview
            TextView topicName = (TextView) rootView.findViewById(R.id.topic);
            TextView desc = (TextView) rootView.findViewById(R.id.desc);
            TextView totalQ = (TextView) rootView.findViewById(R.id.totalQ);
            topic = QuizAppSingleton.getInstance().getTopic(chosenTopicName);
            topicName.setText(topic.getName());
            desc.setText("Topic Description: " + topic.getDescriptionLong());
            totalQ.setText("Total Number of Questions: " + topic.getTotalQ());


            //Transition to first quiz in topic
            Button btnBegin = (Button) rootView.findViewById(R.id.btnBegin);
            btnBegin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, 0, 0)
                            .replace(R.id.container, new questionFragment())
                            .commit();
                }
            });

            return rootView;
        }
    }

    //Fragment displaying single quiz.
    public class questionFragment extends Fragment {

        public questionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_question, container, false);

            //Make "Submit" button invisible when no quiz answer is selected.
            btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
            btnSubmit.setVisibility(btnSubmit.INVISIBLE);

            //Display current quiz
            TextView qText = (TextView) rootView.findViewById(R.id.qText);
            List<Quiz> quizzes = topic.getQuizzes();
            currQuiz = quizzes.get(indexOfCurrQ); //Get current quiz player is on
            qText.setText(currQuiz.getqText());

            //Display current quiz's choices
            RadioButton option1 = (RadioButton) rootView.findViewById(R.id.option1);
            option1.setText(currQuiz.getA1());
            RadioButton option2 = (RadioButton) rootView.findViewById(R.id.option2);
            option2.setText(currQuiz.getA2());
            RadioButton option3 = (RadioButton)rootView.findViewById(R.id.option3);
            option3.setText(currQuiz.getA3());
            RadioButton option4 = (RadioButton) rootView.findViewById(R.id.option4);
            option4.setText(currQuiz.getA4());

            //Transition to Answer Summary of current quiz
            radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);
            Button btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(radioGroup.getCheckedRadioButtonId() != -1) { //If any radio button is checked
                        int id= radioGroup.getCheckedRadioButtonId();
                        View radioButton = radioGroup.findViewById(id);
                        RadioButton selected = (RadioButton) radioGroup.findViewById(id);
                        chosenAText = (String) selected.getText();
                        radioId = radioGroup.indexOfChild(radioButton);
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, 0, 0)
                                .replace(R.id.container, new answerSummaryFragment())
                                .commit();
                    }
                }
            });

            return rootView;
        }

    }

    //Fragment displaying single question's summary.
    public class answerSummaryFragment extends Fragment {

        public answerSummaryFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_answersummary, container, false);

            //Display user's chosen answer
            TextView chosenA = (TextView) rootView.findViewById(R.id.chosenA);
            chosenA.setText("You chose: " + chosenAText);

            //Display correct answer and corresponding message
            TextView correctA = (TextView) rootView.findViewById(R.id.correctA);
            if (currQuiz.getCorrectA() == (radioId + 1)) { //If the chosen answer is correct
                correctA.setText("You're right! The correct answer is: " + currQuiz.getCorrectAText());
                totalCorrQ++;
            } else {
                correctA.setText("Nope. The correct answer is: " + currQuiz.getCorrectAText());
            }
            indexOfCurrQ++;

            TextView score = (TextView) rootView.findViewById(R.id.score);
            score.setText("You have answered " + totalCorrQ + " out of " + topic.getTotalQ() + " correct.");

            //Next button to go to the next question (or Finish button to end quiz)
            btnAction = (Button) rootView.findViewById(R.id.action);
            if (finished()) {
                btnAction.setText("Finish!");
            } else {
                btnAction.setText("Next");
            }

            //Execute action based on quiz progress. Go to next question or finish quiz.
            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (finished()) {
                        Intent nextActivity = new Intent(ActionActivity.this, MainActivity.class);
                        startActivity(nextActivity);
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, 0, 0)
                                .replace(R.id.container, new questionFragment())
                                .commit();
                    }
                }
            });

            return rootView;
        }
    }

}
