package hotel;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;



public class ApartmentsServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int size = parseIntOrDefault(req.getParameter("size"), 10);
        String sortBy = req.getParameter("sortBy") != null ? req.getParameter("sortBy") : "id";

        List<Apartment> apartments = ServiceLocator.getService().list(size, sortBy);
        sendJson(resp, 200, apartments);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<?, ?> body = gson.fromJson(req.getReader(), Map.class);
        if (body == null || !body.containsKey("id") || !body.containsKey("price")) {
            sendJson(resp, 400, Map.of("error", "id and price are required"));
            return;
        }

        int id = ((Number) body.get("id")).intValue();
        double price = ((Number) body.get("price")).doubleValue();

        try {
            Apartment a = ServiceLocator.getService().register(id, price);
            sendJson(resp, 201, a);
        } catch (IllegalArgumentException e) {
            sendJson(resp, 400, Map.of("error", e.getMessage()));
        }
    }

    private void sendJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        gson.toJson(body, resp.getWriter());
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try { return Integer.parseInt(value); }
        catch (Exception e) { return defaultValue; }
    }
}