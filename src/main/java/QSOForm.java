import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class QSOForm extends JPanel{
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

    private JLabel timeStartLabel = new JLabel("Time start");
    private JLabel timeEndLabel = new JLabel("Time end");

    public QSOForm(){
        setLayout(new GridLayout(13,2));
        add(new JLabel("Call sign"));
        add(callSignTextField);
        add(timeStartLabel);
        add(timeStartTextField);
        add(timeEndLabel);
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

        timeStartTextField.getDocument().addDocumentListener(new TimeTextFieldDocumentListener());
        timeEndTextField.getDocument().addDocumentListener(new TimeTextFieldDocumentListener());

        ToolTipManager.sharedInstance().setDismissDelay(20000);

        callSignTextField.setToolTipText("The call sign of the other person");
        timeStartTextField.setToolTipText("The start time in the format YYYY-MM-DD HH:MM:SS");
        timeEndTextField.setToolTipText("The end time in the format YYYY-MM-DD HH:MM:SS");
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

    private void validateTime(){
        boolean validTimeStart = timeStartTextField.getText().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
        boolean validTimeEnd = timeEndTextField.getText().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");

        if (validTimeStart){
            timeStartLabel.setText("Time start (Valid)");
        }else{
            timeStartLabel.setText("Time start (Not valid)");
        }

        if (validTimeEnd){
            timeEndLabel.setText("Time end (Valid)");
        }else{
            timeEndLabel.setText("Time end (Not valid)");
        }
    }

    public void validate(){
        validateTime();
    }

    private class TimeTextFieldDocumentListener implements DocumentListener {
        @Override
        public void changedUpdate(DocumentEvent e){
            validateTime();
        }

        @Override
        public void removeUpdate(DocumentEvent e){
            validateTime();
        }

        @Override
        public void insertUpdate(DocumentEvent e){
            validateTime();
        }
    }
}