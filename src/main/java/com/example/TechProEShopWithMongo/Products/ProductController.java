package com.example.TechProEShopWithMongo.Products;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts(
            @RequestParam(value="priceStart", required = false) String priceStart,
            @RequestParam(value = "priceEnd", required = false) String priceEnd,
            @RequestParam(value = "category", required = false) String category)
    {
        logger.info("getAllProducts called with: " + priceStart + " " + priceEnd + " " + category);
        return productService.getAllProducts(priceStart, priceEnd, category);
    }

    @GetMapping("{id}")
    public Optional<Product> getProductInfo(@PathVariable("id") String productID) {
        logger.info("getProductInfo called for Product ID: " + productID);
        return productService.getProductInfo(productID);
    }
}

