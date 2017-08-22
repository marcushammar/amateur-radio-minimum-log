import javax.swing.*;

public class HamRadioMinimumLog extends JFrame {

    private HamRadioMinimumLog(){
        super("Ham Radio Minimum Log");

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
        add(scrollPane);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(450, 350);
        setVisible(true);
    }
    public static void main(String[] args) {
        new HamRadioMinimumLog();
    }
}