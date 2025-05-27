package com.warManagementGUI.Supply;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SupplyRecord {
    private final StringProperty supplyId;
    private final StringProperty name;
    private final StringProperty type;
    private final StringProperty quantity;
    private final StringProperty unitId;
    private final StringProperty locationId;
    private final StringProperty status;
    
    public SupplyRecord(String supplyId, String name, String type, String quantity, 
                       String unitId, String locationId, String status) {
        this.supplyId = new SimpleStringProperty(supplyId);
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.quantity = new SimpleStringProperty(quantity);
        this.unitId = new SimpleStringProperty(unitId);
        this.locationId = new SimpleStringProperty(locationId);
        this.status = new SimpleStringProperty(status);
    }    
    // Supply ID
    public StringProperty supplyIdProperty() { return supplyId; }
    public String getSupplyId() { return supplyId.get(); }
    public void setSupplyId(String supplyId) { this.supplyId.set(supplyId); }
    
    // Name
    public StringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    
    // Type
    public StringProperty typeProperty() { return type; }
    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }
    
    // Quantity
    public StringProperty quantityProperty() { return quantity; }
    public String getQuantity() { return quantity.get(); }
    public void setQuantity(String quantity) { this.quantity.set(quantity); }
    
    // Unit ID
    public StringProperty unitIdProperty() { return unitId; }
    public String getUnitId() { return unitId.get(); }
    public void setUnitId(String unitId) { this.unitId.set(unitId); }
    
    // Location ID
    public StringProperty locationIdProperty() { return locationId; }
    public String getLocationId() { return locationId.get(); }
    public void setLocationId(String locationId) { this.locationId.set(locationId); }
    
    // Status
    public StringProperty statusProperty() { return status; }
    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
}
