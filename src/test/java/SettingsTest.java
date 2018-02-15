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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SettingsTest {
    @Test
    void defaultSettings() {
        Settings settings = new Settings("TestDefaultSettings");
        settings.removeNode();
        settings = new Settings("TestDefaultSettings");
        settings.loadNode();

        StringBuilder defaultSettingsFromClass = new StringBuilder();
        for (Settings.SettingsField settingsField : settings.getFields()) {
            defaultSettingsFromClass.append(settingsField.getName()).append('|').append(settingsField.getDescription()).append('|').append(settingsField.getSize()).append(System.lineSeparator());
        }

        String defaultSettingsFromDefinition = "CALL|Call sign|70" + System.lineSeparator() + "QSO_DATE|Date|160" + System.lineSeparator() + "TIME_ON|Time start|160" + System.lineSeparator() + "TIME_OFF|Time end|160" + System.lineSeparator() + "FREQ|Frequency|80" + System.lineSeparator() + "BAND|Band|60" + System.lineSeparator() + "MODE|Mode|60" + System.lineSeparator() + "TX_PWR|Power|60" + System.lineSeparator() + "GRIDSQUARE|Gridsquare|70" + System.lineSeparator() + "RST_SENT|RST sent|70" + System.lineSeparator() + "RST_RCVD|RST received|90" + System.lineSeparator() + "OPERATOR|My call sign|80" + System.lineSeparator() + "MY_GRIDSQUARE|My gridsquare|80" + System.lineSeparator() + "COMMENT|Comments|200" + System.lineSeparator();

        settings.removeNode();

        assertEquals(defaultSettingsFromDefinition, defaultSettingsFromClass.toString());
    }

    @Test
    void settingsAddFieldAndSave() {
        Settings settings = new Settings("TestSettingsAddFieldAndSave");
        settings.removeNode();
        settings = new Settings("TestSettingsAddFieldAndSave");
        settings.loadNode();

        settings.addField("MY_NAME", "My name", 100);
        settings.addField("MY_COUNTRY", "My country", 80);

        settings.saveNode();

        settings = new Settings("TestSettingsAddFieldAndSave");

        settings.loadNode();

        StringBuilder settingsFromClass = new StringBuilder();
        for (Settings.SettingsField settingsField : settings.getFields()) {
            settingsFromClass.append(settingsField.getName()).append('|').append(settingsField.getDescription()).append('|').append(settingsField.getSize()).append(System.lineSeparator());
        }

        String settingsFromDefinition = "CALL|Call sign|70" + System.lineSeparator() + "QSO_DATE|Date|160" + System.lineSeparator() + "TIME_ON|Time start|160" + System.lineSeparator() + "TIME_OFF|Time end|160" + System.lineSeparator() + "FREQ|Frequency|80" + System.lineSeparator() + "BAND|Band|60" + System.lineSeparator() + "MODE|Mode|60" + System.lineSeparator() + "TX_PWR|Power|60" + System.lineSeparator() + "GRIDSQUARE|Gridsquare|70" + System.lineSeparator() + "RST_SENT|RST sent|70" + System.lineSeparator() + "RST_RCVD|RST received|90" + System.lineSeparator() + "OPERATOR|My call sign|80" + System.lineSeparator() + "MY_GRIDSQUARE|My gridsquare|80" + System.lineSeparator() + "COMMENT|Comments|200" + System.lineSeparator() + "MY_NAME|My name|100" + System.lineSeparator() + "MY_COUNTRY|My country|80" + System.lineSeparator();

        settings.removeNode();

        assertEquals(settingsFromDefinition, settingsFromClass.toString());
    }

    @Test
    void settingsDeleteFieldAndSave() {
        Settings settings = new Settings("TestSettingsDeleteFieldAndSave");
        settings.removeNode();
        settings = new Settings("TestSettingsDeleteFieldAndSave");
        settings.loadNode();

        settings.deleteField(2);
        settings.deleteField(7);

        settings.saveNode();

        settings = new Settings("TestSettingsDeleteFieldAndSave");

        settings.loadNode();

        StringBuilder settingsFromClass = new StringBuilder();
        for (Settings.SettingsField settingsField : settings.getFields()) {
            settingsFromClass.append(settingsField.getName()).append('|').append(settingsField.getDescription()).append('|').append(settingsField.getSize()).append(System.lineSeparator());
        }

        String settingsFromDefinition = "CALL|Call sign|70" + System.lineSeparator() + "QSO_DATE|Date|160" + System.lineSeparator() + "TIME_OFF|Time end|160" + System.lineSeparator() + "FREQ|Frequency|80" + System.lineSeparator() + "BAND|Band|60" + System.lineSeparator() + "MODE|Mode|60" + System.lineSeparator() + "TX_PWR|Power|60" + System.lineSeparator() + "RST_SENT|RST sent|70" + System.lineSeparator() + "RST_RCVD|RST received|90" + System.lineSeparator() + "OPERATOR|My call sign|80" + System.lineSeparator() + "MY_GRIDSQUARE|My gridsquare|80" + System.lineSeparator() + "COMMENT|Comments|200" + System.lineSeparator();

        settings.removeNode();

        assertEquals(settingsFromDefinition, settingsFromClass.toString());
    }

    @Test
    void settingsModifyFieldAndSave() {
        Settings settings = new Settings("TestSettingsModifyFieldAndSave");
        settings.removeNode();
        settings = new Settings("TestSettingsModifyFieldAndSave");
        settings.loadNode();

        settings.modifyField(8, "QTH", "City", 80);
        settings.modifyField(13, "COMMENT_INTL", "Comments", 220);

        settings.saveNode();

        settings = new Settings("TestSettingsModifyFieldAndSave");

        settings.loadNode();

        StringBuilder settingsFromClass = new StringBuilder();
        for (Settings.SettingsField settingsField : settings.getFields()) {
            settingsFromClass.append(settingsField.getName()).append('|').append(settingsField.getDescription()).append('|').append(settingsField.getSize()).append(System.lineSeparator());
        }

        String settingsFromDefinition = "CALL|Call sign|70" + System.lineSeparator() + "QSO_DATE|Date|160" + System.lineSeparator() + "TIME_ON|Time start|160" + System.lineSeparator() + "TIME_OFF|Time end|160" + System.lineSeparator() + "FREQ|Frequency|80" + System.lineSeparator() + "BAND|Band|60" + System.lineSeparator() + "MODE|Mode|60" + System.lineSeparator() + "TX_PWR|Power|60" + System.lineSeparator() + "QTH|City|80" + System.lineSeparator() + "RST_SENT|RST sent|70" + System.lineSeparator() + "RST_RCVD|RST received|90" + System.lineSeparator() + "OPERATOR|My call sign|80" + System.lineSeparator() + "MY_GRIDSQUARE|My gridsquare|80" + System.lineSeparator() + "COMMENT_INTL|Comments|220" + System.lineSeparator();

        settings.removeNode();

        assertEquals(settingsFromDefinition, settingsFromClass.toString());
    }
}