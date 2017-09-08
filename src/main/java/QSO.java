import java.util.Arrays;
import java.util.HashSet;

public class QSO {
    private static transient HashSet<String> validBands = new HashSet<>(Arrays.asList("2190m", "630m", "560m", "160m", "80m", "60m", "40m", "30m", "20m", "17m", "15m", "12m", "10m", "6m", "4m", "2m", "1.25m", "70cm", "33cm", "23cm", "13cm", "9cm", "6cm", "3cm", "1.25cm", "6mm", "4mm", "2.5mm", "2mm", "1mm"));
    private static transient HashSet<String> validModes = new HashSet<>(Arrays.asList("AM", "ARDOP", "ATV", "C4FM", "CHIP", "CLO", "CONTESTI", "CW", "DIGITALVOICE", "DOMINO", "DSTAR", "FAX", "FM", "FSK441", "FT8", "HELL", "ISCAT", "JT4", "JT6M", "JT9", "JT44", "JT65", "MFSK", "MSK144", "MT63", "OLIVIA", "OPERA", "PAC", "PAX", "PKT", "PSK", "PSK2K", "Q15", "QRA64", "ROS", "RTTY", "RTTYM", "SSB", "SSTV", "T10", "THOR", "THRB", "TOR", "V4", "VOI", "WINMOR", "WSPR"));
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

    public boolean validateAdif(){
        boolean validTimeStart = timeStart.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
        boolean validTimeEnd = timeEnd.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

        return validTimeStart && validTimeEnd;
    }

    public String getAdifRow(){
        return  getAdifField("CALL", callSign) +
                getAdifField("QSO_DATE", timeStart.substring(0, 10).replace("-","")) +
                getAdifField("QSO_DATE_OFF", timeEnd.substring(0, 10).replace("-","")) +
                getAdifField("TIME_ON", timeStart.substring(11).replace(":","")) +
                getAdifField("TIME_OFF", timeEnd.substring(11).replace(":","")) +
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
                "<EOR>";
    }

    private String getAdifField(String field, String data){
        return "<" + field + ":" + data.length() + ">" + data;
    }

    public static boolean validateTimeStart(String value){
        return value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    }

    public static boolean validateTimeEnd(String value){
        return value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    }

    public static boolean validateFrequency(String value){
        return value.matches("[0-9]{1,6}|[0-9]{1,6}[.][0-9]{1,6}");
    }

    public static boolean validateBand(String value){
        return validBands.contains(value);
    }

    public static boolean validateMode(String value){
        return validModes.contains(value);
    }

    public static boolean validatePower(String value){
        return value.matches("[0-9]{1,4}");
    }
}