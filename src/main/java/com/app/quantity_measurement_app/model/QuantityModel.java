package com.app.quantity_measurement_app.model;

import com.app.quantity_measurement_app.unit.IMeasurable;
import com.app.quantity_measurement_app.unit.LengthUnit;
import com.app.quantity_measurement_app.unit.WeightUnit;

public class QuantityModel<U extends IMeasurable> {

    public double value;
    public U unit;

    public QuantityModel(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit.getUnitName());
    }

    public static void main(String[] args) {
        QuantityModel<LengthUnit> lengthModel = new QuantityModel<>(12.0, LengthUnit.FEET);
        System.out.println("QuantityModel created: " + lengthModel);

        QuantityModel<WeightUnit> weightModel = new QuantityModel<>(1.5, WeightUnit.KILOGRAM);
        System.out.println("QuantityModel created: " + weightModel);
    }
}
