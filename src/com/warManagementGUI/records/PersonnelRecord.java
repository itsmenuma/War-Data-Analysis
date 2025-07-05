package com.warManagementGUI.records;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PersonnelRecord {
    private final StringProperty personnelId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty post;
    private final StringProperty unitId;
    private final StringProperty role;
    private final StringProperty contact;
    private final StringProperty status;

    public PersonnelRecord(String personnelId, String firstName, String lastName,
            String post, String unitId, String role, String contact, String status) {
        this.personnelId = new SimpleStringProperty(personnelId);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.post = new SimpleStringProperty(post);
        this.unitId = new SimpleStringProperty(unitId);
        this.role = new SimpleStringProperty(role);
        this.contact = new SimpleStringProperty(contact);
        this.status = new SimpleStringProperty(status);
    }

    // Personnel ID
    public StringProperty personnelIdProperty() {
        return personnelId;
    }

    public String getPersonnelId() {
        return personnelId.get();
    }

    public void setPersonnelId(String personnelId) {
        this.personnelId.set(personnelId);
    }

    // First Name
    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    // Last Name
    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    // Post
    public StringProperty postProperty() {
        return post;
    }

    public String getPost() {
        return post.get();
    }

    public void setPost(String post) {
        this.post.set(post);
    }

    // Unit ID
    public StringProperty unitIdProperty() {
        return unitId;
    }

    public String getUnitId() {
        return unitId.get();
    }

    public void setUnitId(String unitId) {
        this.unitId.set(unitId);
    }

    // Role
    public StringProperty roleProperty() {
        return role;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    // Contact
    public StringProperty contactProperty() {
        return contact;
    }

    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
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
}
