package com.example.OSBackend.Order;

import com.example.OSBackend.Menu.MenuDto;
import com.example.OSBackend.Pagination.PaginationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/v1/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/paged")
    public Map<String, Object> getAllPagedOrders(@RequestBody PaginationDto paginationDto){
        return orderService.getAllPagedOrders(paginationDto);
    }

    @PostMapping("/menu-on-category")
    public List<MenuDto> getMenuBasedOnCategory(@RequestBody MenuOnCategoryDto menuOnCategoryDto){
        return orderService.getMenuBasedOnCategory(menuOnCategoryDto);
    }

    @PostMapping("/ordered")
    public void order (@RequestBody OrderListDto orderListDto){
       orderService.order(orderListDto);
    }

}
