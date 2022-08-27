package com.example.OSBackend.Order;

import com.example.OSBackend.MenuIngredients.MenuIngredientsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class OrderMenuDto {

    private Long menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private Integer menuOrderQuantity;
    private String menuCategoryName;
    private List<MenuIngredientsDto> ingredients;
    private Integer numberOfServingsLeft;
    private Boolean isActive;
}
