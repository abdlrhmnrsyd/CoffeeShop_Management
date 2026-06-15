package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to manage MySQL database connection.
 * It automatically initializes the database and required tables if they don't exist.
 */
public class DatabaseConnection {
    private static final String URL_SERVER = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "coffeeshop_db";
    private static final String URL_DB = URL_SERVER + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static final String SQLITE_URL = "jdbc:sqlite:coffeeshop_db.db";
    private static boolean useSQLite = false;
    private static Connection conn = null;

    static {
        // Try MySQL first
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initializeDatabaseMySQL();
        } catch (Exception e) {
            System.err.println("MySQL initialization failed, falling back to SQLite: " + e.getMessage());
            setupSQLiteFallback();
        }
    }

    private static void initializeDatabaseMySQL() throws SQLException {
        // 1. Connect to MySQL server (without DB name) to create database if it doesn't exist
        try (Connection serverConn = DriverManager.getConnection(URL_SERVER + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", USER, PASSWORD);
             Statement stmt = serverConn.createStatement()) {
            
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database check completed: " + DB_NAME);
        }

        // 2. Connect to the specific database to create required tables
        try (Connection dbConn = DriverManager.getConnection(URL_DB, USER, PASSWORD)) {
            createTables(dbConn, false);
            System.out.println("MySQL database tables check completed successfully.");
        }
    }

    private static void setupSQLiteFallback() {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection dbConn = DriverManager.getConnection(SQLITE_URL)) {
                createTables(dbConn, true);
                useSQLite = true;
                System.out.println("SQLite database tables check completed successfully.");
            }
        } catch (Exception ex) {
            System.err.println("SQLite initialization failed as well: " + ex.getMessage());
        }
    }

    private static void createTables(Connection connection, boolean isSQLite) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // Create menu table
            String createMenuTable = "CREATE TABLE IF NOT EXISTS menu ("
                    + "id VARCHAR(10) PRIMARY KEY,"
                    + "nama VARCHAR(100) NOT NULL,"
                    + "kategori VARCHAR(50) NOT NULL,"
                    + "harga DOUBLE NOT NULL"
                    + ")";
            stmt.executeUpdate(createMenuTable);

            // Create orders table
            String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders ("
                    + "id VARCHAR(30) PRIMARY KEY,"
                    + "tanggal DATETIME NOT NULL,"
                    + "customer_name VARCHAR(100) NOT NULL,"
                    + "total_harga DOUBLE NOT NULL"
                    + ")";
            stmt.executeUpdate(createOrdersTable);

            // Create order_detail table
            String sqlDetail;
            if (isSQLite) {
                sqlDetail = "CREATE TABLE IF NOT EXISTS order_detail ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "order_id VARCHAR(30) NOT NULL,"
                        + "menu_id VARCHAR(10) NOT NULL,"
                        + "quantity INT NOT NULL,"
                        + "subtotal DOUBLE NOT NULL,"
                        + "FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,"
                        + "FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE"
                        + ")";
            } else {
                sqlDetail = "CREATE TABLE IF NOT EXISTS order_detail ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY,"
                        + "order_id VARCHAR(30) NOT NULL,"
                        + "menu_id VARCHAR(10) NOT NULL,"
                        + "quantity INT NOT NULL,"
                        + "subtotal DOUBLE NOT NULL,"
                        + "FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,"
                        + "FOREIGN KEY (menu_id) REFERENCES menu(id) ON DELETE CASCADE"
                        + ")";
            }
            stmt.executeUpdate(sqlDetail);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (useSQLite) {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(SQLITE_URL);
            }
            return conn;
        } else {
            try {
                if (conn == null || conn.isClosed()) {
                    conn = DriverManager.getConnection(URL_DB, USER, PASSWORD);
                }
                return conn;
            } catch (SQLException e) {
                // If MySQL was running but got disconnected or crashed, try SQLite fallback
                System.err.println("MySQL connection lost, trying SQLite fallback: " + e.getMessage());
                useSQLite = true;
                setupSQLiteFallback();
                if (conn == null || conn.isClosed()) {
                    conn = DriverManager.getConnection(SQLITE_URL);
                }
                return conn;
            }
        }
    }
}
