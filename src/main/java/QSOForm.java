import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class QSOForm extends JPanel{
    private final static Color GREEN_COLOR = Color.decode("#ebfce8");
    private final static Color RED_COLOR = Color.decode("#ffeaea");
    private JTextField callSignTextField = new JTextField(15);
    private JTextField timeStartTextField = new JTextField(15);
    private JTextField timeEndTextField = new JTextField(15);
    private JTextField frequencyTextField = new JTextField(15);
    private JTextField bandTextField = new JTextField(15);
    private JTextField modeTextField = new JTextField(15);
    private JTextField powerTextField = new JTextField(15);
    private JTextField locationTextField = new JTextField(15);
    private JTextField rstSentTextField = new JTextField(15);
    private JTextField rstReceivedTextField = new JTextField(15);
    private JTextField myCallSignTextField = new JTextField(15);
    private JTextField myLocationTextField = new JTextField(15);
    private JTextField commentsTextField = new JTextField(15);

    public QSOForm(){
        setLayout(new GridLayout(13,2));
        add(new JLabel("Call sign"));
        add(callSignTextField);
        add(new JLabel("Time start"));
        add(timeStartTextField);
        add(new JLabel("Time end"));
        add(timeEndTextField);
        add(new JLabel("Frequency"));
        add(frequencyTextField);
        add(new JLabel("Band"));
        add(bandTextField);
        add(new JLabel("Mode"));
        add(modeTextField);
        add(new JLabel("Power"));
        add(powerTextField);
        add(new JLabel("Location"));
        add(locationTextField);
        add(new JLabel("RST sent"));
        add(rstSentTextField);
        add(new JLabel("RST received"));
        add(rstReceivedTextField);
        add(new JLabel("My call sign"));
        add(myCallSignTextField);
        add(new JLabel("My location"));
        add(myLocationTextField);
        add(new JLabel("Comments"));
        add(commentsTextField);

        callSignTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        timeStartTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        timeEndTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        frequencyTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        bandTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        modeTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        powerTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        locationTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        rstSentTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        rstReceivedTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        myCallSignTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        myLocationTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());
        commentsTextField.getDocument().addDocumentListener(new TextFieldDocumentListener());

        ToolTipManager.sharedInstance().setDismissDelay(20000);

        callSignTextField.setToolTipText("The call sign of the other person");
        timeStartTextField.setToolTipText("The start time in the format YYYY-MM-DD HH:MM or YYYY-MM-DD HH:MM:SS");
        timeEndTextField.setToolTipText("The end time in the format YYYY-MM-DD HH:MM or YYYY-MM-DD HH:MM:SS");
        frequencyTextField.setToolTipText("The frequency in MHz in the format NN.NNN (use a dot, not comma)");
        bandTextField.setToolTipText("Use a small character for the unit (eng. 20m)");
        modeTextField.setToolTipText("Use large characters for the mode (eng. FT8)");
        powerTextField.setToolTipText("The power in watts with no decimals (eng. 5)");
        locationTextField.setToolTipText("Grid square or city of the other person");
        rstSentTextField.setToolTipText("<html>Signal report in the RST format<br><br>Readability (R)<br>1 - Unreadable<br>2 - Barely readable, occasional words distinguishable<br>3 - Readable with considerable difficulty<br>4 - Readable with practically no difficulty<br>5 - Perfectly readable<br><br>Strength (S)<br>1 - Faint signal, barely perceptible<br>2 - Very weak<br>3 - Weak<br>4 - Fair<br>5 - Fairly good<br>6 - Good<br>7 - Moderately strong<br>8 - Strong<br>9 - Very strong signals<br><br>Tone (T)<br>1 - Sixty cycle a.c or less, very rough and broad<br>2 - Very rough a.c., very harsh and broad<br>3 - Rough a.c. tone, rectified but not filtered<br>4 - Rough note, some trace of filtering<br>5 - Filtered rectified a.c. but strongly ripple-modulated<br>6 - Filtered tone, definite trace of ripple modulation<br>7 - Near pure tone, trace of ripple modulation<br>8 - Near perfect tone, slight trace of modulation<br>9 - Perfect tone, no trace of ripple or modulation of any kind</html>");
        rstReceivedTextField.setToolTipText("<html>Signal report in the RST format<br><br>Readability (R)<br>1 - Unreadable<br>2 - Barely readable, occasional words distinguishable<br>3 - Readable with considerable difficulty<br>4 - Readable with practically no difficulty<br>5 - Perfectly readable<br><br>Strength (S)<br>1 - Faint signal, barely perceptible<br>2 - Very weak<br>3 - Weak<br>4 - Fair<br>5 - Fairly good<br>6 - Good<br>7 - Moderately strong<br>8 - Strong<br>9 - Very strong signals<br><br>Tone (T)<br>1 - Sixty cycle a.c or less, very rough and broad<br>2 - Very rough a.c., very harsh and broad<br>3 - Rough a.c. tone, rectified but not filtered<br>4 - Rough note, some trace of filtering<br>5 - Filtered rectified a.c. but strongly ripple-modulated<br>6 - Filtered tone, definite trace of ripple modulation<br>7 - Near pure tone, trace of ripple modulation<br>8 - Near perfect tone, slight trace of modulation<br>9 - Perfect tone, no trace of ripple or modulation of any kind</html>");
        myCallSignTextField.setToolTipText("Your call sign");
        myLocationTextField.setToolTipText("Your grid square or city");
        commentsTextField.setToolTipText("Your comments");
    }

    public String getCallSign(){
        return callSignTextField.getText();
    }

    public void setCallSign(String callSign){
        callSignTextField.setText(callSign);
    }

    public String getTimeStart(){
        return timeStartTextField.getText();
    }

    public void setTimeStart(String timeStart){
        timeStartTextField.setText(timeStart);
    }

    public String getTimeEnd(){
        return timeEndTextField.getText();
    }

    public void setTimeEnd(String timeEnd){
        timeEndTextField.setText(timeEnd);
    }

    public String getFrequency(){
        return frequencyTextField.getText();
    }

    public void setFrequency(String frequency){
        frequencyTextField.setText(frequency);
    }

    public String getBand(){
        return bandTextField.getText();
    }

    public void setBand(String band){
        bandTextField.setText(band);
    }

    public String getMode(){
        return modeTextField.getText();
    }

    public void setMode(String mode){
        modeTextField.setText(mode);
    }

    public String getPower(){
        return powerTextField.getText();
    }

    public void setPower(String power){
        powerTextField.setText(power);
    }

    public String getLocationText(){
        return locationTextField.getText();
    }

    public void setLocationText(String locationText){
        locationTextField.setText(locationText);
    }

    public String getRstSent(){
        return rstSentTextField.getText();
    }

    public void setRstSent(String rstSent){
        rstSentTextField.setText(rstSent);
    }

    public String getRstReceived(){
        return rstReceivedTextField.getText();
    }

    public void setRstReceived(String rstReceived){
        rstReceivedTextField.setText(rstReceived);
    }

    public String getMyCallSign(){
        return myCallSignTextField.getText();
    }

    public void setMyCallSign(String myCallSign){
        myCallSignTextField.setText(myCallSign);
    }

    public String getMyLocation(){
        return myLocationTextField.getText();
    }

    public void setMyLocation(String myLocation){
        myLocationTextField.setText(myLocation);
    }

    public String getComments(){
        return commentsTextField.getText();
    }

    public void setComments(String comments){
        commentsTextField.setText(comments);
    }

    private void validateCallSign() {
        if (callSignTextField.getText().equals("")) {
            callSignTextField.setBackground(Color.WHITE);
        }else if(QSO.validateCallSign(callSignTextField.getText())) {
            callSignTextField.setBackground(GREEN_COLOR);
        }else{
            callSignTextField.setBackground(RED_COLOR);
        }
    }

    private void validateTimeStart() {
        if (timeStartTextField.getText().equals("")) {
            timeStartTextField.setBackground(Color.WHITE);
        }else if(QSO.validateTimeStart(timeStartTextField.getText())) {
            timeStartTextField.setBackground(GREEN_COLOR);
        }else{
            timeStartTextField.setBackground(RED_COLOR);
        }
    }

    private void validateTimeEnd() {
        if (timeEndTextField.getText().equals("")) {
            timeEndTextField.setBackground(Color.WHITE);
        }else if(QSO.validateTimeEnd(timeEndTextField.getText())){
            timeEndTextField.setBackground(GREEN_COLOR);
        }else{
            timeEndTextField.setBackground(RED_COLOR);
        }
    }

    private void validateFrequency(){
        if (frequencyTextField.getText().equals("")) {
            frequencyTextField.setBackground(Color.WHITE);
        }else if(QSO.validateFrequency(frequencyTextField.getText())){
            frequencyTextField.setBackground(GREEN_COLOR);
        }else{
            frequencyTextField.setBackground(RED_COLOR);
        }
    }

    private void validateBand(){
        if (bandTextField.getText().equals("")){
            bandTextField.setBackground(Color.WHITE);
        }else if(QSO.validateBand(bandTextField.getText())){
            bandTextField.setBackground(GREEN_COLOR);
        }else{
            bandTextField.setBackground(RED_COLOR);
        }
    }

    private void validateMode(){
        if (modeTextField.getText().equals("")){
            modeTextField.setBackground(Color.WHITE);
        }else if(QSO.validateMode(modeTextField.getText())){
            modeTextField.setBackground(GREEN_COLOR);
        }else{
            modeTextField.setBackground(RED_COLOR);
        }
    }

    private void validatePower(){
        if (powerTextField.getText().equals("")) {
            powerTextField.setBackground(Color.WHITE);
        }else if(QSO.validatePower(powerTextField.getText())){
            powerTextField.setBackground(GREEN_COLOR);
        }else{
            powerTextField.setBackground(RED_COLOR);
        }
    }

    private void validateLocation(){
        if (locationTextField.getText().equals("")) {
            locationTextField.setBackground(Color.WHITE);
        }else if(QSO.validateLocation(locationTextField.getText())){
            locationTextField.setBackground(GREEN_COLOR);
        }else{
            locationTextField.setBackground(RED_COLOR);
        }
    }

    private void validateRstSent(){
        if (rstSentTextField.getText().equals("")) {
            rstSentTextField.setBackground(Color.WHITE);
        }else if(QSO.validateRstSent(rstSentTextField.getText())){
            rstSentTextField.setBackground(GREEN_COLOR);
        }else{
            rstSentTextField.setBackground(RED_COLOR);
        }
    }

    private void validateRstReceived(){
        if (rstReceivedTextField.getText().equals("")) {
            rstReceivedTextField.setBackground(Color.WHITE);
        }else if(QSO.validateRstReceived(rstReceivedTextField.getText())){
            rstReceivedTextField.setBackground(GREEN_COLOR);
        }else{
            rstReceivedTextField.setBackground(RED_COLOR);
        }
    }

    private void validateMyCallSign() {
        if (myCallSignTextField.getText().equals("")) {
            myCallSignTextField.setBackground(Color.WHITE);
        }else if(QSO.validateMyCallSign(myCallSignTextField.getText())) {
            myCallSignTextField.setBackground(GREEN_COLOR);
        }else{
            myCallSignTextField.setBackground(RED_COLOR);
        }
    }

    private void validateMyLocation(){
        if (myLocationTextField.getText().equals("")) {
            myLocationTextField.setBackground(Color.WHITE);
        }else if(QSO.validateMyLocation(myLocationTextField.getText())){
            myLocationTextField.setBackground(GREEN_COLOR);
        }else{
            myLocationTextField.setBackground(RED_COLOR);
        }
    }

    private void validateComments(){
        if (commentsTextField.getText().equals("")) {
            commentsTextField.setBackground(Color.WHITE);
        }else if(QSO.validateComments(commentsTextField.getText())){
            commentsTextField.setBackground(GREEN_COLOR);
        }else{
            commentsTextField.setBackground(RED_COLOR);
        }
    }

    public void validate(){
        validateCallSign();
        validateTimeStart();
        validateTimeEnd();
        validateFrequency();
        validateBand();
        validateMode();
        validatePower();
        validateLocation();
        validateRstSent();
        validateRstReceived();
        validateMyCallSign();
        validateMyLocation();
        validateComments();
    }

    private class TextFieldDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent e){
            validate();
        }

        @Override
        public void removeUpdate(DocumentEvent e){
            validate();
        }

        @Override
        public void insertUpdate(DocumentEvent e){
            validate();
        }
    }
}