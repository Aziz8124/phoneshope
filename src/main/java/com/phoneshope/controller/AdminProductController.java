package com.phoneshope.controller;

import com.phoneshope.model.Phone;
import jakarta.servlet.http.HttpSession;
import com.phoneshope.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    // قائمة مؤقتة لتخزين المنتجات (يمكن ربطها بقاعدة بيانات لاحقًا)
    private List<Phone> phones = new ArrayList<>();

    // طريقة لتعيين المسؤول (للتجربة فقط، في مشروع حقيقي تستخدم نظام مصادقة)
    private final String ADMIN_EMAIL = "admin@example.com";

    // عرض صفحة إدارة المنتجات
    @GetMapping
    public String showProductAdminPage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !ADMIN_EMAIL.equalsIgnoreCase(loggedInUser.getEmail())) {
            // غير مسموح - إعادة التوجيه للصفحة الرئيسية أو صفحة الهواتف
            return "redirect:/phones";
        }
        model.addAttribute("phones", phones);
        return "admin_products";
    }

    // إضافة منتج جديد
    @PostMapping("/add")
    public String addPhone(@RequestParam String name,
                           @RequestParam double price,
                           @RequestParam String imageUrl,
                           HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !ADMIN_EMAIL.equalsIgnoreCase(loggedInUser.getEmail())) {
            return "redirect:/phones";
        }

        phones.add(new Phone(name, price, imageUrl));
        return "redirect:/admin/products";
    }

    // حذف منتج
    @PostMapping("/delete")
    public String deletePhone(@RequestParam String name, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !ADMIN_EMAIL.equalsIgnoreCase(loggedInUser.getEmail())) {
            return "redirect:/phones";
        }

        phones.removeIf(p -> p.getName().equals(name));
        return "redirect:/admin/products";
    }

    // تعديل سعر المنتج
    @PostMapping("/edit")
    public String editPhonePrice(@RequestParam String name,
                                 @RequestParam double newPrice,
                                 HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !ADMIN_EMAIL.equalsIgnoreCase(loggedInUser.getEmail())) {
            return "redirect:/phones";
        }

        for (Phone p : phones) {
            if (p.getName().equals(name)) {
                p.setPrice(newPrice);
                break;
            }
        }
        return "redirect:/admin/products";
    }
}
