package edu.washington.norimori.quizdroidv5;

import java.io.Serializable;
import java.util.List;

/**
 * Created by midori on 2/23/15.
 */
public class Topic implements Serializable {

    private String name;
    private String descShort;
    private String descLong;
    private List<Quiz> quizzes; //ArrayList of all quizzes for one topic
    private int totalQ;

    public Topic(String name, String descShort, String descLong, List<Quiz> quizzes, int totalQ) {
        this.name = name;
        this.descShort = descShort;
        this.descLong = descLong;
        this.quizzes = quizzes;
        this.totalQ = totalQ;
    }

    public void setName(String value) {
        name = value;
    }

    public String getName() {
        return name;
    }

    public void setDescriptionShort(String value) {
        descShort = value;
    }

    public String getDescriptionShort() {
        return descShort;
    }

    public void setDescriptionLong(String value) {
        descLong = value;
    }

    public String getDescriptionLong() {
        return descLong;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> value) {
        quizzes = value;
    }

    public void setTotalQ(int value) {
        totalQ = value;
    }

    public int getTotalQ() {
        return totalQ;
    }

}
