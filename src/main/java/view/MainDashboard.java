package view;

import controller.MenuController;
import controller.OrderController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Main application dashboard containing POS Transaksi, Riwayat Transaksi, and Kelola Menu.
 * Fully compatible with the NetBeans Matisse GUI editor design.
 */
public class MainDashboard extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    private MenuController menuController;
    private OrderController orderController;

    public MainDashboard() {
        initComponents();
        setupCustomStyle();
        pack();
        setLocationRelativeTo(null);
        
        // Initialize controllers
        menuController = new MenuController(this);
        orderController = new OrderController(this);

        // Load initial data
        menuController.tampil();
        orderController.tampilMenu();
        orderController.clearOrder();
        orderController.tampilHistory();
    }

    private void setupCustomStyle() {
        // Adjust panel preferred sizes to ensure all components fit when packed
        java.awt.Dimension preferredTabSize = new java.awt.Dimension(908, 504);
        panelHistoryTab.setPreferredSize(preferredTabSize);
        panelMenuTab.setPreferredSize(preferredTabSize);
        panelPosTab.setPreferredSize(preferredTabSize);

        // 1. Populate Category ComboBox with standard items (previously it was empty)
        cbMenuKategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
            "Coffee", "Non-Coffee", "Food", "Dessert"
        }));

        // 2. Add placeholder texts and clear buttons for inputs
        txtSearchMenu.putClientProperty("JTextField.placeholderText", "Cari menu berdasarkan nama atau kode...");
        txtSearchMenu.putClientProperty("JTextField.showClearButton", true);
        
        txtCustName.putClientProperty("JTextField.placeholderText", "Masukkan nama pelanggan...");
        txtCustName.putClientProperty("JTextField.showClearButton", true);
        
        txtCashReceived.putClientProperty("JTextField.placeholderText", "Contoh: 50000...");
        txtCashReceived.putClientProperty("JTextField.showClearButton", true);
        
        txtMenuId.putClientProperty("JTextField.placeholderText", "ID Menu (contoh: MN01)...");
        txtMenuNama.putClientProperty("JTextField.placeholderText", "Nama menu makanan atau minuman...");
        txtMenuHarga.putClientProperty("JTextField.placeholderText", "Harga menu (contoh: 25000)...");

        // 3. Style tables (row heights, headers, grid lines)
        java.util.List<javax.swing.JTable> tables = java.util.List.of(
            tabelHistory, tabelHistoryDetail, tabelMenu, tabelPosMenu, tabelCart
        );
        for (javax.swing.JTable table : tables) {
            table.setRowHeight(32);
            table.setShowHorizontalLines(true);
            table.setShowVerticalLines(false);
            table.setGridColor(new java.awt.Color(60, 60, 60)); // modern subtle line
            table.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
            table.getTableHeader().setReorderingAllowed(false);
        }

        // 4. POS tab button styling
        javax.swing.JButton[] posCategoryButtons = { btnAll, btnCoffee, btnNonCoffee, btnFood, btnDessert };
        for (javax.swing.JButton btn : posCategoryButtons) {
            btn.setFocusPainted(false);
        }

        // 5. Functional action buttons styling with premium accent colors
        btnAddToCart.setBackground(new java.awt.Color(98, 57, 53));
        btnAddToCart.setForeground(java.awt.Color.WHITE); // Coffee Theme
        btnAddToCart.setForeground(java.awt.Color.WHITE);
        btnAddToCart.setFocusPainted(false);

        btnCheckout.setBackground(new java.awt.Color(46, 125, 50)); // Forest Green
        btnCheckout.setForeground(java.awt.Color.WHITE);
        btnCheckout.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnCheckout.setFocusPainted(false);

        btnRemoveFromCart.setBackground(new java.awt.Color(198, 40, 40)); // Red warning
        btnRemoveFromCart.setForeground(java.awt.Color.WHITE);
        btnRemoveFromCart.setFocusPainted(false);

        btnClearOrder.setBackground(new java.awt.Color(117, 117, 117)); // Reset gray
        btnClearOrder.setForeground(java.awt.Color.WHITE);
        btnClearOrder.setFocusPainted(false);

        // History tab buttons
        btnVoid.setBackground(new java.awt.Color(198, 40, 40)); // Warning Red
        btnVoid.setForeground(java.awt.Color.WHITE);
        btnVoid.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnVoid.setFocusPainted(false);

        // Menu Management tab buttons
        btnInsert.setBackground(new java.awt.Color(46, 125, 50)); // Green
        btnInsert.setForeground(java.awt.Color.WHITE);
        btnInsert.setFocusPainted(false);

        btnUpdate.setBackground(new java.awt.Color(21, 101, 192)); // Blue
        btnUpdate.setForeground(java.awt.Color.WHITE);
        btnUpdate.setFocusPainted(false);

        btnDelete.setBackground(new java.awt.Color(198, 40, 40)); // Red
        btnDelete.setForeground(java.awt.Color.WHITE);
        btnDelete.setFocusPainted(false);

        btnCancel.setBackground(new java.awt.Color(117, 117, 117)); // Gray
        btnCancel.setForeground(java.awt.Color.WHITE);
        btnCancel.setFocusPainted(false);

        // 6. Style POS Total display to look like a digital LED receipt screen
        totalDisplayPanel.setBackground(new java.awt.Color(33, 22, 21)); // dark warm coffee tone
        totalDisplayPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(98, 57, 53), 2, true));
        lblTotalHarga.setForeground(new java.awt.Color(255, 179, 0)); // bright warm orange-gold
        lblTotalHarga.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 30));
        
        lblChange.setForeground(new java.awt.Color(76, 175, 80)); // clear green for change
        lblChange.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
        
        lblHistoryTotal.setForeground(new java.awt.Color(255, 179, 0)); // matching history total
        lblHistoryTotal.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));

        // 7. Polish Header styling (Removed as headerPanel was deleted in NetBeans)
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        panelHistoryTab = new javax.swing.JPanel();
        leftHistoryPanel = new javax.swing.JPanel();
        jScrollPaneHistory = new javax.swing.JScrollPane();
        tabelHistory = new javax.swing.JTable();
        rightHistoryPanel = new javax.swing.JPanel();
        jScrollPaneHistoryDetail = new javax.swing.JScrollPane();
        tabelHistoryDetail = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        lblHistoryTotal = new javax.swing.JLabel();
        btnVoid = new javax.swing.JButton();
        panelMenuTab = new javax.swing.JPanel();
        panelFormMenu = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtMenuId = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtMenuNama = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cbMenuKategori = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        txtMenuHarga = new javax.swing.JTextField();
        buttonPanel = new javax.swing.JPanel();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        panelTableMenu = new javax.swing.JPanel();
        jScrollPaneMenu = new javax.swing.JScrollPane();
        tabelMenu = new javax.swing.JTable();
        panelPosTab = new javax.swing.JPanel();
        leftPosPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtSearchMenu = new javax.swing.JTextField();
        categoryPanel = new javax.swing.JPanel();
        btnAll = new javax.swing.JButton();
        btnCoffee = new javax.swing.JButton();
        btnNonCoffee = new javax.swing.JButton();
        btnFood = new javax.swing.JButton();
        btnDessert = new javax.swing.JButton();
        jScrollPanePosMenu = new javax.swing.JScrollPane();
        tabelPosMenu = new javax.swing.JTable();
        btnAddToCart = new javax.swing.JButton();
        rightPosPanel = new javax.swing.JPanel();
        jScrollPaneCart = new javax.swing.JScrollPane();
        tabelCart = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtCustName = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtCashReceived = new javax.swing.JTextField();
        totalDisplayPanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblTotalHarga = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblChange = new javax.swing.JLabel();
        actionBtnPanel = new javax.swing.JPanel();
        btnRemoveFromCart = new javax.swing.JButton();
        btnClearOrder = new javax.swing.JButton();
        btnCheckout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Form Coffee Shop Management");

        tabbedPane.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tabbedPane.addChangeListener(this::tabbedPaneStateChanged);

        panelHistoryTab.setLayout(null);

        leftHistoryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Daftar Riwayat Transaksi"));
        leftHistoryPanel.setLayout(null);

        tabelHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Transaksi", "Tanggal", "Nama Pelanggan", "Total Pembayaran"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelHistory.setRowHeight(25);
        tabelHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelHistoryMouseClicked(evt);
            }
        });
        jScrollPaneHistory.setViewportView(tabelHistory);

        leftHistoryPanel.add(jScrollPaneHistory);
        jScrollPaneHistory.setBounds(16, 24, 448, 440);

        panelHistoryTab.add(leftHistoryPanel);
        leftHistoryPanel.setBounds(12, 12, 480, 480);

        rightHistoryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Detail Transaksi Terpilih"));
        rightHistoryPanel.setLayout(null);

        tabelHistoryDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Menu", "Harga Satuan", "Qty", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelHistoryDetail.setRowHeight(25);
        jScrollPaneHistoryDetail.setViewportView(tabelHistoryDetail);

        rightHistoryPanel.add(jScrollPaneHistoryDetail);
        jScrollPaneHistoryDetail.setBounds(16, 24, 360, 328);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel11.setText("Total Transaksi:");
        rightHistoryPanel.add(jLabel11);
        jLabel11.setBounds(16, 368, 120, 20);

        lblHistoryTotal.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblHistoryTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHistoryTotal.setText("Rp 0,00");
        lblHistoryTotal.setFont(new java.awt.Font("Segoe UI", 1, 20));
        lblHistoryTotal.setForeground(new java.awt.Color(255, 179, 0));
        rightHistoryPanel.add(lblHistoryTotal);
        lblHistoryTotal.setBounds(144, 368, 232, 20);

        btnVoid.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        btnVoid.setText("⚠ Batalkan (Void) Transaksi");
        btnVoid.setBackground(new java.awt.Color(198, 40, 40));
        btnVoid.setForeground(new java.awt.Color(255, 255, 255));
        btnVoid.setFont(new java.awt.Font("Segoe UI", 1, 13));
        btnVoid.addActionListener(this::btnVoidActionPerformed);
        rightHistoryPanel.add(btnVoid);
        btnVoid.setBounds(16, 400, 360, 32);

        panelHistoryTab.add(rightHistoryPanel);
        rightHistoryPanel.setBounds(504, 12, 392, 480);

        tabbedPane.addTab("Riwayat Transaksi", panelHistoryTab);

        panelMenuTab.setLayout(null);

        panelFormMenu.setBorder(javax.swing.BorderFactory.createTitledBorder("Input Data Menu"));
        panelFormMenu.setLayout(null);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel12.setText("Kode Menu:");
        panelFormMenu.add(jLabel12);
        jLabel12.setBounds(16, 32, 80, 20);
        panelFormMenu.add(txtMenuId);
        txtMenuId.setBounds(104, 32, 272, 20);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel13.setText("Nama Menu:");
        panelFormMenu.add(jLabel13);
        jLabel13.setBounds(16, 68, 80, 20);
        panelFormMenu.add(txtMenuNama);
        txtMenuNama.setBounds(104, 68, 272, 20);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel14.setText("Kategori:");
        panelFormMenu.add(jLabel14);
        jLabel14.setBounds(16, 104, 80, 20);

        cbMenuKategori.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "", "", "", "" }));
        panelFormMenu.add(cbMenuKategori);
        cbMenuKategori.setBounds(104, 104, 272, 20);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel15.setText("Harga (Rp):");
        panelFormMenu.add(jLabel15);
        jLabel15.setBounds(16, 140, 80, 20);
        panelFormMenu.add(txtMenuHarga);
        txtMenuHarga.setBounds(104, 140, 272, 20);

        buttonPanel.setLayout(new java.awt.GridLayout(2, 2));

        btnInsert.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnInsert.setText("INSERT");
        btnInsert.setBackground(new java.awt.Color(46, 125, 50));
        btnInsert.setForeground(new java.awt.Color(255, 255, 255));
        btnInsert.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnInsert.addActionListener(this::btnInsertActionPerformed);
        buttonPanel.add(btnInsert);

        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate.setText("UPDATE");
        btnUpdate.setBackground(new java.awt.Color(21, 101, 192));
        btnUpdate.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        buttonPanel.add(btnUpdate);

        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDelete.setText("DELETE");
        btnDelete.setBackground(new java.awt.Color(198, 40, 40));
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        buttonPanel.add(btnDelete);

        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancel.setText("CANCEL");
        btnCancel.setBackground(new java.awt.Color(117, 117, 117));
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnCancel.addActionListener(this::btnCancelActionPerformed);
        buttonPanel.add(btnCancel);

        panelFormMenu.add(buttonPanel);
        buttonPanel.setBounds(16, 192, 360, 72);

        panelMenuTab.add(panelFormMenu);
        panelFormMenu.setBounds(12, 12, 400, 480);

        panelTableMenu.setBorder(javax.swing.BorderFactory.createTitledBorder("Daftar Data Menu"));
        panelTableMenu.setLayout(null);

        tabelMenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Menu", "Nama Menu", "Kategori", "Harga"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelMenu.setRowHeight(25);
        tabelMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelMenuMouseClicked(evt);
            }
        });
        jScrollPaneMenu.setViewportView(tabelMenu);

        panelTableMenu.add(jScrollPaneMenu);
        jScrollPaneMenu.setBounds(16, 24, 440, 440);

        panelMenuTab.add(panelTableMenu);
        panelTableMenu.setBounds(424, 12, 472, 480);

        tabbedPane.addTab("Kelola Menu", panelMenuTab);

        panelPosTab.setMaximumSize(new java.awt.Dimension(1000, 600));
        panelPosTab.setLayout(null);

        leftPosPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Pilih Menu"));
        leftPosPanel.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Cari Menu:");
        leftPosPanel.add(jLabel2);
        jLabel2.setBounds(16, 24, 64, 20);

        txtSearchMenu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchMenuKeyReleased(evt);
            }
        });
        leftPosPanel.add(txtSearchMenu);
        txtSearchMenu.setBounds(88, 24, 336, 20);

        categoryPanel.setLayout(new java.awt.GridLayout(1, 5));

        btnAll.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnAll.setText("Semua");
        btnAll.addActionListener(this::btnAllActionPerformed);
        categoryPanel.add(btnAll);

        btnCoffee.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnCoffee.setText("Coffee");
        btnCoffee.addActionListener(this::btnCoffeeActionPerformed);
        categoryPanel.add(btnCoffee);

        btnNonCoffee.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnNonCoffee.setText("Non-Coffee");
        btnNonCoffee.addActionListener(this::btnNonCoffeeActionPerformed);
        categoryPanel.add(btnNonCoffee);

        btnFood.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnFood.setText("Food");
        btnFood.addActionListener(this::btnFoodActionPerformed);
        categoryPanel.add(btnFood);

        btnDessert.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnDessert.setText("Dessert");
        btnDessert.addActionListener(this::btnDessertActionPerformed);
        categoryPanel.add(btnDessert);

        leftPosPanel.add(categoryPanel);
        categoryPanel.setBounds(16, 52, 408, 24);

        tabelPosMenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Menu", "Nama Menu", "Kategori", "Harga"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelPosMenu.setRowHeight(25);
        jScrollPanePosMenu.setViewportView(tabelPosMenu);

        leftPosPanel.add(jScrollPanePosMenu);
        jScrollPanePosMenu.setBounds(16, 88, 408, 336);

        btnAddToCart.setBackground(new java.awt.Color(98, 57, 53));
        btnAddToCart.setForeground(java.awt.Color.WHITE);
        btnAddToCart.setForeground(java.awt.Color.WHITE);
        btnAddToCart.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAddToCart.setText("Tambah ke Keranjang");
        btnAddToCart.addActionListener(this::btnAddToCartActionPerformed);
        leftPosPanel.add(btnAddToCart);
        btnAddToCart.setBounds(16, 436, 408, 32);

        panelPosTab.add(leftPosPanel);
        leftPosPanel.setBounds(12, 12, 440, 480);

        rightPosPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Keranjang Transaksi"));
        rightPosPanel.setLayout(null);

        tabelCart.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Nama Menu", "Harga", "Jumlah", "Subtotal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelCart.setRowHeight(25);
        jScrollPaneCart.setViewportView(tabelCart);

        rightPosPanel.add(jScrollPaneCart);
        jScrollPaneCart.setBounds(16, 24, 400, 184);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel7.setText("Nama Pelanggan:");
        rightPosPanel.add(jLabel7);
        jLabel7.setBounds(16, 224, 96, 20);
        rightPosPanel.add(txtCustName);
        txtCustName.setBounds(120, 224, 296, 20);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel8.setText("Bayar Tunai (Rp):");
        rightPosPanel.add(jLabel8);
        jLabel8.setBounds(16, 252, 96, 20);

        txtCashReceived.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtCashReceived.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCashReceivedKeyReleased(evt);
            }
        });
        rightPosPanel.add(txtCashReceived);
        txtCashReceived.setBounds(120, 252, 296, 20);

        totalDisplayPanel.setLayout(null);
        totalDisplayPanel.setBackground(new java.awt.Color(33, 22, 21));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel9.setText("TOTAL TAGIHAN :");
        totalDisplayPanel.add(jLabel9);
        jLabel9.setBounds(8, 4, 160, 16);

        lblTotalHarga.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTotalHarga.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalHarga.setText("Rp 0,00");
        lblTotalHarga.setFont(new java.awt.Font("Segoe UI", 1, 30));
        lblTotalHarga.setForeground(new java.awt.Color(255, 179, 0));
        totalDisplayPanel.add(lblTotalHarga);
        lblTotalHarga.setBounds(8, 20, 384, 36);

        rightPosPanel.add(totalDisplayPanel);
        totalDisplayPanel.setBounds(16, 284, 400, 64);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jLabel10.setText("Kembalian:");
        rightPosPanel.add(jLabel10);
        jLabel10.setBounds(16, 360, 96, 20);

        lblChange.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblChange.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChange.setText("Rp 0,00");
        lblChange.setFont(new java.awt.Font("Segoe UI", 1, 18));
        lblChange.setForeground(new java.awt.Color(76, 175, 80));
        rightPosPanel.add(lblChange);
        lblChange.setBounds(120, 360, 296, 20);

        actionBtnPanel.setLayout(new java.awt.GridLayout(1, 3));

        btnRemoveFromCart.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnRemoveFromCart.setText("Hapus Item");
        btnRemoveFromCart.setBackground(new java.awt.Color(198, 40, 40));
        btnRemoveFromCart.setForeground(new java.awt.Color(255, 255, 255));
        btnRemoveFromCart.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnRemoveFromCart.addActionListener(this::btnRemoveFromCartActionPerformed);
        actionBtnPanel.add(btnRemoveFromCart);

        btnClearOrder.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClearOrder.setText("Reset");
        btnClearOrder.setBackground(new java.awt.Color(117, 117, 117));
        btnClearOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnClearOrder.setFont(new java.awt.Font("Segoe UI", 1, 12));
        btnClearOrder.addActionListener(this::btnClearOrderActionPerformed);
        actionBtnPanel.add(btnClearOrder);

        btnCheckout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCheckout.setText("Checkout");
        btnCheckout.setBackground(new java.awt.Color(46, 125, 50));
        btnCheckout.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckout.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnCheckout.addActionListener(this::btnCheckoutActionPerformed);
        actionBtnPanel.add(btnCheckout);

        rightPosPanel.add(actionBtnPanel);
        actionBtnPanel.setBounds(16, 392, 400, 36);

        panelPosTab.add(rightPosPanel);
        rightPosPanel.setBounds(464, 12, 432, 480);

        tabbedPane.addTab("Transaksi Penjualan (POS)", panelPosTab);

        getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllActionPerformed
        orderController.setActiveCategory("All");
    }//GEN-LAST:event_btnAllActionPerformed

    private void btnCoffeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCoffeeActionPerformed
        orderController.setActiveCategory("Coffee");
    }//GEN-LAST:event_btnCoffeeActionPerformed

    private void btnNonCoffeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNonCoffeeActionPerformed
        orderController.setActiveCategory("Non-Coffee");
    }//GEN-LAST:event_btnNonCoffeeActionPerformed

    private void btnFoodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFoodActionPerformed
        orderController.setActiveCategory("Food");
    }//GEN-LAST:event_btnFoodActionPerformed

    private void btnDessertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDessertActionPerformed
        orderController.setActiveCategory("Dessert");
    }//GEN-LAST:event_btnDessertActionPerformed

    private void txtSearchMenuKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchMenuKeyReleased
        orderController.tampilMenuFiltered();
    }//GEN-LAST:event_txtSearchMenuKeyReleased

    private void btnAddToCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToCartActionPerformed
        orderController.addToCart();
    }//GEN-LAST:event_btnAddToCartActionPerformed

    private void txtCashReceivedKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCashReceivedKeyReleased
        orderController.calculateChange();
    }//GEN-LAST:event_txtCashReceivedKeyReleased

    private void btnRemoveFromCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveFromCartActionPerformed
        orderController.removeFromCart();
    }//GEN-LAST:event_btnRemoveFromCartActionPerformed

    private void btnClearOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearOrderActionPerformed
        orderController.clearOrder();
    }//GEN-LAST:event_btnClearOrderActionPerformed

    private void btnCheckoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckoutActionPerformed
        orderController.checkout();
    }//GEN-LAST:event_btnCheckoutActionPerformed

    private void tabelHistoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelHistoryMouseClicked
        orderController.actionClickHistoryTable();
    }//GEN-LAST:event_tabelHistoryMouseClicked

    private void btnVoidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoidActionPerformed
        orderController.voidTransaction();
    }//GEN-LAST:event_btnVoidActionPerformed

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
        menuController.save();
        orderController.tampilMenu();
    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        menuController.update();
        orderController.tampilMenu();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        menuController.delete();
        orderController.tampilMenu();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        menuController.clearForm();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void tabelMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelMenuMouseClicked
        menuController.actionClickTable();
    }//GEN-LAST:event_tabelMenuMouseClicked

    private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabbedPaneStateChanged
        if (tabbedPane.getSelectedIndex() == 1) {
            if (orderController != null) {
                orderController.tampilHistory();
            }
        }
    }//GEN-LAST:event_tabbedPaneStateChanged

    // Getters for Menu Tab components
    public JTextField getTxtMenuId() { return txtMenuId; }
    public JTextField getTxtMenuNama() { return txtMenuNama; }
    public JComboBox<String> getCbMenuKategori() { return cbMenuKategori; }
    public JTextField getTxtMenuHarga() { return txtMenuHarga; }
    public JTable getTabelMenu() { return tabelMenu; }

    // Getters for POS Tab components
    public JTable getTabelPosMenu() { return tabelPosMenu; }
    public JTable getTabelCart() { return tabelCart; }
    public JTextField getTxtCustName() { return txtCustName; }
    public JTextField getTxtCashReceived() { return txtCashReceived; }
    public JLabel getLblTotalHarga() { return lblTotalHarga; }
    public JLabel getLblChange() { return lblChange; }
    public JTextField getTxtSearchMenu() { return txtSearchMenu; }

    // Getters for History Tab components
    public JTable getTabelHistory() { return tabelHistory; }
    public JTable getTabelHistoryDetail() { return tabelHistoryDetail; }
    public JLabel getLblHistoryTotal() { return lblHistoryTotal; }

    public static void main(String args[]) {
        try {
            // Apply modern UI styling properties to UIManager before FlatLaf initialization
            javax.swing.UIManager.put("Button.arc", 0);
            javax.swing.UIManager.put("Component.arc", 0);
            javax.swing.UIManager.put("TextComponent.arc", 0);
            javax.swing.UIManager.put("ProgressBar.arc", 0);
            javax.swing.UIManager.put("ScrollBar.thumbArc", 999);
            javax.swing.UIManager.put("ScrollBar.trackArc", 999);
            javax.swing.UIManager.put("TabbedPane.showTabSeparators", true);
            javax.swing.UIManager.put("TabbedPane.tabHeight", 36);
            javax.swing.UIManager.put("TabbedPane.selectedBackground", new java.awt.Color(98, 57, 53));
            javax.swing.UIManager.put("TabbedPane.selectedForeground", java.awt.Color.WHITE);

            // Attempt to load macOS Dark theme first, fallback to standard dark theme
            try {
                com.formdev.flatlaf.themes.FlatMacDarkLaf.setup();
            } catch (Exception e) {
                com.formdev.flatlaf.FlatDarkLaf.setup();
            }
        } catch (Exception ex) {
            System.err.println("Gagal menginisialisasi FlatLaf: " + ex.getMessage());
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainDashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionBtnPanel;
    private javax.swing.JButton btnAddToCart;
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCheckout;
    private javax.swing.JButton btnClearOrder;
    private javax.swing.JButton btnCoffee;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDessert;
    private javax.swing.JButton btnFood;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnNonCoffee;
    private javax.swing.JButton btnRemoveFromCart;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnVoid;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel categoryPanel;
    private javax.swing.JComboBox cbMenuKategori;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPaneCart;
    private javax.swing.JScrollPane jScrollPaneHistory;
    private javax.swing.JScrollPane jScrollPaneHistoryDetail;
    private javax.swing.JScrollPane jScrollPaneMenu;
    private javax.swing.JScrollPane jScrollPanePosMenu;
    private javax.swing.JLabel lblChange;
    private javax.swing.JLabel lblHistoryTotal;
    private javax.swing.JLabel lblTotalHarga;
    private javax.swing.JPanel leftHistoryPanel;
    private javax.swing.JPanel leftPosPanel;
    private javax.swing.JPanel panelFormMenu;
    private javax.swing.JPanel panelHistoryTab;
    private javax.swing.JPanel panelMenuTab;
    private javax.swing.JPanel panelPosTab;
    private javax.swing.JPanel panelTableMenu;
    private javax.swing.JPanel rightHistoryPanel;
    private javax.swing.JPanel rightPosPanel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tabelCart;
    private javax.swing.JTable tabelHistory;
    private javax.swing.JTable tabelHistoryDetail;
    private javax.swing.JTable tabelMenu;
    private javax.swing.JTable tabelPosMenu;
    private javax.swing.JPanel totalDisplayPanel;
    private javax.swing.JTextField txtCashReceived;
    private javax.swing.JTextField txtCustName;
    private javax.swing.JTextField txtMenuHarga;
    private javax.swing.JTextField txtMenuId;
    private javax.swing.JTextField txtMenuNama;
    private javax.swing.JTextField txtSearchMenu;
    // End of variables declaration//GEN-END:variables
}
