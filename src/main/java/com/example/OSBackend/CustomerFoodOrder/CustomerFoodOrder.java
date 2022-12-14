package com.example.OSBackend.CustomerFoodOrder;

import com.example.OSBackend.FoodOrder.FoodOrder;
import com.example.OSBackend.Order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Entity(name = "customer_food_order")
public class CustomerFoodOrder {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "customer_food_order_id")
    private Long customerFoodOrderId;

    @NonNull
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "food_order_id")
    private FoodOrder foodOrder;

}
