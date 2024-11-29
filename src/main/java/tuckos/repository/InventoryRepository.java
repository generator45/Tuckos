package tuckos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tuckos.entity.Inventory;
import tuckos.entity.Item;

public interface InventoryRepository extends JpaRepository<Inventory, Item> {
    @Query("SELECT i FROM Inventory i WHERE i.item.itemId = :itemId")
    Optional<Inventory> findByItemId(@Param("itemId") Long itemId);
}
