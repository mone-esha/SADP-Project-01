package com.mycompany.sadpproject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


class RoomService {

    private final List<Room> rooms = new ArrayList<>();

    public void addRoom(int number, RoomType type) {
        rooms.add(new Room(number, type));
        System.out.println("[RoomService] Room #" + number + " (" + type + ") added to inventory.");
    }

    public Room findAvailableRoom(RoomType type) {
        for (Room r : rooms) {
            if (r.getType() == type && !r.isBooked()) return r;
        }
        return null;
    }

    public Room findRoomByNumber(int number) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == number) return r;
        }
        return null;
    }

    public void markBooked(Room room) {
        room.setBooked(true);
        System.out.println("[RoomService] Room #" + room.getRoomNumber() + " marked as BOOKED.");
    }

    public void markAvailable(Room room) {
        room.setBooked(false);
        System.out.println("[RoomService] Room #" + room.getRoomNumber() + " marked as AVAILABLE.");
    }

    public void printInventory() {
        System.out.println("[RoomService] ── Room Inventory ──────────────");
        if (rooms.isEmpty()) { System.out.println("[RoomService] No rooms added yet."); return; }
        for (Room r : rooms)
            System.out.println("[RoomService] Room #" + r.getRoomNumber()
                    + " | " + r.getType()
                    + " | " + (r.isBooked() ? "BOOKED" : "FREE"));
    }
}




class CustomerService {

    private final List<Customer> customers = new ArrayList<>();

    public Customer registerCustomer(String name, String phone) {
        for (Customer c : customers) {
            if (c.getPhone().equals(phone)) {
                System.out.println("[CustomerService] Existing customer found: " + c.getName());
                return c;
            }
        }
        Customer c = new Customer(name, phone);
        customers.add(c);
        System.out.println("[CustomerService] New customer registered: " + name);
        return c;
    }

    public Customer findByPhone(String phone) {
        for (Customer c : customers) {
            if (c.getPhone().equals(phone)) return c;
        }
        return null;
    }

    public void printCustomers() {
        System.out.println("[CustomerService] ── Customer List ─────────────");
        if (customers.isEmpty()) { System.out.println("[CustomerService] No customers yet."); return; }
        for (Customer c : customers)
            System.out.println("[CustomerService] " + c.getName() + " | " + c.getPhone());
    }
}




class ReservationService {

    private final List<Reservation> reservations = new ArrayList<>();

    public Reservation createReservation(Customer customer, Room room, Date date) {
        Reservation r = new Reservation(customer, room, date);
        reservations.add(r);
        System.out.println("[ReservationService] Reservation created for "
                + customer.getName() + " in Room #" + room.getRoomNumber());
        return r;
    }

    public boolean cancelReservation(Customer customer, Room room) {
        Reservation toRemove = null;
        for (Reservation r : reservations) {
            if (r.getCustomer() == customer && r.getRoom() == room) {
                toRemove = r; break;
            }
        }
        if (toRemove != null) {
            reservations.remove(toRemove);
            System.out.println("[ReservationService] Reservation cancelled for "
                    + customer.getName() + " in Room #" + room.getRoomNumber());
            return true;
        }
        System.out.println("[ReservationService] No matching reservation found.");
        return false;
    }

    public List<Reservation> getAll() { return reservations; }

    public void printReservations() {
        System.out.println("[ReservationService] ── Active Reservations ─────");
        if (reservations.isEmpty()) { System.out.println("[ReservationService] None."); return; }
        for (Reservation r : reservations)
            System.out.println("[ReservationService] "
                    + r.getCustomer().getName()
                    + " → Room #" + r.getRoom().getRoomNumber()
                    + " | " + r.getRoom().getType()
                    + " | " + r.getDate());
    }
}




class BillingService {

    private static final double TAX_RATE = 0.10;

    public double getBasePrice(RoomType type) {
        switch (type) {
            case SINGLE:  return 50.0;
            case DOUBLE:  return 90.0;
            case SUITE:   return 200.0;
            case DELUXE:  return 350.0;
            default:      return 0.0;
        }
    }

