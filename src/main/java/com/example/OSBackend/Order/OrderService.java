package com.example.OSBackend.Order;

import com.example.OSBackend.CustomerFoodOrder.CustomerFoodOrder;
import com.example.OSBackend.CustomerFoodOrder.CustomerFoodOrderDto;
import com.example.OSBackend.Employee.Employee;
import com.example.OSBackend.Employee.EmployeeDao;
import com.example.OSBackend.Employee.Exceptions.EmployeeNotFoundException;
import com.example.OSBackend.FoodOrder.FoodOrder;
import com.example.OSBackend.FoodOrder.FoodOrderDao;
import com.example.OSBackend.FoodOrder.FoodOrderDto;
import com.example.OSBackend.Menu.Exceptions.MenuNotFoundException;
import com.example.OSBackend.Menu.Menu;
import com.example.OSBackend.Menu.MenuDao;
import com.example.OSBackend.Menu.MenuDto;
import com.example.OSBackend.MenuIngredients.MenuIngredients;
import com.example.OSBackend.MenuIngredients.MenuIngredientsDto;
import com.example.OSBackend.Order.Exceptions.OrderDiscountOutOfRangeException;
import com.example.OSBackend.Order.Exceptions.OrderNotFoundException;
import com.example.OSBackend.Pagination.PaginationDto;
import com.example.OSBackend.Supply.Exceptions.SupplyNotFoundException;
import com.example.OSBackend.Supply.Supply;
import com.example.OSBackend.Supply.SupplyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Qualifier("order_jdbc_mysql")
    OrderDao orderJdbcRepository;

    @Autowired
    @Qualifier("menu_mysql")
    MenuDao menuRepository;

    @Autowired
    @Qualifier("employee_mysql")
    EmployeeDao employeeRepository;

    @Autowired
    @Qualifier("supply_mysql")
    SupplyDao supplyRepository;

    @Autowired
    @Qualifier("foodOrder_jdbc_mysql")
    FoodOrderDao foodOrderJdbcRepository;

    private FoodOrderDto convertEntityToDto(FoodOrder foodOrder){
        return new FoodOrderDto(
                foodOrder.getFoodOrderId(),
                convertEntityToDto(foodOrder.getMenu()),
                foodOrder.getMenuQuantity());
    }

    private CustomerFoodOrderDto convertEntityToDto(CustomerFoodOrder customerFoodOrder){
        return new CustomerFoodOrderDto(
                customerFoodOrder.getCustomerFoodOrderId(),
                convertEntityToDto(customerFoodOrder.getFoodOrder()));
    }


    private OrderDto convertEntityToDto(Order order){
        return new OrderDto(
                order.getOrderId(),
                String.format("%s, %s", order.getEmployee().getLastName(), order.getEmployee().getFirstName()),
                order.getOrderTime(),
                order.getCustomerFoodOrders()
                        .stream()
                        .map((customerFoodOrder) ->
                                convertEntityToDto(customerFoodOrder))
                        .collect(Collectors.toList()),
                order.getPayment(),
                order.getDiscount(),
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
                            .filter(currentIngredient -> supply.getSupplyName().equals(currentIngredient.getSupplyName()))
                            .findFirst()
                            .orElseGet(
                                    () -> new MenuIngredientsDto(1L, "", 0.0, ""))
                            .getQuantity() * menuOrder.getOrderMenuQuantity())
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
        List<OrderMenuDto> orderMenuOnCart = menuOnCategoryDto.getOrderMenu();
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


    public void addOrder(OrderDto orderDto) {
        //TODO: When Customer ordered (Similar to stock out of transactions)
        String[] employeeNameSplit = orderDto.getEmployeeFullName().split(", ");
        String employeeLastName = employeeNameSplit[0];
        String employeeFirstName = employeeNameSplit[1];
        LocalDateTime orderTime = orderDto.getOrderTime();
        BigDecimal payment = orderDto.getPayment();
        BigDecimal totalCost = orderDto.getTotalCost();
        BigDecimal discount = orderDto.getDiscount();
        List<CustomerFoodOrderDto> customerFoodOrders = orderDto.getCustomerFoodOrders();

        if (discount == null ||
                discount.compareTo(new BigDecimal(0)) < 0 ||
                discount.compareTo(new BigDecimal(100)) > 0){
            throw new OrderDiscountOutOfRangeException();
        }

        Employee employee = employeeRepository
                .getEmployeeByFirstAndLastName(employeeFirstName, employeeLastName)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeFirstName, employeeLastName));

        Long orderId = orderJdbcRepository.insertOrder(
                employee.getEmployeeId(),
                orderTime,
                payment,
                totalCost,
                discount);


        customerFoodOrders
                .stream()
                .forEach((customerFoodOrder) -> {
                    String menuName = customerFoodOrder.getFoodOrder().getMenu().getMenuName();
                    Integer menuQuantity = customerFoodOrder.getFoodOrder().getMenuQuantity();

                    Menu menu = menuRepository
                            .getMenuByName(menuName)
                            .orElseThrow(() -> new MenuNotFoundException(menuName));

                    Long foodOrderId = foodOrderJdbcRepository.insertFoodOrder(menu.getMenuId(), menuQuantity);

                    orderRepository.insertCustomerFoodOrder(foodOrderId, orderId);

                    menu.getMenuIngredients()
                            .stream()
                            .forEach((ingredient) -> {
                                Supply ingredientSupply = ingredient.getSupply();

                                Supply supply = supplyRepository
                                        .getSupplyByName(ingredientSupply.getSupplyName())
                                        .orElseThrow(() -> new SupplyNotFoundException(ingredientSupply.getSupplyName()));

                                Double newQuantity = supply.getSupplyQuantity() - (ingredient.getQuantity() * menuQuantity);

                                supply.setSupplyQuantity(newQuantity);
                            });
                });
    }

    public void voidOrder(Long orderId) {

        Order order = orderRepository
                .getOrderByOrderId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

//        List<CustomerFoodOrder> customerFoodOrders = order.getCustomerFoodOrders()
//                .stream()
//                .map((customerFoodOrder) ->
//                        new CustomerFoodOrder(
//                                customerFoodOrder.getCustomerFoodOrderId(),
//                                null,
//                                new FoodOrder(
//                                        customerFoodOrder.getFoodOrder().getFoodOrderId(),
//                                        menuRepository
//                                                .getMenuByName(customerFoodOrder.getFoodOrder().getMenu().getMenuName())
//                                                .orElseThrow(() -> new MenuNotFoundException(customerFoodOrder.getFoodOrder().getMenu().getMenuName())),
//                                        customerFoodOrder.getFoodOrder().getMenuQuantity())))
//                .collect(Collectors.toList());


        order.getCustomerFoodOrders()
                .stream()
                .forEach((customerFoodOrder) -> {
                    Menu menu = customerFoodOrder.getFoodOrder().getMenu();
                    Integer menuQuantity = customerFoodOrder.getFoodOrder().getMenuQuantity();

                    FoodOrder foodOrder = customerFoodOrder.getFoodOrder();

                    orderRepository.insertCustomerFoodOrder(foodOrder.getFoodOrderId(), order.getOrderId());

                    menu.getMenuIngredients()
                            .stream()
                            .forEach((ingredient) -> {
                                Supply ingredientSupply = ingredient.getSupply();

                                Supply supply = supplyRepository
                                        .getSupplyByName(ingredientSupply.getSupplyName())
                                        .orElseThrow(() -> new SupplyNotFoundException(ingredientSupply.getSupplyName()));

                                Double newQuantity = supply.getSupplyQuantity() + (ingredient.getQuantity() * menuQuantity);

                                supply.setSupplyQuantity(newQuantity);
                            });
                });



        orderRepository.removeOrder(orderId);
    }
}
