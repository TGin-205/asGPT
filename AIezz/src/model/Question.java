
package model;

public class Question {

    private String id;
    private String question;
    private String answerS;
    private String difficulty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerS() {
        return answerS;
    }

    public void setAnswerS(String answerS) {
        this.answerS = answerS;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Question(String id, String question, String answerS, String difficulty) {
        this.id = id;
        this.question = question;
        this.answerS = answerS;
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "Question{" + "id=" + id + ", question=" + question + ", answerS=" + answerS + ", difficulty=" + difficulty + '}';
    }

}
