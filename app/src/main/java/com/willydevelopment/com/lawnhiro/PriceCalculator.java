package com.willydevelopment.com.lawnhiro;

/**
 * Created by JJ on 1/16/17.
 */
public class PriceCalculator {
    private int finalPrice = 25;
    private int extraSqFt;
    private double priceModifier;

    public int CalculatePrice(int mowableSize) {

        if (mowableSize > 500 && mowableSize < 2000) {
            finalPrice = 25;
        } else if (mowableSize >= 2000 && mowableSize < 3000) {
            finalPrice = 27;
        } else if (mowableSize >= 3000 && mowableSize < 4000) {
            finalPrice = 30;
        } else if (mowableSize >= 4000 && mowableSize < 5000) {
            finalPrice = 33;
        } else if (mowableSize >= 5000 && mowableSize < 6000) {
            finalPrice = 36;
        } else if (mowableSize >= 6000 && mowableSize < 7000) {
            finalPrice = 39;
        } else if (mowableSize >= 7000 && mowableSize < 8000) {
            finalPrice = 41;
        } else if (mowableSize >= 8000 && mowableSize < 9000) {
            finalPrice = 43;
        } else if (mowableSize >= 9000 && mowableSize < 10000) {
            finalPrice = 45;
        } else if (mowableSize >= 10000 && mowableSize < 11000) {
            finalPrice = 47;
        } else if (mowableSize >= 11000 && mowableSize < 13000) {
            finalPrice = 49;
        } else if (mowableSize >= 13000 && mowableSize < 15000) {
            finalPrice = 54;
        } else if (mowableSize >= 15000 && mowableSize < 17000) {
            finalPrice = 58;
        } else if (mowableSize >= 17000 && mowableSize < 19000) {
            finalPrice = 62;
        } else if (mowableSize >= 19000 && mowableSize < 21000) {
            finalPrice = 67;
        }

        extraSqFt = mowableSize - 3000;
        priceModifier = Math.floor(extraSqFt / 2000);

        if (priceModifier > 0) {
            finalPrice += priceModifier * 5; // add $5
        }

        return finalPrice;
    }
}
