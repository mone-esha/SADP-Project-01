package com.mycompany.project6;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotelFacade
{

    
    private final HotelDataStore store = HotelDataStore.getInstance();

    private PricingStrategy pricing = new StandardPricingStrategy();

    private static final String ROOMS        = "rooms.ser";
    private static final String CUSTOMERS    = "customers.ser";
    private static final String RESERVATIONS = "reservations.ser";

    public HotelFacade() 
    {
        loadData(); 
    }


    public void setPricingStrategy(PricingStrategy strategy) 
    {
        this.pricing = strategy;
    }

   
    public double quotePrice(Room room, int nights) 
    {
        return pricing.calculatePrice(room, nights);
    }

   

    public void addRoom(int roomNumber, RoomType type)
    {
        if (roomNumber <= 0) throw new IllegalArgumentException("Room number must be > 0");
        store.getRooms().add(new Room(roomNumber, type));
    }

    public void updateRoom(int index, int roomNumber, RoomType type) 
    {
        Room r = store.getRooms().get(index);
        r.setRoomNumber(roomNumber);
        r.setType(type);
    }

    public void deleteRoom(int index)
    {
        store.getRooms().remove(index);
    }

    public List<Room> getRooms()
    {
        return store.getRooms();
    }


    public void addCustomer(String name, String phone)
    {
        if (name == null || name.isBlank())   throw new IllegalArgumentException("Name is required");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("Phone is required");
        store.getCustomers().add(new Customer(name.trim(), phone.trim()));
    }

    public void updateCustomer(int index, String name, String phone) 
    {
        Customer c = store.getCustomers().get(index);
        c.setName(name);
        c.setPhone(phone);
    }

    public void deleteCustomer(int index)
    {
        store.getCustomers().remove(index);
    }

    public List<Customer> getCustomers()
    {
        return store.getCustomers();
    }

    

    public void addReservation(Customer customer, Room room, Date date)
    {
        store.getReservations().add(new Reservation(customer, room, date));
        room.setBooked(true);
    }

    public void removeReservation(int index) 
    {
        Reservation removed = store.getReservations().remove(index);
        removed.getRoom().setBooked(false);
    }

    public List<Reservation> getReservations()
    {
        return store.getReservations();
    }

   

    public void saveData() 
    {
        try (
            ObjectOutputStream roomOut = new ObjectOutputStream(Files.newOutputStream(Paths.get(ROOMS)));
            ObjectOutputStream custOut = new ObjectOutputStream(Files.newOutputStream(Paths.get(CUSTOMERS)));
            ObjectOutputStream resOut  = new ObjectOutputStream(Files.newOutputStream(Paths.get(RESERVATIONS)))
        ) {
            roomOut.writeObject(store.getRooms());
            custOut.writeObject(store.getCustomers());
            resOut.writeObject(store.getReservations());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData()
    {
        try {
            if (Files.exists(Paths.get(ROOMS)))
                try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(ROOMS))))
                    { store.setRooms((ArrayList<Room>) in.readObject()); }

            if (Files.exists(Paths.get(CUSTOMERS)))
                try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(CUSTOMERS))))
                    { store.setCustomers((ArrayList<Customer>) in.readObject()); }

            if (Files.exists(Paths.get(RESERVATIONS)))
                try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(RESERVATIONS))))
                    { store.setReservations((ArrayList<Reservation>) in.readObject()); }

        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}