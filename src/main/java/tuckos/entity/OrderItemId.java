package tuckos.entity;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrderItemId implements Serializable {

    @Column(name = "orderId")
    private Long orderId;

    @Column(name = "itemId")
    private Long itemId;

    // Default constructor
    public OrderItemId() {
    }

    // Parameterized constructor
    public OrderItemId(Long orderId, Long itemId) {
        this.orderId = orderId;
        this.itemId = itemId;
    }

    // Getters and setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    // hashCode and equals methods
    @Override
    public int hashCode() {
        return Objects.hash(orderId, itemId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        OrderItemId that = (OrderItemId) obj;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(itemId, that.itemId);
    }
}