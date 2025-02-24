package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ProductDTO;
import org.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductContoller {

    private final ProductService productService;

    @GetMapping("/api/v1/getproducts")
    public List<ProductDTO> getProducts() {
        return productService.getProducts();
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO) {
        if (productDTO == null || productDTO.id() == null ||
            productDTO.name() == null) {

            log.info("Bad request was detected: " + productDTO);
            return ResponseEntity.badRequest()
                    .body("Id or name of the product was null");
        }
        productService.addProduct(productDTO);
        return ResponseEntity.ok("Product is added successfully");
    }

    public ProductService getProductService() {
        return productService;
    }
}
