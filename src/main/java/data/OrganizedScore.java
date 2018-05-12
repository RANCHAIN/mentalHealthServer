package data;

public class OrganizedScore {
    private String verdict;
    private String depScore;

    public OrganizedScore() {
    }

    public OrganizedScore(String verdict, String depScore) {
        this.verdict = verdict;
        this.depScore = depScore;
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public String getDepScore() {
        return depScore;
    }

    public void setDepScore(String depScore) {
        this.depScore = depScore;
    }
}
