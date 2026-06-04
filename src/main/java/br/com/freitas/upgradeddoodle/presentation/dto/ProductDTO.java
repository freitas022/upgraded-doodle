package br.com.freitas.upgradeddoodle.presentation.dto;

import br.com.freitas.upgradeddoodle.domain.model.Product;
import br.com.freitas.upgradeddoodle.domain.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.Set;

public record ProductDTO(
        Long id,
        String name,
        String shortDescription,
        String longDescription,
        BigDecimal price,
        ProductStatus status,
        Set<CategoryDTO> categories
) {
    public static ProductDTO fromEntity(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getShortDescription(),
                product.getLongDescription(),
                product.getPrice(),
                product.getStatus(),
                product.getCategories()
                        .stream()
                        .map(CategoryDTO::fromEntity)
                        .collect(java.util.stream.Collectors.toSet())
        );
    }
}
