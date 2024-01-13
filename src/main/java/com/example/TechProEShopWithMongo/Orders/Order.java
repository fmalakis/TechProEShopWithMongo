package com.example.TechProEShopWithMongo.Orders;

import com.example.TechProEShopWithMongo.Products.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "Orders")
public class Order {

    @Id private String _id;

    private String timestamp;
    private double totalPrice;
    private ArrayList<Product> products;

    public Order(String timestamp, double totalPrice, ArrayList<Product> products) {
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
