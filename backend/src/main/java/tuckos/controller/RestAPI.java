package tuckos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tuckos.dto.request.CreateOrderRequest;
import tuckos.dto.request.InventoryRequest;
import tuckos.dto.response.ItemData;
import tuckos.dto.response.OrderResponse;
import tuckos.entity.Inventory;
import tuckos.entity.Item;
import tuckos.entity.Order;
import tuckos.entity.OrderItem;
import tuckos.entity.Student;
import tuckos.service.InventoryService;
import tuckos.service.ItemService;
import tuckos.service.OrderItemService;
import tuckos.service.OrderService;
import tuckos.service.StudentService;

@RestController
@RequestMapping("/api")
public class RestAPI {
    private final ItemService itemService;
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final StudentService studentService;
    private final OrderItemService orderItemService;

    public RestAPI(ItemService itemService, InventoryService inventoryService, OrderService orderService,
            StudentService studentService, OrderItemService orderItemService) {
        this.itemService = itemService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
        this.studentService = studentService;
        this.orderItemService = orderItemService;
    }

    // Auth: Only employees can add items
    @PostMapping("/item")
    public ResponseEntity<String> addItem(@RequestBody Item item) {
        try {
            itemService.addItem(item);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
        return ResponseEntity.ok(item.toString());
    }

    // Auth: Only employees can view items
    @GetMapping("/items")
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getItems();
        return ResponseEntity.ok(items);
    }

    // Auth: Only employees can view items
    @GetMapping("/item/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        try {
            Item item = itemService.getItemById(id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Auth: Only admin can add to inventory
    @PostMapping("/inventory")
    public ResponseEntity<String> addItemToInventory(@RequestBody InventoryRequest inventoryRequest) {
        try {
            inventoryService.addItemToInventory(inventoryRequest.itemId, inventoryRequest.quantity);
            return ResponseEntity.ok("Item added to inventory");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Auth: everyone can view inventory
    @GetMapping("/inventory")
    public ResponseEntity<?> getItemsFromInventory() {
        try {
            List<Inventory> items = inventoryService.getItemsFromInventory();
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Auth: Only students can create orders
    // Will get JWT header and get student details from there
    @PostMapping("/order")
    public ResponseEntity<String> createOrder(@RequestBody CreateOrderRequest req) {
        try {
            Student student = studentService.getStudent(req.getRollNo());
            if (student == null) {
                throw new IllegalArgumentException("Student not found");
            }
            orderService.createOrder(student, req.getCart());

            return ResponseEntity.ok("Order created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(@RequestBody String rollNo) {
        try {
            return ResponseEntity.ok(orderService.getOrdersByStudentRollNo(rollNo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id, @RequestBody String rollNo) {
        try {
            Order order = orderService.getOrderById(id);
            if (!order.getStudent().getRollNo().equals(rollNo)) {
                return ResponseEntity.badRequest().body("Not your order");
            }

            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setOrder(order);
            List<ItemData> items = new ArrayList<>();

            List<OrderItem> orderItems = orderItemService.getItemsByOrder(order);
            for (OrderItem orderItem : orderItems) {
                ItemData itemData = new ItemData();
                itemData.setItemName(orderItem.getItem().getItemName());
                itemData.setPrice(orderItem.getPrice());
                itemData.setQuantity(orderItem.getQuantity());
                itemData.setTotalPrice(orderItem.getTotalPrice());
                items.add(itemData);
            }
            orderResponse.setItems(items);

            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/user-orders")
    public ResponseEntity<?> getAllUserOrders() {
        try {
            return ResponseEntity.ok(orderService.getAllPendingOrders());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/order/{id}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id) {
        try {
            orderService.fulfillOrder(id);
            return ResponseEntity.ok("Order status updated");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Student student) {
        try {
            studentService.addStudent(student);
            return ResponseEntity.ok("Signed up successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Student student) {
        try {
            Student studentFromDb = studentService.getStudent(student.getRollNo());
            if (studentFromDb == null) {
                return ResponseEntity.badRequest().body("Student not found");
            }
            if (studentFromDb.getPassword().equals(student.getPassword())) {
                return ResponseEntity.ok("Login successful");
            } else {
                return ResponseEntity.badRequest().body("Incorrect password");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
