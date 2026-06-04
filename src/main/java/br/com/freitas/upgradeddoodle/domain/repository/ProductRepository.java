package br.com.freitas.upgradeddoodle.domain.repository;

import br.com.freitas.upgradeddoodle.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
       select p
       from Product p
       left join fetch p.categories
       where p.id = :id
       """)
    Optional<Product> findByIdWithCategories(Long id);
}
