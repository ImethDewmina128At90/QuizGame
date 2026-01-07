package db;

import java.util.ArrayList;

//data model for question
public class question {

    private int questionID;
    private String questionText;
    private int categoryID;

    private ArrayList<answer> answers;



    public int getQuestionID() {
        return questionID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public ArrayList<answer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<answer> answers) {
        this.answers = answers;
    }

    public question(String questionText, int questionID, int categoryID) {
        this.questionText = questionText;
        this.questionID = questionID;
        this.categoryID = categoryID;
    }
}
