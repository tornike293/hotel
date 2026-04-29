package hotel;

import java.util.List;
import java.util.Scanner;

public class HotelUI {
    private final ApartmentService service;
    private final Scanner scanner;

    public HotelUI(ApartmentService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Commands:");
        System.out.println("  apartment register <id> <price>");
        System.out.println("  apartment list [size] [id|price|status|client]");
        System.out.println("  order open <id> <clientName>");
        System.out.println("  order close <id>");
        System.out.println("  exit");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            if (line.equals("exit")) break;

            try {
                String[] t = line.split("\\s+");

                if (t[0].equals("apartment") && t[1].equals("register")) {
                    if (t.length < 4) { System.out.println("Usage: apartment register <id> <price>"); continue; }
                    Apartment a = service.register(Integer.parseInt(t[2]), Double.parseDouble(t[3]));
                    System.out.println("Registered: " + a);

                } else if (t[0].equals("apartment") && t[1].equals("list")) {
                    int size    = t.length > 2 ? Integer.parseInt(t[2]) : 10;
                    String sort = t.length > 3 ? t[3] : "id";
                    List<Apartment> list = service.list(size, sort);
                    if (list.isEmpty()) System.out.println("No apartments.");
                    else list.forEach(System.out::println);

                } else if (t[0].equals("order") && t[1].equals("open")) {
                    if (t.length < 4) { System.out.println("Usage: order open <id> <clientName>"); continue; }
                    Apartment a = service.reserve(Integer.parseInt(t[2]), t[3]);
                    System.out.println("Reserved: " + a);

                } else if (t[0].equals("order") && t[1].equals("close")) {
                    if (t.length < 3) { System.out.println("Usage: order close <id>"); continue; }
                    Apartment a = service.release(Integer.parseInt(t[2]));
                    System.out.println("Released: " + a);

                } else {
                    System.out.println("Unknown command.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
