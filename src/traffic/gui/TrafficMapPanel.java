package traffic.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JPanel;
import traffic.dsa.Graph;
import traffic.model.Intersection;
import traffic.model.Road;

public class TrafficMapPanel extends JPanel {
    private Graph graph;

    public TrafficMapPanel() {
        setBackground(new Color(246, 248, 250));
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graph == null) {
            return;
        }
        int b=0;


        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.setFont(new Font("Arial", Font.PLAIN, 12));

        ArrayList<Road> roads = graph.getAllRoadsUnique();
        for (int i = 0; i < roads.size(); i++) {
            Road road = roads.get(i);
            Intersection from = graph.getIntersectionById(road.getFromId());
            Intersection to = graph.getIntersectionById(road.getToId());
            if (from != null && to != null) {
                g2.setColor(new Color(120, 120, 120));
                g2.drawLine(from.getX(), from.getY(), to.getX(), to.getY());
                int midX = (from.getX() + to.getX()) / 2;
                int midY = (from.getY() + to.getY()) / 2;
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("d:" + road.getDistance() + " c:" + road.getCongestion(), midX, midY);
            }
        }

        ArrayList<Intersection> intersections = graph.getIntersections();
        for (int i = 0; i < intersections.size(); i++) {
            Intersection intersection = intersections.get(i);
            Color signalColor = Color.RED;
            if (intersection.getSignalStatus().startsWith("GREEN")) {
                signalColor = new Color(0, 150, 0);
            } else if (intersection.getSignalStatus().equals("WAITING")) {
                signalColor = Color.ORANGE;
            }

            g2.setColor(signalColor);
            g2.fillOval(intersection.getX() - 18, intersection.getY() - 18, 36, 36);
            g2.setColor(Color.BLACK);
            g2.drawOval(intersection.getX() - 18, intersection.getY() - 18, 36, 36);
            g2.drawString(String.valueOf(intersection.getId()), intersection.getX() - 4, intersection.getY() + 5);
            g2.drawString(intersection.getName(), intersection.getX() - 45, intersection.getY() + 35);
        }
    }
}
