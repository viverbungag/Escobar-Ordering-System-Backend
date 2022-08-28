package com.example.OSBackend.MenuCategory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuCategoryService {

    @Autowired
    @Qualifier("menuCategory_mysql")
    MenuCategoryDao menuCategoryRepository;

    public List<String> getAllActiveMenuCategoryNames(){
        return menuCategoryRepository
                .getAllActiveMenuCategoriesList()
                .stream()
                .map((MenuCategory menuCategory)-> menuCategory.getMenuCategoryName())
                .collect(Collectors.toList());
    }

}
