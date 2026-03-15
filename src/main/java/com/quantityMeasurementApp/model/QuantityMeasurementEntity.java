package com.quantityMeasurementApp.model;

import com.quantityMeasurementApp.IMeasurable;
import com.quantityMeasurementApp.LengthUnit;

import java.io.Serializable;

public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public double thisValue;
    public String thisUnit;
    public String thisMeasurementType;
    public double thatValue;
    public String thatUnit;
    public String thatMeasurementType;
    public String operation;
    public double resultValue;
    public String resultUnit;
    public String resultMeasurementType;
    public String resultString;
    public boolean isError;
    public String errorMessage;

    public QuantityMeasurementEntity(
            QuantityModel<? extends IMeasurable> thisQuantity,
            QuantityModel<? extends IMeasurable> thatQuantity,
            String operation,
            String result
    ) {
        this(thisQuantity, thatQuantity, operation);
        this.resultString = result;
    }

    public QuantityMeasurementEntity(
            QuantityModel<? extends IMeasurable> thisQuantity,
            QuantityModel<? extends IMeasurable> thatQuantity,
            String operation,
            QuantityModel<? extends IMeasurable> result
    ) {
        this(thisQuantity, thatQuantity, operation);
        this.resultValue = result.getValue();
        this.resultUnit = result.getUnit().getUnitName();
        this.resultMeasurementType = result.getUnit().getMeasurementType();
    }

    public QuantityMeasurementEntity(
            QuantityModel<? extends IMeasurable> thisQuantity,
            QuantityModel<? extends IMeasurable> thatQuantity,
            String operation,
            String errorMessage,
            boolean isError
    ) {
        this(thisQuantity, thatQuantity, operation);
        this.errorMessage = errorMessage;
        this.isError = isError;
    }

    private QuantityMeasurementEntity(
            QuantityModel<? extends IMeasurable> thisQuantity,
            QuantityModel<? extends IMeasurable> thatQuantity,
            String operation
    ) {
        if (thisQuantity != null) {
            this.thisValue = thisQuantity.getValue();
            this.thisUnit = thisQuantity.getUnit().getUnitName();
            this.thisMeasurementType = thisQuantity.getUnit().getMeasurementType();
        }
        if (thatQuantity != null) {
            this.thatValue = thatQuantity.getValue();
            this.thatUnit = thatQuantity.getUnit().getUnitName();
            this.thatMeasurementType = thatQuantity.getUnit().getMeasurementType();
        }
        this.operation = operation;
    }

    public double getThisValue() {
        return thisValue;
    }

    public String getThisUnit() {
        return thisUnit;
    }

    public String getThisMeasurementType() {
        return thisMeasurementType;
    }

    public double getThatValue() {
        return thatValue;
    }

    public String getThatUnit() {
        return thatUnit;
    }

    public String getThatMeasurementType() {
        return thatMeasurementType;
    }

    public String getOperation() {
        return operation;
    }

    public double getResultValue() {
        return resultValue;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public String getResultMeasurementType() {
        return resultMeasurementType;
    }

    public String getResultString() {
        return resultString;
    }

    public boolean isError() {
        return isError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        if (isError) {
            return String.format("ERROR [%s]: %s", operation, errorMessage);
        }
        if (resultString != null) {
            return String.format("%s: %.2f %s %s %.2f %s = %s",
                    operation, thisValue, thisUnit, operation.toLowerCase(), thatValue, thatUnit, resultString);
        }
        return String.format("%s: %.2f %s %s %.2f %s = %.2f %s",
                operation, thisValue, thisUnit, operation.toLowerCase(), thatValue, thatUnit, resultValue, resultUnit);
    }

    public static void main(String[] args) {
        QuantityModel<LengthUnit> q1 = new QuantityModel<>(1.0, LengthUnit.FEET);
        QuantityModel<LengthUnit> q2 = new QuantityModel<>(12.0, LengthUnit.INCHES);
        QuantityModel<LengthUnit> result = new QuantityModel<>(2.0, LengthUnit.FEET);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(q1, q2, "ADD", result);
        System.out.println("Entity created: " + entity);
    }
}