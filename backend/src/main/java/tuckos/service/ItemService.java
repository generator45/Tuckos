package tuckos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import tuckos.entity.Item;
import tuckos.repository.ItemRepository;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Add item to the database
    public void addItem(Item item) {
        itemRepository.save(item);

    }

    // Get all items from the database
    public List<Item> getItems() {
        return itemRepository.findAll();

    }

    // Get item by ID
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found with ID: " + id));

    }

}
