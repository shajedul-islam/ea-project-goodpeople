package com.miu.ea.product.controller;

import com.miu.ea.product.io.ProductResponse;
import com.miu.ea.product.io.ProductSaveRequest;
import com.miu.ea.product.model.Products;
import com.miu.ea.product.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProductSaveRequest request) {
        Long requestId = request.getRequestId();

        List<Products> productList = Arrays.stream(request.getProducts().split(","))
                .map(String::trim)
                .map(p -> new Products(null, requestId, p))
                .collect(Collectors.toList());

        productRepository.saveAll(productList);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{requestId}")
    public ProductResponse get(@PathVariable("requestId") Long requestId) {
        List<Products> productByRequestId = productRepository.findProductByRequestId(requestId);
        return new ProductResponse(productByRequestId);
    }
}
