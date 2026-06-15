package traffic.dsa;

import java.util.ArrayList;
import traffic.model.Road;

public class MSTAlgorithm {
    public MSTResult makeMinimumSpanningTree(Graph graph) {
        int n = graph.size();
        ArrayList<Road> selectedRoads = new ArrayList<Road>();
        if (n == 0) {
            return new MSTResult(selectedRoads, 0);
        }

        boolean[] visited = new boolean[n];
        visited[0] = true;
        int totalDistance = 0;

        while (selectedRoads.size() < n - 1) {
            Road bestRoad = null;
            int bestDistance = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (visited[i]) {
                    int fromId = graph.getIdByIndex(i);
                    ArrayList<Road> roads = graph.getRoadsFrom(fromId);
                    for (int j = 0; j < roads.size(); j++) {
                        Road road = roads.get(j);
                        int toIndex = graph.getIndex(road.getToId());
                        if (!visited[toIndex] && road.getDistance() < bestDistance) {
                            bestDistance = road.getDistance();
                            bestRoad = road;
                        }
                    }
                }
            }

            if (bestRoad == null) {
                break;
            }

            selectedRoads.add(bestRoad);
            totalDistance = totalDistance + bestRoad.getDistance();
            visited[graph.getIndex(bestRoad.getToId())] = true;
        }

        return new MSTResult(selectedRoads, totalDistance);
    }
}
