package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.model.Product;
import br.com.freitas.upgradeddoodle.domain.repository.ProductRepository;
import br.com.freitas.upgradeddoodle.presentation.dto.PagedResponse;
import br.com.freitas.upgradeddoodle.presentation.dto.ProductDTO;
import br.com.freitas.upgradeddoodle.presentation.dto.ProductMinDTO;
import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import br.com.freitas.upgradeddoodle.presentation.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDTO findById(Long id) {
        return productRepository.findByIdWithCategories(id)
                .map(ProductDTO::fromEntity)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public PagedResponse<ProductMinDTO> findAll(Pageable pageable) {
        Page<ProductMinDTO> page = productRepository.findAll(pageable)
                .map(ProductMinDTO::fromEntity);

        return PagedResponse.from(page);
    }

    public Map<Long, Product> findAvailableProductsByIds(List<Long> productIds) {
        var productsById = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        for (Long productId : productIds) {
            var product = productsById.get(productId);

            if (product == null) {
                throw new ResourceNotFoundException(
                        "Product not found with id: " + productId
                );
            }

            if (!product.isAvailable()) {
                throw new BusinessException(
                        "Product with id " + productId + " is not available."
                );
            }
        }

        return productsById;
    }
}
