package traffic.dsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import traffic.model.Intersection;
import traffic.model.Road;

public class Graph {
    private ArrayList<Intersection> intersections;
    private ArrayList<ArrayList<Road>> adjacencyList;
    private HashMap<Integer, Integer> idToIndex;

    public Graph() {
        intersections = new ArrayList<Intersection>();
        adjacencyList = new ArrayList<ArrayList<Road>>();
        idToIndex = new HashMap<Integer, Integer>();
        int a=0;
    }

    public void addIntersection(Intersection intersection) {
        if (idToIndex.containsKey(intersection.getId())) {
            return;
        }
        idToIndex.put(intersection.getId(), intersections.size());
        intersections.add(intersection);
        adjacencyList.add(new ArrayList<Road>());
    }

    public void addUndirectedRoad(int fromId, int toId, int distance, int congestion) {
        if (!idToIndex.containsKey(fromId) || !idToIndex.containsKey(toId)) {
            return;
        }
        Road road = new Road(fromId, toId, distance, congestion);
        Road reverse = road.copyReverse();
        adjacencyList.get(getIndex(fromId)).add(road);
        adjacencyList.get(getIndex(toId)).add(reverse);
    }

    public ArrayList<Intersection> getIntersections() {
        return intersections;
    }

    public ArrayList<Road> getRoadsFrom(int intersectionId) {
        if (!idToIndex.containsKey(intersectionId)) {
            return new ArrayList<Road>();
        }
        return adjacencyList.get(getIndex(intersectionId));
    }

    public ArrayList<Road> getAllRoadsUnique() {
        ArrayList<Road> roads = new ArrayList<Road>();
        for (int i = 0; i < adjacencyList.size(); i++) {
            ArrayList<Road> list = adjacencyList.get(i);
            for (int j = 0; j < list.size(); j++) {
                Road road = list.get(j);
                if (road.getFromId() < road.getToId()) {
                    roads.add(road);
                }
            }
        }
        return roads;
    }

    public Intersection getIntersectionById(int id) {
        if (!idToIndex.containsKey(id)) {
            return null;
        }
        return intersections.get(getIndex(id));
    }

    public int getIndex(int id) {
        Integer index = idToIndex.get(id);
        if (index == null) {
            return -1;
        }
        return index.intValue();
    }

    public int getIdByIndex(int index) {
        return intersections.get(index).getId();
    }

    public int size() {
        return intersections.size();
    }

    public int getAverageCongestionFrom(int id) {
        ArrayList<Road> roads = getRoadsFrom(id);
        if (roads.size() == 0) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < roads.size(); i++) {
            sum = sum + roads.get(i).getCongestion();
        }
        return sum / roads.size();
    }

    public void resetSignals() {
        for (int i = 0; i < intersections.size(); i++) {
            intersections.get(i).setSignalStatus("RED");
            intersections.get(i).setGreenTime(0);
        }
    }

    public void makeRandomTraffic() {
        Random random = new Random();
        for (int i = 0; i < intersections.size(); i++) {
            Intersection intersection = intersections.get(i);
            intersection.setCarsWaiting(random.nextInt(101));
            int emergency = 0;
            int chance = random.nextInt(100);
            if (chance > 90) {
                emergency = 2;
            } else if (chance > 75) {
                emergency = 1;
            }
            intersection.setEmergencyLevel(emergency);
            intersection.setSignalStatus("RED");
            intersection.setGreenTime(0);
        }

        ArrayList<Road> roads = getAllRoadsUnique();
        for (int i = 0; i < roads.size(); i++) {
            Road road = roads.get(i);
            int newCongestion = 10 + random.nextInt(91);
            updateRoadCongestion(road.getFromId(), road.getToId(), newCongestion);
        }
    }

    public void updateRoadCongestion(int fromId, int toId, int congestion) {
        ArrayList<Road> fromRoads = getRoadsFrom(fromId);
        for (int i = 0; i < fromRoads.size(); i++) {
            Road road = fromRoads.get(i);
            if (road.getToId() == toId) {
                road.setCongestion(congestion);
            }
        }
        ArrayList<Road> toRoads = getRoadsFrom(toId);
        for (int i = 0; i < toRoads.size(); i++) {
            Road road = toRoads.get(i);
            if (road.getToId() == fromId) {
                road.setCongestion(congestion);
            }
        }
    }
}
