package com.warManagementGUI.Units;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * JavaFX data model class for Unit records used in TableView binding.
 * This class provides StringProperty objects that automatically handle
 * data binding with JavaFX UI components.
 */
public class UnitRecord {
    private final StringProperty unitId;
    private final StringProperty unitName;
    private final StringProperty unitType;
    private final StringProperty commanderId;
    private final StringProperty locationId;

    /**
     * Constructor to create a UnitRecord with all fields.
     *
     * @param unitId Unit identification number
     * @param unitName Name of the unit
     * @param unitType Type/category of the unit (Infantry, Cavalry, etc.)
     * @param commanderId ID of the commanding officer
     * @param locationId ID of the unit's location
     */
    public UnitRecord(String unitId, String unitName, String unitType, String commanderId, String locationId) {
        this.unitId = new SimpleStringProperty(unitId != null ? unitId : "");
        this.unitName = new SimpleStringProperty(unitName != null ? unitName : "");
        this.unitType = new SimpleStringProperty(unitType != null ? unitType : "");
        this.commanderId = new SimpleStringProperty(commanderId != null ? commanderId : "");
        this.locationId = new SimpleStringProperty(locationId != null ? locationId : "");
    }

    // Unit ID property methods
    public String getUnitId() {
        return unitId.get();
    }

    public void setUnitId(String unitId) {
        this.unitId.set(unitId != null ? unitId : "");
    }

    public StringProperty unitIdProperty() {
        return unitId;
    }

    // Unit Name property methods
    public String getUnitName() {
        return unitName.get();
    }

    public void setUnitName(String unitName) {
        this.unitName.set(unitName != null ? unitName : "");
    }

    public StringProperty unitNameProperty() {
        return unitName;
    }

    // Unit Type property methods
    public String getUnitType() {
        return unitType.get();
    }

    public void setUnitType(String unitType) {
        this.unitType.set(unitType != null ? unitType : "");
    }

    public StringProperty unitTypeProperty() {
        return unitType;
    }

    // Commander ID property methods
    public String getCommanderId() {
        return commanderId.get();
    }

    public void setCommanderId(String commanderId) {
        this.commanderId.set(commanderId != null ? commanderId : "");
    }

    public StringProperty commanderIdProperty() {
        return commanderId;
    }

    // Location ID property methods
    public String getLocationId() {
        return locationId.get();
    }

    public void setLocationId(String locationId) {
        this.locationId.set(locationId != null ? locationId : "");
    }

    public StringProperty locationIdProperty() {
        return locationId;
    }

    @Override
    public String toString() {
        return "UnitRecord{" +
                "unitId='" + getUnitId() + '\'' +
                ", unitName='" + getUnitName() + '\'' +
                ", unitType='" + getUnitType() + '\'' +
                ", commanderId='" + getCommanderId() + '\'' +
                ", locationId='" + getLocationId() + '\'' +
                '}';
    }
}
