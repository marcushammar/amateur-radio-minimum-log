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
    void removeFieldFromQso() {
        QSO qso = new QSO();
        qso.setField("BAND", "20m");
        qso.setField("CALL", "FG2HIJ");
        qso.setField("COMMENT", "73");
        qso.setField("COMMENT", "");

        assertEquals("<BAND:3>20m<CALL:6>FG2HIJ<EOR>", qso.getAdifRow());
    }

    @Test
    void removeNonExistingFieldFromQso() {
        QSO qso = new QSO();
        qso.setField("BAND", "20m");
        qso.setField("CALL", "FG2HIJ");
        qso.setField("NOTES", "");

        assertEquals("<BAND:3>20m<CALL:6>FG2HIJ<EOR>", qso.getAdifRow());
    }

    @Test
    void qsoWithIncorrectBand() {
        QSO qso = new QSO();
        qso.setField("BAND", "20 m");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The BAND field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectMode() {
        QSO qso = new QSO();
        qso.setField("MODE", "JT60");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The MODE field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectCall() {
        QSO qso = new QSO();
        qso.setField("CALL", "ABØCDE");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The CALL field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectDateFormatDash() {
        QSO qso = new QSO();
        qso.setField("QSO_DATE", "2018-02-20");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The QSO_DATE field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectDateFormatWrongNumberOfDigits() {
        QSO qso = new QSO();
        qso.setField("QSO_DATE", "2018011");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The QSO_DATE field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectDate() {
        QSO qso = new QSO();
        qso.setField("QSO_DATE", "20180231");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The QSO_DATE field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectTimeOn() {
        QSO qso = new QSO();
        qso.setField("TIME_ON", "1265");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The TIME_ON field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectTimeOnFormatColon() {
        QSO qso = new QSO();
        qso.setField("TIME_ON", "12:45");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The TIME_ON field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectTimeOnFormatWrongNumberOfDigits() {
        QSO qso = new QSO();
        qso.setField("TIME_ON", "12450");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The TIME_ON field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectTimeOff() {
        QSO qso = new QSO();
        qso.setField("TIME_OFF", "1275");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The TIME_OFF field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectTimeOffFormatColon() {
        QSO qso = new QSO();
        qso.setField("TIME_OFF", "12:55");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The TIME_OFF field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectTimeOffFormatWrongNumberOfDigits() {
        QSO qso = new QSO();
        qso.setField("TIME_OFF", "12550");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The TIME_OFF field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectFreq() {
        QSO qso = new QSO();
        qso.setField("FREQ", "7.074.000");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The FREQ field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectTxPwr() {
        QSO qso = new QSO();
        qso.setField("TX_PWR", "High");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The TX_PWR field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectGridsquare() {
        QSO qso = new QSO();
        qso.setField("GRIDSQUARE", "Stockholm");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The GRIDSQUARE field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectRstSent() {
        QSO qso = new QSO();
        qso.setField("RST_SENT", "Gøød");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The RST_SENT field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectRstRcvd() {
        QSO qso = new QSO();
        qso.setField("RST_RCVD", "Gøød");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The RST_RCVD field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectOperator() {
        QSO qso = new QSO();
        qso.setField("OPERATOR", "ZZØYYY");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The OPERATOR field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectMyGridsquare() {
        QSO qso = new QSO();
        qso.setField("MY_GRIDSQUARE", "Stockholm");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The MY_GRIDSQUARE field is invalid", exception.getMessage());
    }

    @Test
    void qsoWithIncorrectNotes() {
        QSO qso = new QSO();
        qso.setField("NOTES", "Gøød");

        Throwable exception = assertThrows(IllegalArgumentException.class, qso::validate);
        assertEquals("The NOTES field is invalid", exception.getMessage());
    }

    @Test
    void createQsoWithFieldValueEqualToNull() {
        QSO qso = new QSO();

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> qso.setField("CALL", null));
        assertEquals("The CALL field cannot have a null value.", exception.getMessage());
    }

    @Test
    void createQsoWithFieldNameEqualToNull() {
        QSO qso = new QSO();

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> qso.setField(null, "AB1CDE"));
        assertEquals("The QSO cannot have a null field.", exception.getMessage());
    }
}