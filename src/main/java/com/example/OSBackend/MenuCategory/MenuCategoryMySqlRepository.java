package com.example.OSBackend.MenuCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("menuCategory_mysql")
public interface MenuCategoryMySqlRepository extends MenuCategoryDao, JpaRepository<MenuCategory, Long> {

    @Query(value = "SELECT * FROM #{#entityName} WHERE is_active=true",
            nativeQuery = true)
    List<MenuCategory> getAllActiveMenuCategoriesList();

}
