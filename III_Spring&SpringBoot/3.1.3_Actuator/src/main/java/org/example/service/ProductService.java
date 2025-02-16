package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class ProductService {

//    private final ProductRepository productRepository;
    List<ProductDTO> productList = new ArrayList<>();

    public ProductService() {
        productList.add(new ProductDTO(1L, "Milk"));
        productList.add(new ProductDTO(2L, "Bread"));
        productList.add(new ProductDTO(3L, "Juice"));
        productList.add(new ProductDTO(4L, "Meat"));
        productList.add(new ProductDTO(5L, "Orange"));
        productList.add(new ProductDTO(6L, "Potato"));
        productList.add(new ProductDTO(7L, "Cheese"));
        productList.add(new ProductDTO(8L, "Water"));
        productList.add(new ProductDTO(9L, "Tomato"));
        productList.add(new ProductDTO(10L, "Cucumber"));
    }

    public List<ProductDTO> getRandomProduct() {
        Random random = new Random();
        int randomId = random.nextInt(productList.size());
        var product = productList.get(randomId);
        return List.of(product);
    }

    public List<ProductDTO> getProducts() {
        return productList;
    }

    public Long getProductsCount() {
        return Long.valueOf(productList.size());
    }

    public void addProduct(ProductDTO productDTO) {
        log.info("New product was added: " + productDTO);
        productList.add(productDTO);
    }
}
