import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static List<Integer> userDate = new ArrayList<>();
    static List<Integer> userStartTime = new ArrayList<>();
    static List<Integer> userHours = new ArrayList<>();
    static List<Integer> userCapacity = new ArrayList<>();
    static String pathName = "C:\\Users\\LENOVO\\Desktop\\java\\Learning\\Hall Booking\\HallBookingData.txt";

    public static void printMenu() {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 2; i++) {
            System.out.println("Enter Date :User " + (i + 1) + ":");
            userDate.add(sc.nextInt());
            System.out.println("Enter Capacity: User" + (i + 1) + ":");
            userCapacity.add(sc.nextInt());
        }

    }

    public static void printMenu(int i) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Date :User " + i + ":");
        userDate.add(sc.nextInt());
        System.out.println("Enter Capacity: User" + i + ":");
        userCapacity.add(sc.nextInt());
    }

    public static void resetDataTxt() {
        Map<Integer, Boolean> reset1 = new HashMap<>();
        Map<Integer, Boolean> reset2 = new HashMap<>();
        Map<Integer, Boolean> reset3 = new HashMap<>();
        // ith day = i*2 +1 ; bool i = i*2 + 2;
        for (int i = 0; i < 31; i++) {
            reset1.put(i, false);
            reset2.put(i, false);
            reset3.put(i, false);
        }
        Halls.fwrite();
        Halls.fwrite("Hall1");
        Halls.fwrite(reset1);
        Halls.fwrite("Hall2");
        Halls.fwrite(reset2);
        Halls.fwrite("Hall3");
        Halls.fwrite(reset3);

    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Reset Data?");
        boolean resetData = sc.nextBoolean();
        if (resetData == true) {
            resetDataTxt();
        }

        Map<Integer, Boolean> bookedHallInMain1 = new HashMap<>();
        Map<Integer, Boolean> bookedHallInMain2 = new HashMap<>();
        Map<Integer, Boolean> bookedHallInMain3 = new HashMap<>();
        // ith day = i*2 +1 ; bool i = i*2 + 2;
        for (int i = 0; i < 31; i++) {
            bookedHallInMain1.put(i, Boolean.parseBoolean(Halls.fread().get((i * 2) + 2)));
            bookedHallInMain2.put(i, Boolean.parseBoolean(Halls.fread().get((i * 2) + 65)));
            bookedHallInMain3.put(i, Boolean.parseBoolean(Halls.fread().get((i * 2) + 128)));
        }
        System.out.println("\t\t\tWelcome to hall booking");
        System.out.println("Current Availability for A1");
        for (int i = 0; i < 31; i++) {
            if (bookedHallInMain1.get(i).equals(false)) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
        System.out.println("Current Availability for A2");
        for (int i = 0; i < 31; i++) {
            if (bookedHallInMain2.get(i).equals(false)) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
        System.out.println("Current Availability for A3");
        for (int i = 0; i < 31; i++) {
            if (bookedHallInMain3.get(i).equals(false)) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
        System.out.println("Do you want to cancel booking made previously?");
        boolean cancelPrevious = sc.nextBoolean();
        if (cancelPrevious == true) {
            System.out.println("Enter Hall Name : ");
            String cancelHallName = sc.next();
            System.out.println("Enter Date booked: ");
            int cancelHallDate = sc.nextInt();
            if (cancelHallName.equals("A1")) {
                bookedHallInMain1.put(cancelHallDate, false);
            } else if (cancelHallName.equals("A2")) {
                bookedHallInMain2.put(cancelHallDate, false);
            } else if (cancelHallName.equals("A3")) {
                bookedHallInMain3.put(cancelHallDate, false);
            }
        }
        System.out.println("Enter Switch preference: ");
        boolean userChoiceSwitchPref = sc.nextBoolean();
        Halls A1 = new Halls("A1", 20, false, bookedHallInMain1);
        Halls A2 = new Halls("A2", 50, userChoiceSwitchPref, bookedHallInMain2);
        Halls A3 = new Halls("A3", 100, false, bookedHallInMain3);

        booking hallBooker = new booking();
        hallBooker.allHalls(A1, A2, A3);
        while (true) {
            printMenu();
            A1.date = userDate.get(0);
            A2.date = userDate.get(1);
            try {
                for (int i = 0; i < 2; i++) {
                    String assignedHall = hallBooker.assignHall(A1, A2, A3, userCapacity.get(i), userDate.get(i));
                    hallBooker.bookHall(hallBooker.match(A1, A2, A3, assignedHall, userDate.get(i)), i + 1);
                }
                break;
            } catch (NullPointerException n) {
                System.out.println("Please choose a different date");
                userDate.clear();
            }
        }

        System.out.println("1.Cancel 2.Continue");
        int userChoice = sc.nextInt();
        switch (userChoice) {
            case 1:
                System.out.println("Enter user Id to cancel booking : ");
                int userChoiceID = sc.nextInt();
                hallBooker.cancelHall(userChoiceID);

            case 2:
                System.out.println("Book another?");
                boolean bookAnother = sc.nextBoolean();
                if (bookAnother) {
                    while (true) {
                        printMenu(3);
                        A3.date = userDate.get(2);
                        try {
                            String assignedHall3 = hallBooker.assignHall(A1, A2, A3, userCapacity.get(2),
                                    userDate.get(2));
                            hallBooker.bookHall(hallBooker.match(A1, A2, A3, assignedHall3, userDate.get(2)), 3);
                            if (((userDate.get(0) == userDate.get(2)) && (A1.bookedHall.get(A1.date) == true))
                                    || ((userDate.get(1) == userDate.get(2)) && (A2.bookedHall.get(A2.date) == true))) {
                                hallBooker.priority(userCapacity.get(2));
                            }
                            break;
                        } catch (NullPointerException n) {
                            System.out.println("Please choose a different date");
                            userDate.remove(2);
                        }
                    }
                }

        }
        Halls.fwrite();
        Halls.fwrite("Hall1");
        Halls.fwrite(A1.bookedHall);
        Halls.fwrite("Hall2");
        Halls.fwrite(A2.bookedHall);
        Halls.fwrite("Hall3");
        Halls.fwrite(A3.bookedHall);

    }

}