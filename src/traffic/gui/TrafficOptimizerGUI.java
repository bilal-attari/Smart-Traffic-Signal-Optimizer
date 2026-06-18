package traffic.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import traffic.dsa.DijkstraAlgorithm;
import traffic.dsa.Graph;
import traffic.dsa.GreedySignalOptimizer;
import traffic.dsa.MSTAlgorithm;
import traffic.dsa.MSTResult;
import traffic.dsa.OptimizerResult;
import traffic.dsa.PathResult;
import traffic.dsa.SignalPriority;
import traffic.dsa.SimpleChainedHashTable;
import traffic.io.TrafficDataManager;
import traffic.model.Intersection;
import traffic.model.Road;

public class TrafficOptimizerGUI extends JFrame {
    private Graph graph;
    private TrafficDataManager dataManager;
    private TrafficMapPanel mapPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea outputArea;
    private JComboBox<Intersection> sourceBox;
    private JComboBox<Intersection> destinationBox;
    private SimpleChainedHashTable signalHashTable;


    public TrafficOptimizerGUI() {
        dataManager = new TrafficDataManager();
        signalHashTable = new SimpleChainedHashTable(10);
        setTitle("Smart Traffic Signal Optimizer - DSA Java Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(235, 240, 245));

        add(createHeader(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createRightPanel(), BorderLayout.EAST);

        loadSampleGraph();
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(27, 94, 32));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("Smart Traffic Signal Optimizer");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.WEST);

        JLabel subtitle = new JLabel("Graphs + Queue + Max-Heap + Greedy + Dijkstra + MST");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(subtitle, BorderLayout.EAST);
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));
        panel.setBackground(new Color(235, 240, 245));

        mapPanel = new TrafficMapPanel();
        mapPanel.setPreferredSize(new Dimension(650, 360));
        mapPanel.setBorder(BorderFactory.createTitledBorder("Traffic Network Graph View"));
        panel.add(mapPanel, BorderLayout.CENTER);

        String[] columns = {"ID", "Intersection", "Cars", "Emergency", "Green Time", "Signal"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 180));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Signal Table"));
        panel.add(scrollPane, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setPreferredSize(new Dimension(350, 600));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        panel.setBackground(new Color(235, 240, 245));

        JPanel controlPanel = new JPanel(new GridLayout(0, 1, 7, 7));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));

        JButton loadButton = new JButton("Load Sample Network");
        JButton randomButton = new JButton("Generate Random Traffic");
        JButton optimizeButton = new JButton("Run Greedy Optimizer");
        JButton dijkstraButton = new JButton("Find Shortest Path");
        JButton mstButton = new JButton("Show MST Roads");
        JButton resetButton = new JButton("Reset Signals");
        JButton saveButton = new JButton("Save Text Report");

        sourceBox = new JComboBox<Intersection>();
        destinationBox = new JComboBox<Intersection>();
        controlPanel.add(new JLabel("Source Intersection:"));
        controlPanel.add(sourceBox);
        controlPanel.add(new JLabel("Destination Intersection:"));
        controlPanel.add(destinationBox);
        controlPanel.add(loadButton);
        controlPanel.add(randomButton);
        controlPanel.add(optimizeButton);
        controlPanel.add(dijkstraButton);
        controlPanel.add(mstButton);
        controlPanel.add(resetButton);
        controlPanel.add(saveButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Output"));

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(outputScroll, BorderLayout.CENTER);

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSampleGraph();
            }
        });
        randomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateRandomTraffic();
            }
        });
        optimizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runOptimizer();
            }
        });
        dijkstraButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runDijkstra();
            }
        });
        mstButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMST();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetSignals();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveReport();
            }
        });
        return panel;
    }

    private void loadSampleGraph() {
        graph = dataManager.loadGraph("data/intersections.csv", "data/roads.csv");
        graph.makeRandomTraffic();
        updateComboBoxes();
        refreshTable();
        mapPanel.setGraph(graph);
        outputArea.setText("Sample traffic graph loaded successfully.\nClick 'Run Greedy Optimizer' to calculate signal priority.\n");
    }

    private void updateComboBoxes() {
        sourceBox.removeAllItems();
        destinationBox.removeAllItems();
        ArrayList<Intersection> intersections = graph.getIntersections();
        for (int i = 0; i < intersections.size(); i++) {
            sourceBox.addItem(intersections.get(i));
            destinationBox.addItem(intersections.get(i));
        }
        if (destinationBox.getItemCount() > 1) {
            destinationBox.setSelectedIndex(1);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        signalHashTable = new SimpleChainedHashTable(10);
        ArrayList<Intersection> intersections = graph.getIntersections();
        for (int i = 0; i < intersections.size(); i++) {
            Intersection intersection = intersections.get(i);
            signalHashTable.put(intersection.getId(), intersection.getSignalStatus());
            Object[] row = {
                intersection.getId(),
                intersection.getName(),
                intersection.getCarsWaiting(),
                intersection.getEmergencyText(),
                intersection.getGreenTime() + " sec",
                signalHashTable.get(intersection.getId())
            };
            tableModel.addRow(row);
        }
        mapPanel.repaint();
    }

    private void generateRandomTraffic() {
        graph.makeRandomTraffic();
        refreshTable();
        outputArea.setText("New random traffic data generated.\nCongestion, waiting cars, and emergency conditions were updated.\n");
    }

    private void runOptimizer() {
        GreedySignalOptimizer optimizer = new GreedySignalOptimizer();
        OptimizerResult result = optimizer.optimize(graph);
        refreshTable();

        StringBuilder sb = new StringBuilder();
        sb.append("GREEDY SIGNAL OPTIMIZATION RESULT\n");
        sb.append("----------------------------------\n");
        sb.append(result.getExplanation()).append("\n\n");
        ArrayList<SignalPriority> order = result.getOrder();
        for (int i = 0; i < order.size(); i++) {
            SignalPriority priority = order.get(i);
            sb.append((i + 1)).append(". ");
            sb.append(priority.getIntersection().getName());
            sb.append(" | Cars: ").append(priority.getIntersection().getCarsWaiting());
            sb.append(" | Emergency: ").append(priority.getIntersection().getEmergencyText());
            sb.append(" | Score: ").append(priority.getPriorityScore());
            sb.append(" | Green Time: ").append(priority.getGreenTime()).append(" sec\n");
        }
        outputArea.setText(sb.toString());
    }

    private void runDijkstra() {
        Intersection source = (Intersection) sourceBox.getSelectedItem();
        Intersection destination = (Intersection) destinationBox.getSelectedItem();
        if (source == null || destination == null) {
            return;
        }
        DijkstraAlgorithm algorithm = new DijkstraAlgorithm();
        PathResult result = algorithm.findShortestPath(graph, source.getId(), destination.getId());

        StringBuilder sb = new StringBuilder();
        sb.append("DIJKSTRA SHORTEST PATH RESULT\n");
        sb.append("--------------------------------\n");
        sb.append("Source: ").append(source.getName()).append("\n");
        sb.append("Destination: ").append(destination.getName()).append("\n");
        sb.append("Path: ").append(result.getPathAsText()).append("\n");
        if (result.getDistance() == -1) {
            sb.append("Total Cost: No route available\n");
        } else {
            sb.append("Total Cost = Distance + Congestion = ").append(result.getDistance()).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    private void showMST() {
        MSTAlgorithm algorithm = new MSTAlgorithm();
        MSTResult result = algorithm.makeMinimumSpanningTree(graph);
        StringBuilder sb = new StringBuilder();
        sb.append("MINIMUM SPANNING TREE RESULT\n");
        sb.append("-----------------------------\n");
        sb.append("MST is used to select minimum road connections for city network maintenance.\n\n");
        ArrayList<Road> roads = result.getRoads();
        for (int i = 0; i < roads.size(); i++) {
            Road road = roads.get(i);
            sb.append((i + 1)).append(". ");
            sb.append(road.getFromId()).append(" <-> ").append(road.getToId());
            sb.append(" | distance = ").append(road.getDistance()).append("\n");
        }
        sb.append("\nTotal MST Distance: ").append(result.getTotalDistance()).append("\n");
        outputArea.setText(sb.toString());
    }

    private void resetSignals() {
        graph.resetSignals();
        refreshTable();
        outputArea.setText("All signals are reset to RED state.\n");
    }

    private void saveReport() {
        try {
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "reports/traffic_report_" + time + ".txt";
            StringBuilder report = new StringBuilder();
            report.append("Smart Traffic Signal Optimizer Report\n");
            report.append("Generated: ").append(new Date()).append("\n\n");
            report.append(outputArea.getText()).append("\n");
            dataManager.saveTextReport(fileName, report.toString());
            JOptionPane.showMessageDialog(this, "Report saved: " + fileName);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Report could not be saved: " + ex.getMessage());
        }
    }

    public static void open() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TrafficOptimizerGUI().setVisible(true);
            }
        });
    }
}
