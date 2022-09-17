package com.example.OSBackend.Menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("menu_mysql")
public interface MenuMySqlRepository extends MenuDao, JpaRepository<Menu, Long> {

    @Query(value = "SELECT * FROM #{#entityName} menu " +
            "INNER JOIN menu_category AS menu_category ON menu.menu_category_id = menu_category.menu_category_id " +
            "WHERE menu.is_active=true AND menu_category.menu_category_name = :menuCategoryName",
            nativeQuery = true)
    List<Menu> getMenuBasedOnCategory(@Param("menuCategoryName") String menuCategoryName);

    @Query(value = "SELECT * FROM #{#entityName} WHERE is_active=true",
            nativeQuery = true)
    List<Menu> getAllActiveMenu();

    @Query(value = "SELECT * FROM #{#entityName} WHERE menu_id = :menuId",
            nativeQuery = true)
    Optional<Menu> getMenuById(@Param("menuId") Long menuId);

    @Query(value = "SELECT * FROM #{#entityName} WHERE menu_name = :menuName",
            nativeQuery = true)
    Optional<Menu> getMenuByName(@Param("menuName") String menuName);
}
