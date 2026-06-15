package traffic.dsa;

import java.util.ArrayList;

public class PathResult {
    private ArrayList<Integer> path;
    private int distance;

    public PathResult(ArrayList<Integer> path, int distance) {
        this.path = path;
        this.distance = distance;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

    public int getDistance() {
        return distance;
    }

    public String getPathAsText() {
        if (path == null || path.size() == 0) {
            return "No path found";
        }
        String text = "";
        for (int i = 0; i < path.size(); i++) {
            text = text + path.get(i);
            if (i < path.size() - 1) {
                text = text + " -> ";
            }
        }
        return text;
    }
}
