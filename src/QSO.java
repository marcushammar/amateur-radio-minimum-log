import java.io.Serializable;

public class QSO implements Serializable {
    private String callSign;
    private String timeStart;
    private String timeEnd;
    private String frequency;
    private String band;
    private String mode;
    private String power;
    private String location;
    private String rstSent;
    private String rstReceived;
    private String myCallSign;
    private String myLocation;
    private String comments;

    public QSO(){
    }

    public String getCallSign(){
        return callSign;
    }

    public void setCallSign(String callSign){
        this.callSign = callSign;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRstSent() {
        return rstSent;
    }

    public void setRstSent(String rstSent) {
        this.rstSent = rstSent;
    }

    public String getRstRecevied() {
        return rstReceived;
    }

    public void setRstRecevied(String rstRecevied) {
        this.rstReceived = rstRecevied;
    }

    public String getMyCallSign() {
        return myCallSign;
    }

    public void setMyCallSign(String myCallSign) {
        this.myCallSign = myCallSign;
    }

    public String getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(String myLocation) {
        this.myLocation = myLocation;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAdifRow(){
        String row =
                getAdifField("CALL", callSign) +
                getAdifField("QSO_DATE", timeStart.substring(0, 10)) +
                getAdifField("QSO_DATE_OFF", timeEnd.substring(0, 10)) +
                getAdifField("TIME_ON", timeStart.substring(11)) +
                getAdifField("TIME_OFF", timeEnd.substring(11)) +
                getAdifField("FREQ", frequency) +
                getAdifField("BAND", band) +
                getAdifField("MODE", mode) +
                getAdifField("TX_PWR", power) +
                getAdifField("RST_SENT", rstSent) +
                getAdifField("RST_RCVD", rstReceived) +
                getAdifField("GRIDSQUARE", location) +
                getAdifField("MY_GRIDSQUARE", myLocation) +
                getAdifField("OPERATOR", myCallSign) +
                getAdifField("COMMENT", comments) +
                "<EOR>"
                ;
        return row;
    }

    private String getAdifField(String field, String data){
        return "<" + field + ":" + data.length() + ">" + data;
    }
}