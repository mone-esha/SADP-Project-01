package com.mycompany.sadpproject;

import java.util.ArrayList;
import java.util.List;


interface HotelObserver 
{
    void update(String event, Object data);
}

interface HotelObservable 
{
    void addObserver(HotelObserver observer);
    void removeObserver(HotelObserver observer);
    void notifyObservers(String event, Object data);
}


class ReservationObservable implements HotelObservable 
{

    private final List<HotelObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(HotelObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(HotelObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, Object data) {
        for (HotelObserver observer : observers)
            observer.update(event, data);
    }

    public void onReservationAdded(Reservation r) {
        notifyObservers("RESERVATION_ADDED", r);
    }

    public void onReservationDeleted(Reservation r) {
        notifyObservers("RESERVATION_DELETED", r);
    }

    public void onReservationUndone() {
        notifyObservers("RESERVATION_UNDONE", null);
    }

    public void onRoomBooked(Room r) {
        notifyObservers("ROOM_BOOKED", r);
    }

    public void onRoomFreed(Room r) {
        notifyObservers("ROOM_FREED", r);
    }
}