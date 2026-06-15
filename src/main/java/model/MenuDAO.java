package model;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for the Menu model.
 * Performs database operations on the 'menu' table.
 */
public class MenuDAO {
    
    public void insert(Menu menu) throws SQLException {
        String sql = "INSERT INTO menu (id, nama, kategori, harga) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, menu.getId());
            ps.setString(2, menu.getNama());
            ps.setString(3, menu.getKategori());
            ps.setDouble(4, menu.getHarga());
            ps.executeUpdate();
        }
    }
    
    public void update(Menu menu) throws SQLException {
        String sql = "UPDATE menu SET nama = ?, kategori = ?, harga = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, menu.getNama());
            ps.setString(2, menu.getKategori());
            ps.setDouble(3, menu.getHarga());
            ps.setString(4, menu.getId());
            ps.executeUpdate();
        }
    }
    
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM menu WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
    
    public Menu getMenu(String id) throws SQLException {
        String sql = "SELECT * FROM menu WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Menu(
                        rs.getString("id"),
                        rs.getString("nama"),
                        rs.getString("kategori"),
                        rs.getDouble("harga")
                    );
                }
            }
        }
        return null;
    }
    
    public List<Menu> getAll() throws SQLException {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM menu ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Menu(
                    rs.getString("id"),
                    rs.getString("nama"),
                    rs.getString("kategori"),
                    rs.getDouble("harga")
                ));
            }
        }
        return list;
    }
}
