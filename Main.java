import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// class reservation
class Reservation {
    // unique id for the reservation
    private int number;          
    private LocalDate startDate;
    private LocalDate endDate;
    private ReserverPayer reserverPayer; 
    private Room allocatedRoom;       

    public Reservation(int number,LocalDate startDate,LocalDate endDate,ReserverPayer reserverPayer,Room allocatedRoom) {
        // making condition if no: in negative so throw exception /using defensive programing approach
        if (number <= 0) {
            throw new IllegalArgumentException("Reservation number must be positive.");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null.");
        }
        this.number = number;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reserverPayer = reserverPayer;
        this.allocatedRoom = allocatedRoom;
    }

    public int getNumber() {
         return number;
     }
    public LocalDate getStartDate() { 
        return startDate;
     }
    public LocalDate getEndDate() { 
        return endDate;
     }
    public ReserverPayer getReserverPayer() { 
       return reserverPayer; 
    }
    public Room getAllocatedRoom() { 
        return allocatedRoom; 
    }
}
//    Class Reserver Payer
class ReserverPayer {
     private Map<Integer, Reservation> myReservations = new HashMap<>();
    public void addReservation(Reservation r) {
        if (r != null) {
        myReservations.put(r.getNumber(), r);
    }
}

    public void removeReservation(int reservationNumber) {
        // Defensive checks here also
        if (reservationNumber <= 0) {
            throw new IllegalArgumentException("reservationNumber must be positive.");
        }
        // remove the reservation from the reservations HashMap
        myReservations.remove(reservationNumber);
    }
}

// Class Room
class Room {

    private int number;
    private Set<Reservation> roomReservations = new HashSet<>();

    public Room(int number) {
        if (number <= 0) {
        throw new IllegalArgumentException("Room number must be positive.");
    }
        this.number = number;
    }

    public int getNumber() {
         return number; 
        }

    public void allocate(Reservation r) {
        if (r != null){
             roomReservations.add(r);
    }
}
    
    public void deallocateRoom(Reservation reservation) {
         if (reservation != null) {
        // remove the reservation allocation deallocate
        roomReservations.remove(reservation);
    }
}
}
// Guest House Class
class GuestHouse {

    // All reservations known to this guest house, by reservation number.
   private Map<Integer, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        if (r != null) {
            reservations.put(r.getNumber(), r);
        }
    }

    public String cancelReservation(int reservationNumber) {
        // Defensive check
        if (reservationNumber <= 0) {
            return "Invalid reservation number!";
        }

        Reservation reservation = reservations.get(reservationNumber);

        if (reservation == null) {
            return "Cancellation failed: Reservation " + reservationNumber + " not found.";
        }

        // Step 1: Remove from reserver payer
        ReserverPayer rp = reservation.getReserverPayer();
        if (rp != null) {
            rp.removeReservation(reservationNumber);
        }

        // Step 2: Deallocate room
        Room room = reservation.getAllocatedRoom();
        if (room != null) {
            room.deallocateRoom(reservation);
        }

        // Step 3: Remove from guest house map
        reservations.remove(reservationNumber);

        // Step 4: Build feedback text
        String feedback = "Reservation "+reservationNumber + " cancelled.";
        if (room != null) {
            feedback += " Room " + room.getNumber() + " deallocated.";
        }
        feedback += " (Dates: " + reservation.getStartDate() + " to " + reservation.getEndDate() + ")";
        return feedback;
    }
}


//Main class to check the output and defensive program handling
class Main {
    public static void main(String[] args) {
        // Creating the guest house object gh 
        GuestHouse gh = new GuestHouse();


        // Creating  a reserver payer rp1 and rp2
        ReserverPayer rp1 = new ReserverPayer();
        ReserverPayer rp2 = new ReserverPayer();

        // Create rooms 804 and 007 
        Room room804 = new Room(804);
        Room room007 = new Room(007);

        // Creating reservations for r1 r2 and r3
        Reservation r1 = new Reservation(1,java.time.LocalDate.of(2025, 1, 10),java.time.LocalDate.of(2025, 1, 12),rp1,room804);

        Reservation r2 = new Reservation(2,java.time.LocalDate.of(2025, 2, 5),java.time.LocalDate.of(2025, 2, 9),rp1,room007);

        Reservation r3 = new Reservation(3,java.time.LocalDate.of(2025, 3, 1),java.time.LocalDate.of(2025, 3,3),rp2,room804);

        // Add reservations into guest house, reserver, and rooms
        gh.addReservation(r1);
        rp1.addReservation(r1);
        room804.allocate(r1);

        gh.addReservation(r2);
        rp1.addReservation(r2);
        room007.allocate(r2);

        gh.addReservation(r3);
        rp2.addReservation(r3);
        room804.allocate(r3);

        // Now cancel them which one you want here i am canclling 1,2,3 one  by one
        System.out.println(gh.cancelReservation(1)); 
        System.out.println(gh.cancelReservation(2)); 
        System.out.println(gh.cancelReservation(3)); 

        // Try canceling a non-existing r4 to show defensive programing handling accordingly
        System.out.println(gh.cancelReservation(4));
    }
}

