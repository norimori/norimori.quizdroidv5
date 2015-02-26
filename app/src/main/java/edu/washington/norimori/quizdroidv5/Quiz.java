package edu.washington.norimori.quizdroidv5;

/**
 * Created by midori on 2/23/15.
 * Essentially each "Question".
 */
public class Quiz {

    private String qText;
    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private int correctA;

    public Quiz(String qText, String a1, String a2, String a3, String a4, int correctA) {
        this.qText = qText;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
        this.correctA = correctA;
    }

    public String getqText() {
        return qText;
    }

    public void setqText(String value) {
        qText = value;
    }

    public String getA1() {
        return a1;
    }

    public void setA1(String value) {
        a1 = value;
    }

    public String getA2() {
        return a2;
    }

    public void setA2(String value) {
        a2 = value;
    }

    public String getA3() {
        return a3;
    }

    public void setA3(String value) {
        a3 = value;
    }

    public String getA4() {
        return a4;
    }

    public void setA4(String value) {
        a4 = value;
    }

    public int getCorrectA() {
        return correctA;
    }

    public void setCorrectA(int value) {
        correctA = value;
    }

    public String getCorrectAText() {
        if (getCorrectA() == 1) {
            return getA1();
        } else if (getCorrectA() == 2) {
            return getA2();
        } else if (getCorrectA() == 3) {
            return getA3();
        } else {
            return getA4();
        }
    }

}
