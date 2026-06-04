package br.com.freitas.upgradeddoodle.domain.model;

import br.com.freitas.upgradeddoodle.presentation.exceptions.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_inventories")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantityAvailable = 0;

    @Column(nullable = false)
    private Integer reorderLevel = 0;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id")
    private Product product;


    public void increase(Integer qty) {
        validateQty(qty);
        this.quantityAvailable += qty;
    }

    public void decrease(Integer qty) {
        validateQty(qty);

        if (this.quantityAvailable < qty) {
            throw new BusinessException("Not enough stock available.");
        }

        this.quantityAvailable -= qty;
    }

    public boolean isBelowReorderLevel() {
        return this.quantityAvailable <= this.reorderLevel;
    }

    private void validateQty(Integer qty) {
        if (qty == null || qty <= 0) {
            throw new BusinessException("Quantity must be greater than zero.");
        }
    }
}