    public void generateBill(Reservation reservation, int nights) {
        double base  = getBasePrice(reservation.getRoom().getType()) * nights;
        double tax   = base * TAX_RATE;
        double total = base + tax;

        System.out.println("[BillingService] ── Invoice ──────────────────────");
        System.out.println("[BillingService] Customer : " + reservation.getCustomer().getName());
        System.out.println("[BillingService] Room     : #" + reservation.getRoom().getRoomNumber()
                + " (" + reservation.getRoom().getType() + ")");
        System.out.println("[BillingService] Nights   : " + nights);
        System.out.printf ("[BillingService] Subtotal : $%.2f%n", base);
        System.out.printf ("[BillingService] Tax (10%%): $%.2f%n", tax);
        System.out.printf ("[BillingService] TOTAL    : $%.2f%n", total);
        System.out.println("[BillingService] ────────────────────────────────────");
    }
}



class NotificationService {

    public void sendBookingConfirmation(Customer customer, Room room) {
        System.out.println("[NotificationService] Confirmation sent to "
                + customer.getName() + " for Room #" + room.getRoomNumber() + ".");
    }

    public void sendCancellationNotice(Customer customer, Room room) {
        System.out.println("[NotificationService] Cancellation notice sent to "
                + customer.getName() + " for Room #" + room.getRoomNumber() + ".");
    }

    public void sendBillReceipt(Customer customer, double amount) {
        System.out.printf("[NotificationService] Receipt sent to %s. Amount: $%.2f%n",
                customer.getName(), amount);
    }
}



class HotelFacade {

    private final RoomService         roomService         = new RoomService();
    private final CustomerService     customerService     = new CustomerService();
    private final ReservationService  reservationService  = new ReservationService();
    private final BillingService      billingService      = new BillingService();
    private final NotificationService notificationService = new NotificationService();

    // ── add room ─────────────────────────────────────────────────
    public void addRoom(int number, RoomType type) {
        roomService.addRoom(number, type);
    }

    // ── find room by number ──────────────────────────────────────
    public Room findRoomByNumber(int number) {
        return roomService.findRoomByNumber(number);
    }

    // ── FACADE METHOD 1: bookRoom() ──────────────────────────────
    public Reservation bookRoom(String customerName, String phone,
                                RoomType type, Date date) {
        System.out.println("\n[HotelFacade] ── bookRoom() ─────────────────────────────");
        Customer customer = customerService.registerCustomer(customerName, phone);
        Room room = roomService.findAvailableRoom(type);
        if (room == null) {
            System.out.println("[HotelFacade] Sorry, no available room of type: " + type);
            return null;
        }
        Reservation reservation = reservationService.createReservation(customer, room, date);
        roomService.markBooked(room);
        notificationService.sendBookingConfirmation(customer, room);
        System.out.println("[HotelFacade] Booking complete.");
        return reservation;
    }

    // ── FACADE METHOD 2: cancelBooking() ─────────────────────────
    public void cancelBooking(String phone, int roomNumber) {
        System.out.println("\n[HotelFacade] ── cancelBooking() ───────────────────────");
        Customer customer = customerService.findByPhone(phone);
        if (customer == null) {
            System.out.println("[HotelFacade] Customer not found for phone: " + phone);
            return;
        }
        Room room = roomService.findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("[HotelFacade] Room #" + roomNumber + " not found.");
            return;
        }
        boolean cancelled = reservationService.cancelReservation(customer, room);
        if (!cancelled) return;
        roomService.markAvailable(room);
        notificationService.sendCancellationNotice(customer, room);
        System.out.println("[HotelFacade] Cancellation complete.");
    }

    // ── FACADE METHOD 3: checkOut() ──────────────────────────────
    public void checkOut(String phone, int roomNumber, int nights) {
        System.out.println("\n[HotelFacade] ── checkOut() ─────────────────────────────");
        Customer customer = customerService.findByPhone(phone);
        if (customer == null) {
            System.out.println("[HotelFacade] Customer not found.");
            return;
        }
        Room room = roomService.findRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("[HotelFacade] Room #" + roomNumber + " not found.");
            return;
        }
        Reservation target = null;
        for (Reservation r : reservationService.getAll()) {
            if (r.getCustomer() == customer && r.getRoom() == room) {
                target = r; break;
            }
        }
        if (target == null) {
            System.out.println("[HotelFacade] No active reservation found.");
            return;
        }
        billingService.generateBill(target, nights);
        roomService.markAvailable(room);
        reservationService.cancelReservation(customer, room);
        double total = billingService.getBasePrice(room.getType()) * nights * 1.10;
        notificationService.sendBillReceipt(customer, total);
        System.out.println("[HotelFacade] Checkout complete.");
    }

    // ── FACADE METHOD 4: printHotelStatus() ─────────────────────
    public void printHotelStatus() {
        System.out.println("\n[HotelFacade] ══ Hotel Status ══════════════════════════");
        roomService.printInventory();
        customerService.printCustomers();
        reservationService.printReservations();
    }
}




