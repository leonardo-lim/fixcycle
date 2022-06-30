package com.leo.fixcycle.models;

import java.io.Serializable;

public class ServiceDataService implements Serializable {
    private int id;
    private int motorcycleId;
    private int type;
    private String request;
    private String serviceTime;
    private int serviceStatus;
    private boolean pickUpAndDrop;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMotorcycleId() {
        return motorcycleId;
    }

    public void setMotorcycleId(int motorcycleId) {
        this.motorcycleId = motorcycleId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public boolean isPickUpAndDrop() {
        return pickUpAndDrop;
    }

    public void setPickUpAndDrop(boolean pickUpAndDrop) {
        this.pickUpAndDrop = pickUpAndDrop;
    }
}