package traffic.dsa;

import java.util.ArrayList;
import traffic.model.Intersection;

public class GreedySignalOptimizer {
    public OptimizerResult optimize(Graph graph) {
        MyQueue<Intersection> waitingQueue = new MyQueue<Intersection>();
        MaxHeap<SignalPriority> priorityHeap = new MaxHeap<SignalPriority>();
        ArrayList<SignalPriority> finalOrder = new ArrayList<SignalPriority>();

        for (int i = 0; i < graph.getIntersections().size(); i++) {
            Intersection intersection = graph.getIntersections().get(i);
            if (intersection.getCarsWaiting() > 0 || intersection.getEmergencyLevel() > 0) {
                waitingQueue.enqueue(intersection);
            }
        }

        while (!waitingQueue.isEmpty()) {
            Intersection intersection = waitingQueue.dequeue();
            int averageRoadCongestion = graph.getAverageCongestionFrom(intersection.getId());
            int priorityScore = calculatePriority(intersection, averageRoadCongestion);
            int greenTime = calculateGreenTime(intersection, averageRoadCongestion);
            priorityHeap.insert(new SignalPriority(intersection, priorityScore, greenTime));
        }

        graph.resetSignals();
        int rank = 1;
        while (!priorityHeap.isEmpty()) {
            SignalPriority priority = priorityHeap.removeMax();
            Intersection intersection = priority.getIntersection();
            intersection.setGreenTime(priority.getGreenTime());
            if (rank == 1) {
                intersection.setSignalStatus("GREEN NOW");
            } else if (rank <= 3) {
                intersection.setSignalStatus("WAITING");
            } else {
                intersection.setSignalStatus("RED");
            }
            finalOrder.add(priority);
            rank++;
        }

        String explanation = "Queue first stores all busy intersections. Max-Heap then gives the highest priority intersection first. Greedy rule gives green signal to the maximum score intersection.";
        return new OptimizerResult(finalOrder, explanation);
    }

    private int calculatePriority(Intersection intersection, int averageRoadCongestion) {
        int carsScore = intersection.getCarsWaiting() * 2;
        int emergencyScore = intersection.getEmergencyLevel() * 60;
        int roadScore = averageRoadCongestion;
        return carsScore + emergencyScore + roadScore;
    }

    private int calculateGreenTime(Intersection intersection, int averageRoadCongestion) {
        int greenTime = 10;
        greenTime = greenTime + intersection.getCarsWaiting();
        greenTime = greenTime + (intersection.getEmergencyLevel() * 20);
        greenTime = greenTime + (averageRoadCongestion / 3);

        if (greenTime < 15) {
            greenTime = 15;
        }
        if (greenTime > 90) {
            greenTime = 90;
        }
        return greenTime;
    }
}
