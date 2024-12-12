package com.harusora.longcn.apporderfood.model;

import java.util.List;

public class Order {

    private int id;

    private String orderCode;

    private int userId;

    private double totalAmount;

    private int status;

    private int paymentType;

    private List<Cart> cartList;

    private String address;

    private String note;


    public Order(int id, String orderCode, int userId, double totalAmount, int status, int paymentType, List<Cart> cartList) {
        this.id = id;
        this.orderCode = orderCode;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentType = paymentType;
        this.cartList = cartList;
    }

    public Order(String orderCode, int userId, double totalAmount, int status, int paymentType, List<Cart> cartList, String address, String note) {
        this.orderCode = orderCode;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentType = paymentType;
        this.cartList = cartList;
        this.address = address;
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }
}
