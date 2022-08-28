package com.example.OSBackend.CustomerFoodOrder;

import com.example.OSBackend.FoodOrder.FoodOrderDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerFoodOrderDto {

    private Long customerFoodOrderId;
    private FoodOrderDto foodOrder;
}
