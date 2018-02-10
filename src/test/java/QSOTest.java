import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QSOTest {
    @Test
    void getCallSign() {
        QSO qso = new QSO();
        qso.setCallSign("AB1CDE");
        assertEquals("AB1CDE", qso.getCallSign());
    }

    @Test
    void getBand() {
        QSO qso = new QSO();
        qso.setBand("20m");
        assertEquals("20m", qso.getBand());
    }

    @Test
    void getFrequency() {
        QSO qso = new QSO();
        qso.setFrequency("14.074");
        assertEquals("14.074", qso.getFrequency());
    }

    @Test
    void getComments() {
        QSO qso = new QSO();
        qso.setComments("Test comment");
        assertEquals("Test comment", qso.getComments());
    }
}