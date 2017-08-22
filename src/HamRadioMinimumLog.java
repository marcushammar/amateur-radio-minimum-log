import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HamRadioMinimumLog extends JFrame {

    private HamRadioMinimumLog(){
        super("Ham Radio Minimum Log");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMapMenuItem = new JMenuItem("Load log");
        JMenuItem loadPlacesMenuItem = new JMenuItem("Save log");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        fileMenu.add(newMapMenuItem);
        fileMenu.add(loadPlacesMenuItem);
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        String[] columns = {
                "Call sign",
                "Time start",
                "Time end",
                "Frequency",
                "Band",
                "Mode",
                "Power",
                "Location",
                "RST sent",
                "RST received",
                "My call sign",
                "My location",
                "Comments"
        };

        Object[][] sampleData = {
                {"AA0AAA", "2017-08-22 16:05", "2017-08-22 16:10", "14.076", "20M", "JT65", "5W", "AA11", "-04", "-06", "ZZ0ZZZ", "ZZ99", "Comment #1"},
                {"BB0BBB", "2017-08-22 16:15", "2017-08-22 16:20", "14.076", "20M", "JT65", "5W", "BB11", "-03", "-05", "ZZ0ZZZ", "ZZ99", "Comment #2"},
                {"CC0CCC", "2017-08-22 16:25", "2017-08-22 16:30", "14.076", "20M", "JT65", "5W", "CC11", "-02", "-04", "ZZ0ZZZ", "ZZ99", "Comment #3"}
        };

        JTable table = new JTable(sampleData, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        add(scrollPane);

        DefaultTableModel tableModel = new DefaultTableModel(sampleData, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(140);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);
        table.getColumnModel().getColumn(7).setPreferredWidth(70);
        table.getColumnModel().getColumn(8).setPreferredWidth(70);
        table.getColumnModel().getColumn(9).setPreferredWidth(90);
        table.getColumnModel().getColumn(10).setPreferredWidth(80);
        table.getColumnModel().getColumn(11).setPreferredWidth(80);
        table.getColumnModel().getColumn(12).setPreferredWidth(200);

        JPanel southPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add");
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");
        southPanel.add(addButton);
        southPanel.add(modifyButton);
        southPanel.add(deleteButton);

        add(southPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1000, 500);
        setVisible(true);
    }
    public static void main(String[] args) {
        new HamRadioMinimumLog();
    }
}