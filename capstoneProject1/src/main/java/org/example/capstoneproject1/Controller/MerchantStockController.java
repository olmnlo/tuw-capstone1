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
}