class FacadePatternDemo {

    private static final Scanner sc = new Scanner(System.in);
    private static final HotelFacade hotel = new HotelFacade();

    public static void main(String[] args) {
        int choice;
        do {
            printMenu();
            System.out.print("Enter choice: ");
            choice = readInt();
            switch (choice) {
                case 1: addRoom();        break;
                case 2: bookRoom();       break;
                case 3: cancelBooking();  break;
                case 4: checkOut();       break;
                case 5: hotel.printHotelStatus(); break;
                case 0: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
        sc.close();
    }

    // ── menu ─────────────────────────────────────────────────────
    private static void printMenu() {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║     Hotel Facade Pattern Menu    ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Add Room                     ║");
        System.out.println("║  2. Book Room                    ║");
        System.out.println("║  3. Cancel Booking               ║");
        System.out.println("║  4. Check Out                    ║");
        System.out.println("║  5. View Hotel Status            ║");
        System.out.println("║  0. Exit                         ║");
        System.out.println("╚══════════════════════════════════╝");
    }

    // ── option 1: add room ───────────────────────────────────────
    private static void addRoom() {
        System.out.print("Enter room number: ");
        int number = readInt();

        System.out.println("Select room type:");
        RoomType[] types = RoomType.values();
        for (int i = 0; i < types.length; i++)
            System.out.println("  " + (i + 1) + ". " + types[i]);
        System.out.print("Enter choice (1-" + types.length + "): ");
        int t = readInt();
        if (t < 1 || t > types.length) {
            System.out.println("Invalid type selection.");
            return;
        }
        hotel.addRoom(number, types[t - 1]);
    }

    // ── option 2: book room ──────────────────────────────────────
    private static void bookRoom() {
        System.out.print("Enter customer name : ");
        String name = sc.nextLine().trim();
        System.out.print("Enter customer phone: ");
        String phone = sc.nextLine().trim();

        System.out.println("Select room type to book:");
        RoomType[] types = RoomType.values();
        for (int i = 0; i < types.length; i++)
            System.out.println("  " + (i + 1) + ". " + types[i]);
        System.out.print("Enter choice (1-" + types.length + "): ");
        int t = readInt();
        if (t < 1 || t > types.length) {
            System.out.println("Invalid type selection.");
            return;
        }
        hotel.bookRoom(name, phone, types[t - 1], new Date());
    }

    // ── option 3: cancel booking ─────────────────────────────────
    private static void cancelBooking() {
        System.out.print("Enter customer phone : ");
        String phone = sc.nextLine().trim();
        System.out.print("Enter room number    : ");
        int roomNumber = readInt();
        hotel.cancelBooking(phone, roomNumber);
    }

    // ── option 4: check out ──────────────────────────────────────
    private static void checkOut() {
        System.out.print("Enter customer phone : ");
        String phone = sc.nextLine().trim();
        System.out.print("Enter room number    : ");
        int roomNumber = readInt();
        System.out.print("Enter number of nights: ");
        int nights = readInt();
        hotel.checkOut(phone, roomNumber, nights);
    }

    // ── safe int reader ──────────────────────────────────────────
    private static int readInt() {
        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}