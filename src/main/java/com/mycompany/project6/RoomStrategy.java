package com.mycompany.sadpproject;

interface PricingStrategy 
{
    double calculatePrice(Room room, int nights);
    String getLabel();
}

class StandardPricing implements PricingStrategy 
{

    @Override
    public double calculatePrice(Room room, int nights) {
        double nightly = switch (room.getType()) {
            case SINGLE -> 80.0;
            case DOUBLE -> 120.0;
            case SUITE  -> 200.0;
            case DELUXE -> 300.0;
        };
        return nightly * nights;
    }

    @Override
    public String getLabel() { return "Standard Rate"; }
}

class DiscountPricing implements PricingStrategy {

    private final PricingStrategy base;
    private final double          discountPercent;

    /**
     * @param base            the base strategy to discount from
     * @param discountPercent e.g. 20 for 20% off
     */
    public DiscountPricing(PricingStrategy base, double discountPercent) {
        this.base            = base;
        this.discountPercent = discountPercent;
    }

    @Override
    public double calculatePrice(Room room, int nights) {
        return base.calculatePrice(room, nights) * (1.0 - discountPercent / 100.0);
    }

    @Override
    public String getLabel() {
        return (int) discountPercent + "% Discount Rate";
    }
}
