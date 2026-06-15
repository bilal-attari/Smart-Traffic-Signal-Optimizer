package traffic.io;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import traffic.dsa.Graph;
import traffic.model.Intersection;

public class TrafficDataManager {
    public Graph loadGraph(String intersectionFile, String roadFile) {
        Graph graph = new Graph();
        try {
            File file = new File(intersectionFile);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                int x = Integer.parseInt(parts[2].trim());
                int y = Integer.parseInt(parts[3].trim());
                graph.addIntersection(new Intersection(id, name, x, y));
            }
            scanner.close();

            File roadData = new File(roadFile);
            Scanner roadScanner = new Scanner(roadData);
            while (roadScanner.hasNextLine()) {
                String line = roadScanner.nextLine().trim();
                if (line.length() == 0 || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(",");
                int from = Integer.parseInt(parts[0].trim());
                int to = Integer.parseInt(parts[1].trim());
                int distance = Integer.parseInt(parts[2].trim());
                int congestion = Integer.parseInt(parts[3].trim());
                graph.addUndirectedRoad(from, to, distance, congestion);
            }
            roadScanner.close();
        } catch (Exception ex) {
            graph = makeDefaultGraph();
        }
        return graph;
    }

    public Graph makeDefaultGraph() {
        Graph graph = new Graph();
        graph.addIntersection(new Intersection(1, "University Gate", 90, 80));
        graph.addIntersection(new Intersection(2, "Main Chowk", 280, 90));
        graph.addIntersection(new Intersection(3, "Hospital Road", 460, 120));
        graph.addIntersection(new Intersection(4, "Market Square", 130, 260));
        graph.addIntersection(new Intersection(5, "Bus Stop", 330, 280));
        graph.addIntersection(new Intersection(6, "Ring Road", 520, 310));

        graph.addUndirectedRoad(1, 2, 4, 35);
        graph.addUndirectedRoad(2, 3, 3, 40);
        graph.addUndirectedRoad(1, 4, 5, 25);
        graph.addUndirectedRoad(2, 4, 2, 55);
        graph.addUndirectedRoad(2, 5, 4, 65);
        graph.addUndirectedRoad(3, 6, 5, 30);
        graph.addUndirectedRoad(4, 5, 3, 50);
        graph.addUndirectedRoad(5, 6, 4, 70);
        graph.addUndirectedRoad(3, 5, 6, 20);
        return graph;
    }

    public void saveTextReport(String filePath, String text) throws Exception {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println(text);
        writer.close();
    }
}
