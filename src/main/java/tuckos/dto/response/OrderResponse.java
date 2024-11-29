package tuckos.dto.response;

import java.util.List;

import tuckos.entity.Order;

public class OrderResponse {
    private Order order;
    private List<ItemData> items;

    // getters and setters
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<ItemData> getItems() {
        return items;
    }

    public void setItems(List<ItemData> items) {
        this.items = items;
    }
}
