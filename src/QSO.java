import java.io.Serializable;

public class QSO implements Serializable {
    private String callSign;

    public QSO(String callSign){
        this.callSign = callSign;
    }
}