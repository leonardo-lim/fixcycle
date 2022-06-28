package com.leo.fixcycle.models;

public class Invoice {
    private int serviceTypeCost;
    private int cylinderCapacityCost;
    private int adminFee;
    private int additionalFee;
    private int totalCost;
    private boolean isPaid;
    private InvoiceData data;

    public int getServiceTypeCost() {
        return serviceTypeCost;
    }

    public void setServiceTypeCost(int serviceTypeCost) {
        this.serviceTypeCost = serviceTypeCost;
    }

    public int getCylinderCapacityCost() {
        return cylinderCapacityCost;
    }

    public void setCylinderCapacityCost(int cylinderCapacityCost) {
        this.cylinderCapacityCost = cylinderCapacityCost;
    }

    public int getAdminFee() {
        return adminFee;
    }

    public void setAdminFee(int adminFee) {
        this.adminFee = adminFee;
    }

    public int getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(int additionalFee) {
        this.additionalFee = additionalFee;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public InvoiceData getData() {
        return data;
    }

    public void setData(InvoiceData data) {
        this.data = data;
    }
}