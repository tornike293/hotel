package hotel;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseApartmentRepository implements ApartmentRepository {
    private final DataSource dataSource;

    public DatabaseApartmentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS apartments (
                    id INTEGER PRIMARY KEY,
                    price DECIMAL(15,2) NOT NULL,
                    reserved BOOLEAN NOT NULL DEFAULT FALSE,
                    client_name VARCHAR(255)
                )
                """;
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create apartments table", e);
        }
    }

    @Override
    public void save(Apartment apartment) {
        String sql = "INSERT INTO apartments (id, price, reserved, client_name) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, apartment.getId());
            stmt.setBigDecimal(2, apartment.getPrice());
            stmt.setBoolean(3, apartment.getReservationStatus());
            stmt.setString(4, apartment.getClientName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save apartment", e);
        }
    }

    @Override
    public void update(Apartment apartment) {
        String sql = "UPDATE apartments SET reserved = ?, client_name = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, apartment.getReservationStatus());
            stmt.setString(2, apartment.getClientName());
            stmt.setInt(3, apartment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update apartment", e);
        }
    }

    @Override
    public Optional<Apartment> findById(int id) {
        String sql = "SELECT * FROM apartments WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find apartment", e);
        }
    }

    @Override
    public List<Apartment> findAll() {
        String sql = "SELECT * FROM apartments";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<Apartment> result = new ArrayList<>();
            while (rs.next()) result.add(mapRow(rs));
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list apartments", e);
        }
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT COUNT(*) FROM apartments WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check apartment existence", e);
        }
    }

    private Apartment mapRow(ResultSet rs) throws SQLException {
        Apartment a = new Apartment(rs.getInt("id"), rs.getBigDecimal("price"));
        if (rs.getBoolean("reserved")) {
            a.reserve(rs.getString("client_name"));
        }
        return a;
    }
}