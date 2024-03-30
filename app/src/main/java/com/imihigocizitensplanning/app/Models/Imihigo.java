package com.imihigocizitensplanning.app.Models;

public class Imihigo {
    String planName;
    String planStartDate;
    String planEndDate;
    String planDistrictName;
    int planBudget;
    int planTarget;
    String planTargetKey;
    String planImage;
    int planLevel;
    int planLevelPercent;
    String planCategory;

    public Imihigo() {
    }


    public Imihigo(String planName, String planStartDate, String planEndDate, String planDistrictName, int planBudget, int planTarget, String planTargetKey, String planImage, int planLevel, String planCategory) {
        this.planName = planName;
        this.planStartDate = planStartDate;
        this.planEndDate = planEndDate;
        this.planDistrictName = planDistrictName;
        this.planBudget = planBudget;
        this.planTarget = planTarget;
        this.planTargetKey = planTargetKey;
        this.planImage = planImage;
        this.planLevel = planLevel;
        this.planLevelPercent = (int) ((float) planLevel / planTarget * 100);
        this.planCategory = planCategory;
    }

    public String getPlanCategory() {
        return planCategory;
    }

    public void setPlanCategory(String planCategory) {
        this.planCategory = planCategory;
    }

    public String getPlanTargetKey() {
        return planTargetKey;
    }

    public void setPlanTargetKey(String planTargetKey) {
        this.planTargetKey = planTargetKey;
    }


    public int getPlanTarget() {
        return planTarget;
    }

    public void setPlanTarget(int planTarget) {
        this.planTarget = planTarget;
    }

    public int getPlanLevelPercent() {
        if(planLevel == 0)
        {
            return 0;
        }
        else
        {
            int perc = (int) ((float) planLevel / planTarget * 100);
            return perc;
        }
    }

    public void setPlanLevelPercent(int planLevelPercent) {
        this.planLevelPercent = planLevelPercent;
    }

    public int getPlanLevel() {
        return planLevel;
    }

    public void setPlanLevel(int planLevel) {
        this.planLevel = planLevel;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        this.planStartDate = planStartDate;
    }

    public String getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        this.planEndDate = planEndDate;
    }

    public String getPlanDistrictName() {
        return planDistrictName;
    }

    public void setPlanDistrictName(String planDistrictName) {
        this.planDistrictName = planDistrictName;
    }

    public int getPlanBudget() {
        return planBudget;
    }

    public void setPlanBudget(int planBudget) {
        this.planBudget = planBudget;
    }

    public String getPlanImage() {
        return planImage;
    }

    public void setPlanImage(String planImage) {
        this.planImage = planImage;
    }
}
