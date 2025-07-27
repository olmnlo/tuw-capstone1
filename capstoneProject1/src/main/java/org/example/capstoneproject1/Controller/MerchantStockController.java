package org.example.capstoneproject1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Api.ApiResponse;
import org.example.capstoneproject1.Model.MerchantStock;
import org.example.capstoneproject1.Service.MerchantStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        merchantStockService.addMerchantStock(merchantStock);
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




    public ResponseEntity<?> updateStock(@PathVariable String productId, @PathVariable String merchantId, @PathVariable int amount ){
        boolean stock = merchantStockService.addToStock(productId, merchantId, amount);
        if (stock){
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("product add to stock successfully"));
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("not found product/merchant in stock"));
        }
    }

    @PostMapping("/buy/user/{userId}/product/{productId}/merchant/{merchantId}")
    public ResponseEntity<?> buyDirectlyFromStock(@PathVariable String userId,@PathVariable String productId,@PathVariable String merchantId){
        int msg = merchantStockService.buyDirectly(userId, productId, merchantId);
        switch (msg){
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
}
