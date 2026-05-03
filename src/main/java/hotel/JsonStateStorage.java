package hotel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonStateStorage {
    private final String filePath;
    private final Gson gson = new Gson();

    public JsonStateStorage(String filePath) {
        this.filePath = filePath;
    }

    public void save(List<Apartment> apartments) {
        try (Writer w = new FileWriter(filePath)) {
            gson.toJson(apartments, w);
        } catch (IOException e) {
            System.out.println("Warning: could not save state: " + e.getMessage());
        }
    }

    public List<Apartment> load() {
        File file = new File(filePath);
        if (!file.exists()) return new ArrayList<>();

        try (Reader r = new FileReader(file)) {
            Type listType = new TypeToken<List<Apartment>>() {}.getType();
            List<Apartment> result = gson.fromJson(r, listType);
            return result != null ? result : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Warning: could not load state: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}