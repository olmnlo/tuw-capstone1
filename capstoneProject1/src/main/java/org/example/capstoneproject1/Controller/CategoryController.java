package org.example.capstoneproject1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Api.ApiResponse;
import org.example.capstoneproject1.Model.Category;
import org.example.capstoneproject1.Service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Get all categories
    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAll());
    }

    // Add new category
    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid Category category) {
        if(categoryService.addCategory(category)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("category added successfully"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("category duplicated"));
    }

    // Update category by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid Category category) {
        boolean updated = categoryService.updateCategory(id, category);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("category updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
    }

    // Delete category by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("category deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
    }
}
