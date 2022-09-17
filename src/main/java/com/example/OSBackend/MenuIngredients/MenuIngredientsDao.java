package com.example.OSBackend.MenuIngredients;

import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MenuIngredientsDao {

    Optional<MenuIngredients> getMenuIngredientsById(Long menuIngredientsId);
    Optional<MenuIngredients> getMenuIngredientsByName(String menuIngredientsName);
    void deleteAllMenuIngredientsByMenuId(Long menuId);
}
