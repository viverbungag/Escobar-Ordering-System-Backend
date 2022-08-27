package com.example.OSBackend.FoodOrder;

import com.example.OSBackend.Menu.Menu;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity(name = "food_order")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "food_order_id")
    private Long foodOrderId;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "order_quantity")
    private Integer orderQuantity;

}
