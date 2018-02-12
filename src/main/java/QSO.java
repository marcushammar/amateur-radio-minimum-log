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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class QSO {
    private static final HashSet<String> VALID_BANDS = new HashSet<>(Arrays.asList("2190m", "630m", "560m", "160m", "80m", "60m", "40m", "30m", "20m", "17m", "15m", "12m", "10m", "6m", "4m", "2m", "1.25m", "70cm", "33cm", "23cm", "13cm", "9cm", "6cm", "3cm", "1.25cm", "6mm", "4mm", "2.5mm", "2mm", "1mm"));
    private static final HashSet<String> VALID_MODES = new HashSet<>(Arrays.asList("AM", "ARDOP", "ATV", "C4FM", "CHIP", "CLO", "CONTESTI", "CW", "DIGITALVOICE", "DOMINO", "DSTAR", "FAX", "FM", "FSK441", "FT8", "HELL", "ISCAT", "JT4", "JT6M", "JT9", "JT44", "JT65", "MFSK", "MSK144", "MT63", "OLIVIA", "OPERA", "PAC", "PAX", "PKT", "PSK", "PSK2K", "Q15", "QRA64", "ROS", "RTTY", "RTTYM", "SSB", "SSTV", "T10", "THOR", "THRB", "TOR", "V4", "VOI", "WINMOR", "WSPR"));
    private TreeMap<String, String> fields = new TreeMap<>();

    public QSO() {
    }

    public void setField(String field, String value) {
        fields.put(field, value);
    }

    public String getField(String field) {
        return fields.get(field);
    }

    public String getAdifRow() {
        String output = "";
        for (Map.Entry<String, String> pair : this.fields.entrySet()) {
            output = output + "<" + pair.getKey() + ":" + pair.getValue().length() + ">" + pair.getValue();
        }
        output = output + "<EOR>";

        return output;
    }

    public boolean validateAdif() { //TODO: This method needs to be implemented
        return true;
    }

    private static boolean isGridSquare(String value) {
        boolean gridSquareTwoCharacters = value.matches("[A-Z]{2}");
        boolean gridSquareFourCharacters = value.matches("[A-Z]{2}[0-9]{2}");
        boolean gridSquareSixCharacters = value.matches("[A-Z]{2}[0-9]{2}[a-z]{2}");
        boolean gridSquareEightCharacters = value.matches("[A-Z]{2}[0-9]{2}[a-z]{2}[0-9]{2}");

        return gridSquareTwoCharacters || gridSquareFourCharacters || gridSquareSixCharacters || gridSquareEightCharacters;
    }

    private static boolean isAdifString(String value) {
        boolean valid = true;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c < 32 || c > 126) {
                valid = false;
            }
        }
        return valid;
    }

    private static boolean isAdifIntlString(String value) {
        boolean valid = true;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == 13 || c == 10) {
                valid = false;
            }
        }
        return valid;
    }

    public static boolean validateCallSign(String value) {
        return isAdifString(value);
    }

    public static boolean validateTimeStart(String value) {
        boolean firstCheck = value.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}[ ](([0-1][0-9])|([2][0-3]))[:][0-5][0-9]([:][0-5][0-9])?");
        boolean secondCheck = false;

        if (firstCheck) {
            if (isDateValid(value.substring(0, 10))) {
                secondCheck = true;
            }
        }

        return firstCheck && secondCheck;
    }

    public static boolean validateTimeEnd(String value) {
        boolean firstCheck = value.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}[ ](([0-1][0-9])|([2][0-3]))[:][0-5][0-9]([:][0-5][0-9])?");
        boolean secondCheck = false;

        if (firstCheck) {
            if (isDateValid(value.substring(0, 10))) {
                secondCheck = true;
            }
        }

        return firstCheck && secondCheck;
    }

    public static boolean validateFrequency(String value) {
        return value.matches("[0-9]+([.][0-9]+)?");
    }

    public static boolean validateBand(String value) {
        return VALID_BANDS.contains(value);
    }

    public static boolean validateMode(String value) {
        return VALID_MODES.contains(value);
    }

    public static boolean validatePower(String value) {
        return value.matches("[0-9]+([.][0-9]+)?");
    }

    public static boolean validateLocation(String value) {
        return isGridSquare(value) || isAdifString(value) || isAdifIntlString(value);
    }

    public static boolean validateRstSent(String value) {
        return isAdifString(value);
    }

    public static boolean validateRstReceived(String value) {
        return isAdifString(value);
    }

    public static boolean validateMyCallSign(String value) {
        return isAdifString(value);
    }

    public static boolean validateMyLocation(String value) {
        return isGridSquare(value) || isAdifString(value) || isAdifIntlString(value);
    }

    public static boolean validateComments(String value) {
        return isAdifString(value) || isAdifIntlString(value);
    }

    public static boolean isDateValid(String date) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}