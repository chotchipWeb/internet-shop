package com.chotchip.catalogue.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductUpdateDTO {
    @NotNull
    @NotEmpty
    String title;

    String details;
}
