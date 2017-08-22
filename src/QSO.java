import java.io.Serializable;

public class QSO implements Serializable {
    private String callSign;

    public QSO(){

    }

    public String getCallSign(){
        return callSign;
    }

    public void setCallSign(String callSign){
        this.callSign = callSign;
    }
}