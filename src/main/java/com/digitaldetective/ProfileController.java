
package com.digitaldetective;

import com.digitaldetective.database.UserDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model)
            throws SQLException {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) return "redirect:/login";

        model.addAttribute("profile", UserDAO.getProfile(userId));

        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam String name,
            @RequestParam String email,
            HttpSession session,
            Model model) throws SQLException {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) return "redirect:/login";

        if (name.isBlank() || !email.contains("@")
                || !email.contains(".")) {
            model.addAttribute("error", "Enter a valid name and email.");
            model.addAttribute("profile", UserDAO.getProfile(userId));
            return "profile";
        }

        try {
            boolean updated =
                    UserDAO.updateProfile(userId, name.trim(), email.trim());

            if (updated) {
                session.setAttribute("userEmail", email.trim());
                return "redirect:/profile?updated";
            }

        } catch (SQLException ex) {
            if (ex.getMessage() != null
                    && ex.getMessage().contains("UNIQUE")) {
                model.addAttribute("error", "That email is already registered.");
            } else {
                throw ex;
            }
        }

        model.addAttribute("profile", UserDAO.getProfile(userId));
        return "profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            HttpSession session,
            Model model) throws SQLException {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) return "redirect:/login";

        if (newPassword.length() < 8) {
            model.addAttribute("error", "New password must contain at least 8 characters.");
        } else if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
        } else {
            boolean changed = UserDAO.changePassword(
                    userId, currentPassword, newPassword
            );

            if (changed) {
                return "redirect:/profile?passwordChanged";
            }

            model.addAttribute("error", "Current password is incorrect.");
        }

        model.addAttribute("profile", UserDAO.getProfile(userId));
        return "profile";
    }
}

