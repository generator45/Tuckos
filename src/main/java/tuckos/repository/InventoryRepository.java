package tuckos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tuckos.entity.Inventory;
import tuckos.entity.Item;

public interface InventoryRepository extends JpaRepository<Inventory, Item> {

}
