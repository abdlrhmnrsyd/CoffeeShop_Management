package controller;

import model.Menu;
import model.MenuDAO;
import view.MainDashboard;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Controller class to manage the Coffee Shop Menu Management Tab.
 * Connects MenuDAO with MainDashboard UI elements.
 */
public class MenuController {
    private MainDashboard dashboard;
    private MenuDAO menuDao;
    
    public MenuController(MainDashboard dashboard) {
        this.dashboard = dashboard;
        this.menuDao = new MenuDAO();
    }
    
    public void clearForm() {
        dashboard.getTxtMenuId().setText("");
        dashboard.getTxtMenuNama().setText("");
        dashboard.getCbMenuKategori().setSelectedIndex(0);
        dashboard.getTxtMenuHarga().setText("");
        dashboard.getTabelMenu().clearSelection();
        dashboard.getTxtMenuId().setEditable(true);
    }
    
    public void tampil() {
        DefaultTableModel model = (DefaultTableModel) dashboard.getTabelMenu().getModel();
        model.setRowCount(0);
        try {
            List<Menu> list = menuDao.getAll();
            for (Menu m : list) {
                Object[] row = {
                    m.getId(),
                    m.getNama(),
                    m.getKategori(),
                    m.getHarga()
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dashboard, "Gagal memuat data menu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void save() {
        String id = dashboard.getTxtMenuId().getText().trim();
        String nama = dashboard.getTxtMenuNama().getText().trim();
        String kategori = (String) dashboard.getCbMenuKategori().getSelectedItem();
        String hargaStr = dashboard.getTxtMenuHarga().getText().trim();
        
        if (id.isEmpty() || nama.isEmpty() || hargaStr.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard, "Semua input harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double harga;
        try {
            harga = Double.parseDouble(hargaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dashboard, "Harga harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            if (menuDao.getMenu(id) != null) {
                JOptionPane.showMessageDialog(dashboard, "Kode Menu " + id + " sudah terdaftar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Menu menu = new Menu(id, nama, kategori, harga);
            menuDao.insert(menu);
            JOptionPane.showMessageDialog(dashboard, "Menu berhasil ditambahkan!");
            tampil();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dashboard, "Gagal menambahkan menu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void update() {
        String id = dashboard.getTxtMenuId().getText().trim();
        String nama = dashboard.getTxtMenuNama().getText().trim();
        String kategori = (String) dashboard.getCbMenuKategori().getSelectedItem();
        String hargaStr = dashboard.getTxtMenuHarga().getText().trim();
        
        if (id.isEmpty() || nama.isEmpty() || hargaStr.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard, "Pilih data menu dari tabel terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double harga;
        try {
            harga = Double.parseDouble(hargaStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(dashboard, "Harga harus berupa angka!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Menu menu = new Menu(id, nama, kategori, harga);
            menuDao.update(menu);
            JOptionPane.showMessageDialog(dashboard, "Menu berhasil diperbarui!");
            tampil();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(dashboard, "Gagal memperbarui menu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void delete() {
        int row = dashboard.getTabelMenu().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(dashboard, "Pilih menu dari tabel yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = dashboard.getTabelMenu().getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(dashboard, "Apakah Anda yakin ingin menghapus menu " + id + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                menuDao.delete(id);
                JOptionPane.showMessageDialog(dashboard, "Menu berhasil dihapus!");
                tampil();
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(dashboard, "Gagal menghapus menu: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void actionClickTable() {
        int row = dashboard.getTabelMenu().getSelectedRow();
        if (row != -1) {
            String id = dashboard.getTabelMenu().getValueAt(row, 0).toString();
            String nama = dashboard.getTabelMenu().getValueAt(row, 1).toString();
            String kategori = dashboard.getTabelMenu().getValueAt(row, 2).toString();
            String harga = dashboard.getTabelMenu().getValueAt(row, 3).toString();
            
            dashboard.getTxtMenuId().setText(id);
            dashboard.getTxtMenuId().setEditable(false);
            dashboard.getTxtMenuNama().setText(nama);
            dashboard.getCbMenuKategori().setSelectedItem(kategori);
            dashboard.getTxtMenuHarga().setText(harga);
        }
    }
}
