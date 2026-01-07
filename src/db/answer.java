package db;

public class answer {
    private int answerID;
    private String answerText;
    private boolean isCorrect;
    private int questionID;

    public int getAnswerID() {
        return answerID;
    }

    public String getAnswerText() {
        return answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
    public int getQuestionID() {
        return questionID;
    }

    public answer(int answerID, int questionID, String answerText, boolean isCorrect) {
        this.answerID = answerID;
        this.questionID = questionID;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

}
