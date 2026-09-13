import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    double price;
    boolean booked;

    Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.booked = false;
    }
}

class Reservation {
    int reservationId;
    String guestName;
    int roomNumber;
    String category;
    LocalDate checkIn;
    LocalDate checkOut;
    double totalCost;
    String status;
    boolean paid;

    Reservation(int reservationId, String guestName, int roomNumber, String category,
                LocalDate checkIn, LocalDate checkOut, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalCost = totalCost;
        this.status = "CONFIRMED";
        this.paid = false;
    }

    long getNights() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    String toCsv() {
        return reservationId + "," + guestName + "," + roomNumber + "," + category + ","
                + checkIn + "," + checkOut + "," + totalCost + "," + status + "," + paid;
    }

    static Reservation fromCsv(String line) {
        String[] p = line.split(",");
        Reservation r = new Reservation(
                Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]), p[3],
                LocalDate.parse(p[4]), LocalDate.parse(p[5]), Double.parseDouble(p[6])
        );
        r.status = p[7];
        r.paid = Boolean.parseBoolean(p[8]);
        return r;
    }
}

class PaymentSimulator {
    static Random random = new Random();

    static boolean processPayment(String guestName, double amount, String cardLast4) {
        System.out.println("\n--- Processing Payment ---");
        System.out.println("Guest       : " + guestName);
        System.out.println("Amount      : $" + String.format("%.2f", amount));
        System.out.println("Card ending : **** " + cardLast4);

        try {
            Thread.sleep(400);
        } catch (InterruptedException ignored) {
        }

        boolean success = random.nextInt(100) < 90;

        if (success) {
            String txnId = "TXN" + (100000 + random.nextInt(900000));
            System.out.println("Payment SUCCESSFUL. Transaction ID: " + txnId);
        } else {
            System.out.println("Payment FAILED. Please try again.");
        }

        return success;
    }
}

public class HotelReservationSystem {

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Reservation> reservations = new ArrayList<>();
    static int nextReservationId = 1;
    static Scanner sc = new Scanner(System.in);
    static final String DATA_FILE = "reservations.txt";

