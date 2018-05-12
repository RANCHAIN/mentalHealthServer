package data;

// {Positive: 0.020492774,Negative: 0.012798294,Neutral: 0.9655484,Mixed: 0.0011606131}"
public class Score {
    private Double Positive;
    private Double Negative;
    private Double Neutral;
    private Double Mixed;

    public Score() {
    }

    public Score(Double positive, Double negative, Double neutral, Double mixed) {
        Positive = positive;
        Negative = negative;
        Neutral = neutral;
        Mixed = mixed;
    }

    public Double getPositive() {
        return Positive;
    }

    public void setPositive(Double positive) {
        Positive = positive;
    }

    public Double getNegative() {
        return Negative;
    }

    public void setNegative(Double negative) {
        Negative = negative;
    }

    public Double getNeutral() {
        return Neutral;
    }

    public void setNeutral(Double neutral) {
        Neutral = neutral;
    }

    public Double getMixed() {
        return Mixed;
    }

    public void setMixed(Double mixed) {
        Mixed = mixed;
    }
}
