package com.mycompany.project6;

import java.io.Serializable;
import java.util.Date;

enum RoomType
{
    SINGLE,DOUBLE,SUITE,DELUXE
}

class Room implements Serializable
{
    private int roomNumber;
    private RoomType type;
    private boolean isBooked;

    public Room(int roomNumber,RoomType type)
    {
        this.roomNumber=roomNumber;
        this.type=type;
        this.isBooked=false;
    }

    public int getRoomNumber()
    {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber)
    {
        this.roomNumber=roomNumber;
    }

    public RoomType getType()
    {
        return type;
    }

    public void setType(RoomType type)
    {
        this.type=type;
    }

    public boolean isBooked()
    {
        return isBooked;
    }

    public void setBooked(boolean booked)
    {
        isBooked=booked;
    }

    @Override
    public String toString()
    {
        return "Room "+roomNumber+" ("+type+")";
    }
}

class Customer implements Serializable
{
    private String name;
    private String phone;

    public Customer(String name,String phone)
    {
        this.name=name;
        this.phone=phone;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone=phone;
    }

    @Override
    public String toString()
    {
        return name+" ("+phone+")";
    }
}

class Reservation implements Serializable
{
    private Customer customer;
    private Room room;
    private Date date;

    public Reservation(Customer customer,Room room,Date date)
    {
        this.customer=customer;
        this.room=room;
        this.date=date;
        room.setBooked(true);
    }


    public Customer getCustomer()
    {
        return customer;
    }

    public Room getRoom()
    {
        return room;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date=date;
    }
}
