package com.mycompany.project6;

import java.awt.GridLayout;
import javax.swing.JFrame;

public class HotelTest 
{
    public static void main(String[] args) 
    {
        HotelReservationGUI frame = new HotelReservationGUI();
        frame.setTitle("Hotel Reservation System");
        frame.setSize(400,400);
        frame.setLayout(new GridLayout(5,1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        frame.setVisible(true);
    }
}
