package com.mycompany.project6;


public class DiscountPricingStrategy implements PricingStrategy 
{

    private final PricingStrategy base;
    private final double discountPercent;

    public DiscountPricingStrategy(PricingStrategy base, double discountPercent)
    {
        this.base = base;
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculatePrice(Room room, int nights)
    {
        return base.calculatePrice(room, nights) * (1.0 - discountPercent / 100.0);
    }
}