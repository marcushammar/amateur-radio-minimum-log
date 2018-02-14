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
    private enum LoadState { NONE, FIELD_NAME, FIELD_SIZE, FIELD_CONTENT }
    private enum LoadPart { HEAD, ROWS }
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
        pw.println("<ADIF_VER:" + Application.ADIF_VERSION.length() + ">" + Application.ADIF_VERSION);
        pw.println("<CREATED_TIMESTAMP:15>" + timeNow);
        pw.println("<PROGRAMID:25>Amateur Radio Minimum Log");
        pw.println("<PROGRAMVERSION:" + Application.VERSION.length() + ">" + Application.VERSION);
        pw.println("<EOH>");
        pw.println();

        for (int i : what) {
            pw.println(log.get(i).getAdifRow());
        }

        fw.close();
    }

    public void load(File file) throws IOException {
        log.clear();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        LoadState loadState = LoadState.NONE;
        LoadPart loadPart = LoadPart.HEAD;
        StringBuilder currentFieldName = new StringBuilder();
        StringBuilder currentFieldSize = new StringBuilder();
        StringBuilder currentFieldContent = new StringBuilder();
        int currentContentRemaining = 0;
        QSO currentQso = null;

        int read;
        while ((read = bufferedReader.read()) != -1) {
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
        bufferedReader.close();
    }
}