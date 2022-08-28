package com.example.OSBackend.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderDao {

    Page<Order> getAllPagedOrders(Pageable pageable);

    void insertOrder(Long employeeId, LocalDateTime orderTime, BigDecimal payment, BigDecimal totalCost);

    void insertCustomerFoodOrder(Long foodOrderId, Long orderId);

    Optional<Order> getOrderByOrderTime(LocalDateTime orderTime);

    void removeOrder(LocalDateTime orderTime);
}
