package traffic.dsa;

import java.util.ArrayList;
import traffic.model.Road;

public class DijkstraAlgorithm {
    public PathResult findShortestPath(Graph graph, int sourceId, int destinationId) {
        int n = graph.size();
        int sourceIndex = graph.getIndex(sourceId);
        int destinationIndex = graph.getIndex(destinationId);

        if (sourceIndex == -1 || destinationIndex == -1) {
            return new PathResult(new ArrayList<Integer>(), -1);
        }

        int[] distance = new int[n];
        int[] parent = new int[n];
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            distance[i] = Integer.MAX_VALUE / 4;
            parent[i] = -1;
            visited[i] = false;
        }
        distance[sourceIndex] = 0;

        for (int count = 0; count < n; count++) {
            int current = getMinDistanceIndex(distance, visited);
            if (current == -1) {
                break;
            }
            visited[current] = true;

            int currentId = graph.getIdByIndex(current);
            ArrayList<Road> roads = graph.getRoadsFrom(currentId);
            for (int i = 0; i < roads.size(); i++) {
                Road road = roads.get(i);
                int neighborIndex = graph.getIndex(road.getToId());
                int newDistance = distance[current] + road.getTrafficWeight();
                if (!visited[neighborIndex] && newDistance < distance[neighborIndex]) {
                    distance[neighborIndex] = newDistance;
                    parent[neighborIndex] = current;
                }
            }
        }

        ArrayList<Integer> path = makePath(parent, sourceIndex, destinationIndex, graph);
        if (distance[destinationIndex] >= Integer.MAX_VALUE / 4) {
            return new PathResult(new ArrayList<Integer>(), -1);
        }
        return new PathResult(path, distance[destinationIndex]);
    }

    private int getMinDistanceIndex(int[] distance, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < distance.length; i++) {
            if (!visited[i] && distance[i] < min) {
                min = distance[i];
                index = i;
            }
        }
        return index;
    }

    private ArrayList<Integer> makePath(int[] parent, int sourceIndex, int destinationIndex, Graph graph) {
        ArrayList<Integer> reverse = new ArrayList<Integer>();
        int current = destinationIndex;
        while (current != -1) {
            reverse.add(graph.getIdByIndex(current));
            if (current == sourceIndex) {
                break;
            }
            current = parent[current];
        }

        ArrayList<Integer> path = new ArrayList<Integer>();
        for (int i = reverse.size() - 1; i >= 0; i--) {
            path.add(reverse.get(i));
        }
        if (path.size() == 0 || path.get(0) != graph.getIdByIndex(sourceIndex)) {
            return new ArrayList<Integer>();
        }
        return path;
    }
}
