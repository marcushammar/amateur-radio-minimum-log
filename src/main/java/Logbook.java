import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Logbook {
    private static final String APPLICATION_VERSION = "1.1.0";

    private ArrayList<QSO> log = new ArrayList<>();

    public int count() {
        return log.size();
    }

    public Object[][] getDataForTable() {
        Object[][] dataObject = new Object[log.size()][13];
        int i = 0;
        for (QSO qso : log) {
            dataObject[i][0] = qso.getField("CALL");
            dataObject[i][1] = qso.getField("QSO_DATE");
            dataObject[i][2] = qso.getField("QSO_DATE_OFF");
            dataObject[i][3] = qso.getField("FREQ");
            dataObject[i][4] = qso.getField("BAND");
            dataObject[i][5] = qso.getField("MODE");
            dataObject[i][6] = qso.getField("TX_PWR");
            dataObject[i][7] = qso.getField("GRIDSQUARE");
            dataObject[i][8] = qso.getField("RST_SENT");
            dataObject[i][9] = qso.getField("RST_RCVD");
            dataObject[i][10] = qso.getField("OPERATOR");
            dataObject[i][11] = qso.getField("MY_GRIDSQUARE");
            dataObject[i][12] = qso.getField("COMMENT");
            i++;
        }
        return dataObject;
    }

    public void add(QSO qso) {
        log.add(qso);
    }

    public QSO get(int index) {
        return log.get(index);
    }

    public void remove(int index) {
        log.remove(index);
    }

    public void clear() {
        log.clear();
    }

    public void save(File file) throws IOException {
        int[] what = new int[log.size()];
        for (int i = 0; i < log.size(); i++) {
            what[i] = i;
        }
        save(file, what);
    }

    public void save(File file, int[] what) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeNow = sdf.format(new Date());

        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);

        pw.println("This ADIF file was extracted from Amateur Radio Minimum Log");
        pw.println();
        pw.println("<ADIF_VER:5>3.0.6");
        pw.println("<CREATED_TIMESTAMP:15>" + timeNow);
        pw.println("<PROGRAMID:25>Amateur Radio Minimum Log");
        pw.println("<PROGRAMVERSION:" + APPLICATION_VERSION.length() + ">" + APPLICATION_VERSION);
        pw.println("<EOH>");
        pw.println();

        for (int i : what) {
            pw.println(log.get(i).getAdifRow());
        }

        fw.close();
    }
}