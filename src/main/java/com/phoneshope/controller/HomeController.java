package com.phoneshope.controller;

import com.phoneshope.model.Phone;
import com.phoneshope.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class HomeController {

    // قائمة المستخدمين في الذاكرة (للتبسيط، في مشروع حقيقي تستخدم قاعدة بيانات)
    private List<User> users = new ArrayList<>();

    @GetMapping("/")
    public String welcome() {
        return "welcome";  // صفحة الترحيب
    }

    @GetMapping("/phones")
    public String phones(@RequestParam(required = false) Boolean guest, Model model, HttpSession session) {
        List<Phone> phones = List.of(
                new Phone("iPhone 14", 799, "/images/iPhone14.jpg"),
                new Phone("Samsung S23", 699, "/images/SamsungS23.jpg"),
                new Phone("Xiaomi Note 12", 399, "/images/XiaomiNote12.jpg"),
                new Phone("honorX& a", 399, "/images/honorX&a.jpg")
        );
        model.addAttribute("phones", phones);

        if (guest != null && guest) {
            // إزالة بيانات المستخدم المسجل ليكون زائر
            session.removeAttribute("loggedInUser");
            model.addAttribute("guestWarning", "أنت تشاهد كزائر، بعض الخصائص قد تكون محدودة.");
        }

        // عرض اسم المستخدم إذا مسجل دخول
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser != null) {
            model.addAttribute("userName", loggedInUser.getName());
        }

        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        // كابتشا عشوائية
        Random rand = new Random();
        int num1 = rand.nextInt(10) + 1;
        int num2 = rand.nextInt(10) + 1;
        String[] ops = {"+", "-", "*"};
        String op = ops[rand.nextInt(ops.length)];

        int answer = switch (op) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            default -> num1 * num2;
        };

        session.setAttribute("captchaAnswer", answer);
        model.addAttribute("captchaQuestion", num1 + " " + op + " " + num2 + " = ؟");

        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {

        for (User u : users) {
            if (u.getEmail().equals(username) && u.getPassword().equals(password)) {
                session.setAttribute("loggedInUser", u);

                // ✅ تعيين صلاحية مسؤول إذا كان البريد يطابق admin@example.com
                if ("admin@example.com".equalsIgnoreCase(u.getEmail())) {
                    session.setAttribute("isAdmin", true);
                } else {
                    session.removeAttribute("isAdmin");
                }

                return "redirect:/phones";
            }
        }

        model.addAttribute("error", "البريد الإلكتروني أو كلمة المرور غير صحيحة");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, HttpSession session) {
        // كابتشا
        Random rand = new Random();
        int num1 = rand.nextInt(10) + 1;
        int num2 = rand.nextInt(10) + 1;

        String[] ops = {"+", "-", "*"};
        String op = ops[rand.nextInt(ops.length)];

        int answer = 0;
        switch (op) {
            case "+" -> answer = num1 + num2;
            case "-" -> answer = num1 - num2;
            case "*" -> answer = num1 * num2;
        }

        session.setAttribute("captchaAnswer", answer);
        model.addAttribute("captchaQuestion", num1 + " " + op + " " + num2 + " = ؟");

        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam String name,
                                 @RequestParam int age,
                                 @RequestParam String gender,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String captchaAnswer,
                                 RedirectAttributes redirectAttributes,
                                 HttpSession session) {

        Object answerObj = session.getAttribute("captchaAnswer");
        if (answerObj == null) {
            redirectAttributes.addFlashAttribute("error", "خطأ في التحقق، حاول مرة أخرى");
            return "redirect:/register";
        }

        int correctAnswer = (int) answerObj;
        try {
            int userAnswer = Integer.parseInt(captchaAnswer.trim());
            if (userAnswer != correctAnswer) {
                redirectAttributes.addFlashAttribute("error", "إجابة الكابتشا غير صحيحة!");
                return "redirect:/register";
            }
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "الرجاء إدخال رقم صحيح للكابتشا");
            return "redirect:/register";
        }

        // إضافة مستخدم جديد
        users.add(new User(name, age, gender, email, password));
        redirectAttributes.addFlashAttribute("success", "تم إنشاء الحساب بنجاح! الرجاء تسجيل الدخول");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}