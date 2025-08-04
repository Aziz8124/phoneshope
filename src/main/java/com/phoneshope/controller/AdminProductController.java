package com.phoneshope.controller;

import com.phoneshope.model.Phone;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    // قائمة مؤقتة لتخزين المنتجات
    private List<Phone> phones = new ArrayList<>();

    // عرض صفحة الإدارة
    @GetMapping
    public String showProductAdminPage(Model model, HttpSession session) {
        Object isAdmin = session.getAttribute("isAdmin");
        if (isAdmin == null || !(Boolean) isAdmin) {
            return "redirect:/phones";
        }
        model.addAttribute("phones", phones);
        return "admin_products";
    }

    // إضافة منتج
    @PostMapping("/add")
    public String addPhone(@RequestParam String name,
                           @RequestParam double price,
                           @RequestParam String imageUrl,
                           HttpSession session) {
        phones.add(new Phone(name, price, imageUrl));
        return "redirect:/admin/products";
    }

    // حذف منتج
    @PostMapping("/delete")
    public String deletePhone(@RequestParam String name) {
        phones.removeIf(p -> p.getName().equals(name));
        return "redirect:/admin/products";
    }

    // تعديل السعر
    @PostMapping("/edit")
    public String editPhonePrice(@RequestParam String name,
                                 @RequestParam double newPrice) {
        for (Phone p : phones) {
            if (p.getName().equals(name)) {
                p.setPrice(newPrice);
                break;
            }
        }
        return "redirect:/admin/products";
    }
} 
