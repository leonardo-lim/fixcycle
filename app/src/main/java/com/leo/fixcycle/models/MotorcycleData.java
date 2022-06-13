package com.leo.fixcycle.models;

import java.util.List;

public class MotorcycleData {
    private List<MotorcycleDataMotorcycle> motorcycles;
    private MotorcycleDataMotorcycle motorcycle;

    public List<MotorcycleDataMotorcycle> getMotorcycles() {
        return motorcycles;
    }

    public void setMotorcycles(List<MotorcycleDataMotorcycle> motorcycles) {
        this.motorcycles = motorcycles;
    }

    public MotorcycleDataMotorcycle getMotorcycle() {
        return motorcycle;
    }

    public void setMotorcycle(MotorcycleDataMotorcycle motorcycle) {
        this.motorcycle = motorcycle;
    }
}