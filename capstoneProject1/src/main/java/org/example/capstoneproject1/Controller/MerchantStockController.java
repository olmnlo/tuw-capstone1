package org.example.capstoneproject1.Controller;

import jakarta.servlet.http.PushBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Api.ApiResponse;
import org.example.capstoneproject1.Model.MerchantStock;
import org.example.capstoneproject1.Service.MerchantStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/merchant-stock")
@RequiredArgsConstructor
public class MerchantStockController {

    private final MerchantStockService merchantStockService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(merchantStockService.getAll());
    }

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid MerchantStock merchantStock) {
        int msg = merchantStockService.addMerchantStock(merchantStock);
        if (msg == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("merchant not found"));
        }
        if (msg == -2){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("product not found"));
        }
        if(msg == -3){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("stock is duplicated"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("merchant stock added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid MerchantStock merchantStock) {
        boolean updated = merchantStockService.updateMerchantStock(id, merchantStock);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("merchant stock updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Merchant stock not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean deleted = merchantStockService.deleteMerchantStock(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("merchant stock deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Merchant stock not found");
    }


    public ResponseEntity<?> updateStock(@PathVariable String productId, @PathVariable String merchantId, @PathVariable int amount) {
        boolean stock = merchantStockService.addToStock(productId, merchantId, amount);
        if (stock) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("product add to stock successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("not found product/merchant in stock"));
        }
    }

    @PostMapping("/buy/user/{userId}/product/{productId}/merchant/{merchantId}")
    public ResponseEntity<?> buyDirectlyFromStock(@PathVariable String userId, @PathVariable String productId, @PathVariable String merchantId) {
        int msg = merchantStockService.buyDirectly(userId, productId, merchantId);
        switch (msg) {
            case 1:
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("thank you for buying come again"));
            case 2:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("user balance less than product price"));
            case 3:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("product not found"));
            case 4:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("user not found"));
            default:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("merchant not found"));

        }
    }

    @PostMapping("/{merchantId}/product/{productId}/toggle-bigdeal/{discount}")
    public ResponseEntity<?> toggleSeasonalProduct(@PathVariable String productId, @PathVariable String merchantId, @PathVariable double discount, PushBuilder pushBuilder) {
        int msg = merchantStockService.seasonalProducts(merchantId, productId, discount);
        if (msg == -1) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("discount not applicable check discount value"));
        } else if (msg == 1) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("product now updated successfully: it is in seasonal product offers now"));
        } else if (msg == 2) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("product not found"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("merchant not found"));
        }
    }

    @GetMapping("/performance/user/{userId}/quality")
    public ResponseEntity<?> merchantPerformance(@PathVariable String userId){
        Map<String, Object> merchantPerform = merchantStockService.merchantPerformance(userId);
        if (merchantPerform == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(merchantPerform);
    }


    @PutMapping("/{merchantId}/product/{productId}/subscribed-discount")
    public ResponseEntity<?> toggleDiscount20(@PathVariable String merchantId, @PathVariable String productId){
        int msg = merchantStockService.toggleDiscount20(merchantId,productId);

        if(msg == -1 ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("merchant not found");
        }else if (msg == -2){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("product not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("discount updated"));
    }

}
