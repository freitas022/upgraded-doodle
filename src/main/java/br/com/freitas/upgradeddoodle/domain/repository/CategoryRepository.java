package br.com.freitas.upgradeddoodle.domain.repository;

import br.com.freitas.upgradeddoodle.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
