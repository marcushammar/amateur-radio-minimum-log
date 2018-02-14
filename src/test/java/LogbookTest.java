/*
 * Amateur Radio Minimum Log
 * Copyright (C) 2017-2018  Marcus Hammar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

class LogbookTest {
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
        String tmpFilename = tmpDir + "com.marcushammar.amateur-radio-minimum-log_test1.adi";
        File tmpFile = new File(tmpFilename);

        String contentOfFile = "";

        try {
            logbook.save(tmpFile);
            contentOfFile = new String(Files.readAllBytes(Paths.get(tmpFilename)), Charset.defaultCharset());
            boolean deleteResult = tmpFile.delete();
            if (!deleteResult) {
                throw new IOException("Cannot delete test file.");
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        StringBuilder sb = new StringBuilder();

        sb.append("This ADIF file was extracted from Amateur Radio Minimum Log").append(System.lineSeparator()).append(System.lineSeparator());
        sb.append("<ADIF_VER:").append(Application.ADIF_VERSION.length()).append(">").append(Application.ADIF_VERSION).append(System.lineSeparator());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeNow = sdf.format(new Date());

        sb.append("<CREATED_TIMESTAMP:15>").append(timeNow).append(System.lineSeparator());
        sb.append("<PROGRAMID:25>Amateur Radio Minimum Log").append(System.lineSeparator());
        sb.append("<PROGRAMVERSION:").append(Application.VERSION.length()).append(">").append(Application.VERSION).append(System.lineSeparator());
        sb.append("<EOH>").append(System.lineSeparator());
        sb.append(System.lineSeparator());

        int numberOfQso = logbook.count();

        for (int i = 0; i < numberOfQso; i++) {
            sb.append(logbook.get(i).getAdifRow()).append(System.lineSeparator());
        }

        assertEquals(sb.toString(), contentOfFile);
    }

    @Test
    void saveOnlyOneAndThree() {
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

        QSO qso3 = new QSO();
        qso3.setField("BAND", "70cm");
        qso3.setField("CALL", "CD5EFG");
        qso3.setField("FREQ", "433.500");
        qso3.setField("GRIDSQUARE", "QR90");
        qso3.setField("MODE", "FM");
        qso3.setField("MY_GRIDSQUARE", "MN78op");
        qso3.setField("OPERATOR", "XY4ZAB");
        qso3.setField("QSO_DATE", "20180103");
        qso3.setField("QSO_DATE_OFF", "20180103");
        qso3.setField("RST_RCVD", "59");
        qso3.setField("RST_SENT", "59");
        qso3.setField("TIME_OFF", "2235");
        qso3.setField("TIME_ON", "2230");
        qso3.setField("TX_PWR", "5");
        logbook.add(qso3);

        String tmpDir = System.getProperty("java.io.tmpdir");
        String tmpFilename = tmpDir + "com.marcushammar.amateur-radio-minimum-log_test2.adi";
        File tmpFile = new File(tmpFilename);

        String contentOfFile = "";

        int[] what = new int[2];
        what[0] = 0;
        what[1] = 2;

        try {
            logbook.save(tmpFile, what);
            contentOfFile = new String(Files.readAllBytes(Paths.get(tmpFilename)), Charset.defaultCharset());
            boolean deleteResult = tmpFile.delete();
            if (!deleteResult) {
                throw new IOException("Cannot delete test file.");
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        StringBuilder sb = new StringBuilder();

        sb.append("This ADIF file was extracted from Amateur Radio Minimum Log").append(System.lineSeparator()).append(System.lineSeparator());
        sb.append("<ADIF_VER:").append(Application.ADIF_VERSION.length()).append(">").append(Application.ADIF_VERSION).append(System.lineSeparator());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeNow = sdf.format(new Date());

        sb.append("<CREATED_TIMESTAMP:15>").append(timeNow).append(System.lineSeparator());
        sb.append("<PROGRAMID:25>Amateur Radio Minimum Log").append(System.lineSeparator());
        sb.append("<PROGRAMVERSION:").append(Application.VERSION.length()).append(">").append(Application.VERSION).append(System.lineSeparator());
        sb.append("<EOH>").append(System.lineSeparator());
        sb.append(System.lineSeparator());

        sb.append(logbook.get(0).getAdifRow()).append(System.lineSeparator());
        sb.append(logbook.get(2).getAdifRow()).append(System.lineSeparator());

        assertEquals(sb.toString(), contentOfFile);
    }

    @Test
    void simpleLoad() {
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

        StringBuilder qsoBeforeLoad = new StringBuilder();
        for (int i = 0; i < logbook.count(); i++) {
            qsoBeforeLoad.append(logbook.get(i).getAdifRow()).append(System.lineSeparator());
        }

        StringBuilder sb = new StringBuilder();

        sb.append("This ADIF file was extracted from Amateur Radio Minimum Log").append(System.lineSeparator()).append(System.lineSeparator());
        sb.append("<ADIF_VER:").append(Application.ADIF_VERSION.length()).append(">").append(Application.ADIF_VERSION).append(System.lineSeparator());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeNow = sdf.format(new Date());

        sb.append("<CREATED_TIMESTAMP:15>").append(timeNow).append(System.lineSeparator());
        sb.append("<PROGRAMID:25>Amateur Radio Minimum Log").append(System.lineSeparator());
        sb.append("<PROGRAMVERSION:").append(Application.VERSION.length()).append(">").append(Application.VERSION).append(System.lineSeparator());
        sb.append("<EOH>").append(System.lineSeparator());
        sb.append(System.lineSeparator());

        int numberOfQso = logbook.count();

        for (int i = 0; i < numberOfQso; i++) {
            sb.append(logbook.get(i).getAdifRow()).append(System.lineSeparator());
        }

        String tmpDir = System.getProperty("java.io.tmpdir");
        String tmpFilename = tmpDir + "com.marcushammar.amateur-radio-minimum-log_test3.adi";
        File tmpFile = new File(tmpFilename);

        try {
            FileWriter fw = new FileWriter(tmpFile);
            PrintWriter pw = new PrintWriter(fw);
            pw.print(sb.toString());
            fw.close();
        } catch(IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        try {
            logbook.load(tmpFile);
            boolean deleteResult = tmpFile.delete();
            if (!deleteResult) {
                throw new IOException("Cannot delete test file.");
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

        StringBuilder qsoAfterLoad = new StringBuilder();
        for (int i = 0; i < logbook.count(); i++) {
            qsoAfterLoad.append(logbook.get(i).getAdifRow()).append(System.lineSeparator());
        }


        assertEquals(qsoBeforeLoad.toString(), qsoAfterLoad.toString());
    }
}
