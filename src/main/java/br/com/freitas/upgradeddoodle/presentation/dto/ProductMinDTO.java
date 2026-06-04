package br.com.freitas.upgradeddoodle.presentation.dto;

import br.com.freitas.upgradeddoodle.domain.model.Product;
import br.com.freitas.upgradeddoodle.domain.model.enums.ProductStatus;

import java.math.BigDecimal;

public record ProductMinDTO(
        Long id,
        String name,
        String shortDescription,
        BigDecimal price,
        ProductStatus status
) {
    public static ProductMinDTO fromEntity(Product product) {
        return new ProductMinDTO(
                product.getId(),
                product.getName(),
                product.getShortDescription(),
                product.getPrice(),
                product.getStatus()
        );
    }
}
