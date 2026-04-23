package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductDTO> getAll() {
        return service.getAllProducts();
    }

    @GetMapping("/{id:\\d+}")
    public ProductDTO getById(@PathVariable Long id) {
        return service.getProductById(id);
    }

    @PostMapping
    public ProductDTO create(@RequestBody Product product) {
        return service.createProduct(product);
    }

    @PutMapping("/{id:\\d+}")
    public ProductDTO update(@PathVariable Long id, @RequestBody Product product) {
        return service.updateProduct(id, product);
    }

    @DeleteMapping("/{id:\\d+}")
    public String delete(@PathVariable Long id) {
        service.deleteProduct(id);
        return "Product deleted";
    }
}