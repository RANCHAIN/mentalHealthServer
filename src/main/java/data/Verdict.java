package data;

public class Verdict {


    private String state;
    private String score;

    public Verdict(String state, String score) {
        this.state = state;
        this.score = score;
    }

    public Verdict() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
