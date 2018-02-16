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
import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class InputDialog extends JPanel {
    private TreeMap<String, JTextField> data = new TreeMap<>();

    public InputDialog() {
        Settings settings = new Settings("Settings");
        settings.loadNode();

        setLayout(new GridLayout(settings.getFields().size(), 2));

        for (int i = 0; i < settings.getFields().size(); i++) {
            add(new JLabel(settings.getFields().get(i).getDescription()));

            JTextField textField = new JTextField(15);
            data.put(settings.getFields().get(i).getName(), textField);
            add(textField);
        }
    }

    public InputDialog(QSO qso) {
        Settings settings = new Settings("Settings");
        settings.loadNode();

        Set<String> qsoSet = new TreeSet<>(qso.getKeys());

        for (Settings.SettingsField settingsField : settings.getFields()) {
            qsoSet.remove(settingsField.getName());
        }

        setLayout(new GridLayout(settings.getFields().size() + qsoSet.size(), 2));

        for (int i = 0; i < settings.getFields().size(); i++) {
            add(new JLabel(settings.getFields().get(i).getDescription()));
            JTextField textField = new JTextField(15);
            textField.setText(qso.getField(settings.getFields().get(i).getName()));
            data.put(settings.getFields().get(i).getName(), textField);
            add(textField);
        }

        for (String key : qsoSet) {
            add(new JLabel(key));
            JTextField textField = new JTextField(15);
            textField.setText(qso.getField(key));
            data.put(key, textField);
            add(textField);
        }
    }

    public TreeMap<String, String> getData() {
        TreeMap<String, String> output = new TreeMap<>();
        for (Map.Entry<String, JTextField> field : data.entrySet()) {
            output.put(field.getKey(), field.getValue().getText());
        }
        return output;
    }
}