    public static void main(String[] args) {

        // Adding rooms
        rooms.add(new Room(101, "Standard", 100));
        rooms.add(new Room(102, "Standard", 100));
        rooms.add(new Room(103, "Standard", 110));
        rooms.add(new Room(201, "Deluxe", 180));
        rooms.add(new Room(202, "Deluxe", 190));
        rooms.add(new Room(301, "Suite", 300));
        rooms.add(new Room(302, "Suite", 320));

        loadReservations();

        int choice;

        System.out.println("=================================================");
        System.out.println("      HORIZON TECHX - HOTEL RESERVATION SYSTEM");
        System.out.println("=================================================");

        do {
            System.out.println("\n1. View Available Rooms");
            System.out.println("2. Search Room by Category");
            System.out.println("3. Book Room");
            System.out.println("4. Cancel Reservation");
            System.out.println("5. View All Reservations");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = readInt();

            switch (choice) {

                case 1:
                    viewAvailableRooms();
                    break;

                case 2:
                    searchRoom();
                    break;

                case 3:
                    bookRoom();
                    break;

                case 4:
                    cancelReservation();
                    break;

                case 5:
                    viewAllReservations();
                    break;

                case 6:
                    saveReservations();
                    System.out.println("Thank you for using Hotel Reservation System!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 6);

        sc.close();
    }

    // Display available rooms
    static void viewAvailableRooms() {

        System.out.println("\n--- Available Rooms ---");

        boolean found = false;

        for (Room room : rooms) {

            if (!room.booked) {
                System.out.println(
                    "Room No: " + room.roomNumber +
                    " | Category: " + room.category +
                    " | Price: $" + room.price + "/night"
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available.");
        }
    }

    // Search room by category
    static void searchRoom() {

        System.out.print("Enter room category (Standard/Deluxe/Suite): ");
        String category = sc.nextLine();

        boolean found = false;

        System.out.println("\n--- Search Results ---");

        for (Room room : rooms) {

            if (room.category.equalsIgnoreCase(category) && !room.booked) {

                System.out.println(
                    "Room No: " + room.roomNumber +
                    " | Category: " + room.category +
                    " | Price: $" + room.price + "/night"
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No available room found.");
        }
    }

    // Book room with payment simulation
    static void bookRoom() {

        System.out.print("Enter room number to book: ");
        int roomNumber = readInt();

        for (Room room : rooms) {

            if (room.roomNumber == roomNumber) {

                if (room.booked) {
                    System.out.println("Room is already booked.");
                    return;
                }

                System.out.print("Enter guest name: ");
                String name = sc.nextLine();

                LocalDate checkIn = readDate("Enter check-in date (YYYY-MM-DD): ");
                LocalDate checkOut = readDate("Enter check-out date (YYYY-MM-DD): ");

                if (!checkOut.isAfter(checkIn)) {
                    System.out.println("Check-out must be after check-in. Booking cancelled.");
                    return;
                }

                long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
                double total = room.price * nights;

                System.out.println("\n--- Booking Summary ---");
                System.out.println("Room No   : " + room.roomNumber);
                System.out.println("Category  : " + room.category);
                System.out.println("Nights    : " + nights);
                System.out.println("Total     : $" + total);

                System.out.print("Enter last 4 digits of card to pay: ");
                String card = sc.nextLine();

                boolean paid = PaymentSimulator.processPayment(name, total, card);

                if (!paid) {
                    System.out.println("Booking not confirmed. Payment failed.");
                    return;
                }

                Reservation res = new Reservation(
                        nextReservationId++, name, room.roomNumber,
                        room.category, checkIn, checkOut, total
                );
                res.paid = true;
                reservations.add(res);
                room.booked = true;
                saveReservations();

                System.out.println("\n--- Booking Successful ---");
                System.out.println("Reservation ID: " + res.reservationId);

                return;
            }
        }

        System.out.println("Room not found.");
    }

    // Cancel reservation
    static void cancelReservation() {

        System.out.print("Enter reservation ID to cancel: ");
        int id = readInt();

        for (Reservation res : reservations) {

            if (res.reservationId == id) {

                if (res.status.equals("CANCELLED")) {
                    System.out.println("This reservation is already cancelled.");
                    return;
                }

                res.status = "CANCELLED";

                for (Room room : rooms) {
                    if (room.roomNumber == res.roomNumber) {
                        room.booked = false;
                        break;
                    }
                }

                saveReservations();
                System.out.println("Reservation cancelled successfully.");
                return;
            }
        }

        System.out.println("Reservation ID not found.");
    }

    // View all reservations
    static void viewAllReservations() {

        System.out.println("\n--- All Reservations ---");

        if (reservations.isEmpty()) {
            System.out.println("No reservations yet.");
            return;
        }

        for (Reservation res : reservations) {
            System.out.println(
                "ID: " + res.reservationId +
                " | Guest: " + res.guestName +
                " | Room: " + res.roomNumber +
                " | " + res.checkIn + " to " + res.checkOut +
                " | Total: $" + res.totalCost +
                " | Status: " + res.status +
                " | Paid: " + res.paid
            );
        }
    }

    // Save reservations to file
    static void saveReservations() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Reservation res : reservations) {
                pw.println(res.toCsv());
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save reservations.");
        }
    }

    // Load reservations from file
    static void loadReservations() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                Reservation res = Reservation.fromCsv(line);
                reservations.add(res);

                if (res.reservationId >= nextReservationId) {
                    nextReservationId = res.reservationId + 1;
                }

                if (res.status.equals("CONFIRMED")) {
                    for (Room room : rooms) {
                        if (room.roomNumber == res.roomNumber) {
                            room.booked = true;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load reservations.");
        }
    }

    // Safe integer input
    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid number: ");
            sc.next();
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    // Safe date input
    static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Use YYYY-MM-DD.");
            }
        }
    }
}