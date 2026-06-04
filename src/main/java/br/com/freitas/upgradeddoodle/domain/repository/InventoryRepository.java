package br.com.freitas.upgradeddoodle.domain.repository;

import br.com.freitas.upgradeddoodle.domain.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
