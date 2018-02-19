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

import java.util.*;

public class QSO {
    private static final HashSet<String> VALID_BANDS = new HashSet<>(Arrays.asList("2190m", "630m", "560m", "160m", "80m", "60m", "40m", "30m", "20m", "17m", "15m", "12m", "10m", "6m", "4m", "2m", "1.25m", "70cm", "33cm", "23cm", "13cm", "9cm", "6cm", "3cm", "1.25cm", "6mm", "4mm", "2.5mm", "2mm", "1mm"));
    private static final HashSet<String> VALID_MODES = new HashSet<>(Arrays.asList("AM", "ARDOP", "ATV", "C4FM", "CHIP", "CLO", "CONTESTI", "CW", "DIGITALVOICE", "DOMINO", "DSTAR", "FAX", "FM", "FSK441", "FT8", "HELL", "ISCAT", "JT4", "JT6M", "JT9", "JT44", "JT65", "MFSK", "MSK144", "MT63", "OLIVIA", "OPERA", "PAC", "PAX", "PKT", "PSK", "PSK2K", "Q15", "QRA64", "ROS", "RTTY", "RTTYM", "SSB", "SSTV", "T10", "THOR", "THRB", "TOR", "V4", "VOI", "WINMOR", "WSPR"));
    private TreeMap<String, String> fields = new TreeMap<>();

    public QSO() {
    }

    public void setField(String field, String value) {
        if (value == null) {
            throw new IllegalArgumentException("The field " + field + " cannot have a null value.");
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
                case "BAND":
                    validateBand(value);
                    break;
                case "MODE":
                    validateMode(value);
                    break;
                default:
                    break;
            }
        }
    }

    private void validateBand(String value) {
        if (!VALID_BANDS.contains(value)) {
            throw new IllegalArgumentException("The BAND is invalid");
        }
    }

    private void validateMode(String value) {
        if (!VALID_MODES.contains(value)) {
            throw new IllegalArgumentException("The MODE is invalid");
        }
    }
}