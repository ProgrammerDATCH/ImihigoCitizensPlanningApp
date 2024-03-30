package com.imihigocizitensplanning.app.Models;

public class User {
    String nationalId, names, sector, cell, gender, phone, occupation, userCategory;

    public User() {
    }

    public User(String nationalId, String names, String sector, String cell, String gender, String phone, String occupation, String userCategory) {
        this.nationalId = nationalId;
        this.names = names;
        this.sector = sector;
        this.cell = cell;
        this.gender = gender;
        this.phone = phone;
        this.occupation = occupation;
        this.userCategory = userCategory;
    }

    public String getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
