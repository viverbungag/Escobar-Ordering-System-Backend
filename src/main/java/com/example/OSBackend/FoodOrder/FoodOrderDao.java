package com.example.OSBackend.FoodOrder;

public interface FoodOrderDao {

    Long insertFoodOrder(Long menuId, Integer menuQuantity);
}
