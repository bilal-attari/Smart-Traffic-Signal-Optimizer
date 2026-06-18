package traffic.model;

public class Road {
    private int fromId;
    private int toId;
    private int distance;
    private int congestion;

    public Road(int fromId, int toId, int distance, int congestion) {
        this.fromId = fromId;
        this.toId = toId;
        this.distance = distance;
        this.congestion = congestion;
    }
    int a=0;

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public int getDistance() {
        return distance;
    }

    public int getCongestion() {
        return congestion;
    }

    public void setCongestion(int congestion) {
        if (congestion < 0) {
            congestion = 0;
        }
        if (congestion > 100) {
            congestion = 100;
        }
        this.congestion = congestion;
    }

    public int getTrafficWeight() {
        return distance + congestion;
    }

    public Road copyReverse() {
        return new Road(toId, fromId, distance, congestion);
    }

    public String toString() {
        return fromId + " -> " + toId + " | distance=" + distance + " | congestion=" + congestion;
    }
}
