package com.example.OSBackend.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository("order_mysql")
public interface OrderMySqlRepository extends  OrderDao, JpaRepository<Order, Long> {

    @Query(value = "SELECT * FROM #{#entityName}",
            countQuery = "SELECT * FROM #{#entityName}",
            nativeQuery = true)
    Page<Order> getAllPagedOrders(Pageable pageable);

    @Query(value = "INSERT INTO #{#entityName}(employee_id, order_time, payment, total_cost) " +
            "VALUES (:employeeId, :orderTime, :payment, :totalCost)",
            nativeQuery = true)
    void insertOrder(@Param("employeeId")Long employeeId,
                     @Param("orderTime")LocalDateTime orderTime,
                     @Param("payment")BigDecimal payment,
                     @Param("totalCost")BigDecimal totalCost);
}
