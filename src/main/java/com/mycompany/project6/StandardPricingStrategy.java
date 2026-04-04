package com.mycompany.project6;


public class StandardPricingStrategy implements PricingStrategy 
{

    @Override
    public double calculatePrice(Room room, int nights)
    {
        double nightly = switch (room.getType())
        {
            case SINGLE -> 80.0;
            case DOUBLE -> 120.0;
            case SUITE  -> 200.0;
            case DELUXE -> 300.0;
        };
        return nightly * nights;
    }
}