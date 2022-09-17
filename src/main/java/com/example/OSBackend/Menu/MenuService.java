package com.example.OSBackend.Menu;

import com.example.OSBackend.MenuIngredients.MenuIngredients;
import com.example.OSBackend.MenuIngredients.MenuIngredientsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    @Autowired
    @Qualifier("menu_mysql")
    MenuDao menuRepository;

    private MenuDto convertEntityToDto(Menu menu){
        return new MenuDto(
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getMenuPrice(),
                menu.getMenuCategory().getMenuCategoryName(),

                menu.getMenuIngredients()
                        .stream()
                        .map((MenuIngredients menuIngredients) ->
                                new MenuIngredientsDto(
                                        menuIngredients.getMenuIngredientsId(),
                                        menuIngredients.getSupply().getSupplyName(),
                                        menuIngredients.getQuantity(),
                                        menuIngredients.getSupply().getUnitOfMeasurement().getUnitOfMeasurementAbbreviation()))
                        .collect(Collectors.toList()),

                menu.getNumberOfServingsLeft(),
                menu.getIsActive()
        );
    }

    private Integer calculateNumberOfServingsLeft (Menu menu) {
        Integer currentNumberOfServings = null;

        if (menu.getMenuIngredients().size() == 0){
            return 0;
        }

        for (MenuIngredients ingredient: menu.getMenuIngredients()){
            Double ingredientQuantity = ingredient.getQuantity();
            Double supplyQuantity = ingredient.getSupply().getSupplyQuantity();

            Integer ingredientAvailableServings = Integer.valueOf((int)Math.floor(supplyQuantity / ingredientQuantity));

            if (currentNumberOfServings == null || ingredientAvailableServings < currentNumberOfServings){
                currentNumberOfServings = ingredientAvailableServings;
            }

            if (ingredientAvailableServings <= 0){
                currentNumberOfServings = 0;
            }
        }

        return currentNumberOfServings;
    }

    public List<MenuDto> getAllUnavailableActiveMenu(){
        List<MenuDto> unavailableActiveMenu = menuRepository
                .getAllActiveMenu()
                .stream()
                .map((menu) -> {
                    MenuDto menuDto = convertEntityToDto(menu);
                    menuDto.setNumberOfServingsLeft(calculateNumberOfServingsLeft(menu));
                    return menuDto;
                })
                .filter((menuDto) -> menuDto.getNumberOfServingsLeft() <= 0)
                .collect(Collectors.toList());

        return unavailableActiveMenu;
    }
}
