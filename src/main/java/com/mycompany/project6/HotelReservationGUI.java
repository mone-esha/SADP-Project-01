package com.mycompany.project6;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;


public class HotelReservationGUI extends JFrame
{
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Reservation> reservations= new ArrayList<>();

    private static final String ROOMS="rooms.ser";
    private static final String CUSTOMERS="customers.ser";
    private static final String RESERVATIONS="reservations.ser";

    public HotelReservationGUI()
    {
        loadData();

        JButton b1=new JButton("Manage Rooms", new ImageIcon(getClass().getResource("/hotel.png")));
        JButton b2=new JButton("Manage Customers", new ImageIcon(getClass().getResource("/guest.png")));
        JButton b3=new JButton("Manage Reservations", new ImageIcon(getClass().getResource("/check-in.png")));
        JButton b4=new JButton("Exit", new ImageIcon(getClass().getResource("/exit-door.png")));
        
        b1.setHorizontalTextPosition(SwingConstants.RIGHT);
        b2.setHorizontalTextPosition(SwingConstants.RIGHT);
        b3.setHorizontalTextPosition(SwingConstants.RIGHT);
        b4.setHorizontalTextPosition(SwingConstants.RIGHT);

        b1.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                roomForm();
            }
        });
        b2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                customerForm();
            }
        });
        b3.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e){
                reservationForm();
            }
        });
        b4.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                saveData();
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 rows, 2 cols, 10px gaps
        buttonPanel.add(b1);
        buttonPanel.add(b2);
        buttonPanel.add(b3);
        buttonPanel.add(b4);
        
        setLayout(new BorderLayout());
        add(new JLabel("Welcome To Hotel Reservation System", SwingConstants.CENTER), BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void roomForm()
    {
        JDialog dialog = new JDialog(this, "Rooms", true);
        dialog.setSize(800, 400); 
        dialog.setLayout(new BorderLayout(10, 10)); 

        JTextField textfield = new JTextField();
        JComboBox combo = new JComboBox(RoomType.values());

        DefaultTableModel model = new DefaultTableModel(new String[]{"No", "Type", "Status"}, 0);
        JTable table = new JTable(model);
        refreshRooms(model);

        JPanel leftPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Room No:")); 
        inputPanel.add(textfield);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(combo);

        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(add); 
        buttonPanel.add(update); 
        buttonPanel.add(delete);

        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                rooms.add(new Room(Integer.parseInt(textfield.getText()), (RoomType) combo.getSelectedItem()));
                refreshRooms(model);
            }
        });

        update.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int r = table.getSelectedRow();
                 if (r >= 0) 
                 {
                     rooms.get(r).setRoomNumber(Integer.parseInt(textfield.getText()));
                     rooms.get(r).setType((RoomType) combo.getSelectedItem());
                     refreshRooms(model);
                 }
            }
        });

        delete.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                int r = table.getSelectedRow();
                if (r >= 0)
                {
                    rooms.remove(r);
                    refreshRooms(model);
                }
            }
        });

        table.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                textfield.setText(model.getValueAt(r, 0).toString());
                combo.setSelectedItem(RoomType.valueOf(model.getValueAt(r, 1).toString()));
            }
        });

        dialog.add(leftPanel, BorderLayout.WEST); 
        dialog.add(new JScrollPane(table), BorderLayout.CENTER); 

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void refreshRooms(DefaultTableModel model)
    {
        model.setRowCount(0);
        for(Room r:rooms)
            model.addRow(new Object[]{r.getRoomNumber(),r.getType(),r.isBooked()?"Booked":"Free"});
    }

    private void customerForm()
    {
        JDialog dialog = new JDialog(this, "Customers", true);
        dialog.setSize(800, 400); 
        dialog.setLayout(new BorderLayout(10, 10)); 

        JTextField textfield1 = new JTextField();
        JTextField textfield2 = new JTextField();

        DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Phone"}, 0);
        JTable table = new JTable(model);
        refreshCustomers(model);

        JPanel leftPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(textfield1);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(textfield2);

        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(add);
        buttonPanel.add(update);
        buttonPanel.add(delete);

        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                customers.add(new Customer(textfield1.getText(), textfield2.getText()));
                refreshCustomers(model);
            }
        });

        update.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int r = table.getSelectedRow();
                if (r >= 0)
                {
                    customers.get(r).setName(textfield1.getText());
                    customers.get(r).setPhone(textfield2.getText());
                    refreshCustomers(model);
                }
            }
        });

        delete.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int r = table.getSelectedRow();
                if (r >= 0)
                {
                    customers.remove(r);
                    refreshCustomers(model);
                }
            }
        });

        table.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int r = table.getSelectedRow();
                textfield1.setText(model.getValueAt(r, 0).toString());
                textfield2.setText(model.getValueAt(r, 1).toString());
            }
        });

        dialog.add(leftPanel, BorderLayout.WEST);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void refreshCustomers(DefaultTableModel model)
    {
        model.setRowCount(0);
        for(Customer c:customers)
            model.addRow(new Object[]{c.getName(), c.getPhone()});
    }

    private void reservationForm()
    {
        if(rooms.isEmpty() || customers.isEmpty()) 
            return;

        JDialog dialog = new JDialog(this, "Reservations", true);
        dialog.setSize(800, 400); 
        dialog.setLayout(new BorderLayout(10, 10));

        JComboBox combo1 = new JComboBox(customers.toArray());
        JComboBox combo2 = new JComboBox();
        for(Room r : rooms)
            if(!r.isBooked()) combo2.addItem(r);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(editor);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Customer","Room","Date"}, 0);
        JTable table = new JTable(model);
        refreshRes(model);

        JPanel leftPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("Customer"));
        inputPanel.add(combo1);
        inputPanel.add(new JLabel("Room"));
        inputPanel.add(combo2);
        inputPanel.add(new JLabel("Date"));
        inputPanel.add(dateSpinner);

        JButton add = new JButton("Add");
        JButton delete = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(add);
        buttonPanel.add(delete);

        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Room selectedRoom = (Room) combo2.getSelectedItem();
                if(selectedRoom == null) return;

                Date date = (Date) dateSpinner.getValue();

                Reservation r = new Reservation(
                        (Customer) combo1.getSelectedItem(),
                        selectedRoom,
                        date
                );

                reservations.add(r);
                selectedRoom.setBooked(true);
                combo2.removeItemAt(combo2.getSelectedIndex());
                refreshRes(model);
            }
        });

        delete.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int row = table.getSelectedRow();
                if(row >= 0)
                {
                    Reservation res = reservations.remove(row);
                    res.getRoom().setBooked(false);
                    combo2.addItem(res.getRoom());
                    refreshRes(model);
                }
            }
        });

        table.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int row = table.getSelectedRow();
                if(row >= 0)
                {
                    combo1.setSelectedItem(reservations.get(row).getCustomer());
                    combo2.setSelectedItem(reservations.get(row).getRoom());
                    dateSpinner.setValue(reservations.get(row).getDate());
                }
            }
        });

        dialog.add(leftPanel, BorderLayout.WEST);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void refreshRes(DefaultTableModel model)
    {
        model.setRowCount(0);
        for(Reservation r:reservations)
            model.addRow(new Object[]{r.getCustomer().getName(), r.getRoom().getRoomNumber(), r.getDate()});
    }

    private void saveData()
    {
        try (
                ObjectOutputStream roomOut = new ObjectOutputStream(Files.newOutputStream(Paths.get(ROOMS)));
                ObjectOutputStream customerOut = new ObjectOutputStream(Files.newOutputStream(Paths.get(CUSTOMERS)));
                ObjectOutputStream reservationOut = new ObjectOutputStream(Files.newOutputStream(Paths.get(RESERVATIONS)))
            )
        {
            roomOut.writeObject(rooms);
            customerOut.writeObject(customers);
            reservationOut.writeObject(reservations);
        }
        catch (IOException e)
        {
            e.printStackTrace(); 
        }
    }

    private void loadData()
    {
        try
        {
            if (Files.exists(Paths.get(ROOMS)))
            {
                try (ObjectInputStream roomIn = new ObjectInputStream(Files.newInputStream(Paths.get(ROOMS)))) 
                {
                    rooms = (ArrayList<Room>) roomIn.readObject();
                }
            }

            if (Files.exists(Paths.get(CUSTOMERS)))
            {
                try (ObjectInputStream customerIn = new ObjectInputStream(Files.newInputStream(Paths.get(CUSTOMERS)))) 
                {
                    customers = (ArrayList<Customer>) customerIn.readObject();
                }
            }

            if (Files.exists(Paths.get(RESERVATIONS)))
            {
                try (ObjectInputStream reservationIn = new ObjectInputStream(Files.newInputStream(Paths.get(RESERVATIONS)))) 
                {
                    reservations = (ArrayList<Reservation>) reservationIn.readObject();
                }
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace(); 
        }
    }

}
