package org.example.capstoneproject1.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @NotBlank(message = "ID must not be empty")
    private String id;

    @NotBlank(message = "Name must not be empty")
    @Size(min = 4, message = "Name must be more than 3 characters")
    private String name;

    @NotNull(message = "Price must not be empty")
    @Positive(message = "Price must be a positive number")
    private Double price;

    @NotBlank(message = "Category ID must not be empty")
    private String categoryID;

    private double productRate;

    private boolean discount20;
    private boolean isSeasonalProduct;
    private double offer;

}
