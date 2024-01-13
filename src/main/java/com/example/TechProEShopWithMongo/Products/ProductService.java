package com.example.TechProEShopWithMongo.Products;

import com.example.TechProEShopWithMongo.Orders.Order;
import com.example.TechProEShopWithMongo.Orders.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;
    private final OrderRepository orderRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, MongoTemplate mongoTemplate, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
        this.orderRepository = orderRepository;
    }

    public List<Product> getAllProducts(String priceStart,
                                        String priceEnd,
                                        String category,
                                        String discounted,
                                        String inStock,
                                        String search) {

        if (priceStart == null && priceEnd == null && category == null && discounted == null && inStock == null && search == null) {
            return productRepository.findAll();
        }

        Query filteredQuery = new Query();

        if (priceStart != null) {
            filteredQuery.addCriteria(Criteria.where("price").gte(Double.parseDouble(priceStart)));
        }

        if (priceEnd != null) {
            filteredQuery.addCriteria(Criteria.where("price").lte(Double.parseDouble(priceEnd)));
        }

        if (category != null) {
            filteredQuery.addCriteria(Criteria.where("category").in((Object[]) category.split(",")));
        }

        if (discounted != null) {
            filteredQuery.addCriteria(Criteria.where("isDiscounted").is(Boolean.parseBoolean(discounted)));
        }

        if (Boolean.parseBoolean(inStock)) {
            filteredQuery.addCriteria(Criteria.where("stock").gt(0));
        } else {
            filteredQuery.addCriteria(Criteria.where("stock").gte(0));
        }

        if (search != null) {
            Criteria searchByTitle = Criteria.where("productName").regex(".*"+search+".*", "i");
            Criteria searchByDesc = Criteria.where("description").regex(".*"+search+".*", "i");
            filteredQuery.addCriteria(new Criteria().orOperator(searchByTitle, searchByDesc));
        }

        return mongoTemplate.find(filteredQuery, Product.class);

    }

    public Optional<Product> getProductInfo(String productID) {
        return productRepository.findById(productID);
    }

    public void buyProducts(List<String> productIds) {

        ArrayList<Product> purchasedProducts = new ArrayList<>();
        double totalOrderPrice = 0;

        // Check if all products in the basket are available or in stock before making the order
        for (String productId: productIds) {
            Optional<Product> optionalProduct = productRepository.findById(productId);

            if (optionalProduct.isEmpty()) {
                // Product not found, throw ResponseStatusException with HTTP 500
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Product not found");
            }

            Product product = optionalProduct.get();

            if (product.getStock() <= 0) {
                // Product out of stock, throw ResponseStatusException with HTTP 500
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Product is out of stock");
            }
        }

        for (String productId : productIds) {
            Product product = productRepository.findById(productId).get();
            product.setStock(product.getStock() - 1);
            productRepository.save(product);
            purchasedProducts.add(product);
            totalOrderPrice += product.isDiscounted() ? product.getdPrice() : product.getPrice();
        }

        Order newOrder = new Order(
                new Date(new Timestamp(System.currentTimeMillis()).getTime()).toString(),
                totalOrderPrice,
                purchasedProducts
        );
        orderRepository.save(newOrder);
    }
}
