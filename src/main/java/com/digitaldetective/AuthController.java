
package com.digitaldetective;

import com.digitaldetective.database.UserDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        if (name.isBlank() || email.isBlank()
                || password.isBlank() || confirmPassword.isBlank()) {
            model.addAttribute("error", "Please fill in all fields.");
            return "register";
        }

        if (!email.contains("@") || !email.contains(".")) {
            model.addAttribute("error", "Enter a valid email address.");
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        try {
            boolean registered =
                    UserDAO.registerUser(name, email, password);

            if (registered) {
                return "redirect:/login?registered";
            }

            model.addAttribute("error", "Registration could not be completed.");

        } catch (SQLException ex) {
            if (ex.getMessage() != null &&
                    ex.getMessage().contains("UNIQUE")) {
                model.addAttribute("error", "This email is already registered.");
            } else {
                model.addAttribute("error", "Database error during registration.");
                ex.printStackTrace();
            }
        }

        return "register";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        try {
            boolean valid = UserDAO.loginUser(email, password);

            if (valid) {
                int userId = UserDAO.getUserIdByEmail(email);

                session.setAttribute("userId", userId);
                session.setAttribute("userEmail", email);

                return "redirect:/dashboard";
            }

            model.addAttribute("error", "Invalid email or password.");

        } catch (SQLException ex) {
            model.addAttribute("error", "Unable to connect to the database.");
            ex.printStackTrace();
        }

        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
