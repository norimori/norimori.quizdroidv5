package edu.washington.norimori.quizdroidv5;

import java.io.Serializable;
import java.util.List;

/**
 * Created by midori on 2/23/15.
 */
public class Question implements Serializable{

    private String qText;
    private List<String> possibleA;
    private int indexOfCorrA;

    public Question(String qText, List<String> possibleA, int indexOfCorrA) {
        this.qText = qText;
        this.possibleA = possibleA;
        this.indexOfCorrA = indexOfCorrA;
    }

    public void setqText(String value) {
        qText = value;
    }

    public String getqText() {
        return qText;
    }

    public void setPossibleA(List<String> value) {
        possibleA = value;
    }

    public List<String> getPossibleA() {
        return possibleA;
    }

    public String getChosenA(int value) {
        return possibleA.get(value);
    }

    public void setIndexOfCorrA(int value) {
        indexOfCorrA = value;
    }

    public int getIndexOfCorrA() {
        return indexOfCorrA;
    }

    public String getCorrAText() {
        return possibleA.get(indexOfCorrA);
    }

}
