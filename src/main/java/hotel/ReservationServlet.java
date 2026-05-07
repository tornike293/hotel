package hotel;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Map;

@WebServlet("/apartments/*")
public class ReservationServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = extractId(req);
        if (id < 0) { sendJson(resp, 400, Map.of("error", "Invalid apartment id")); return; }

        Map<?, ?> body = gson.fromJson(req.getReader(), Map.class);
        if (body == null || !body.containsKey("clientName")) {
            sendJson(resp, 400, Map.of("error", "clientName is required"));
            return;
        }
        try {
            Apartment a = ServiceLocator.getService().reserve(id, (String) body.get("clientName"));
            sendJson(resp, 200, a);
        } catch (IllegalArgumentException e) {
            sendJson(resp, 404, Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            sendJson(resp, 409, Map.of("error", e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = extractId(req);
        if (id < 0) { sendJson(resp, 400, Map.of("error", "Invalid apartment id")); return; }

        try {
            Apartment a = ServiceLocator.getService().release(id);
            sendJson(resp, 200, a);
        } catch (IllegalArgumentException e) {
            sendJson(resp, 404, Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            sendJson(resp, 409, Map.of("error", e.getMessage()));
        }
    }

    private int extractId(HttpServletRequest req) {
        try {
            String pathInfo = req.getPathInfo();
            return Integer.parseInt(pathInfo.split("/")[1]);
        } catch (Exception e) { return -1; }
    }

    private void sendJson(HttpServletResponse resp, int status, Object body) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        gson.toJson(body, resp.getWriter());
    }
}