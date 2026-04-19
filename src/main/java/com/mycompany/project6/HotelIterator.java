package com.mycompany.project6;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


class CustomerReservationIterator implements Iterator<Reservation> 
{

    private final List<Reservation> all;
    private final Customer target;
    private int cursor   = 0;
    private Reservation nextItem = null;

    CustomerReservationIterator(List<Reservation> all, Customer target) 
    {
        this.all    = all;
        this.target = target;
        advance(); 
    }


    private void advance() 
    {
        nextItem = null;
        while (cursor < all.size()) {
            Reservation r = all.get(cursor++);
            if (r.getCustomer().getName().equals(target.getName())) {
                nextItem = r;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() 
    { 
        return nextItem != null; 
    }

    @Override
    public Reservation next() {
        if (nextItem == null) throw new NoSuchElementException();
        Reservation result = nextItem;
        advance(); 
        return result;
    }
}
class ReservationCollection
{

    private final List<Reservation> reservations;

    public ReservationCollection(List<Reservation> reservations) 
    {
        this.reservations = reservations;
    }

    
    public Iterator<Reservation> iteratorFor(Customer customer) 
    {
        return new CustomerReservationIterator(reservations, customer);
    }

   
    public Iterator<Reservation> iterator() 
    {
        return reservations.iterator();
    }
}