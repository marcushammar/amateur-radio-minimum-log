import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HamRadioMinimumLog extends JFrame {
    private ArrayList<QSO> log = new ArrayList<QSO>();
    private JTable table;
    private String[] columns = { "Call sign", "Time start", "Time end", "Frequency", "Band", "Mode", "Power", "Location", "RST sent", "RST received", "My call sign", "My location", "Comments"};
    private JButton addButton;
    private JButton copyButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JButton exportButton;
    private JLabel countLabel;

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

        JPanel southPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add row");
        copyButton = new JButton("Copy row");
        modifyButton = new JButton("Modify row");
        deleteButton = new JButton("Delete row");
        exportButton = new JButton("Export selected rows");

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

                pw.println("<ADIF_VER:5>3.0.6");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                pw.println("<CREATED_TIMESTAMP:15>" + sdf.format(new Date()));
                pw.println("<PROGRAMID:18>HamRadioMinimumLog");
                pw.println("<PROGRAMVERSION:5>0.1.0");
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


    private class LoadLog implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            try{
                FileReader fr = new FileReader("qsodata.log");
                BufferedReader br = new BufferedReader(fr);
                log.clear();
                Gson gson = new Gson();

                String line;
                while ( (line = br.readLine()) != null) {
                    log.add(gson.fromJson(line, QSO.class));
                }

                fr.close();
                updateTable();

            }catch(IOException e){
                JOptionPane.showMessageDialog(HamRadioMinimumLog.this, "Something went wrong. Error message: " + e.getMessage());
            }
        }
    }


    private class SaveLog implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent){
            try{
                FileWriter fw = new FileWriter("qsodata.log");
                PrintWriter pw = new PrintWriter(fw);
                Gson gson = new Gson();
                for (QSO qso : log){
                    pw.println(gson.toJson(qso));
                }
                fw.close();
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

    public static void main(String[] args) {
        new HamRadioMinimumLog();
    }
}