package tuckos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Inventory")
public class Inventory {
    @Id
    private Long itemId;

    @OneToOne
    @JoinColumn(name = "itemId", referencedColumnName = "itemId")
    @MapsId
    private Item item;

    @Column(nullable = false)
    private int quantity;

    // setters and getters

    public int getQuantity() {
        return quantity;
    }

    public Item getItem() {
        return item;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
