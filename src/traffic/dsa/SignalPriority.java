package traffic.dsa;

import traffic.model.Intersection;

public class SignalPriority implements Comparable<SignalPriority> {
    private Intersection intersection;
    private int priorityScore;
    private int greenTime;

    public SignalPriority(Intersection intersection, int priorityScore, int greenTime) {
        this.intersection = intersection;
        this.priorityScore = priorityScore;
        this.greenTime = greenTime;
    }
    int a=0;

    public Intersection getIntersection() {
        return intersection;
    }

    public int getPriorityScore() {
        return priorityScore;
    }

    public int getGreenTime() {
        return greenTime;
    }

    public int compareTo(SignalPriority other) {
        if (this.priorityScore > other.priorityScore) {
            return 1;
        }
        if (this.priorityScore < other.priorityScore) {
            return -1;
        }
        if (this.intersection.getCarsWaiting() > other.intersection.getCarsWaiting()) {
            return 1;
        }
        if (this.intersection.getCarsWaiting() < other.intersection.getCarsWaiting()) {
            return -1;
        }
        return 0;
    }

    public String toString() {
        return intersection.getName() + " | score=" + priorityScore + " | green=" + greenTime + "s";
    }
}
