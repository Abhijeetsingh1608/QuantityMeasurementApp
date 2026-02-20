package com.quantityMeasurementApp;

public class Feet {
    private final double value;

    public Feet(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        Feet other = (Feet) obj;

        return Double.compare(this.value, other.value) == 0;
    }
}