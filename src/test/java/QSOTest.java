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

import static org.junit.jupiter.api.Assertions.*;

class QSOTest {

    @Test
    void simpleCorrectQsoAdif() {
        QSO qso = new QSO();
        qso.setField("BAND", "20m");
        qso.setField("CALL", "FG2HIJ");
        qso.setField("FREQ", "14.074");
        qso.setField("GRIDSQUARE", "EF34");
        qso.setField("MODE", "FT8");
        qso.setField("MY_GRIDSQUARE", "AB12cd");
        qso.setField("OPERATOR", "AB1CDE");
        qso.setField("QSO_DATE", "20180101");
        qso.setField("QSO_DATE_OFF", "20180101");
        qso.setField("RST_RCVD", "-12");
        qso.setField("RST_SENT", "-06");
        qso.setField("TIME_OFF", "2035");
        qso.setField("TIME_ON", "2030");
        qso.setField("TX_PWR", "5");

        assertEquals("<BAND:3>20m<CALL:6>FG2HIJ<FREQ:6>14.074<GRIDSQUARE:4>EF34<MODE:3>FT8<MY_GRIDSQUARE:6>AB12cd<OPERATOR:6>AB1CDE<QSO_DATE:8>20180101<QSO_DATE_OFF:8>20180101<RST_RCVD:3>-12<RST_SENT:3>-06<TIME_OFF:4>2035<TIME_ON:4>2030<TX_PWR:1>5<EOR>", qso.getAdifRow());
    }

    @Test
    void simpleCorrectQsoValidation() {
        QSO qso = new QSO();
        qso.setField("BAND", "20m");
        qso.setField("CALL", "FG2HIJ");
        qso.setField("FREQ", "14.074");
        qso.setField("GRIDSQUARE", "EF34");
        qso.setField("MODE", "FT8");
        qso.setField("MY_GRIDSQUARE", "AB12cd");
        qso.setField("OPERATOR", "AB1CDE");
        qso.setField("QSO_DATE", "20180101");
        qso.setField("QSO_DATE_OFF", "20180101");
        qso.setField("RST_RCVD", "-12");
        qso.setField("RST_SENT", "-06");
        qso.setField("TIME_OFF", "2035");
        qso.setField("TIME_ON", "2030");
        qso.setField("TX_PWR", "5");

        qso.validate();
    }

    @Test
    void qsoWithIncorrectBand() {
        QSO qso = new QSO();
        qso.setField("BAND", "20 m");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The BAND is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectMode() {
        QSO qso = new QSO();
        qso.setField("MODE", "JT60");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The MODE is invalid", exception.getMessage());
    }

    @Test
    void createQsoWithFieldValueEqualToNull() {
        QSO qso = new QSO();

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> qso.setField("CALL", null));
        assertEquals("The field CALL cannot have a null value.", exception.getMessage());
    }

    @Test
    void createQsoWithFieldNameEqualToNull() {
        QSO qso = new QSO();

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> qso.setField(null, "AB1CDE"));
        assertEquals("The QSO cannot have a null field.", exception.getMessage());
    }
}