package com.mycompany.project6;

import java.util.ArrayList;
import java.util.List;

public class HotelDataStore 
{

    private static HotelDataStore instance;

    private List<Room> rooms= new ArrayList<>();
    private List<Customer>customers  = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    private HotelDataStore() {}   

    public static synchronized HotelDataStore getInstance() 
    {
        if (instance == null)
            instance = new HotelDataStore();
        return instance;
    }

    public List<Room>  getRooms()      
    {
        return rooms; 
    }
    public List<Customer> getCustomers()  
    { 
        return customers; 
    }
    public List<Reservation> getReservations()
    { 
        return reservations; 
    }

    public void setRooms(List<Room> r)             
    { 
        this.rooms = r; 
    }
    public void setCustomers(List<Customer> c)      
    { 
        this.customers = c;
    }
    public void setReservations(List<Reservation> r)
    { 
        this.reservations = r;
    }
}