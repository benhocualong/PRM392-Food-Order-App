package com.harusora.longcn.apporderfood.model;

public class Comment {

    private int id;

    private String username;

    private String detail;

    private int productId;

    private String date;

    public Comment(int id, String username, String detail, int productId, String date) {
        this.id = id;
        this.username = username;
        this.detail = detail;
        this.productId = productId;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
