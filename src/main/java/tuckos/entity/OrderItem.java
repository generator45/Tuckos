package tuckos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "OrderItem")
public class OrderItem {

    @EmbeddedId
    private OrderItemId id;

    // one order can have many orderItems
    @ManyToOne
    @JoinColumn(name = "orderId", insertable = false, updatable = false)
    private Order order;

    // one item can be in many orderItems
    @ManyToOne
    @JoinColumn(name = "itemId", insertable = false, updatable = false)
    private Item item;

    @Column(nullable = false)
    private int quantity;

    // storing copy of price at that time
    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double totalPrice;

    // Getters and setters
    public OrderItemId getId() {
        return id;
    }

    public void setId(OrderItemId id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}