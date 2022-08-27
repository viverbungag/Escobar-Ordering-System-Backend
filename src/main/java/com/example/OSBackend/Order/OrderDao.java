package com.example.OSBackend.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface OrderDao {

    Page<Order> getAllPagedOrders(Pageable pageable);

    void insertOrder(Long employeeId, LocalDateTime orderTime, BigDecimal payment, BigDecimal totalCost);
}
