package org.example.bookmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestAddUser {
    @NotBlank
    private String name;

    @Size(min = 8, max = 20)
    private String phone;

    private String memo;
}
