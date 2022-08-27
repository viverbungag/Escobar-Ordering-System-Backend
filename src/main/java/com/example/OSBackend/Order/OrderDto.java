package com.example.OSBackend.Order;

import com.example.OSBackend.CustomerFoodOrder.CustomerFoodOrder;
import com.example.OSBackend.Employee.Employee;
import com.example.OSBackend.FoodOrder.FoodOrder;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderDto {

    private Long orderId;
    private Employee employee;
    private LocalDateTime serveTime;
    private List<CustomerFoodOrder> customerFoodOrders;
    private BigDecimal payment;
    private BigDecimal totalCost;
}
