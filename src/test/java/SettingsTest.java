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

        StringBuilder defaultSettingsFromDefinition = new StringBuilder();

        defaultSettingsFromDefinition.append("CALL|Call sign|70").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("QSO_DATE|Date|160").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("TIME_ON|Time start|160").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("TIME_OFF|Time end|160").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("FREQ|Frequency|80").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("BAND|Band|60").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("MODE|Mode|60").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("TX_PWR|Power|60").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("GRIDSQUARE|Gridsquare|70").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("RST_SENT|RST sent|70").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("RST_RCVD|RST received|90").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("OPERATOR|My call sign|80").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("MY_GRIDSQUARE|My gridsquare|80").append(System.lineSeparator());
        defaultSettingsFromDefinition.append("COMMENT|Comments|200").append(System.lineSeparator());

        settings.removeNode();

        assertEquals(defaultSettingsFromDefinition.toString(), defaultSettingsFromClass.toString());
    }
}