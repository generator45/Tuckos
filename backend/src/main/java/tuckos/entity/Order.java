package tuckos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "OrderHistory")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private String orderStatus = "PENDING";

    @Column(nullable = false)
    private String date = LocalDate.now().toString();

    // On student can have many orders
    @ManyToOne
    @JoinColumn(name = "rollNo", referencedColumnName = "rollNo")
    private Student student;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @JsonIgnore
    public Student getStudent() {
        return student;
    }
}
