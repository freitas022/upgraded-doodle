package br.com.freitas.upgradeddoodle.presentation.controller;

import br.com.freitas.upgradeddoodle.domain.service.ProductService;
import br.com.freitas.upgradeddoodle.presentation.dto.PagedResponse;
import br.com.freitas.upgradeddoodle.presentation.dto.ProductDTO;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ProductDTO>> findAll(
            @PageableDefault(size = 20, sort = "name")
            Pageable pageable
    ) {
        return ResponseEntity.ok(productService.findAll(pageable));
    }
}
