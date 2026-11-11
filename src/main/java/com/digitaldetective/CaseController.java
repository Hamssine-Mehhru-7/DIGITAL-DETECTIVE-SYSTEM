
package com.digitaldetective;

import com.digitaldetective.database.CaseDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
public class CaseController {

    @GetMapping("/cases")
    public String viewCases(HttpSession session, Model model) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        try {
            List<String> cases = CaseDAO.getCasesByUserId(userId);
            model.addAttribute("cases", cases);
        } catch (SQLException e) {
            model.addAttribute("error", "Unable to load cases.");
            e.printStackTrace();
        }

        return "cases";
    }

    @GetMapping("/cases/new")
    public String newCasePage(HttpSession session) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        return "create-case";
    }

    @PostMapping("/cases/create")
    public String createCase(
            @RequestParam String title,
            @RequestParam String description,
            HttpSession session,
            Model model) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        if (title.isBlank() || description.isBlank()) {
            model.addAttribute("error", "Please fill in all fields.");
            return "create-case";
        }

        try {
            boolean saved = CaseDAO.createCase(
                    userId, title.trim(), description.trim());

            if (saved) {
                return "redirect:/cases?created";
            }

            model.addAttribute("error", "Could not create the case.");

        } catch (SQLException e) {
            model.addAttribute("error", "Database error while saving case.");
            e.printStackTrace();
        }

        return "create-case";
    }
}

