package model;

/**
 * Model class representing a line item in an order.
 */
public class OrderDetail {
    private int id;
    private String orderId;
    private String menuId;
    private String menuNama;
    private double harga;
    private int quantity;
    private double subtotal;

    public OrderDetail() {
    }

    public OrderDetail(String menuId, String menuNama, double harga, int quantity) {
        this.menuId = menuId;
        this.menuNama = menuNama;
        this.harga = harga;
        this.quantity = quantity;
        this.subtotal = harga * quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuNama() {
        return menuNama;
    }

    public void setMenuNama(String menuNama) {
        this.menuNama = menuNama;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = this.harga * this.quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
