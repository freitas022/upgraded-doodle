package br.com.freitas.upgradeddoodle.domain.repository;

import br.com.freitas.upgradeddoodle.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
