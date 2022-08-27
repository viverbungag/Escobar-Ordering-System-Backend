package com.example.OSBackend.Menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("menu_mysql")
public interface MenuMySqlRepository extends MenuDao, JpaRepository<Menu, Long> {

    @Query(value = "SELECT * FROM #{#entityName} WHERE menu_category = :menuCategoryName",
            nativeQuery = true)
    List<Menu> getMenuBasedOnCategory(@Param("menuCategoryName") String menuCategoryName);
}
