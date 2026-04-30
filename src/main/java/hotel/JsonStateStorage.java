package hotel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonStateStorage {
    private final String filePath;

    public JsonStateStorage(String filePath) {
        this.filePath = filePath;
    }

    public void save(List<Apartment> apartments) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        for (int i = 0; i < apartments.size(); i++) {
            Apartment a = apartments.get(i);
            sb.append("  {\n");
            sb.append("    \"id\": ").append(a.getId()).append(",\n");
            sb.append("    \"price\": ").append(a.getPrice()).append(",\n");
            sb.append("    \"reserved\": ").append(a.getReservationStatus()).append(",\n");
            sb.append("    \"client\": ")
                    .append(a.getClientName() != null ? "\"" + a.getClientName() + "\"" : "null")
                    .append("\n");
            sb.append("  }");
            if (i < apartments.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");

        try (Writer w = new FileWriter(filePath)) {
            w.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Warning: could not save state: " + e.getMessage());
        }
    }

    public List<Apartment> load() {
        List<Apartment> result = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return result;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String content = br.lines().reduce("", String::concat);
            int i = 0;
            while ((i = content.indexOf('{', i)) != -1) {
                int end = content.indexOf('}', i);
                if (end == -1) break;
                String block = content.substring(i + 1, end);
                Apartment a = parseBlock(block);
                if (a != null) result.add(a);
                i = end + 1;
            }
        } catch (IOException e) {
            System.out.println("Warning: could not load state: " + e.getMessage());
        }
        return result;
    }

    private Apartment parseBlock(String block) {
        try {
            int id = Integer.parseInt(extractValue(block, "id"));
            double price = Double.parseDouble(extractValue(block, "price"));
            boolean reserved = Boolean.parseBoolean(extractValue(block, "reserved"));
            String clientRaw = extractValue(block, "client");
            String client = clientRaw.equals("null") ? null : clientRaw.replace("\"", "");

            Apartment a = new Apartment(id, price);
            if (reserved && client != null) a.reserve(client);
            return a;
        } catch (Exception e) {
            return null;
        }
    }

    private String extractValue(String block, String key) {
        String search = "\"" + key + "\":";
        int start = block.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        int end = block.indexOf(',', start);
        if (end == -1) end = block.length();
        return block.substring(start, end).trim();
    }
}
