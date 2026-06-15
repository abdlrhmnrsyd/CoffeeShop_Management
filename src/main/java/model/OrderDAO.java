package model;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for the Order model.
 * Performs database transactions for order headers and details.
 */
public class OrderDAO {
    
    public void insert(Order order) throws SQLException {
        String insertOrderSql = "INSERT INTO orders (id, tanggal, customer_name, total_harga) VALUES (?, ?, ?, ?)";
        String insertDetailSql = "INSERT INTO order_detail (order_id, menu_id, quantity, subtotal) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // 1. Insert order header
            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql)) {
                psOrder.setString(1, order.getId());
                psOrder.setTimestamp(2, order.getTanggal());
                psOrder.setString(3, order.getCustomerName());
                psOrder.setDouble(4, order.getTotalHarga());
                psOrder.executeUpdate();
            }
            
            // 2. Insert order details
            try (PreparedStatement psDetail = conn.prepareStatement(insertDetailSql)) {
                for (OrderDetail detail : order.getDetails()) {
                    psDetail.setString(1, order.getId());
                    psDetail.setString(2, detail.getMenuId());
                    psDetail.setInt(3, detail.getQuantity());
                    psDetail.setDouble(4, detail.getSubtotal());
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }
            
            conn.commit(); // Commit transaction
            System.out.println("Order transaction saved successfully: " + order.getId());
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback if error
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset to default
                } catch (SQLException ex) {
                    System.err.println("Reset auto commit failed: " + ex.getMessage());
                }
            }
        }
    }

    public List<Order> getAll() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY tanggal DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Order(
                    rs.getString("id"),
                    rs.getTimestamp("tanggal"),
                    rs.getString("customer_name"),
                    rs.getDouble("total_harga")
                ));
            }
        }
        return list;
    }

    public List<OrderDetail> getDetails(String orderId) throws SQLException {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT od.*, m.nama AS menu_nama, m.harga AS menu_harga " +
                     "FROM order_detail od JOIN menu m ON od.menu_id = m.id " +
                     "WHERE od.order_id = ? ORDER BY od.id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail d = new OrderDetail();
                    d.setId(rs.getInt("id"));
                    d.setOrderId(rs.getString("order_id"));
                    d.setMenuId(rs.getString("menu_id"));
                    d.setMenuNama(rs.getString("menu_nama"));
                    d.setHarga(rs.getDouble("menu_harga"));
                    d.setQuantity(rs.getInt("quantity"));
                    d.setSubtotal(rs.getDouble("subtotal"));
                    list.add(d);
                }
            }
        }
        return list;
    }

    public void delete(String orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            ps.executeUpdate();
        }
    }
}
