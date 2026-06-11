package br.com.freitas.upgradeddoodle.domain.repository;

import br.com.freitas.upgradeddoodle.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            select o
            from Order o
            left join fetch o.items i
            left join fetch i.product
            left join fetch o.payment
            where o.id = :id
            """)
    Optional<Order> findDetailedById(Long id);
}
