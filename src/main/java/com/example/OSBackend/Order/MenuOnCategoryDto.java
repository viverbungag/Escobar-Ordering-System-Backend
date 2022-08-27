package com.example.OSBackend.Order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MenuOnCategoryDto {

    String menuCategoryName;
    List<OrderMenuDto> orderMenuDto;
}
