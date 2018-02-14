import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

    public enum LoadState { NONE, FIELD_NAME, FIELD_SIZE, FIELD_CONTENT }
    public enum LoadPart { HEAD, ROWS }

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

    public void load(File file) throws IOException {
        log.clear();

        BufferedReader br = new BufferedReader(new FileReader(file));

        LoadState loadState = LoadState.NONE;
        LoadPart loadPart = LoadPart.HEAD;
        StringBuilder currentFieldName = new StringBuilder();
        StringBuilder currentFieldSize = new StringBuilder();
        StringBuilder currentFieldContent = new StringBuilder();
        int currentContentRemaining = 0;
        QSO currentQso = null;

        while (true) {
            int read = br.read();
            if (read == -1) {
                break;
            }

            char currentChar = (char)read;

            if (loadState == LoadState.NONE && currentChar == '<') {
                loadState = LoadState.FIELD_NAME;
            } else if (loadState == LoadState.FIELD_NAME && currentChar == '>') {
                if (currentFieldName.toString().equals("EOH")) {
                    loadPart = LoadPart.ROWS;
                }
                if (currentFieldName.toString().equals("EOR")) {
                    log.add(currentQso);
                    currentQso = null;
                }
                currentFieldName.setLength(0);
                loadState = LoadState.NONE;
            } else if (loadState == LoadState.FIELD_NAME && currentChar == ':') {
                loadState = LoadState.FIELD_SIZE;
            } else if (loadState == LoadState.FIELD_NAME) {
                currentFieldName.append(Character.toString(currentChar).toUpperCase());
            } else if (loadState == LoadState.FIELD_SIZE && currentChar == '>') {
                loadState = LoadState.FIELD_CONTENT;
                currentContentRemaining = Integer.parseInt(currentFieldSize.toString());
            } else if (loadState == LoadState.FIELD_SIZE) {
                currentFieldSize.append(currentChar);
            } else if (loadState == LoadState.FIELD_CONTENT && currentContentRemaining != 0) {
                currentFieldContent.append(currentChar);
                currentContentRemaining--;
                if (currentContentRemaining == 0) {
                    if (loadPart == LoadPart.ROWS) {
                        if (currentQso == null) {
                            currentQso = new QSO();
                        }
                        currentQso.setField(currentFieldName.toString(), currentFieldContent.toString());
                    }
                    currentFieldName.setLength(0);
                    currentFieldSize.setLength(0);
                    currentFieldContent.setLength(0);
                    loadState = LoadState.NONE;
                }
            }
        }
        br.close();
    }
}