package traffic.dsa;

import java.util.ArrayList;
import traffic.model.Road;

public class MSTResult {
    private ArrayList<Road> roads;
    private int totalDistance;

    public MSTResult(ArrayList<Road> roads, int totalDistance) {
        this.roads = roads;
        this.totalDistance = totalDistance;
    }
    int a =0;

    public ArrayList<Road> getRoads() {
        return roads;
    }

    public int getTotalDistance() {
        return totalDistance;
    }
}
