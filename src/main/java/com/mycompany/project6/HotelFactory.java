package com.mycompany.sadpproject;

import java.util.*;


enum ActionType {
    BOOK, CANCEL, CHECKOUT
}


interface ReservationAction {
    void execute(List<Reservation> reservations, Reservation reservation);
}


class BookReservation implements ReservationAction {
    @Override
    public void execute(List<Reservation> reservations, Reservation reservation) {
        if (reservation.getRoom().isBooked()) {
            System.out.println("Room already booked!");
            return;
        }
        reservation.getRoom().setBooked(true);
        reservations.add(reservation);
        System.out.println("Reservation booked successfully.");
    }
}


class CancelReservation implements ReservationAction {
    @Override
    public void execute(List<Reservation> reservations, Reservation reservation) {
        if (reservations.remove(reservation)) {
            reservation.getRoom().setBooked(false);
            System.out.println("Reservation cancelled.");
        } else {
            System.out.println("Reservation not found.");
        }
    }
}

// ===== Concrete Product: CHECKOUT =====
class CheckoutReservation implements ReservationAction {
    @Override
    public void execute(List<Reservation> reservations, Reservation reservation) {
        if (reservations.contains(reservation)) {
            reservation.getRoom().setBooked(false);
            reservations.remove(reservation);
            System.out.println("Checked out successfully.");
        } else {
            System.out.println("Reservation not found.");
        }
    }
}

// ===== FACTORY CLASS =====
class ReservationFactory {

    public static ReservationAction getAction(ActionType type) {
        switch (type) {
            case BOOK:
                return new BookReservation();
            case CANCEL:
                return new CancelReservation();
            case CHECKOUT:
                return new CheckoutReservation();
            default:
                throw new IllegalArgumentException("Invalid action type");
        }
    }
}