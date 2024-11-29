package tuckos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tuckos.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.student.rollNo = :rollNo")
    List<Order> findByStudentRollNo(@Param("rollNo") String rollNo);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = 'PENDING'")
    List<Order> findAllPendingOrders();
}
