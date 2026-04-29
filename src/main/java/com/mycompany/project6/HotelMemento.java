
package com.mycompany.project6;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


class ReservationMemento 
{

    private final List<Reservation> snapshot;

    ReservationMemento(List<Reservation> reservations)
    {
       
        this.snapshot = new ArrayList<>(reservations);
    }

    
    List<Reservation> getSnapshot() {
        return new ArrayList<>(snapshot);
    }
}


class ReservationHistory 
{

    private final Stack<ReservationMemento> history = new Stack<>();

 
    public void save(List<Reservation> reservations) 
    {
        history.push(new ReservationMemento(reservations));
    }

    
    public List<Reservation> undo() 
    {
        if (history.isEmpty()) return null;
        return history.pop().getSnapshot();
    }

    
    public boolean canUndo() 
    {
        return !history.isEmpty();
    }
}
