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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class SettingsDialog extends JPanel {
    private Settings settings = new Settings("Settings");
    private JTable table;
    private String[] columns = {"ADIF Name", "Description", "Width"};
    JButton addButton = new JButton("Add");
    JButton modifyButton = new JButton("Modify");
    JButton deleteButton = new JButton("Delete");
    JButton moveUpButton = new JButton("Move up");
    JButton moveDownButton = new JButton("Move down");

    public SettingsDialog() {
        settings.loadNode();

        setLayout(new BorderLayout());

        addButton.addActionListener(new AddButtonActionListener());
        modifyButton.addActionListener(new ModifyButtonActionListener());
        deleteButton.addActionListener(new DeleteButtonActionListener());
        moveUpButton.addActionListener(new MoveUpButtonActionListener());
        moveDownButton.addActionListener(new MoveDownButtonActionListener());

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.add(addButton);
        eastPanel.add(modifyButton);
        eastPanel.add(deleteButton);
        eastPanel.add(moveUpButton);
        eastPanel.add(moveDownButton);
        add(eastPanel, BorderLayout.EAST);

        table = new JTable(settings.getDataForTable(), columns);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new TableSelectionListener());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(480, 200));
        add(scrollPane, BorderLayout.CENTER);

        addButton.setEnabled(true);
        modifyButton.setEnabled(false);
        deleteButton.setEnabled(false);
        moveUpButton.setEnabled(false);
        moveDownButton.setEnabled(false);

        updateTable();
    }

    private class AddButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3,2));

            JTextField name = new JTextField(15);
            JTextField description = new JTextField(15);
            JTextField width = new JTextField(15);

            panel.add(new JLabel("ADIF Name"));
            panel.add(name);
            panel.add(new JLabel("Description"));
            panel.add(description);
            panel.add(new JLabel("Width"));
            panel.add(width);

            int responseFromDialog = JOptionPane.showConfirmDialog(SettingsDialog.this, panel, "Add", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (responseFromDialog == JOptionPane.YES_OPTION) {
                settings.addField(name.getText(), description.getText(), Integer.parseInt(width.getText()));
                updateTable();
            }
        }
    }

    private class ModifyButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length != 1) {
                return;
            }

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3,2));

            JTextField name = new JTextField(15);
            JTextField description = new JTextField(15);
            JTextField width = new JTextField(15);

            name.setText(settings.getFields().get(selectedRows[0]).getName());
            description.setText(settings.getFields().get(selectedRows[0]).getDescription());
            width.setText(Integer.toString(settings.getFields().get(selectedRows[0]).getSize()));

            panel.add(new JLabel("ADIF Name"));
            panel.add(name);
            panel.add(new JLabel("Description"));
            panel.add(description);
            panel.add(new JLabel("Width"));
            panel.add(width);

            int responseFromDialog = JOptionPane.showConfirmDialog(SettingsDialog.this, panel, "Modify", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (responseFromDialog == JOptionPane.YES_OPTION) {
                settings.modifyField(selectedRows[0], name.getText(), description.getText(), Integer.parseInt(width.getText()));
                updateTable();
            }
        }
    }

    private class DeleteButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            int responseFromDialog = JOptionPane.showConfirmDialog(SettingsDialog.this, "Are you sure you want to delete the field?", "Delete field", JOptionPane.YES_NO_OPTION);
            if (responseFromDialog != JOptionPane.YES_OPTION) {
                return;
            }

            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 1) {
                settings.deleteField(selectedRows[0]);
                updateTable();
            }
        }
    }


    private class MoveUpButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 1) {
                settings.moveUp(selectedRows[0]);
                updateTable();
            }
        }
    }

    private class MoveDownButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 1) {
                settings.moveDown(selectedRows[0]);
                updateTable();
            }
        }
    }

    private class TableSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            switch (table.getSelectedRows().length) {
                case 1:
                    addButton.setEnabled(true);
                    modifyButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                    moveUpButton.setEnabled(true);
                    moveDownButton.setEnabled(true);
                    break;
                default:
                    addButton.setEnabled(true);
                    modifyButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                    moveUpButton.setEnabled(false);
                    moveDownButton.setEnabled(false);
                    break;
            }
        }
    }

    public void save() {
        settings.saveNode();
    }

    private void updateTable() {
        DefaultTableModel tableModel = new DefaultTableModel(settings.getDataForTable(), columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);

        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
    }
}
