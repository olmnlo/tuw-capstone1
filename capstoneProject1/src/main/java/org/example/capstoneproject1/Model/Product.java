package org.example.capstoneproject1.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "Category ID must not be empty")
    private String categoryID;

}
