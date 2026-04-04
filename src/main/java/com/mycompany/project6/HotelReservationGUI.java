package com.mycompany.project6;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class HotelReservationGUI extends JFrame {

    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Reservation> reservations = new ArrayList<>();

    private static final String ROOMS = "rooms.ser";
    private static final String CUSTOMERS = "customers.ser";
    private static final String RESERVATIONS = "reservations.ser";

    // ── Colours ───────────────────────────────────────────────────────────────
    private static final Color DARK_BLUE   = new Color(23, 42, 70);
    private static final Color ACCENT_BLUE = new Color(52, 120, 246);
    private static final Color LIGHT_GRAY  = new Color(245, 247, 250);
    private static final Color WHITE       = Color.WHITE;
    private static final Color TEXT_DARK   = new Color(30, 30, 30);
    private static final Color TABLE_STRIPE= new Color(235, 242, 255);

    public HotelReservationGUI() {
        loadData();

        setTitle("Hotel Reservation System");
        setSize(520, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_GRAY);

        // ── Header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Hotel Reservation System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(WHITE);
        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBackground(LIGHT_GRAY);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JButton b1 = createMenuButton("Manage Rooms");
        JButton b2 = createMenuButton("Manage Customers");
        JButton b3 = createMenuButton("Manage Reservations");
        JButton b4 = createMenuButton("Exit");
        b4.setBackground(new Color(200, 50, 50));  // red for exit

        b1.addActionListener(e -> roomForm());
        b2.addActionListener(e -> customerForm());
        b3.addActionListener(e -> reservationForm());
        b4.addActionListener(e -> { saveData(); System.exit(0); });

        buttonPanel.add(b1);
        buttonPanel.add(b2);
        buttonPanel.add(b3);
        buttonPanel.add(b4);

        add(buttonPanel, BorderLayout.CENTER);

        // ── Footer ────────────────────────────────────────────────────────────
        JPanel footer = new JPanel();
        footer.setBackground(DARK_BLUE);
        footer.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        JLabel footerLabel = new JLabel("© 2025 Hotel Management");
        footerLabel.setForeground(new Color(160, 180, 210));
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);
    }

    // ── Styled menu button ────────────────────────────────────────────────────
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(ACCENT_BLUE);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            Color original = btn.getBackground();
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(btn.getBackground().darker());
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(original);
            }
        });
        return btn;
    }

    // ── Styled dialog ─────────────────────────────────────────────────────────
    private JDialog createDialog(String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(850, 450);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(LIGHT_GRAY);
        dialog.setLocationRelativeTo(this);

        // Dialog header
        JPanel dHeader = new JPanel(new BorderLayout());
        dHeader.setBackground(DARK_BLUE);
        dHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel dTitle = new JLabel(title);
        dTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dTitle.setForeground(WHITE);
        dHeader.add(dTitle, BorderLayout.WEST);
        dialog.add(dHeader, BorderLayout.NORTH);

        return dialog;
    }

    // ── Styled table ──────────────────────────────────────────────────────────
    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row))
                    c.setBackground(row % 2 == 0 ? WHITE : TABLE_STRIPE);
                return c;
            }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setGridColor(new Color(220, 225, 235));
        table.setSelectionBackground(ACCENT_BLUE);
        table.setSelectionForeground(WHITE);
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(DARK_BLUE);
        header.setForeground(WHITE);
        header.setBorder(BorderFactory.createEmptyBorder());

        return table;
    }

    // ── Styled action button ──────────────────────────────────────────────────
    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(color);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }

    // ── Styled label ──────────────────────────────────────────────────────────
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(TEXT_DARK);
        return label;
    }

    // ── Styled text field ─────────────────────────────────────────────────────
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    // ── Room form ─────────────────────────────────────────────────────────────
    private void roomForm() {
        JDialog dialog = createDialog("🛏   Manage Rooms");

        JTextField numField = createTextField();
        JComboBox<RoomType> typeCombo = new JComboBox<>(RoomType.values());
        typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableModel model = new DefaultTableModel(new String[]{"Room No", "Type", "Status"}, 0);
        JTable table = createTable(model);
        refreshRooms(model);

        JButton add    = createActionButton("Add",    new Color(39, 174, 96));
        JButton update = createActionButton("Update", ACCENT_BLUE);
        JButton delete = createActionButton("Delete", new Color(200, 50, 50));

        add.addActionListener(e -> {
            try {
                rooms.add(new Room(Integer.parseInt(numField.getText()),
                                   (RoomType) typeCombo.getSelectedItem()));
                refreshRooms(model);
                numField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid room number.");
            }
        });

        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                rooms.get(r).setRoomNumber(Integer.parseInt(numField.getText()));
                rooms.get(r).setType((RoomType) typeCombo.getSelectedItem());
                refreshRooms(model);
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) { rooms.remove(r); refreshRooms(model); }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                numField.setText(model.getValueAt(r, 0).toString());
                typeCombo.setSelectedItem(RoomType.valueOf(model.getValueAt(r, 1).toString()));
            }
        });

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(LIGHT_GRAY);
        inputPanel.add(createLabel("Room No:")); inputPanel.add(numField);
        inputPanel.add(createLabel("Type:"));    inputPanel.add(typeCombo);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBackground(LIGHT_GRAY);
        btnPanel.add(add); btnPanel.add(update); btnPanel.add(delete);

        JPanel left = new JPanel(new BorderLayout(0, 15));
        left.setBackground(LIGHT_GRAY);
        left.add(inputPanel, BorderLayout.NORTH);
        left.add(btnPanel,   BorderLayout.SOUTH);
        left.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        dialog.add(left, BorderLayout.WEST);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void refreshRooms(DefaultTableModel model) {
        model.setRowCount(0);
        for (Room r : rooms)
            model.addRow(new Object[]{r.getRoomNumber(), r.getType(),
                                      r.isBooked() ? "Booked" : "Free"});
    }

    // ── Customer form ─────────────────────────────────────────────────────────
    private void customerForm() {
        JDialog dialog = createDialog("👤   Manage Customers");

        JTextField nameField  = createTextField();
        JTextField phoneField = createTextField();

        DefaultTableModel model = new DefaultTableModel(new String[]{"Name", "Phone"}, 0);
        JTable table = createTable(model);
        refreshCustomers(model);

        JButton add    = createActionButton("Add",    new Color(39, 174, 96));
        JButton update = createActionButton("Update", ACCENT_BLUE);
        JButton delete = createActionButton("Delete", new Color(200, 50, 50));

        add.addActionListener(e -> {
            if (nameField.getText().isBlank() || phoneField.getText().isBlank()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                return;
            }
            customers.add(new Customer(nameField.getText(), phoneField.getText()));
            refreshCustomers(model);
            nameField.setText(""); phoneField.setText("");
        });

        update.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                customers.get(r).setName(nameField.getText());
                customers.get(r).setPhone(phoneField.getText());
                refreshCustomers(model);
            }
        });

        delete.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) { customers.remove(r); refreshCustomers(model); }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                nameField.setText(model.getValueAt(r, 0).toString());
                phoneField.setText(model.getValueAt(r, 1).toString());
            }
        });

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(LIGHT_GRAY);
        inputPanel.add(createLabel("Name:"));  inputPanel.add(nameField);
        inputPanel.add(createLabel("Phone:")); inputPanel.add(phoneField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBackground(LIGHT_GRAY);
        btnPanel.add(add); btnPanel.add(update); btnPanel.add(delete);

        JPanel left = new JPanel(new BorderLayout(0, 15));
        left.setBackground(LIGHT_GRAY);
        left.add(inputPanel, BorderLayout.NORTH);
        left.add(btnPanel,   BorderLayout.SOUTH);
        left.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        dialog.add(left, BorderLayout.WEST);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void refreshCustomers(DefaultTableModel model) {
        model.setRowCount(0);
        for (Customer c : customers)
            model.addRow(new Object[]{c.getName(), c.getPhone()});
    }

    // ── Reservation form ──────────────────────────────────────────────────────
    private void reservationForm() {
        if (rooms.isEmpty() || customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add rooms and customers first.");
            return;
        }

        JDialog dialog = createDialog("📋   Manage Reservations");

        JComboBox<Customer> combo1 = new JComboBox<>(customers.toArray(new Customer[0]));
        JComboBox<Room>     combo2 = new JComboBox<>();
        combo1.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        for (Room r : rooms) if (!r.isBooked()) combo2.addItem(r);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
        dateSpinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Customer", "Room", "Date"}, 0);
        JTable table = createTable(model);
        refreshRes(model);

        JButton add    = createActionButton("Add",    new Color(39, 174, 96));
        JButton delete = createActionButton("Delete", new Color(200, 50, 50));

        add.addActionListener(e -> {
            Room selected = (Room) combo2.getSelectedItem();
            if (selected == null) return;
            Reservation r = new Reservation(
                    (Customer) combo1.getSelectedItem(), selected,
                    (Date) dateSpinner.getValue());
            reservations.add(r);
            selected.setBooked(true);
            combo2.removeItemAt(combo2.getSelectedIndex());
            refreshRes(model);
        });

        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Reservation res = reservations.remove(row);
                res.getRoom().setBooked(false);
                combo2.addItem(res.getRoom());
                refreshRes(model);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    combo1.setSelectedItem(reservations.get(row).getCustomer());
                    combo2.setSelectedItem(reservations.get(row).getRoom());
                    dateSpinner.setValue(reservations.get(row).getDate());
                }
            }
        });

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(LIGHT_GRAY);
        inputPanel.add(createLabel("Customer")); inputPanel.add(combo1);
        inputPanel.add(createLabel("Room"));     inputPanel.add(combo2);
        inputPanel.add(createLabel("Date"));     inputPanel.add(dateSpinner);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBackground(LIGHT_GRAY);
        btnPanel.add(add); btnPanel.add(delete);

        JPanel left = new JPanel(new BorderLayout(0, 15));
        left.setBackground(LIGHT_GRAY);
        left.add(inputPanel, BorderLayout.NORTH);
        left.add(btnPanel,   BorderLayout.SOUTH);
        left.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        dialog.add(left, BorderLayout.WEST);
        dialog.add(new JScrollPane(table), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void refreshRes(DefaultTableModel model) {
        model.setRowCount(0);
        for (Reservation r : reservations)
            model.addRow(new Object[]{r.getCustomer().getName(),
                                      r.getRoom().getRoomNumber(), r.getDate()});
    }

    // ── Save & Load ───────────────────────────────────────────────────────────
    private void saveData() {
        try (
            ObjectOutputStream roomOut = new ObjectOutputStream(Files.newOutputStream(Paths.get(ROOMS)));
            ObjectOutputStream custOut = new ObjectOutputStream(Files.newOutputStream(Paths.get(CUSTOMERS)));
            ObjectOutputStream resOut  = new ObjectOutputStream(Files.newOutputStream(Paths.get(RESERVATIONS)))
        ) {
            roomOut.writeObject(rooms);
            custOut.writeObject(customers);
            resOut.writeObject(reservations);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            if (Files.exists(Paths.get(ROOMS)))
                try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(ROOMS))))
                    { rooms = (ArrayList<Room>) in.readObject(); }
            if (Files.exists(Paths.get(CUSTOMERS)))
                try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(CUSTOMERS))))
                    { customers = (ArrayList<Customer>) in.readObject(); }
            if (Files.exists(Paths.get(RESERVATIONS)))
                try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(Paths.get(RESERVATIONS))))
                    { reservations = (ArrayList<Reservation>) in.readObject(); }
        } catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
    }
}