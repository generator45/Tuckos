package tuckos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tuckos.dto.request.CartItem;
import tuckos.entity.Inventory;
import tuckos.entity.Order;
import tuckos.entity.OrderItem;
import tuckos.entity.OrderItemId;
import tuckos.repository.OrderItemRepository;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final InventoryService inventoryService;

    public OrderItemService(OrderItemRepository orderItemRepository, InventoryService inventoryService) {
        this.orderItemRepository = orderItemRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public void addItemsToOrder(Order order, List<CartItem> cart) {
        for (CartItem cartItem : cart) {
            // Check if item in stock and sufficient quantity
            Long itemId = cartItem.getItemId();
            int quantity = cartItem.getQuantity();

            Optional<Inventory> optionalInventory = inventoryService.getItemFromInventory(itemId);
            if (optionalInventory.isEmpty()) {
                throw new RuntimeException("Item not found in inventory with ID: " + itemId);
            }

            Inventory inventory = optionalInventory.get();
            if (inventory.getQuantity() < quantity) {
                throw new RuntimeException("Insufficient quantity in stock for item with ID: " + itemId);
            }

            // Update inventory
            inventory.setQuantity(inventory.getQuantity() - quantity);
            inventoryService.updateInventory(inventory);

            // Add item to order
            OrderItem orderItem = new OrderItem();
            orderItem.setId(new OrderItemId(order.getOrderId(), inventory.getItem().getItemId()));
            orderItem.setOrder(order);
            orderItem.setItem(inventory.getItem());
            orderItem.setQuantity(quantity);
            orderItem.setPrice(inventory.getItem().getPrice());
            orderItem.setTotalPrice(quantity * inventory.getItem().getPrice());
            orderItemRepository.save(orderItem);
        }

    }

    public List<OrderItem> getItemsByOrder(Order order) {
        return orderItemRepository.findByOrder(order);
    }
}
