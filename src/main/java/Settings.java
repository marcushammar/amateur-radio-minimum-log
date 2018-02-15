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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Settings {
    ArrayList<SettingsField> fields = new ArrayList<>();
    private Preferences preferences;

    public Settings(String node) {
        preferences = Preferences.userRoot().node(node);
    }

    public void loadNode() {
        String fieldNames = preferences.get("NAMES", "CALL,QSO_DATE,TIME_ON,TIME_OFF,FREQ,BAND,MODE,TX_PWR,GRIDSQUARE,RST_SENT,RST_RCVD,OPERATOR,MY_GRIDSQUARE,COMMENT");
        String fieldDescriptions = preferences.get("DESCRIPTIONS", "Call sign,Date,Time start,Time end,Frequency,Band,Mode,Power,Gridsquare,RST sent,RST received,My call sign,My gridsquare,Comments");
        String fieldSizes = preferences.get("SIZES", "70,160,160,160,80,60,60,60,70,70,90,80,80,200");

        String[] fieldNamesArray = fieldNames.split(",");
        String[] fieldDescriptionsArray = fieldDescriptions.split(",");
        int[] fieldSizesArray = Arrays.stream(fieldSizes.split(",")).mapToInt(Integer::parseInt).toArray();

        for (int i = 0; i < fieldNamesArray.length; i++) {
            addField(fieldNamesArray[i], fieldDescriptionsArray[i], fieldSizesArray[i]);
        }
    }

    public void removeNode() {
        try {
            preferences.removeNode();
        } catch(BackingStoreException bse) {
            System.out.println(bse.getMessage());
        }
    }

    public void saveNode() {
        StringBuilder names = new StringBuilder();
        StringBuilder descriptions = new StringBuilder();
        StringBuilder sizes = new StringBuilder();

        for (SettingsField settingsField : fields) {
            names.append(settingsField.getName()).append(',');
            descriptions.append(settingsField.getDescription()).append(',');
            sizes.append(settingsField.getSize()).append(',');
        }

        preferences.put("NAMES", names.toString().substring(0, names.length() - 1));
        preferences.put("DESCRIPTIONS", descriptions.toString().substring(0, descriptions.length() - 1));
        preferences.put("SIZES", sizes.toString().substring(0, sizes.length() - 1));
    }

    public void addField(String name, String description, int width) {
        fields.add(new SettingsField(name, description, width));
    }

    public ArrayList<SettingsField> getFields() {
        return fields;
    }

    public void deleteField(int index) {
        fields.remove(index);
    }

    public class SettingsField {
        private String name;
        private String description;
        private int size;

        public SettingsField(String name, String description, int size) {
            this.name = name;
            this.description = description;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
