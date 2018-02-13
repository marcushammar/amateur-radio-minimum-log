import java.util.ArrayList;

public class Logbook {
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
}