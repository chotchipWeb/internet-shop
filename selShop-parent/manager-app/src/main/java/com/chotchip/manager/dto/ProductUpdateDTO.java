package com.chotchip.manager.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductUpdateDTO {
    @NotNull
    @NotEmpty
    String title;

    String details;
}
