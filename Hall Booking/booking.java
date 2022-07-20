import java.util.*;

public class booking {

    static Queue<String> availableHalls = new LinkedList<>();

    public void hallAvailability(Halls hall1, Halls hall2, Halls hall3, int userCapacity, int day) {
        if (hall1.capacity >= userCapacity && hall1.bookedHall.get(day) == false) {
            availableHalls.add(hall1.hallName);
        } else if (hall2.capacity >= userCapacity && hall2.bookedHall.get(day) == false) {
            availableHalls.add(hall2.hallName);
        } else if (hall3.capacity >= userCapacity && hall3.bookedHall.get(day) == false) {
            availableHalls.add(hall3.hallName);
        } else {
            System.out.println("Sorry, No Halls are Available at the chosen Date.");
        }
    }

    static Map<Integer, Halls> waitList = new HashMap<>();

    public String assignHall(Halls A1, Halls A2, Halls A3, int userCapacity, int day) {
        hallAvailability(A1, A2, A3, userCapacity, day);
        String hallAssigned = availableHalls.poll();

        if (hallAssigned.equals(A1.hallName)) {
            A1.bookedHall.put(day, true);
            A1.userCapacity = userCapacity;
            if (A1.switchPref == true) {
                waitList.put(userCapacity, A1);
            }

            return A1.hallName;
        } else if (hallAssigned.equals(A2.hallName)) {
            A2.bookedHall.put(day, true);
            A2.userCapacity = userCapacity;
            if (A2.switchPref == true) {
                waitList.put(userCapacity, A2);
            }
            return A2.hallName;
        } else if (hallAssigned.equals(A3.hallName)) {
            A3.bookedHall.put(day, true);
            A3.userCapacity = userCapacity;
            if (A3.switchPref == true) {
                waitList.put(userCapacity, A3);
            }
            return A3.hallName;
        }
        return "No halls found";

    }

    public Halls match(Halls A1, Halls A2, Halls A3, String hallNam, int day) {
        if (hallNam.equals(A1.hallName)) {
            A1.bookedHall.put(day, true);
            return A1;
        } else if (hallNam.equals(A2.hallName)) {
            A2.bookedHall.put(day, true);
            return A2;
        } else if (hallNam.equals(A3.hallName)) {
            A3.bookedHall.put(day, true);
            return A3;
        }
        return Halls.none;
    }

    static Map<Integer, Halls> hMap = new HashMap<>();
    static List<String> bookedHalls = new ArrayList<>();
    static List<Halls> filledHalls = new ArrayList<>();

    public void bookHall(Halls halls, int userId) {
        halls.userID = userId;
        hMap.put(halls.userID, halls);
        String bookingDetail = halls.hallName + ":" + userId;
        bookedHalls.add(bookingDetail);
        filledHalls.add(halls);
        halls.date = Main.userDate.get(userId - 1);
        halls.bookedHall.put(halls.date, true);
        System.out.println("Hall successfully Booked!");
        System.out.println("Booking Details Hall & User ID :" + bookingDetail);
        System.out.println("Booked Map");
        printBookedHall(userId);
    }

    public Halls waitingList(int hallCapacity) {
        int waitCapacity = 200;
        int flag = 0;
        for (Integer i : waitList.keySet()) {
            if (waitCapacity > i && i <= hallCapacity) {
                waitCapacity = i;
                flag = 1;
            }
        }
        if (flag == 1) {
            Halls prefHall = waitList.get(waitCapacity);
            if (prefHall.switchPref == true) {
                return prefHall;
            } else {
                waitList.remove(waitCapacity);
                waitingList(hallCapacity);
            }
        } else {
            return null;
        }
        return null;
    }

    static List<Halls> allHallsList = new ArrayList<>();

    public void allHalls(Halls h1, Halls h2, Halls h3) {
        allHallsList.add(h1);
        allHallsList.add(h2);
        allHallsList.add(h3);
    }

    static List<Halls> missingHallsList = new ArrayList<>();

    public Halls missingHall(Halls h1, Halls h2) {
        missingHallsList.add(allHallsList.get(0));
        missingHallsList.add(allHallsList.get(1));
        missingHallsList.add(allHallsList.get(2));
        // System.out.println("Missing Hall List " + missingHallsList);
        missingHallsList.remove(h1);
        missingHallsList.remove(h2);
        return missingHallsList.get(0);
    }

    static int switched = 0;

    public void switchBooking(Halls halls, Halls cancelledHall, int flag) {
        if (flag == 0) {
            cancelHall(halls.userID);
            bookHall(cancelledHall, halls.userID);
            System.out.println("Since you have opted to switch, Your Hall has been switched.");
            switched += 1;
        } else if (flag == 1) {
            cancelHall(cancelledHall.userID);
            bookHall(allHallsList.get(0), cancelledHall.userID);
            cancelHall(halls.userID);
            bookHall(allHallsList.get(1), halls.userID);
            System.out.println(
                    "Even though you didn't opt for switch, Your Hall has been forcefully switched due to priority.");
        }
    }

    public void cancelHall(int userId) {

        Halls h = hMap.get(userId);
        hMap.remove(Integer.valueOf(userId));
        bookedHalls.remove(h.hallName);
        filledHalls.remove(h);
        h.bookedHall.put(h.date, false);
        // if waitingList? book that hall(h,userId)
        if (!waitList.isEmpty()) {
            try {
                Halls newHall = waitingList(h.capacity);
                if (newHall.switchPref == true) {
                    switchBooking(newHall, h, 0);
                }
            } catch (NullPointerException e) {
            }
        }
        System.out.println("Bookings after Cancelling");
        printBookedHall(userId);
    }

    public int checkPriority(Halls h, int userCapacity) {
        float hallCap = h.capacity;
        int efficiency = Math.round((userCapacity / hallCap) * 100);
        return efficiency;
    }

    public void priority(int userCapacity) {
        Halls h1 = filledHalls.get(0);
        Halls h2 = filledHalls.get(1);
        Halls missing = missingHall(h1, h2);
        if (h1.capacity >= userCapacity && switched == 0) {
            // System.out.println("In priority method");
            int h1Efficiency = checkPriority(h1, h1.userCapacity);
            int h2Efficiency = checkPriority(h2, h2.userCapacity);
            int missingH1 = checkPriority(missing, h1.userCapacity);
            int missingH2 = checkPriority(missing, h2.userCapacity);

            // System.out.println("In priority method after checking");
            // System.out.println("H2 = " + h2Efficiency);
            // System.out.println("H1 = " + h1Efficiency);
            // System.out.println("missing H2 = " + missingH2);
            // System.out.println("missing H1 = " + missingH1);
            if (h2Efficiency <= missingH1 && h1Efficiency <= missingH2) {
                switchBooking(h2, h1, 1);
            }
            // if (h2Efficiency < h1Efficiency || h2Efficiency - h1Efficiency <= 10) {
            // switchBooking(h2, h1, 1);
            // }
        }
    }

    public void printBookedHall(int userID) {
        int i = 0;
        for (Integer name : hMap.keySet()) {
            int key = name;
            String value = (hMap.get(name)).hallName;
            System.out.println("ID = " + key + "> Hall = " + value + "> Date " + Main.userDate.get(i));
            i++;

        }

    }

}
