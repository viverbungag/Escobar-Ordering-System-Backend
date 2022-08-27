package com.example.OSBackend.Order;

import com.example.OSBackend.Customer.Customer;
import com.example.OSBackend.Employee.Employee;
import com.example.OSBackend.FoodOrder.FoodOrder;
import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "serving_time")
    private LocalDateTime servingTime;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "food_order_id")
    private FoodOrder foodOrder;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "total_cost")
    private BigDecimal totalCost;
}
