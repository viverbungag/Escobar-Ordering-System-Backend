package com.example.OSBackend.MenuCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuCategoryDao {

    List<MenuCategory> getAllActiveMenuCategoriesList();

}
