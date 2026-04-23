package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "products")
    public List<ProductDTO> getAllProducts() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "product", key = "#id")
    public ProductDTO getProductById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(product);
    }

    @CachePut(value = "product", key = "#result.id")
    public ProductDTO createProduct(Product product) {
        return mapToDTO(repository.save(product));
    }

    @CachePut(value = "product", key = "#id")
    @CacheEvict(value = "products", allEntries = true)
    public ProductDTO updateProduct(Long id, Product updated) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(updated.getName());
        product.setDescription(updated.getDescription());
        product.setPrice(updated.getPrice());
        product.setQuantity(updated.getQuantity());

        return mapToDTO(repository.save(product));
    }

    @CacheEvict(value = {"product", "products"}, key = "#id", allEntries = true)
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}