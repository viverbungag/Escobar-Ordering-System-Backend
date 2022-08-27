package com.example.OSBackend.Order;

import com.example.OSBackend.Menu.Menu;
import com.example.OSBackend.Menu.MenuDao;
import com.example.OSBackend.Menu.MenuDto;
import com.example.OSBackend.MenuIngredients.MenuIngredients;
import com.example.OSBackend.MenuIngredients.MenuIngredientsDto;
import com.example.OSBackend.Pagination.PaginationDto;
import com.example.OSBackend.Supply.Supply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    @Qualifier("order_mysql")
    OrderDao orderRepository;

    @Autowired
    @Qualifier("menu_mysql")
    MenuDao menuRepository;


    private OrderDto convertEntityToDto(Order order){
        return new OrderDto(
                order.getOrderId(),
                order.getEmployee(),
                order.getOrderTime(),
                order.getCustomerFoodOrders(),
                order.getPayment(),
                order.getTotalCost()
        );
    }

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

    private Sort getSortingMethod(Boolean isAscending, Sort sort){
        if (isAscending){
            return sort.ascending();
        }
        return sort.descending();
    }

    private Sort getSortingValue(String sortedBy){
        return Sort.unsorted();
    }

    private Pageable initializePageable(PaginationDto paginationDto){
        int pageNo = paginationDto.getPageNo();
        int pageSize = paginationDto.getPageSize();
        Boolean isAscending = paginationDto.getIsAscending();
        String sortedBy = paginationDto.getSortedBy();

        Sort sort = getSortingValue(sortedBy);
        Sort finalSort = getSortingMethod(isAscending, sort);

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, finalSort);

        return pageable;
    }

    private Map<String, Object> initializeOrderWithPageDetails(Page<Order> orderPage, PaginationDto paginationDto){
        Integer pageNo = paginationDto.getPageNo();
        Integer totalPages = orderPage.getTotalPages();
        Long totalCount = orderPage.getTotalElements();

        Map<String, Object> orderWithPageDetails = new HashMap<>();

        if (pageNo < 1 || pageNo > totalPages){
            orderWithPageDetails.put("contents", new ArrayList<>());
            orderWithPageDetails.put("totalPages", 0);
            orderWithPageDetails.put("totalCount", 0);
            return orderWithPageDetails;
        }

        orderWithPageDetails.put("contents",
                orderPage
                        .getContent()
                        .stream()
                        .map((Order order)-> convertEntityToDto(order))
                        .collect(Collectors.toList()));



        orderWithPageDetails.put("totalPages", totalPages);
        orderWithPageDetails.put("totalCount", totalCount);
        return orderWithPageDetails;
    }

    public Map<String, Object> getAllPagedOrders(PaginationDto paginationDto) {
        Pageable pageable = initializePageable(paginationDto);
        Page<Order> orderPage = orderRepository
                .getAllPagedOrders(pageable);

        return initializeOrderWithPageDetails(orderPage, paginationDto);
    }

    private Integer calculateNumberOfServingsLeft (Menu menu, List<OrderMenuDto> menuOnCart) {
        Integer currentNumberOfServings = null;

        if (menu.getMenuIngredients().size() == 0){
            return 0;
        }

        for (MenuIngredients ingredient: menu.getMenuIngredients()){
            Double ingredientQuantity = ingredient.getQuantity();
            Supply supply = ingredient.getSupply();

            Double reducedQuantity = menuOnCart
                    .stream()
                    .map((menuOrder) -> menuOrder
                            .getIngredients()
                            .stream()
                            .filter(currentIngredient -> supply.getSupplyName() == currentIngredient.getSupplyName())
                            .findFirst()
                            .orElseGet(
                                    () -> new MenuIngredientsDto(1L, "", 0.0, ""))
                            .getQuantity() * menuOrder.getMenuOrderQuantity())
                    .reduce(0.0, (sum, currentQuantity) -> sum + currentQuantity);

            Double supplyQuantity = supply.getSupplyQuantity() - reducedQuantity;

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

    public List<MenuDto> getMenuBasedOnCategory(MenuOnCategoryDto menuOnCategoryDto) {
        List<OrderMenuDto> orderMenuOnCart = menuOnCategoryDto.getOrderMenuDto();
        String menuCategoryName = menuOnCategoryDto.getMenuCategoryName();

        List<MenuDto> menus = menuRepository
                .getMenuBasedOnCategory(menuCategoryName)
                .stream()
                .map((Menu menu)-> {
                    menu.setNumberOfServingsLeft(calculateNumberOfServingsLeft(menu, orderMenuOnCart));
                    return convertEntityToDto(menu);
                })
                .collect(Collectors.toList());

        return menus;
    }


    public void order(OrderListDto orderListDto) {
        //TODO: When Customer ordered (Similar to stock out of transactions)
    }
}
