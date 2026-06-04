package br.com.freitas.upgradeddoodle.domain.service;

import br.com.freitas.upgradeddoodle.domain.repository.ProductRepository;
import br.com.freitas.upgradeddoodle.presentation.dto.PagedResponse;
import br.com.freitas.upgradeddoodle.presentation.dto.ProductDTO;
import br.com.freitas.upgradeddoodle.presentation.dto.ProductMinDTO;
import br.com.freitas.upgradeddoodle.presentation.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
