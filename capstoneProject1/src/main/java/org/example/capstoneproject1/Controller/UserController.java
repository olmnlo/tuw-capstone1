package org.example.capstoneproject1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Api.ApiResponse;
import org.example.capstoneproject1.Model.Product;
import org.example.capstoneproject1.Model.User;
import org.example.capstoneproject1.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid User user) {
        if(userService.addUser(user)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("user added successfully"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("user is duplicated"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid User user) {
        boolean updated = userService.updateUser(id, user);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("user updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("user deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }


    @GetMapping("/category-id/{categoryId}")
    public ResponseEntity<?> filterByCategory(@PathVariable String categoryId) {
        ArrayList<Product> filtered = userService.filterByCategoryId(categoryId);
        if (filtered.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("no products in this category"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(filtered);
        }
    }

    @GetMapping("/category-price")
    public ResponseEntity<?> filterByPriceRange(@RequestParam("min") int min, @RequestParam("max") int max) {
        ArrayList<Product> filtered = userService.filterByPriceRange(min, max);
        if (filtered.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("no products in this category"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(filtered);
        }
    }


    //5 endpoints
    //2: compare two products
    @GetMapping("/compare")
    public ResponseEntity<?> compareTwoProducts(@RequestParam("product1") String productId1, @RequestParam("product2") String productId2) {
        Map<String, Map<String, Boolean>> compared = userService.compareTwoProducts(productId1, productId2);
        if (compared == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("some products not found check the id again"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(compared);
        }
    }

    //5 endpoints
    //3: discount for subscribed users
    @GetMapping("/{userId}/discount")
    public ResponseEntity<?> getDiscount(@PathVariable String userId) {
        ArrayList<Product> discount = userService.discount(userId);

        if (discount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("user not found"));
        } else if (discount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("no discounts"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(discount);
        }
    }

    @PostMapping("/{userId}/product/{productId}/rate/{rate}")
    public ResponseEntity<?> rateProduct(@PathVariable String userId, @PathVariable String productId, @PathVariable double rate) {
        if (rate > 5 || rate < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("rate must be 0 to 5"));
        }
        if (userService.rateProduct(productId, userId, rate)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("thank you for rating product"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("you did not buy this product"));
    }

    @PostMapping("/{userId}/subscribe")
    public ResponseEntity<?> makeSubscribe(@PathVariable String userId){
        if(userService.subscribe(userId)){
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("thank you for Subscribe"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("user not have balance"));
    }

}
