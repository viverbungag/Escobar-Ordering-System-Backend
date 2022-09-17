package com.example.OSBackend.Menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MenuDao {

    List<Menu> getMenuBasedOnCategory(String menuCategoryName);

    Optional<Menu> getMenuById(Long menuId);

    Optional<Menu> getMenuByName(String menuName);

    List<Menu> getAllActiveMenu();

}
