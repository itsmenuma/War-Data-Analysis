package com.warManagementGUI.Mission;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MissionRecord {
    private final StringProperty missionId;
    private final StringProperty name;
    private final StringProperty objective;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final StringProperty status;
    private final StringProperty locationId;
    
    public MissionRecord(String missionId, String name, String objective, 
                        String startDate, String endDate, String status, String locationId) {
        this.missionId = new SimpleStringProperty(missionId);
        this.name = new SimpleStringProperty(name);
        this.objective = new SimpleStringProperty(objective);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.status = new SimpleStringProperty(status);
        this.locationId = new SimpleStringProperty(locationId);
    }
    
    // Mission ID
    public StringProperty missionIdProperty() { return missionId; }
    public String getMissionId() { return missionId.get(); }
    public void setMissionId(String missionId) { this.missionId.set(missionId); }
    
    // Name
    public StringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    
    // Objective
    public StringProperty objectiveProperty() { return objective; }
    public String getObjective() { return objective.get(); }
    public void setObjective(String objective) { this.objective.set(objective); }
    
    // Start Date
    public StringProperty startDateProperty() { return startDate; }
    public String getStartDate() { return startDate.get(); }
    public void setStartDate(String startDate) { this.startDate.set(startDate); }
    
    // End Date
    public StringProperty endDateProperty() { return endDate; }
    public String getEndDate() { return endDate.get(); }
    public void setEndDate(String endDate) { this.endDate.set(endDate); }
    
    // Status
    public StringProperty statusProperty() { return status; }
    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    
    // Location ID
    public StringProperty locationIdProperty() { return locationId; }
    public String getLocationId() { return locationId.get(); }
    public void setLocationId(String locationId) { this.locationId.set(locationId); }
}
