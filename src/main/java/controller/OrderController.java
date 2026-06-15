package controller;

import model.Menu;
import model.MenuDAO;
import model.Order;
import model.OrderDetail;
import model.OrderDAO;
import view.MainDashboard;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Controller class to manage the Point of Sale (POS) Transaction & History Tab.
 * Connects OrderDAO and MenuDAO with MainDashboard views.
 */
public class OrderController {
    private MainDashboard dashboard;
    private MenuDAO menuDao;
    private OrderDAO orderDao;
    private List<OrderDetail> cart;
    private double totalHarga;
    private String activeCategory = "All";
    
    private NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(Locale.of("in", "ID"));
    
    public OrderController(MainDashboard dashboard) {
        this.dashboard = dashboard;
        this.menuDao = new MenuDAO();
        this.orderDao = new OrderDAO();
        this.cart = new ArrayList<>();
        this.totalHarga = 0.0;
    }
    
    public void tampilMenu() {
        tampilMenuFiltered();
    }

    public void setActiveCategory(String category) {
        this.activeCategory = category;
        tampilMenuFiltered();
    }
    
    public void tampilMenuFiltered() {
        DefaultTableModel model = (DefaultTableModel) dashboard.getTabelPosMenu().getModel();
        model.setRowCount(0);
        
        String searchKey = dashboard.getTxtSearchMenu().getText().trim().toLowerCase();
        
        try {
            List<Menu> list = menuDao.getAll();
            for (Menu m : list) {
                // Filter by category
                if (!activeCategory.equals("All") && !m.getKategori().equalsIgnoreCase(activeCategory)) {
                    continue;
                }
                
                // Filter by search keyword
                if (!searchKey.isEmpty() && 
                    !m.getNama().toLowerCase().contains(searchKey) && 
                    !m.getId().toLowerCase().contains(searchKey)) {
                    continue;
                }
                
                Object[] row = {
                    m.getId(),
                    m.getNama(),
                    m.getKategori(),
                    m.getHarga()
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dashboard, "Gagal memuat daftar menu transaksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void addToCart() {
        int selectedRow = dashboard.getTabelPosMenu().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(dashboard, "Pilih menu dari tabel menu terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = dashboard.getTabelPosMenu().getValueAt(selectedRow, 0).toString();
        String nama = dashboard.getTabelPosMenu().getValueAt(selectedRow, 1).toString();
        double harga = Double.parseDouble(dashboard.getTabelPosMenu().getValueAt(selectedRow, 3).toString());
        
        // If item exists in cart, increment quantity
        for (OrderDetail detail : cart) {
            if (detail.getMenuId().equals(id)) {
                detail.setQuantity(detail.getQuantity() + 1);
                updateCartTable();
                calculateTotal();
                return;
            }
        }
        
        // Add new detail item to cart
        OrderDetail detail = new OrderDetail(id, nama, harga, 1);
        cart.add(detail);
        updateCartTable();
        calculateTotal();
    }
    
    public void removeFromCart() {
        int selectedRow = dashboard.getTabelCart().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(dashboard, "Pilih baris di tabel keranjang yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cart.remove(selectedRow);
        updateCartTable();
        calculateTotal();
    }
    
    public void updateCartTable() {
        DefaultTableModel model = (DefaultTableModel) dashboard.getTabelCart().getModel();
        model.setRowCount(0);
        for (OrderDetail detail : cart) {
            Object[] row = {
                detail.getMenuId(),
                detail.getMenuNama(),
                detail.getHarga(),
                detail.getQuantity(),
                detail.getSubtotal()
            };
            model.addRow(row);
        }
    }
    
    public void calculateTotal() {
        totalHarga = 0;
        for (OrderDetail detail : cart) {
            totalHarga += detail.getSubtotal();
        }
        dashboard.getLblTotalHarga().setText(rupiahFormat.format(totalHarga));
        calculateChange();
    }
    
    public void calculateChange() {
        String cashStr = dashboard.getTxtCashReceived().getText().trim();
        if (cashStr.isEmpty()) {
            dashboard.getLblChange().setText(rupiahFormat.format(0.0));
            return;
        }
        
        try {
            double cash = Double.parseDouble(cashStr);
            double change = cash - totalHarga;
            dashboard.getLblChange().setText(rupiahFormat.format(change));
        } catch (NumberFormatException e) {
            dashboard.getLblChange().setText("Input Salah");
        }
    }
    
    public void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard, "Keranjang belanja masih kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String customerName = dashboard.getTxtCustName().getText().trim();
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard, "Nama pelanggan harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String cashStr = dashboard.getTxtCashReceived().getText().trim();
        if (cashStr.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard, "Masukkan nominal pembayaran tunai!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double cash;
        try {
            cash = Double.parseDouble(cashStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dashboard, "Nominal pembayaran harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (cash < totalHarga) {
            JOptionPane.showMessageDialog(dashboard, "Uang pembayaran kurang dari total tagihan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Generate Invoice ID TX-yyyyMMdd-HHmmss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String txId = "TX-" + sdf.format(new java.util.Date());
        
        Order order = new Order();
        order.setId(txId);
        order.setTanggal(new Timestamp(System.currentTimeMillis()));
        order.setCustomerName(customerName);
        order.setTotalHarga(totalHarga);
        order.setDetails(cart);
        
        try {
            orderDao.insert(order);
            
            // Format invoice text
            double change = cash - totalHarga;
            StringBuilder invoice = new StringBuilder();
            invoice.append("====================================\n");
            invoice.append("       COFFEE SHOP MANAGEMENT       \n");
            invoice.append("====================================\n");
            invoice.append("ID Transaksi  : ").append(txId).append("\n");
            invoice.append("Nama Pelanggan: ").append(customerName).append("\n");
            invoice.append("Tanggal       : ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(order.getTanggal())).append("\n");
            invoice.append("------------------------------------\n");
            for (OrderDetail d : cart) {
                invoice.append(d.getMenuNama()).append(" (x").append(d.getQuantity()).append(")\n");
                invoice.append("   @ ").append(rupiahFormat.format(d.getHarga())).append(" = ").append(rupiahFormat.format(d.getSubtotal())).append("\n");
            }
            invoice.append("------------------------------------\n");
            invoice.append("TOTAL TAGIHAN : ").append(rupiahFormat.format(totalHarga)).append("\n");
            invoice.append("UANG BAYAR    : ").append(rupiahFormat.format(cash)).append("\n");
            invoice.append("KEMBALIAN     : ").append(rupiahFormat.format(change)).append("\n");
            invoice.append("====================================\n");
            invoice.append("  Terima Kasih, Selamat Menikmati!  \n");
            
            JOptionPane.showMessageDialog(dashboard, invoice.toString(), "Transaksi Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            clearOrder();
            tampilMenu(); // Refresh table menu
            tampilHistory(); // Refresh history tab
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dashboard, "Gagal menyimpan transaksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void clearOrder() {
        cart.clear();
        updateCartTable();
        dashboard.getTxtCustName().setText("");
        dashboard.getTxtCashReceived().setText("");
        totalHarga = 0.0;
        dashboard.getLblTotalHarga().setText(rupiahFormat.format(0.0));
        dashboard.getLblChange().setText(rupiahFormat.format(0.0));
    }

    public void tampilHistory() {
        DefaultTableModel model = (DefaultTableModel) dashboard.getTabelHistory().getModel();
        model.setRowCount(0);
        
        // Clear history details
        DefaultTableModel detailModel = (DefaultTableModel) dashboard.getTabelHistoryDetail().getModel();
        detailModel.setRowCount(0);
        dashboard.getLblHistoryTotal().setText(rupiahFormat.format(0.0));
        
        try {
            List<Order> orders = orderDao.getAll();
            for (Order o : orders) {
                Object[] row = {
                    o.getId(),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(o.getTanggal()),
                    o.getCustomerName(),
                    o.getTotalHarga()
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dashboard, "Gagal memuat riwayat transaksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actionClickHistoryTable() {
        int selectedRow = dashboard.getTabelHistory().getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        String txId = dashboard.getTabelHistory().getValueAt(selectedRow, 0).toString();
        DefaultTableModel detailModel = (DefaultTableModel) dashboard.getTabelHistoryDetail().getModel();
        detailModel.setRowCount(0);
        
        try {
            List<OrderDetail> details = orderDao.getDetails(txId);
            double total = 0.0;
            for (OrderDetail d : details) {
                Object[] row = {
                    d.getMenuNama(),
                    d.getHarga(),
                    d.getQuantity(),
                    d.getSubtotal()
                };
                detailModel.addRow(row);
                total += d.getSubtotal();
            }
            dashboard.getLblHistoryTotal().setText(rupiahFormat.format(total));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dashboard, "Gagal memuat detail transaksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void voidTransaction() {
        int selectedRow = dashboard.getTabelHistory().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(dashboard, "Pilih transaksi dari tabel riwayat yang ingin dibatalkan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String txId = dashboard.getTabelHistory().getValueAt(selectedRow, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(dashboard, 
                "Apakah Anda yakin ingin membatalkan (Void) transaksi " + txId + "?\nTindakan ini bersifat permanen.", 
                "Konfirmasi Void Transaksi", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                orderDao.delete(txId);
                JOptionPane.showMessageDialog(dashboard, "Transaksi " + txId + " berhasil dibatalkan!");
                tampilHistory();
                tampilMenuFiltered();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(dashboard, "Gagal menghapus transaksi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
