import javax.swing.*;
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
    }

    public String getCallSign(){
        return callSignTextField.getText();
    }

    public String getTimeStart(){
        return timeStartTextField.getText();
    }

    public String getTimeEnd(){
        return timeEndTextField.getText();
    }

    public String getFrequency(){
        return frequencyTextField.getText();
    }

    public String getBand(){
        return bandTextField.getText();
    }

    public String getMode(){
        return modeTextField.getText();
    }

    public String getPower(){
        return powerTextField.getText();
    }

    public String getLocationText(){
        return locationTextField.getText();
    }

    public String getRstSent(){
        return rstSentTextField.getText();
    }

    public String getRstReceived(){
        return rstReceivedTextField.getText();
    }

    public String getMyCallSign(){
        return myCallSignTextField.getText();
    }

    public String getMyLocation(){
        return myLocationTextField.getText();
    }

    public String getComments(){
        return commentsTextField.getText();
    }
}
