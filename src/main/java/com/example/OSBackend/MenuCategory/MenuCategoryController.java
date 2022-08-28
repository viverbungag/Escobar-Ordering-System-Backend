package com.example.OSBackend.MenuCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/v1/menu-category")
public class MenuCategoryController {

    @Autowired
    MenuCategoryService menuCategoryService;

    @GetMapping
    public List<String> getAllActiveMenuCategoryNames(){
        return menuCategoryService.getAllActiveMenuCategoryNames();
    }

}
