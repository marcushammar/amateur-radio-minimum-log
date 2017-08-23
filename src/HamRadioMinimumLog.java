import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class HamRadioMinimumLog extends JFrame {
    private ArrayList<QSO> log = new ArrayList<>();
    private JTable table;
    private String[] columns = { "Call sign", "Time start", "Time end", "Frequency", "Band", "Mode", "Power", "Location", "RST sent", "RST received", "My call sign", "My location", "Comments"};
    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JButton exportButton;

    private HamRadioMinimumLog(){
        super("Ham Radio Minimum Log");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem loadLogMenuItem = new JMenuItem("Load log");
        JMenuItem saveLogMenuItem = new JMenuItem("Save log");
        JMenuItem exitMenuItem = new JMenuItem("Exit");

        loadLogMenuItem.addActionListener(new LoadLog());
        saveLogMenuItem.addActionListener(new SaveLog());
        exitMenuItem.addActionListener(new Exit());

        fileMenu.add(loadLogMenuItem);
        fileMenu.add(saveLogMenuItem);
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        table = new JTable(getDataForTable(), columns);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getSelectionModel().addListSelectionListener(new TableSelectionListener());
        add(scrollPane);
        updateTable();

        JPanel southPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add");
        modifyButton = new JButton("Modify");
        deleteButton = new JButton("Delete");
        exportButton = new JButton("Export");

        addButton.addActionListener(new AddButtonActionListener());
        modifyButton.addActionListener(new ModifyButtonActionListener());
        deleteButton.addActionListener(new DeleteButtonActionListener());
        exportButton.addActionListener(new ExportButtonActionListener());

        addButton.setMnemonic(KeyEvent.VK_A);
        modifyButton.setMnemonic(KeyEvent.VK_M);
        deleteButton.setMnemonic(KeyEvent.VK_D);
        exportButton.setMnemonic(KeyEvent.VK_E);

        southPanel.add(addButton);
        southPanel.add(modifyButton);
        southPanel.add(deleteButton);
        southPanel.add(exportButton);

        add(southPanel, BorderLayout.SOUTH);

        addButton.setEnabled(true);
        modifyButton.setEnabled(false);
        deleteButton.setEnabled(false);
        exportButton.setEnabled(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1000, 500);
        setVisible(true);
    }

    private void updateTable(){
        DefaultTableModel tableModel = new DefaultTableModel(getDataForTable(), columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(60);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);
        table.getColumnModel().getColumn(7).setPreferredWidth(70);
        table.getColumnModel().getColumn(8).setPreferredWidth(70);
        table.getColumnModel().getColumn(9).setPreferredWidth(90);
        table.getColumnModel().getColumn(10).setPreferredWidth(80);
        table.getColumnModel().getColumn(11).setPreferredWidth(80);
        table.getColumnModel().getColumn(12).setPreferredWidth(200);

    }

    private Object[][] getDataForTable(){
        Object[][] dataObject = new Object[log.size()][13];
        int i = 0;
        for(QSO qso : log){
            dataObject[i][0] = qso.getCallSign();
            dataObject[i][1] = qso.getTimeStart();
            dataObject[i][2] = qso.getTimeEnd();
            dataObject[i][3] = qso.getFrequency();
            dataObject[i][4] = qso.getBand();
            dataObject[i][5] = qso.getMode();
            dataObject[i][6] = qso.getPower();
            dataObject[i][7] = qso.getLocation();
            dataObject[i][8] = qso.getRstSent();
            dataObject[i][9] = qso.getRstRecevied();
            dataObject[i][10] = qso.getMyCallSign();
            dataObject[i][11] = qso.getMyLocation();
            dataObject[i][12] = qso.getComments();
            i++;
        }
        return dataObject;
    }

    private class AddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            QSOForm form = new QSOForm();
            int responseFromDialog = JOptionPane.showConfirmDialog(HamRadioMinimumLog.this, form, "Add", JOptionPane.OK_CANCEL_OPTION);

            if (responseFromDialog == JOptionPane.YES_OPTION){
                QSO qso = new QSO();
                qso.setCallSign(form.getCallSign());
                qso.setTimeStart(form.getTimeStart());
                qso.setTimeEnd(form.getTimeEnd());
                qso.setFrequency(form.getFrequency());
                qso.setBand(form.getBand());
                qso.setMode(form.getMode());
                qso.setPower(form.getPower());
                qso.setLocation(form.getLocationText());
                qso.setRstRecevied(form.getRstReceived());
                qso.setRstSent(form.getRstSent());
                qso.setMyCallSign(form.getMyCallSign());
                qso.setMyLocation(form.getMyLocation());
                qso.setComments(form.getComments());
                log.add(qso);
                updateTable();
            }
        }
    }

    private class ModifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length != 1){
                return;
            }

            QSO qso = log.get(selectedRows[0]);

            QSOForm qsoForm = new QSOForm();
            qsoForm.setCallSign(qso.getCallSign());
            qsoForm.setTimeStart(qso.getTimeStart());
            qsoForm.setTimeEnd(qso.getTimeEnd());
            qsoForm.setFrequency(qso.getFrequency());
            qsoForm.setBand(qso.getBand());
            qsoForm.setMode(qso.getMode());
            qsoForm.setPower(qso.getPower());
            qsoForm.setLocationText(qso.getLocation());
            qsoForm.setRstReceived(qso.getRstRecevied());
            qsoForm.setRstSent(qso.getRstSent());
            qsoForm.setMyCallSign(qso.getMyCallSign());
            qsoForm.setMyLocation(qso.getMyLocation());
            qsoForm.setComments(qso.getComments());

            int responseFromDialog = JOptionPane.showConfirmDialog(HamRadioMinimumLog.this, qsoForm, "Modify", JOptionPane.OK_CANCEL_OPTION);

            if (responseFromDialog == JOptionPane.YES_OPTION){
                qso.setCallSign(qsoForm.getCallSign());
                qso.setTimeStart(qsoForm.getTimeStart());
                qso.setTimeEnd(qsoForm.getTimeEnd());
                qso.setFrequency(qsoForm.getFrequency());
                qso.setBand(qsoForm.getBand());
                qso.setMode(qsoForm.getMode());
                qso.setPower(qsoForm.getPower());
                qso.setLocation(qsoForm.getLocationText());
                qso.setRstRecevied(qsoForm.getRstReceived());
                qso.setRstSent(qsoForm.getRstSent());
                qso.setMyCallSign(qsoForm.getMyCallSign());
                qso.setMyLocation(qsoForm.getMyLocation());
                qso.setComments(qsoForm.getComments());
                updateTable();
            }
        }
    }


    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 1){
                log.remove(selectedRows[0]);
                updateTable();
            }
        }
    }

    private class ExportButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            try{
                FileWriter fw = new FileWriter("export.adi");
                PrintWriter pw = new PrintWriter(fw);

                for (int i: table.getSelectedRows()){
                    pw.println(log.get(i).getAdifRow());
                }

                fw.close();
            }catch(IOException e){
                JOptionPane.showMessageDialog(HamRadioMinimumLog.this, "Something went wrong. Error message: " + e.getMessage());
            }
        }
    }


    private class LoadLog implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            try{
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("test.dat"));
                log = (ArrayList<QSO>)objectInputStream.readObject();
                objectInputStream.close();
                updateTable();

            }catch(IOException|ClassNotFoundException e){
                JOptionPane.showMessageDialog(HamRadioMinimumLog.this, "Something went wrong. Error message: " + e.getMessage());
            }
        }
    }


    private class SaveLog implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            try{
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("test.dat"));
                objectOutputStream.writeObject(log);
                objectOutputStream.close();
            }catch(IOException ioe){
                JOptionPane.showMessageDialog(HamRadioMinimumLog.this, "Something went wrong. Error message: " + ioe.getMessage());
            }
        }
    }

    private class Exit implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            System.exit(0);
        }
    }

    private class TableSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent){
            int[] selectedRows = table.getSelectedRows();

            switch (selectedRows.length) {
                case 0:
                    addButton.setEnabled(true);
                    modifyButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    exportButton.setEnabled(false);
                    break;
                case 1:
                    addButton.setEnabled(true);
                    modifyButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    exportButton.setEnabled(true);
                    break;
                default:
                    addButton.setEnabled(true);
                    modifyButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    exportButton.setEnabled(true);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new HamRadioMinimumLog();
    }
}