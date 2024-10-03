package com.chotchip.customer.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewCreateDTO {

    @NotNull(message = "${customer.products.review.error.rating_is_null}")
    @Min(value = 1, message = "${customer.products.review.error.rating_is_below_min}")
    @Max(value = 5, message = "${customer.products.review.error.rating_is_above_max}")
    private Integer rating;
    @Size(max = 1000, message = "${customer.products.details.error.details_is_above_max}")
    private String details;
}
