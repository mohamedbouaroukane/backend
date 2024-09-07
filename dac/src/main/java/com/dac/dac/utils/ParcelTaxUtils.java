package com.dac.dac.utils;

public class ParcelTaxUtils {

    public static double ParcelTaxCalculator(double weight){
        double overWight = weight - 22;
        double reputation = overWight / 2;
        return reputation * 100;
    }
}
