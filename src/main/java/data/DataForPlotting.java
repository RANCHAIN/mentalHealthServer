package data;

/*



            x: [1, 2, 3, 4],
            y: [10, 15, 13, 17],
            type: 'scatter'
        };

 */

import java.util.List;

public class DataForPlotting {
    private List<Integer> x;
    private List<Double> y;
    private String type = "scatter";

    public DataForPlotting() {
    }

    public DataForPlotting(List<Integer> x, List<Double> y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public List<Integer> getX() {
        return x;
    }

    public void setX(List<Integer> x) {
        this.x = x;
    }

    public List<Double> getY() {
        return y;
    }

    public void setY(List<Double> y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
