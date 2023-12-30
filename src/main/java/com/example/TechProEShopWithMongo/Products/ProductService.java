package com.example.TechProEShopWithMongo.Products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ProductService(ProductRepository productRepository, MongoTemplate mongoTemplate) {
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public List<Product> getAllProducts(String priceStart, String priceEnd, String category) {

        if (priceStart == null && priceEnd == null && category == null) {
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

        return mongoTemplate.find(filteredQuery, Product.class);

    }

    public Optional<Product> getProductInfo(String productID) {
        return productRepository.findById(productID);
    }
}
