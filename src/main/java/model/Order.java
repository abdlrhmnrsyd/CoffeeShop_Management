package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a purchase transaction.
 */
public class Order {
    private String id;
    private Timestamp tanggal;
    private String customerName;
    private double totalHarga;
    private List<OrderDetail> details = new ArrayList<>();

    public Order() {
    }

    public Order(String id, Timestamp tanggal, String customerName, double totalHarga) {
        this.id = id;
        this.tanggal = tanggal;
        this.customerName = customerName;
        this.totalHarga = totalHarga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }
    
    public void addDetail(OrderDetail detail) {
        this.details.add(detail);
    }
}
