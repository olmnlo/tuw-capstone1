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
public class User {

    @NotBlank(message = "id must not be empty")
    private String id;

    @NotBlank(message = "username must not be empty")
    @Size(min = 6, message = "username must be more than 5 characters")
    private String username;

    @NotBlank(message = "password must not be empty")
    @Size(min = 7, message = "password must be more than 6 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "password must contain letters and digits")
    private String password;

    @NotBlank(message = "email must not be empty")
    @Email(message = "email must be a valid email")
    private String email;

    @NotBlank(message = "role must not be empty")
    @Pattern(regexp = "^(Admin|Customer)$", message = "role must be either 'Admin' or 'Customer'")
    private String role;

    @NotNull(message = "balance must not be empty")
    @Positive(message = "balance must be a positive number")
    private Double balance;


    @AssertFalse(message = "subscribe by default false")
    private boolean subscribed;
}
