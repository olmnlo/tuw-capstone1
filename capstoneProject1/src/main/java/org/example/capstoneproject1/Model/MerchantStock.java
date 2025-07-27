package org.example.capstoneproject1.Model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class MerchantStock {

    @NotBlank(message = "id must not be empty")
    private String id;

    @NotBlank(message = "productId must not be empty")
    private String productId;

    @NotBlank(message = "merchantId must not be empty")
    private String merchantId;

    @Min(value = 11, message = "stock must be more than 10")
    private int stock;


}
