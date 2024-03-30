package com.imihigocizitensplanning.app.Models;

public class PendingImihigo {
    String pendingPlanName;
    String pendingPlanCategory;
    String pendingPlanDesc;
    String pendingUserId;
    String pendingPlanUserNames;
    String pendingPlanUserPhone;
    String pendingPlanUserSector;
    String pendingPlanUserCell;
    int pendingPlanProgress;
    String pendingPlanDeclineReason;

    public PendingImihigo() {
    }

    public PendingImihigo(String pendingPlanName, String pendingPlanCategory, String pendingPlanDesc, String pendingUserId, String pendingPlanUserNames, String pendingPlanUserPhone, String pendingPlanUserSector, String pendingPlanUserCell, int pendingPlanProgress) {
        this.pendingPlanName = pendingPlanName;
        this.pendingPlanCategory = pendingPlanCategory;
        this.pendingPlanDesc = pendingPlanDesc;
        this.pendingUserId = pendingUserId;
        this.pendingPlanUserNames = pendingPlanUserNames;
        this.pendingPlanUserPhone = pendingPlanUserPhone;
        this.pendingPlanUserSector = pendingPlanUserSector;
        this.pendingPlanUserCell = pendingPlanUserCell;
        this.pendingPlanProgress = pendingPlanProgress;
        this.pendingPlanDeclineReason = null;
    }


    public String getPendingPlanDeclineReason() {
        return pendingPlanDeclineReason;
    }

    public void setPendingPlanDeclineReason(String pendingPlanDeclineReason) {
        this.pendingPlanDeclineReason = pendingPlanDeclineReason;
    }

    public String getPendingPlanUserNames() {
        return pendingPlanUserNames;
    }

    public void setPendingPlanUserNames(String pendingPlanUserNames) {
        this.pendingPlanUserNames = pendingPlanUserNames;
    }

    public String getPendingPlanUserPhone() {
        return pendingPlanUserPhone;
    }

    public void setPendingPlanUserPhone(String pendingPlanUserPhone) {
        this.pendingPlanUserPhone = pendingPlanUserPhone;
    }

    public String getPendingPlanUserSector() {
        return pendingPlanUserSector;
    }

    public void setPendingPlanUserSector(String pendingPlanUserSector) {
        this.pendingPlanUserSector = pendingPlanUserSector;
    }

    public String getPendingPlanUserCell() {
        return pendingPlanUserCell;
    }

    public void setPendingPlanUserCell(String pendingPlanUserCell) {
        this.pendingPlanUserCell = pendingPlanUserCell;
    }

    public String getPendingPlanName() {
        return pendingPlanName;
    }

    public void setPendingPlanName(String pendingPlanName) {
        this.pendingPlanName = pendingPlanName;
    }

    public String getPendingPlanCategory() {
        return pendingPlanCategory;
    }

    public void setPendingPlanCategory(String pendingPlanCategory) {
        this.pendingPlanCategory = pendingPlanCategory;
    }

    public String getPendingPlanDesc() {
        return pendingPlanDesc;
    }

    public void setPendingPlanDesc(String pendingPlanDesc) {
        this.pendingPlanDesc = pendingPlanDesc;
    }

    public String getPendingUserId() {
        return pendingUserId;
    }

    public void setPendingUserId(String pendingUserId) {
        this.pendingUserId = pendingUserId;
    }

    public int getPendingPlanProgress() {
        return pendingPlanProgress;
    }

    public void setPendingPlanProgress(int pendingPlanProgress) {
        this.pendingPlanProgress = pendingPlanProgress;
    }
}