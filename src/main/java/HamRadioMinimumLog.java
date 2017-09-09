import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HamRadioMinimumLog extends JFrame {
    private final String APPLICATION_VERSION = "1.0.0";

    private ArrayList<QSO> log = new ArrayList<>();
    private boolean unsavedChanges = false;
    private File currentFile;
    private JTable table;
    private String[] columns = { "Call sign", "Time start", "Time end", "Frequency", "Band", "Mode", "Power", "Location", "RST sent", "RST received", "My call sign", "My location", "Comments"};
    private JButton addButton;
    private JButton copyButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JButton exportButton;
    private JFileChooser exportFileChooser = new JFileChooser();
    private JFileChooser loadAndSaveFileChooser = new JFileChooser();
    private JLabel countLabel;

    private HamRadioMinimumLog(){
        super("Ham Radio Minimum Log");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem newLogMenuItem = new JMenuItem("New log");
        JMenuItem loadMenuItem = new JMenuItem("Load...");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem saveAsMenuItem = new JMenuItem("Save as...");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        JMenuItem aboutMenuItem = new JMenuItem("About");

        newLogMenuItem.addActionListener(new NewLog());
        loadMenuItem.addActionListener(new Load());
        saveMenuItem.addActionListener(new Save());
        saveAsMenuItem.addActionListener(new SaveAs());
        exitMenuItem.addActionListener(new Exit());
        aboutMenuItem.addActionListener(new About());

        fileMenu.add(newLogMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        table = new JTable(getDataForTable(), columns);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getSelectionModel().addListSelectionListener(new TableSelectionListener());
        add(scrollPane);

        JPanel southPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add row");
        copyButton = new JButton("Copy row");
        modifyButton = new JButton("Modify row");
        deleteButton = new JButton("Delete row");
        exportButton = new JButton("Export selected rows (ADIF format)");

        addButton.addActionListener(new AddButtonActionListener());
        copyButton.addActionListener(new CopyButtonActionListener());
        modifyButton.addActionListener(new ModifyButtonActionListener());
        deleteButton.addActionListener(new DeleteButtonActionListener());
        exportButton.addActionListener(new ExportButtonActionListener());

        addButton.setMnemonic(KeyEvent.VK_A);
        copyButton.setMnemonic(KeyEvent.VK_C);
        modifyButton.setMnemonic(KeyEvent.VK_M);
        deleteButton.setMnemonic(KeyEvent.VK_D);
        exportButton.setMnemonic(KeyEvent.VK_E);

        southPanel.add(addButton);
        southPanel.add(copyButton);
        southPanel.add(modifyButton);
        southPanel.add(deleteButton);
        southPanel.add(exportButton);

        countLabel = new JLabel("Count: 0");
        southPanel.add(countLabel);

        add(southPanel, BorderLayout.SOUTH);

        addButton.setEnabled(true);
        copyButton.setEnabled(false);
        modifyButton.setEnabled(false);
        deleteButton.setEnabled(false);
        exportButton.setEnabled(false);

        updateTable();

        exportFileChooser.setSelectedFile(new File("export.adi"));
        exportFileChooser.setFileFilter(new FileNameExtensionFilter("ADIF format","adi"));
        loadAndSaveFileChooser.setFileFilter(new FileNameExtensionFilter("Ham Radio Minimum Log format","json"));

        addWindowListener(new ApplicationTerminationListener());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
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

        countLabel.setText("Count: " + log.size());
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
            form.validate();
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
                unsavedChanges = true;
            }
        }
    }

    private class CopyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length != 1){
                return;
            }

            QSO qsoExisting = log.get(selectedRows[0]);

            QSOForm qsoForm = new QSOForm();
            qsoForm.setCallSign(qsoExisting.getCallSign());
            qsoForm.setTimeStart(qsoExisting.getTimeStart());
            qsoForm.setTimeEnd(qsoExisting.getTimeEnd());
            qsoForm.setFrequency(qsoExisting.getFrequency());
            qsoForm.setBand(qsoExisting.getBand());
            qsoForm.setMode(qsoExisting.getMode());
            qsoForm.setPower(qsoExisting.getPower());
            qsoForm.setLocationText(qsoExisting.getLocation());
            qsoForm.setRstReceived(qsoExisting.getRstRecevied());
            qsoForm.setRstSent(qsoExisting.getRstSent());
            qsoForm.setMyCallSign(qsoExisting.getMyCallSign());
            qsoForm.setMyLocation(qsoExisting.getMyLocation());
            qsoForm.setComments(qsoExisting.getComments());

            qsoForm.validate();

            int responseFromDialog = JOptionPane.showConfirmDialog(HamRadioMinimumLog.this, qsoForm, "Copy", JOptionPane.OK_CANCEL_OPTION);

            if (responseFromDialog == JOptionPane.YES_OPTION){
                QSO qsoNew = new QSO();
                qsoNew.setCallSign(qsoForm.getCallSign());
                qsoNew.setTimeStart(qsoForm.getTimeStart());
                qsoNew.setTimeEnd(qsoForm.getTimeEnd());
                qsoNew.setFrequency(qsoForm.getFrequency());
                qsoNew.setBand(qsoForm.getBand());
                qsoNew.setMode(qsoForm.getMode());
                qsoNew.setPower(qsoForm.getPower());
                qsoNew.setLocation(qsoForm.getLocationText());
                qsoNew.setRstRecevied(qsoForm.getRstReceived());
                qsoNew.setRstSent(qsoForm.getRstSent());
                qsoNew.setMyCallSign(qsoForm.getMyCallSign());
                qsoNew.setMyLocation(qsoForm.getMyLocation());
                qsoNew.setComments(qsoForm.getComments());
                log.add(qsoNew);
                updateTable();
                unsavedChanges = true;
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

            qsoForm.validate();

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
                unsavedChanges = true;
            }
        }
    }


    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            int responseFromDialog = JOptionPane.showConfirmDialog(HamRadioMinimumLog.this, "Are you sure you want to delete the row?", "Delete row", JOptionPane.YES_NO_OPTION);
            if (responseFromDialog != JOptionPane.YES_OPTION){
                return;
            }

            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 1){
                log.remove(selectedRows[0]);
                updateTable();
                unsavedChanges = true;
            }
        }
    }

    private class ExportButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            for (int i : table.getSelectedRows()) {
                if (!log.get(i).validateAdif()){
                    JOptionPane.showMessageDialog(HamRadioMinimumLog.this, "Can't export due to issues in row " + (i + 1) + " (callsign " + log.get(i).getCallSign() + "). Please correct the row and try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try{
                int resultFromDialog = exportFileChooser.showSaveDialog(HamRadioMinimumLog.this);
                if (resultFromDialog != JFileChooser.APPROVE_OPTION){
                    return;
                }

                File exportFile = exportFileChooser.getSelectedFile();

                if (exportFile.exists()) {
                    int responseFromDialog = JOptionPane.showConfirmDialog(HamRadioMinimumLog.this, "There is already a file with that name. Do you want to overwrite the file?", "Export", JOptionPane.YES_NO_OPTION);
                    if (responseFromDialog != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                FileWriter fw = new FileWriter(exportFile);
                PrintWriter pw = new PrintWriter(fw);

                pw.println("This ADIF file was extracted from Ham Radio Minimum Log");
                pw.println();
                pw.println("<ADIF_VER:5>3.0.6");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                pw.println("<CREATED_TIMESTAMP:15>" + sdf.format(new Date()));
                pw.println("<PROGRAMID:21>Ham Radio Minimum Log");
                pw.println("<PROGRAMVERSION:" + APPLICATION_VERSION.length() + ">" + APPLICATION_VERSION);
                pw.println("<EOH>");
                pw.println();

                for (int i: table.getSelectedRows()){
                    pw.println(log.get(i).getAdifRow());
                }

                fw.close();
            }catch(IOException e){
                JOptionPane.showMessageDialog(HamRadioMinimumLog.this, "Something went wrong. Error message: " + e.getMessage());
            }
        }
    }

    private class NewLog implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            if (unsavedChanges){
                int responseFromDialog = JOptionPane.showConfirmDialog(HamRadioMinimumLog.this, "There are unsaved changes. Do you want create a new log and ignore any changes made before?", "New log", JOptionPane.YES_NO_OPTION);
                if (responseFromDialog != JOptionPane.YES_OPTION){
                    return;
                }
            }
            log.clear();
            updateTable();
            unsavedChanges = false;
            currentFile = null;
        }
    }

    private class Load implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            if (unsavedChanges){
                int responseFromDialog = JOptionPane.showConfirmDialog(HamRadioMinimumLog.this, "There are unsaved changes. Do you want load a new log and ignore any changes made before?", "Load log", JOptionPane.YES_NO_OPTION);
                if (responseFromDialog != JOptionPane.YES_OPTION){
                    return;
                }
            }
            try{
                int resultFromDialog = loadAndSaveFileChooser.showOpenDialog(HamRadioMinimumLog.this);
                if (resultFromDialog != JFileChooser.APPROVE_OPTION){
                    return;
                }

                currentFile = loadAndSaveFileChooser.getSelectedFile();

                BufferedReader br = new BufferedReader(new FileReader(currentFile));
                Gson gson = new Gson();
                log.clear();
                log = gson.fromJson(br, new TypeToken<ArrayList<QSO>>(){}.getType());
                br.close();

                updateTable();
                unsavedChanges = false;

            }catch(IOException e){
                JOptionPane.showMessageDialog(HamRadioMinimumLog.this, "Something went wrong. Error message: " + e.getMessage());
            }
        }
    }

    private class Save implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            try{
                if (currentFile == null) {
                    int resultFromDialog = loadAndSaveFileChooser.showSaveDialog(HamRadioMinimumLog.this);
                    if (resultFromDialog != JFileChooser.APPROVE_OPTION) {
                        return;
                    }

                    File newFile = loadAndSaveFileChooser.getSelectedFile();

                    if ((newFile.exists() && currentFile == null) || (newFile.exists() && currentFile != null && !newFile.equals(currentFile))) {
                        int responseFromDialog = JOptionPane.showConfirmDialog(HamRadioMinimumLog.this, "There is already a file with that name. Do you want to overwrite the file?", "Save log", JOptionPane.YES_NO_OPTION);
                        if (responseFromDialog != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }

                    String fileName = newFile.toString();
                    if (!fileName.endsWith(".json")){
                        fileName += ".json";
                    }

                    currentFile = new File(fileName);
                }

                Gson gson = new Gson();
                FileWriter fw = new FileWriter(currentFile);
                fw.write(gson.toJson(log));
                fw.close();

                unsavedChanges = false;
            }catch(IOException ioe){
                JOptionPane.showMessageDialog(HamRadioMinimumLog.this, "Something went wrong. Error message: " + ioe.getMessage());
            }
        }
    }

    private class SaveAs implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            try{
                int resultFromDialog = loadAndSaveFileChooser.showSaveDialog(HamRadioMinimumLog.this);
                if (resultFromDialog != JFileChooser.APPROVE_OPTION){
                    return;
                }

                File newFile = loadAndSaveFileChooser.getSelectedFile();

                if ((newFile.exists() && currentFile == null) || (newFile.exists() && currentFile != null && !newFile.equals(currentFile))){
                    int responseFromDialog = JOptionPane.showConfirmDialog(HamRadioMinimumLog.this, "There is already a file with that name. Do you want to overwrite the file?", "Save log", JOptionPane.YES_NO_OPTION);
                    if (responseFromDialog != JOptionPane.YES_OPTION){
                        return;
                    }
                }

                String fileName = newFile.toString();
                if (!fileName.endsWith(".json")){
                    fileName += ".json";
                }

                currentFile = new File(fileName);

                Gson gson = new Gson();
                FileWriter fw = new FileWriter(currentFile);
                fw.write(gson.toJson(log));
                fw.close();

                unsavedChanges = false;
            }catch(IOException ioe){
                JOptionPane.showMessageDialog(HamRadioMinimumLog.this, "Something went wrong. Error message: " + ioe.getMessage());
            }
        }
    }

    private class Exit implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            applicationIsAboutToClose();
        }
    }

    private class About implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            JOptionPane.showMessageDialog(HamRadioMinimumLog.this, "Ham Radio Minimum Log\nVersion " + APPLICATION_VERSION + "\n\nThis application uses GSON from Google. GSON is licensed under the Apache License 2.0.\nApache License 2.0 can be found at http://www.apache.org/licenses/LICENSE-2.0", "About", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private class TableSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent){
            int[] selectedRows = table.getSelectedRows();

            switch (selectedRows.length) {
                case 0:
                    addButton.setEnabled(true);
                    copyButton.setEnabled(false);
                    modifyButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    exportButton.setEnabled(false);
                    break;
                case 1:
                    addButton.setEnabled(true);
                    copyButton.setEnabled(true);
                    modifyButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    exportButton.setEnabled(true);
                    break;
                default:
                    addButton.setEnabled(true);
                    copyButton.setEnabled(false);
                    modifyButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    exportButton.setEnabled(true);
                    break;
            }
        }
    }

    private void applicationIsAboutToClose(){
        if (unsavedChanges){
            int responseFromDialog = JOptionPane.showConfirmDialog(HamRadioMinimumLog.this, "There are unsaved changes. Do you want to proceed with closing the application?", "Exit", JOptionPane.YES_NO_OPTION);
            if (responseFromDialog != JOptionPane.YES_OPTION){
                return;
            }
        }
        System.exit(0);
    }

    private class ApplicationTerminationListener extends WindowAdapter implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){}

        @Override
        public void windowClosing(WindowEvent we){
            applicationIsAboutToClose();
        }
    }

    public static void main(String[] args) {
        new HamRadioMinimumLog();
    }
}