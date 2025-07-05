package com.warManagementGUI.records;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EquipmentRecord {
    private final IntegerProperty equipmentId;
    private final StringProperty name;
    private final StringProperty type;
    private final IntegerProperty unitId;
    private final StringProperty status;
    private final IntegerProperty locationId;

    public EquipmentRecord(Integer equipmentId, String name, String type,
            Integer unitId, String status, Integer locationId) {
        this.equipmentId = new SimpleIntegerProperty(equipmentId);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.unitId = new SimpleIntegerProperty(unitId);
        this.status = new SimpleStringProperty(status);
        this.locationId = new SimpleIntegerProperty(locationId);
    }

    // Equipment ID
    public IntegerProperty equipmentIdProperty() {
        return equipmentId;
    }

    public Integer getEquipmentId() {
        return equipmentId.get();
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId.set(equipmentId);
    }

    // Name
    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    // Type
    public StringProperty typeProperty() {
        return type;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    // Unit ID
    public IntegerProperty unitIdProperty() {
        return unitId;
    }

    public Integer getUnitId() {
        return unitId.get();
    }

    public void setUnitId(Integer unitId) {
        this.unitId.set(unitId);
    }

    // Status
    public StringProperty statusProperty() {
        return status;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    // Location ID
    public IntegerProperty locationIdProperty() {
        return locationId;
    }

    public Integer getLocationId() {
        return locationId.get();
    }

    public void setLocationId(Integer locationId) {
        this.locationId.set(locationId);
    }
}
