package com.example.TechProEShopWithMongo.Orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderRepository orderRepository;

    private final OrderService orderService;
    @Autowired
    public OrderController(OrderRepository orderRepository, OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        logger.info("getAllOrders called");
        return orderRepository.findAll();
    }

    @GetMapping("{id}")
    public Optional<Order> getOrderById(@PathVariable("id") String productId) {
        logger.info("getOrderById called for " + productId);
        return orderRepository.findById(productId);
    }

    @PostMapping("/newOrder")
    public Order buyProducts(@RequestBody List<String> productIds) {
        logger.info("newOrder called for " + productIds.toString());
        return orderService.createNewOrder(productIds);
    }
}
