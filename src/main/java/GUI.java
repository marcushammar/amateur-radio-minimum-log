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

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GUI extends JFrame {
    private final String APPLICATION_VERSION = "1.1.0";

    private Logbook logbook = new Logbook();
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

        table = new JTable(logbook.getDataForTable(), columns);
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
        loadAndSaveFileChooser.setFileFilter(new FileNameExtensionFilter("ADIF format", "adi"));

        addWindowListener(new ApplicationTerminationListener());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateTable() {
        DefaultTableModel tableModel = new DefaultTableModel(logbook.getDataForTable(), columns) {
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

        countLabel.setText("Count: " + logbook.count());
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
            InputDialog inputDialog = new InputDialog();
            inputDialog.validate();
            int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, inputDialog, "Add", JOptionPane.OK_CANCEL_OPTION);

            if (responseFromDialog == JOptionPane.YES_OPTION) {
                QSO qso = new QSO();
                qso.setField("CALL", inputDialog.getCallSign());
                qso.setField("QSO_DATE", inputDialog.getTimeStart());
                qso.setField("QSO_DATE_OFF", inputDialog.getTimeEnd());
                qso.setField("FREQ", inputDialog.getFrequency());
                qso.setField("BAND", inputDialog.getBand());
                qso.setField("MODE", inputDialog.getMode());
                qso.setField("TX_PWR", inputDialog.getPower());
                qso.setField("GRIDSQUARE", inputDialog.getLocationText());
                qso.setField("RST_RCVD", inputDialog.getRstReceived());
                qso.setField("RST_SENT", inputDialog.getRstSent());
                qso.setField("OPERATOR", inputDialog.getMyCallSign());
                qso.setField("MY_GRIDSQUARE", inputDialog.getMyLocation());
                qso.setField("COMMENT", inputDialog.getComments());
                logbook.add(qso);
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

            QSO qsoExisting = logbook.get(selectedRows[0]);

            InputDialog inputDialog = new InputDialog();
            inputDialog.setCallSign(qsoExisting.getField("CALL"));
            inputDialog.setTimeStart(qsoExisting.getField("QSO_DATE"));
            inputDialog.setTimeEnd(qsoExisting.getField("QSO_DATE_OFF"));
            inputDialog.setFrequency(qsoExisting.getField("FREQ"));
            inputDialog.setBand(qsoExisting.getField("BAND"));
            inputDialog.setMode(qsoExisting.getField("MODE"));
            inputDialog.setPower(qsoExisting.getField("TX_PWR"));
            inputDialog.setLocationText(qsoExisting.getField("GRIDSQUARE"));
            inputDialog.setRstReceived(qsoExisting.getField("RST_RCVD"));
            inputDialog.setRstSent(qsoExisting.getField("RST_SENT"));
            inputDialog.setMyCallSign(qsoExisting.getField("OPERATOR"));
            inputDialog.setMyLocation(qsoExisting.getField("MY_GRIDSQUARE"));
            inputDialog.setComments(qsoExisting.getField("COMMENT"));

            inputDialog.validate();

            int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, inputDialog, "Copy", JOptionPane.OK_CANCEL_OPTION);

            if (responseFromDialog == JOptionPane.YES_OPTION) {
                QSO qsoNew = new QSO();
                qsoNew.setField("CALL", inputDialog.getCallSign());
                qsoNew.setField("QSO_DATE", inputDialog.getTimeStart());
                qsoNew.setField("QSO_DATE_OFF", inputDialog.getTimeEnd());
                qsoNew.setField("FREQ", inputDialog.getFrequency());
                qsoNew.setField("BAND", inputDialog.getBand());
                qsoNew.setField("MODE", inputDialog.getMode());
                qsoNew.setField("TX_PWR", inputDialog.getPower());
                qsoNew.setField("GRIDSQUARE", inputDialog.getLocationText());
                qsoNew.setField("RST_RCVD", inputDialog.getRstReceived());
                qsoNew.setField("RST_SENT", inputDialog.getRstSent());
                qsoNew.setField("OPERATOR", inputDialog.getMyCallSign());
                qsoNew.setField("MY_GRIDSQUARE", inputDialog.getMyLocation());
                qsoNew.setField("COMMENT", inputDialog.getComments());
                logbook.add(qsoNew);
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

            QSO qso = logbook.get(selectedRows[0]);

            InputDialog inputDialog = new InputDialog();
            inputDialog.setCallSign(qso.getField("CALL"));
            inputDialog.setTimeStart(qso.getField("QSO_DATE"));
            inputDialog.setTimeEnd(qso.getField("QSO_DATE_OFF"));
            inputDialog.setFrequency(qso.getField("FREQ"));
            inputDialog.setBand(qso.getField("BAND"));
            inputDialog.setMode(qso.getField("MODE"));
            inputDialog.setPower(qso.getField("TX_PWR"));
            inputDialog.setLocationText(qso.getField("GRIDSQUARE"));
            inputDialog.setRstReceived(qso.getField("RST_RCVD"));
            inputDialog.setRstSent(qso.getField("RST_SENT"));
            inputDialog.setMyCallSign(qso.getField("OPERATOR"));
            inputDialog.setMyLocation(qso.getField("MY_GRIDSQUARE"));
            inputDialog.setComments(qso.getField("COMMENT"));

            inputDialog.validate();

            int responseFromDialog = JOptionPane.showConfirmDialog(GUI.this, inputDialog, "Modify", JOptionPane.OK_CANCEL_OPTION);

            if (responseFromDialog == JOptionPane.YES_OPTION) {
                qso.setField("CALL", inputDialog.getCallSign());
                qso.setField("QSO_DATE", inputDialog.getTimeStart());
                qso.setField("QSO_DATE_OFF", inputDialog.getTimeEnd());
                qso.setField("FREQ", inputDialog.getFrequency());
                qso.setField("BAND", inputDialog.getBand());
                qso.setField("MODE", inputDialog.getMode());
                qso.setField("TX_PWR", inputDialog.getPower());
                qso.setField("GRIDSQUARE", inputDialog.getLocationText());
                qso.setField("RST_RCVD", inputDialog.getRstReceived());
                qso.setField("RST_SENT", inputDialog.getRstSent());
                qso.setField("OPERATOR", inputDialog.getMyCallSign());
                qso.setField("MY_GRIDSQUARE", inputDialog.getMyLocation());
                qso.setField("COMMENT", inputDialog.getComments());
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
                logbook.remove(selectedRows[0]);
                updateTable();
                unsavedChanges = true;
            }
        }
    }

    private class ExportButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for (int i : table.getSelectedRows()) {
                if (!logbook.get(i).validateAdif()) {
                    JOptionPane.showMessageDialog(GUI.this, "Can't export due to issues in row " + (i + 1) + " (callsign " + logbook.get(i).getField("CALL") + "). Please correct the row and try again.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    pw.println(logbook.get(i).getAdifRow());
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
            logbook.clear();
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
                logbook.clear();
                // TODO: The load function needs to be created
                br.close();

                updateTable();
                unsavedChanges = false;

            } catch (IOException e) {
                JOptionPane.showMessageDialog(GUI.this, "Something went wrong. Error message: " + e.getMessage());
            }
        }
    }

    private void save(boolean saveAs) {
        try {
            if (currentFile == null || saveAs) {
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
                if (!fileName.endsWith(".adi")) {
                    fileName += ".adi";
                }

                currentFile = new File(fileName);
            }

            logbook.save(currentFile);

            unsavedChanges = false;
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(GUI.this, "Something went wrong. Error message: " + ioe.getMessage());
        }
    }

    private class Save implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            save(false);
        }
    }

    private class SaveAs implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            save(true);
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
            JOptionPane.showMessageDialog(GUI.this, "Amateur Radio Minimum Log (v " + APPLICATION_VERSION + ")\nCopyright (C) 2017-2018 Marcus Hammar\n\nThis program is free software: you can redistribute it and/or modify\nit under the terms of the GNU General Public License as published by\nthe Free Software Foundation, either version 3 of the License, or\n(at your option) any later version.\n\nThis program is distributed in the hope that it will be useful,\nbut WITHOUT ANY WARRANTY; without even the implied warranty of\nMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\nGNU General Public License for more details.\n\nYou should have received a copy of the GNU General Public License\nalong with this program. If not, see http://www.gnu.org/licenses/.", "About", JOptionPane.INFORMATION_MESSAGE);
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