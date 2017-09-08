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
        boolean valid = true;

        if (!callSign.equals("")){
            if (!validateCallSign(callSign)){
                valid = false;
            }
        }

        if (!timeStart.equals("")){
            if (!validateTimeStart(timeStart)){
                valid = false;
            }
        }

        if (!timeEnd.equals("")){
            if (!validateTimeEnd(timeEnd)){
                valid = false;
            }
        }

        if (!frequency.equals("")){
            if (!validateFrequency(frequency)){
                valid = false;
            }
        }

        if (!band.equals("")){
            if (!validateBand(band)){
                valid = false;
            }
        }

        if (!mode.equals("")){
            if (!validateMode(mode)){
                valid = false;
            }
        }

        if (!power.equals("")){
            if (!validatePower(power)){
                valid = false;
            }
        }

        if (!myCallSign.equals("")){
            if (!validateMyCallSign(myCallSign)){
                valid = false;
            }
        }

        return valid;
    }

    public String getAdifRow(){
        String adifRow = "";

        if (!callSign.equals("")){
            adifRow += getAdifField("CALL", callSign);
        }

        if (!timeStart.equals("")){
            adifRow += getAdifField("QSO_DATE", timeStart.substring(0, 10).replace("-",""));
            adifRow += getAdifField("TIME_ON", timeStart.substring(11).replace(":",""));
        }

        if (!timeEnd.equals("")){
            adifRow += getAdifField("QSO_DATE_OFF", timeEnd.substring(0, 10).replace("-",""));
            adifRow += getAdifField("TIME_OFF", timeEnd.substring(11).replace(":",""));
        }

        if (!frequency.equals("")){
            adifRow += getAdifField("FREQ", frequency);
        }

        if (!band.equals("")){
            adifRow += getAdifField("BAND", band);
        }

        if (!mode.equals("")){
            adifRow += getAdifField("MODE", mode);
        }

        if (!power.equals("")){
            adifRow += getAdifField("TX_PWR", power);
        }

        if (!location.equals("")){
            if (isGridSquare(location)){
                adifRow += getAdifField("GRIDSQUARE", location);
            }else if(isAdifString(location)){
                adifRow += getAdifField("QTH", location);
            }else if(isAdifIntlString(location)){
                adifRow += getAdifField("QTH_INTL", location);
            }
        }

        if (!rstSent.equals("")){
            adifRow += getAdifField("RST_SENT", rstSent);
        }

        if (!rstReceived.equals("")){
            adifRow += getAdifField("RST_RCVD", rstReceived);
        }

        if (!myLocation.equals("")){
            if (isGridSquare(myLocation)){
                adifRow += getAdifField("MY_GRIDSQUARE", myLocation);
            }else if(isAdifString(myLocation)){
                adifRow += getAdifField("MY_CITY", myLocation);
            }else if(isAdifIntlString(myLocation)){
                adifRow += getAdifField("MY_CITY_INTL", myLocation);
            }
        }

        if (!myCallSign.equals("")){
            adifRow += getAdifField("OPERATOR", myCallSign);
        }

        if (!comments.equals("")){
            adifRow += getAdifField("COMMENT", comments);
        }

        adifRow += "<EOR>";

        return adifRow;
    }

    private String getAdifField(String field, String data){
        return "<" + field + ":" + data.length() + ">" + data;
    }

    private static boolean isGridSquare(String value){
        boolean gridSquareTwoCharacters = value.matches("[A-Z]{2}");
        boolean gridSquareFourCharacters = value.matches("[A-Z]{2}[0-9]{2}");
        boolean gridSquareSixCharacters = value.matches("[A-Z]{2}[0-9]{2}[a-z]{2}");
        boolean gridSquareEightCharacters = value.matches("[A-Z]{2}[0-9]{2}[a-z]{2}[0-9]{2}");

        return gridSquareTwoCharacters || gridSquareFourCharacters || gridSquareSixCharacters || gridSquareEightCharacters;
    }

    private static boolean isAdifString(String value){
        boolean valid = true;
        for (int i = 0; i < value.length(); i++){
            char c = value.charAt(i);
            if (c < 32 || c > 126){
                valid = false;
            }
        }
        return valid;
    }

    private static boolean isAdifIntlString(String value){
        boolean valid = true;
        for (int i = 0; i < value.length(); i++){
            char c = value.charAt(i);
            if (c == 13 || c == 10){
                valid = false;
            }
        }
        return valid;
    }

    public static boolean validateCallSign(String value){
        return isAdifString(value);
    }

    public static boolean validateTimeStart(String value){
        return value.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}[ ][0-9]{2}[:][0-9]{2}([:][0-9]{2})?");
    }

    public static boolean validateTimeEnd(String value){
        return value.matches("[0-9]{4}[-][0-9]{2}[-][0-9]{2}[ ][0-9]{2}[:][0-9]{2}([:][0-9]{2})?");
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

    public static boolean validateMyCallSign(String value){
        return isAdifString(value);
    }

    public static boolean validateLocation(String value){
        return isGridSquare(value) || isAdifString(value) || isAdifIntlString(value);
    }

    public static boolean validateMyLocation(String value){
        return isGridSquare(value) || isAdifString(value) || isAdifIntlString(value);
    }
}