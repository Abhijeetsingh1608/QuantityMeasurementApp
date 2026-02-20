package com.quantityMeasurementApp;

public class QuantityMeasurementApp {

	public static void main(String[] args) {

		Feet f1 = new Feet(1.0);
		Feet f2 = new Feet(1.0);
		
		Inches n1 = new Inches(1.0);
		Inches n2 = new Inches(1.0);

		System.out.println("Equal: " + f1.equals(f2));
		System.out.println("Inches equal: " + n1.equals(n2));
	}
}