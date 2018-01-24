import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QSOTest {

    @Test
    void getComments() {
        QSO qso = new QSO();
        qso.setComments("Test comment");
        assertEquals("Test comment", qso.getComments());
    }
}