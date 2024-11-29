package tuckos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tuckos.entity.Inventory;
import tuckos.entity.Item;
import tuckos.repository.InventoryRepository;
import tuckos.repository.ItemRepository;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;

    public InventoryService(InventoryRepository inventoryRepository, ItemRepository itemRepository) {
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
    }

    public Optional<Inventory> getItemFromInventory(Long itemId) {
        return inventoryRepository.findByItemId(itemId);
    }

    @Transactional
    public void addItemToInventory(Long itemId, int quantity) {
        // Checks whether the item is already in the inventory
        Optional<Inventory> optionalInventory = getItemFromInventory(itemId);
        if (optionalInventory.isPresent()) {
            Inventory inventory = optionalInventory.get();
            inventory.setQuantity(inventory.getQuantity() + quantity);
            inventoryRepository.save(inventory);
            return;
        }

        // If the item is not in the inventory, add it
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) {
            throw new RuntimeException("Item not found with ID: " + itemId);
        }

        Item item = optionalItem.get();

        Inventory inventory = new Inventory();
        inventory.setItem(item);
        inventory.setQuantity(quantity);
        inventoryRepository.save(inventory);
    }

    public List<Inventory> getItemsFromInventory() {
        return inventoryRepository.findAll();
    }

    public void updateInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

}
