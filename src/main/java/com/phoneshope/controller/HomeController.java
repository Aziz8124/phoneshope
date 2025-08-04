// تعديل HomeController.java لإضافة ترحيب بالمدير وحساب مسؤول

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

    private List<User> users = new ArrayList<>();

    @GetMapping("/")
    public String welcome() {
        return "welcome";
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
            session.removeAttribute("loggedInUser");
            model.addAttribute("guestWarning", "أنت تشاهد كزائر، بعض الخصائص قد تكون محدودة.");
        }

        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
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
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam String captchaAnswer,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {

        Object answerObj = session.getAttribute("captchaAnswer");
        if (answerObj == null) {
            redirectAttributes.addFlashAttribute("error", "خطأ في التحقق، حاول مرة أخرى");
            return "redirect:/login";
        }

        int correctAnswer = (int) answerObj;
        try {
            int userAnswer = Integer.parseInt(captchaAnswer.trim());
            if (userAnswer != correctAnswer) {
                redirectAttributes.addFlashAttribute("error", "إجابة الكابتشا غير صحيحة!");
                return "redirect:/login";
            }
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "الرجاء إدخال رقم صحيح للكابتشا");
            return "redirect:/login";
        }

        if (username.equals("Ali@gmail.com") && password.equals("Ali@12345")) {
            User admin = new User("Ali", 40, "ذكر", username, password);
            session.setAttribute("loggedInUser", admin);
            session.setAttribute("isAdmin", true);
            redirectAttributes.addFlashAttribute("message", "مرحباً بالمدير 👑");
            return "redirect:/phones";
        }

        for (User u : users) {
            if (u.getEmail().equals(username) && u.getPassword().equals(password)) {
                session.setAttribute("loggedInUser", u);
                session.setAttribute("isAdmin", false);
                return "redirect:/phones";
            }
        }

        redirectAttributes.addFlashAttribute("error", "اسم المستخدم أو كلمة المرور غير صحيحة ❌");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register(Model model, HttpSession session) {
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

        users.add(new User(name, age, gender, email, password));
        redirectAttributes.addFlashAttribute("success", "تم إنشاء الحساب بنجاح! الرجاء تسجيل الدخول");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/account")
    public String accountManagement(HttpSession session, Model model) {
        if (session.getAttribute("loggedInUser") == null) return "redirect:/login";
        return "account"; // صفحة إدارة الحساب الشخصي
    }

   /* @GetMapping("/admin/products")
    public String manageProducts(HttpSession session) {
        if (session.getAttribute("isAdmin") == null || !(Boolean) session.getAttribute("isAdmin")) {
            return "redirect:/phones";
        }
        return "admin-products"; // صفحة إدارة المنتجات (سيتم إنشاؤها لاحقاً)
    }*/
}
