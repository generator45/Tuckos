package tuckos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tuckos.dto.request.CartItem;
import tuckos.entity.Order;
import tuckos.entity.Student;
import tuckos.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;

    public OrderService(OrderRepository orderRepository, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }

    @Transactional
    public void createOrder(Student student, List<CartItem> cartItem) {
        Order order = new Order();
        order.setStudent(student);
        orderRepository.save(order);
        orderItemService.addItemsToOrder(order, cartItem);
    }

    public List<Order> getOrdersByStudentRollNo(String rollNo) {
        return orderRepository.findByStudentRollNo(rollNo);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getAllPendingOrders() {
        return orderRepository.findAllPendingOrders();
    }

    public void fulfillOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.setOrderStatus("FULFILLED");
        orderRepository.save(order);
    }
}
