package org.example.capstoneproject1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Api.ApiResponse;
import org.example.capstoneproject1.Model.Product;
import org.example.capstoneproject1.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Get all products
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAll());
    }

    // Add a new product
    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid Product product) {
        if(productService.addProduct(product) == -1){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("category not found you must add new category"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("product add successfully"));
    }

    // Update a product by id
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid Product product) {
        boolean updated = productService.updateProduct(id, product);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("product is updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    // Delete a product by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("product deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }


    //5 endpoints
    //1: filter top 10 products
    @GetMapping("/top")
    public ResponseEntity<?> filterByTop10Rate(){
        ArrayList<Product> filtered = productService.filterByRate();
        if(filtered == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("no products"));
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(filtered);
        }
    }



}
