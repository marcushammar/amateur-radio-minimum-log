/*
 * Amateur Radio Minimum Log
 * Copyright (C) 2017-2018  Marcus Hammar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

public class GUI extends JFrame {
    private final String APPLICATION_VERSION = "1.1.0";

    private ArrayList<QSO> log = new ArrayList<>();
    private boolean unsavedChanges = false;
    private File currentFile;
    private JTable table;
    private String[] columns = {"Call sign", "Time start", "Time end", "Frequency", "Band", "Mode", "Power", "Location", "RST sent", "RST received", "My call sign", "My location", "Comments"};
    private JButton addButton;
    private JButton copyButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JButton exportButton;
    private JFileChooser exportFileChooser = new JFileChooser();
    private JFileChooser loadAndSaveFileChooser = new JFileChooser();
    private JLabel countLabel;

    public GUI() {
        super("Amateur Radio Minimum Log");

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

        exportFileChooser.setFileFilter(new FileNameExtensionFilter("ADIF format", "adi"));
        loadAndSaveFileChooser.setFileFilter(new FileNameExtensionFilter("Amateur Radio Minimum Log format", "json"));

        addWindowListener(new ApplicationTerminationListener());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateTable() {
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

    private Object[][] getDataForTable() {
        Object[][] dataObject = new Object[log.size()][13];
        int i = 0;
        for (QSO qso : log) {
            dataObject[i][0] = qso.getField("CALL");
            dataObject[i][1] = qso.getField("QSO_DATE");
            dataObject[i][2] = qso.getField("QSO_DATE_OFF");
            dataObject[i][3] = qso.getField("FREQ");
            dataObject[i][4] = qso.getField("BAND");
            dataObject[i][5] = qso.getField("MODE");
            dataObject[i][6] = qso.getField("TX_PWR");
            dataObject[i][7] = qso.getField("GRIDSQUARE");
            dataObject[i][8] = qso.getField("RST_SENT");
            dataObject[i][9] = qso.getField("RST_RCVD");
            dataObject[i][10] = qso.getField("OPERATOR");
            dataObject[i][11] = qso.getField("MY_GRIDSQUARE");
            dataObject[i][12] = qso.getField("COMMENT");
            i++;
        }
        return dataObject;
    }

    private void applicationIsAboutToClose() {
        if (unsavedChanges) {
            int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, "There are unsaved changes. Do you want to proceed with closing the application?", "Exit", JOptionPane.YES_NO_OPTION);
            if (responseFromDialog != JOptionPane.YES_OPTION) {
                return;
            }
        }
        System.exit(0);
    }

    private class AddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            QSOForm form = new QSOForm();
            form.validate();
            int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, form, "Add", JOptionPane.OK_CANCEL_OPTION);

            if (responseFromDialog == JOptionPane.YES_OPTION) {
                QSO qso = new QSO();
                qso.setField("CALL", form.getCallSign());
                qso.setField("QSO_DATE", form.getTimeStart());
                qso.setField("QSO_DATE_OFF", form.getTimeEnd());
                qso.setField("FREQ", form.getFrequency());
                qso.setField("BAND", form.getBand());
                qso.setField("MODE", form.getMode());
                qso.setField("TX_PWR", form.getPower());
                qso.setField("GRIDSQUARE", form.getLocationText());
                qso.setField("RST_RCVD", form.getRstReceived());
                qso.setField("RST_SENT", form.getRstSent());
                qso.setField("OPERATOR", form.getMyCallSign());
                qso.setField("MY_GRIDSQUARE", form.getMyLocation());
                qso.setField("COMMENT", form.getComments());
                log.add(qso);
                updateTable();
                unsavedChanges = true;
            }
        }
    }

    private class CopyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length != 1) {
                return;
            }

            QSO qsoExisting = log.get(selectedRows[0]);

            QSOForm qsoForm = new QSOForm();
            qsoForm.setCallSign(qsoExisting.getField("CALL"));
            qsoForm.setTimeStart(qsoExisting.getField("QSO_DATE"));
            qsoForm.setTimeEnd(qsoExisting.getField("QSO_DATE_OFF"));
            qsoForm.setFrequency(qsoExisting.getField("FREQ"));
            qsoForm.setBand(qsoExisting.getField("BAND"));
            qsoForm.setMode(qsoExisting.getField("MODE"));
            qsoForm.setPower(qsoExisting.getField("TX_PWR"));
            qsoForm.setLocationText(qsoExisting.getField("GRIDSQUARE"));
            qsoForm.setRstReceived(qsoExisting.getField("RST_RCVD"));
            qsoForm.setRstSent(qsoExisting.getField("RST_SENT"));
            qsoForm.setMyCallSign(qsoExisting.getField("OPERATOR"));
            qsoForm.setMyLocation(qsoExisting.getField("MY_GRIDSQUARE"));
            qsoForm.setComments(qsoExisting.getField("COMMENT"));

            qsoForm.validate();

            int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, qsoForm, "Copy", JOptionPane.OK_CANCEL_OPTION);

            if (responseFromDialog == JOptionPane.YES_OPTION) {
                QSO qsoNew = new QSO();
                qsoNew.setField("CALL", qsoForm.getCallSign());
                qsoNew.setField("QSO_DATE", qsoForm.getTimeStart());
                qsoNew.setField("QSO_DATE_OFF", qsoForm.getTimeEnd());
                qsoNew.setField("FREQ", qsoForm.getFrequency());
                qsoNew.setField("BAND", qsoForm.getBand());
                qsoNew.setField("MODE", qsoForm.getMode());
                qsoNew.setField("TX_PWR", qsoForm.getPower());
                qsoNew.setField("GRIDSQUARE", qsoForm.getLocationText());
                qsoNew.setField("RST_RCVD", qsoForm.getRstReceived());
                qsoNew.setField("RST_SENT", qsoForm.getRstSent());
                qsoNew.setField("OPERATOR", qsoForm.getMyCallSign());
                qsoNew.setField("MY_GRIDSQUARE", qsoForm.getMyLocation());
                qsoNew.setField("COMMENT", qsoForm.getComments());
                log.add(qsoNew);
                updateTable();
                unsavedChanges = true;
            }
        }
    }

    private class ModifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length != 1) {
                return;
            }

            QSO qso = log.get(selectedRows[0]);

            QSOForm qsoForm = new QSOForm();
            qsoForm.setCallSign(qso.getField("CALL"));
            qsoForm.setTimeStart(qso.getField("QSO_DATE"));
            qsoForm.setTimeEnd(qso.getField("QSO_DATE_OFF"));
            qsoForm.setFrequency(qso.getField("FREQ"));
            qsoForm.setBand(qso.getField("BAND"));
            qsoForm.setMode(qso.getField("MODE"));
            qsoForm.setPower(qso.getField("TX_PWR"));
            qsoForm.setLocationText(qso.getField("GRIDSQUARE"));
            qsoForm.setRstReceived(qso.getField("RST_RCVD"));
            qsoForm.setRstSent(qso.getField("RST_SENT"));
            qsoForm.setMyCallSign(qso.getField("OPERATOR"));
            qsoForm.setMyLocation(qso.getField("MY_GRIDSQUARE"));
            qsoForm.setComments(qso.getField("COMMENT"));

            qsoForm.validate();

            int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, qsoForm, "Modify", JOptionPane.OK_CANCEL_OPTION);

            if (responseFromDialog == JOptionPane.YES_OPTION) {
                qso.setField("CALL", qsoForm.getCallSign());
                qso.setField("QSO_DATE", qsoForm.getTimeStart());
                qso.setField("QSO_DATE_OFF", qsoForm.getTimeEnd());
                qso.setField("FREQ", qsoForm.getFrequency());
                qso.setField("BAND", qsoForm.getBand());
                qso.setField("MODE", qsoForm.getMode());
                qso.setField("TX_PWR", qsoForm.getPower());
                qso.setField("GRIDSQUARE", qsoForm.getLocationText());
                qso.setField("RST_RCVD", qsoForm.getRstReceived());
                qso.setField("RST_SENT", qsoForm.getRstSent());
                qso.setField("OPERATOR", qsoForm.getMyCallSign());
                qso.setField("MY_GRIDSQUARE", qsoForm.getMyLocation());
                qso.setField("COMMENT", qsoForm.getComments());
                updateTable();
                unsavedChanges = true;
            }
        }
    }

    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, "Are you sure you want to delete the row?", "Delete row", JOptionPane.YES_NO_OPTION);
            if (responseFromDialog != JOptionPane.YES_OPTION) {
                return;
            }

            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 1) {
                log.remove(selectedRows[0]);
                updateTable();
                unsavedChanges = true;
            }
        }
    }

    private class ExportButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for (int i : table.getSelectedRows()) {
                if (!log.get(i).validateAdif()) {
                    JOptionPane.showMessageDialog(GUI.this, "Can't export due to issues in row " + (i + 1) + " (callsign " + log.get(i).getField("CALL") + "). Please correct the row and try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String timeNow = sdf.format(new Date());

                exportFileChooser.setSelectedFile(new File("ADIF export " + timeNow + ".adi"));

                int resultFromDialog = exportFileChooser.showSaveDialog(GUI.this);
                if (resultFromDialog != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                File exportFile = exportFileChooser.getSelectedFile();

                if (exportFile.exists()) {
                    int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, "There is already a file with that name. Do you want to overwrite the file?", "Export", JOptionPane.YES_NO_OPTION);
                    if (responseFromDialog != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                FileWriter fw = new FileWriter(exportFile);
                PrintWriter pw = new PrintWriter(fw);

                pw.println("This ADIF file was extracted from Amateur Radio Minimum Log");
                pw.println();
                pw.println("<ADIF_VER:5>3.0.6");
                pw.println("<CREATED_TIMESTAMP:15>" + timeNow);
                pw.println("<PROGRAMID:25>Amateur Radio Minimum Log");
                pw.println("<PROGRAMVERSION:" + APPLICATION_VERSION.length() + ">" + APPLICATION_VERSION);
                pw.println("<EOH>");
                pw.println();

                for (int i : table.getSelectedRows()) {
                    pw.println(log.get(i).getAdifRow());
                }

                fw.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(GUI.this, "Something went wrong. Error message: " + e.getMessage());
            }
        }
    }

    private class NewLog implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (unsavedChanges) {
                int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, "There are unsaved changes. Do you want create a new log and ignore any changes made before?", "New log", JOptionPane.YES_NO_OPTION);
                if (responseFromDialog != JOptionPane.YES_OPTION) {
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
        public void actionPerformed(ActionEvent actionEvent) {
            if (unsavedChanges) {
                int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, "There are unsaved changes. Do you want load a new log and ignore any changes made before?", "Load log", JOptionPane.YES_NO_OPTION);
                if (responseFromDialog != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            try {
                int resultFromDialog = loadAndSaveFileChooser.showOpenDialog(GUI.this);
                if (resultFromDialog != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                currentFile = loadAndSaveFileChooser.getSelectedFile();

                BufferedReader br = new BufferedReader(new FileReader(currentFile));
                Gson gson = new Gson();
                log.clear();
                log = gson.fromJson(br, new TypeToken<ArrayList<QSO>>() {
                }.getType());
                br.close();

                updateTable();
                unsavedChanges = false;

            } catch (IOException e) {
                JOptionPane.showMessageDialog(GUI.this, "Something went wrong. Error message: " + e.getMessage());
            }
        }
    }

    private class Save implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                if (currentFile == null) {
                    int resultFromDialog = loadAndSaveFileChooser.showSaveDialog(GUI.this);
                    if (resultFromDialog != JFileChooser.APPROVE_OPTION) {
                        return;
                    }

                    File newFile = loadAndSaveFileChooser.getSelectedFile();

                    if ((newFile.exists() && currentFile == null) || (newFile.exists() && currentFile != null && !newFile.equals(currentFile))) {
                        int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, "There is already a file with that name. Do you want to overwrite the file?", "Save log", JOptionPane.YES_NO_OPTION);
                        if (responseFromDialog != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }

                    String fileName = newFile.toString();
                    if (!fileName.endsWith(".json")) {
                        fileName += ".json";
                    }

                    currentFile = new File(fileName);
                }

                Gson gson = new Gson();
                FileWriter fw = new FileWriter(currentFile);
                fw.write(gson.toJson(log));
                fw.close();

                unsavedChanges = false;
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(GUI.this, "Something went wrong. Error message: " + ioe.getMessage());
            }
        }
    }

    private class SaveAs implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                int resultFromDialog = loadAndSaveFileChooser.showSaveDialog(GUI.this);
                if (resultFromDialog != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                File newFile = loadAndSaveFileChooser.getSelectedFile();

                if ((newFile.exists() && currentFile == null) || (newFile.exists() && currentFile != null && !newFile.equals(currentFile))) {
                    int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, "There is already a file with that name. Do you want to overwrite the file?", "Save log", JOptionPane.YES_NO_OPTION);
                    if (responseFromDialog != JOptionPane.YES_OPTION) {
                        return;
                    }
                }

                String fileName = newFile.toString();
                if (!fileName.endsWith(".json")) {
                    fileName += ".json";
                }

                currentFile = new File(fileName);

                Gson gson = new Gson();
                FileWriter fw = new FileWriter(currentFile);
                fw.write(gson.toJson(log));
                fw.close();

                unsavedChanges = false;
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(GUI.this, "Something went wrong. Error message: " + ioe.getMessage());
            }
        }
    }

    private class Exit implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            applicationIsAboutToClose();
        }
    }

    private class About implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JOptionPane.showMessageDialog(GUI.this, "Amateur Radio Minimum Log (v " + APPLICATION_VERSION + ")\nCopyright (C) 2017 Marcus Hammar\n\nThis program is free software: you can redistribute it and/or modify\nit under the terms of the GNU General Public License as published by\nthe Free Software Foundation, either version 3 of the License, or\n(at your option) any later version.\n\nThis program is distributed in the hope that it will be useful,\nbut WITHOUT ANY WARRANTY; without even the implied warranty of\nMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\nGNU General Public License for more details.\n\nYou should have received a copy of the GNU General Public License\nalong with this program. If not, see http://www.gnu.org/licenses/.\n\n----------------------------------------\n\nThis application uses GSON from Google. GSON is licensed under\nthe Apache License 2.0. Apache License 2.0 can be found at\nhttp://www.apache.org/licenses/LICENSE-2.0", "About", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class TableSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
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

    private class ApplicationTerminationListener extends WindowAdapter implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
        }

        @Override
        public void windowClosing(WindowEvent we) {
            applicationIsAboutToClose();
        }
    }
}