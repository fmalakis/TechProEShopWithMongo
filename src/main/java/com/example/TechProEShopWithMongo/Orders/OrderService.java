package com.example.TechProEShopWithMongo.Orders;

import com.example.TechProEShopWithMongo.Products.Product;
import com.example.TechProEShopWithMongo.Products.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public Order createNewOrder(List<String> productIds) {
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
        return newOrder;
    }
}
