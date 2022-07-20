import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Halls {
    String hallName;
    int date, capacity, startTime, hours;
    boolean switchPref;
    int userID = 0;
    int userCapacity;
    static Halls none;
    Map<Integer, Boolean> bookedHall = new HashMap<>();
    static String pathName = "C:\\Users\\LENOVO\\Desktop\\java\\Learning\\Hall Booking\\HallBookingData.txt";

    public Halls(String hallName, int capacity, boolean switchPref, Map<Integer, Boolean> bookedHall) {
        this.hallName = hallName;
        this.capacity = capacity;
        this.switchPref = switchPref;
        // booked = false;
        int userId;
        int userCapacity;
        int date, startTime, hours;
        this.bookedHall = bookedHall;

    }

    public static void fwrite() {
        try {
            FileWriter newFile = new FileWriter(pathName);
            newFile.close();
            // System.out.println("File has been written");
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public static void fwrite(String writeData) {
        try {
            FileWriter newFile = new FileWriter(pathName, true);
            newFile.write(writeData + "\n");
            newFile.close();
            // System.out.println("File has been written");
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public static void fwrite(Map<Integer, Boolean> writeData) {
        try {
            FileWriter newFile = new FileWriter(pathName, true);
            for (Integer name : writeData.keySet()) {
                int key = name;
                Boolean value = (writeData.get(name));
                newFile.write(key + " " + value + " ");
            }
            newFile.write("\n");
            newFile.close();
            // System.out.println("File has been written");
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public static List<String> fread() throws IOException {
        File readFile = new File(pathName);
        // FileReader fr = new FileReader(readFile);
        // BufferedReader br = new BufferedReader(fr);
        Scanner sc = new Scanner(readFile);
        List<String> readData = new ArrayList<String>();
        String s[] = new String[10];
        while (sc.hasNextLine()) {
            s = sc.nextLine().split(" ");
            for (String i : s) {
                readData.add(i);
            }
        }
        return readData;
    }

}
