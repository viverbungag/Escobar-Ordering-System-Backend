package com.example.OSBackend.Menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    @GetMapping("/unavailable")
    public List<MenuDto> getAllUnavailableActiveMenu(){
        return menuService.getAllUnavailableActiveMenu();
    }
}
