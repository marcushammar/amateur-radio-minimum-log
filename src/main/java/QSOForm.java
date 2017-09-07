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