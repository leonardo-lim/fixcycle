package com.leo.fixcycle.models;

import java.util.List;

public class ServiceData {
    private List<ServiceDataService> services;
    private ServiceDataService service;

    public List<ServiceDataService> getServices() {
        return services;
    }

    public void setServices(List<ServiceDataService> services) {
        this.services = services;
    }

    public ServiceDataService getService() {
        return service;
    }

    public void setService(ServiceDataService service) {
        this.service = service;
    }
}