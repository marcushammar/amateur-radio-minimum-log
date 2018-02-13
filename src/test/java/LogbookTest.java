import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class LogbookTest {
    private static final String APPLICATION_VERSION = "1.1.0";

    @Test
    void simpleSave() {
        Logbook logbook = new Logbook();

        QSO qso1 = new QSO();
        qso1.setField("BAND", "20m");
        qso1.setField("CALL", "FG2HIJ");
        qso1.setField("FREQ", "14.074");
        qso1.setField("GRIDSQUARE", "EF34");
        qso1.setField("MODE", "FT8");
        qso1.setField("MY_GRIDSQUARE", "AB12cd");
        qso1.setField("OPERATOR", "AB1CDE");
        qso1.setField("QSO_DATE", "20180101");
        qso1.setField("QSO_DATE_OFF", "20180101");
        qso1.setField("RST_RCVD", "-12");
        qso1.setField("RST_SENT", "-06");
        qso1.setField("TIME_OFF", "2035");
        qso1.setField("TIME_ON", "2030");
        qso1.setField("TX_PWR", "5");
        logbook.add(qso1);

        QSO qso2 = new QSO();
        qso2.setField("BAND", "40m");
        qso2.setField("CALL", "PQ2RST");
        qso2.setField("FREQ", "7.076");
        qso2.setField("GRIDSQUARE", "KL34");
        qso2.setField("MODE", "JT65");
        qso2.setField("MY_GRIDSQUARE", "GH56ij");
        qso2.setField("OPERATOR", "KL1MNO");
        qso2.setField("QSO_DATE", "20180102");
        qso2.setField("QSO_DATE_OFF", "20180102");
        qso2.setField("RST_RCVD", "-8");
        qso2.setField("RST_SENT", "-04");
        qso2.setField("TIME_OFF", "2135");
        qso2.setField("TIME_ON", "2130");
        qso2.setField("TX_PWR", "10");
        logbook.add(qso2);

        String tmpDir = System.getProperty("java.io.tmpdir");
        String tmpFilename = tmpDir + "com.marcushammar.amateur-radio-minimum-log_test.adi";
        File tmpFile = new File(tmpFilename);
        logbook.save(tmpFile);

        String contents = "";

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(tmpFilename));
            contents = new String(encoded, Charset.defaultCharset());

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        StringBuilder sb = new StringBuilder();

        sb.append("This ADIF file was extracted from Amateur Radio Minimum Log").append(System.lineSeparator()).append(System.lineSeparator());
        sb.append("<ADIF_VER:5>3.0.6").append(System.lineSeparator());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeNow = sdf.format(new Date());

        sb.append("<CREATED_TIMESTAMP:15>").append(timeNow).append(System.lineSeparator());
        sb.append("<PROGRAMID:25>Amateur Radio Minimum Log").append(System.lineSeparator());
        sb.append("<PROGRAMVERSION:").append(APPLICATION_VERSION.length()).append(">").append(APPLICATION_VERSION).append(System.lineSeparator());
        sb.append("<EOH>").append(System.lineSeparator());
        sb.append(System.lineSeparator());

        int numberOfQso = logbook.count();

        for (int i = 0; i < numberOfQso; i++) {
            sb.append(logbook.get(i).getAdifRow()).append(System.lineSeparator());
        }

        assertEquals(sb.toString(), contents);
    }
}
