import javax.swing.*;
import java.awt.*;

public class QSOForm extends JPanel{
    private JTextField callSignTextField = new JTextField(10);
    private JTextField timeStartTextField = new JTextField(10);

    public QSOForm(){
        setLayout(new GridLayout(4,1));
        add(new JLabel("Call sign"));
        add(callSignTextField);
        add(new JLabel("Time start"));
        add(timeStartTextField);
    }

    public String getCallSign(){
        return callSignTextField.getText();
    }

    public String getTimeStart(){
        return timeStartTextField.getText();
    }
}
