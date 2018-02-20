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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class QSO {
    private static final HashSet<String> VALID_BANDS = new HashSet<>(Arrays.asList("2190m", "630m", "560m", "160m", "80m", "60m", "40m", "30m", "20m", "17m", "15m", "12m", "10m", "6m", "4m", "2m", "1.25m", "70cm", "33cm", "23cm", "13cm", "9cm", "6cm", "3cm", "1.25cm", "6mm", "4mm", "2.5mm", "2mm", "1mm"));
    private static final HashSet<String> VALID_MODES = new HashSet<>(Arrays.asList("AM", "ARDOP", "ATV", "C4FM", "CHIP", "CLO", "CONTESTI", "CW", "DIGITALVOICE", "DOMINO", "DSTAR", "FAX", "FM", "FSK441", "FT8", "HELL", "ISCAT", "JT4", "JT6M", "JT9", "JT44", "JT65", "MFSK", "MSK144", "MT63", "OLIVIA", "OPERA", "PAC", "PAX", "PKT", "PSK", "PSK2K", "Q15", "QRA64", "ROS", "RTTY", "RTTYM", "SSB", "SSTV", "T10", "THOR", "THRB", "TOR", "V4", "VOI", "WINMOR", "WSPR"));
    private TreeMap<String, String> fields = new TreeMap<>();

    public QSO() {
    }

    public void setField(String field, String value) {
        if (value == null) {
            throw new IllegalArgumentException("The " + field + " field cannot have a null value.");
        }

        if (field == null) {
            throw new IllegalArgumentException("The QSO cannot have a null field.");
        }

        fields.put(field, value);
    }

    public String getField(String field) {
        return fields.get(field);
    }

    public Set<String> getKeys() {
        return fields.keySet();
    }

    public String getAdifRow() {
        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, String> pair : this.fields.entrySet()) {
            output.append("<").append(pair.getKey()).append(":").append(pair.getValue().length()).append(">").append(pair.getValue());
        }
        output.append("<EOR>");

        return output.toString();
    }

    public void validate() {
        for (Map.Entry<String, String> pair : this.fields.entrySet()) {
            String value = pair.getValue();
            switch (pair.getKey()) {
                case "CALL":
                    validateCall(value);
                    break;
                case "QSO_DATE":
                    validateQsoDate(value);
                    break;
                case "TIME_ON":
                    validateQsoTimeOn(value);
                    break;
                case "TIME_OFF":
                    validateQsoTimeOff(value);
                    break;
                case "FREQ":
                    validateQsoFreq(value);
                    break;
                case "BAND":
                    validateQsoBand(value);
                    break;
                case "MODE":
                    validateQsoMode(value);
                    break;
                case "TX_PWR":
                    validateQsoTxPwr(value);
                    break;
                case "GRIDSQUARE":
                    validateQsoGridsquare(value);
                    break;
                case "RST_SENT":
                    validateQsoRstSent(value);
                    break;
                case "RST_RCVD":
                    validateQsoRstRcvd(value);
                    break;
                case "OPERATOR":
                    validateQsoOperator(value);
                    break;
                case "MY_GRIDSQUARE":
                    validateQsoMyGridsquare(value);
                    break;
                case "NOTES":
                    validateQsoNotes(value);
                    break;
                default:
                    break;
            }
        }
    }

    private void validateCall(String value) {
        if (!isString(value)) {
            throw new IllegalArgumentException("The CALL field is invalid");
        }
    }

    private void validateQsoDate(String value) {
        if (!isDate(value)) {
            throw new IllegalArgumentException("The QSO_DATE field is invalid");
        }
    }

    private void validateQsoTimeOn(String value) {
        if (!isTime(value)) {
            throw new IllegalArgumentException("The TIME_ON field is invalid");
        }
    }

    private void validateQsoTimeOff(String value) {
        if (!isTime(value)) {
            throw new IllegalArgumentException("The TIME_OFF field is invalid");
        }
    }

    private void validateQsoFreq(String value) {
        if (!isNumber(value)) {
            throw new IllegalArgumentException("The FREQ field is invalid");
        }
    }

    private void validateQsoBand(String value) {
        if (!VALID_BANDS.contains(value)) {
            throw new IllegalArgumentException("The BAND field is invalid");
        }
    }

    private void validateQsoMode(String value) {
        if (!VALID_MODES.contains(value)) {
            throw new IllegalArgumentException("The MODE field is invalid");
        }
    }

    private void validateQsoTxPwr(String value) {
        if (!isNumber(value)) {
            throw new IllegalArgumentException("The TX_PWR field is invalid");
        }
    }

    private void validateQsoGridsquare(String value) {
        if (!isGridsquare(value)) {
            throw new IllegalArgumentException("The GRIDSQUARE field is invalid");
        }
    }

    private void validateQsoRstSent(String value) {
        if (!isString(value)) {
            throw new IllegalArgumentException("The RST_SENT field is invalid");
        }
    }

    private void validateQsoRstRcvd(String value) {
        if (!isString(value)) {
            throw new IllegalArgumentException("The RST_RCVD field is invalid");
        }
    }

    private void validateQsoOperator(String value) {
        if (!isString(value)) {
            throw new IllegalArgumentException("The OPERATOR field is invalid");
        }
    }

    private void validateQsoMyGridsquare(String value) {
        if (!isGridsquare(value)) {
            throw new IllegalArgumentException("The MY_GRIDSQUARE field is invalid");
        }
    }

    private void validateQsoNotes(String value) {
        if (!isString(value)) {
            throw new IllegalArgumentException("The NOTES field is invalid");
        }
    }

    private static boolean isString(String value) {
        boolean valid = true;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c < 32 || c > 126) {
                valid = false;
            }
        }
        return valid;
    }

    public static boolean isDate(String date) {
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isTime(String time) {
        boolean validShort;
        boolean validLong;
        try {
            DateFormat df = new SimpleDateFormat("HHmm");
            df.setLenient(false);
            df.parse(time);
            validShort = true;
        } catch (ParseException e) {
            validShort = false;
        }

        try {
            DateFormat df = new SimpleDateFormat("HHmmss");
            df.setLenient(false);
            df.parse(time);
            validLong = true;
        } catch (ParseException e) {
            validLong = false;
        }

        return validShort || validLong;
    }

    public static boolean isNumber(String value) {
        return value.matches("[0-9]+([.][0-9]+)?");
    }

    private static boolean isGridsquare(String value) {
        boolean gridSquareTwoCharacters = value.matches("[A-Z]{2}");
        boolean gridSquareFourCharacters = value.matches("[A-Z]{2}[0-9]{2}");
        boolean gridSquareSixCharacters = value.matches("[A-Z]{2}[0-9]{2}[a-z]{2}");
        boolean gridSquareEightCharacters = value.matches("[A-Z]{2}[0-9]{2}[a-z]{2}[0-9]{2}");

        return gridSquareTwoCharacters || gridSquareFourCharacters || gridSquareSixCharacters || gridSquareEightCharacters;
    }
}