package com.example.TechProEShopWithMongo.Products;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Document(collection = "Products")
public class Product {

    @Id private String _id;

    private String productName;
    private double price;
    private String category;
    private boolean isDiscounted;
    @Field(targetType = FieldType.DOUBLE)
    private Double dPrice;
    private int stock;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isDiscounted() {
        return isDiscounted;
    }

    public void setDiscounted(boolean discounted) {
        isDiscounted = discounted;
    }

    public Double getdPrice() {
        return dPrice;
    }

    public void setdPrice(Double dPrice) {
        this.dPrice = dPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String get_id() {
        return _id;
    }
}
