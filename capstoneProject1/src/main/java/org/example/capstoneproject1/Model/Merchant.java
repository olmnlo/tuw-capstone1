package org.example.capstoneproject1.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Merchant {

    @NotBlank(message = "id must not be empty")
    private String id;

    @NotBlank(message = "name must not be empty")
    @Size(min = 4, message = "name must be more than 3 characters")
    private String name;

}
