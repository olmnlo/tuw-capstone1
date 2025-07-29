package org.example.capstoneproject1.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.capstoneproject1.Api.ApiResponse;
import org.example.capstoneproject1.Model.Merchant;
import org.example.capstoneproject1.Service.MerchantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(merchantService.getAll());
    }

    @PostMapping("")
    public ResponseEntity<?> add(@RequestBody @Valid Merchant merchant) {
        if(merchantService.addMerchant(merchant)) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("merchant added successfully"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("merchant is duplicated"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid Merchant merchant) {
        boolean updated = merchantService.updateMerchant(id, merchant);
        if (updated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("merchant updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Merchant not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        boolean deleted = merchantService.deleteMerchant(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("merchant deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Merchant not found");
    }
}
